package services

import (
	"context"
	"crypto/rand"
	"errors"
	"etienne.miret.io/registre/back/db"
	"etienne.miret.io/registre/back/types"
	"fmt"
	"github.com/coreos/go-oidc/v3/oidc"
	"github.com/labstack/echo/v4"
	"go.mongodb.org/mongo-driver/v2/mongo"
	"golang.org/x/oauth2"
	"net/http"
	"os"
	"regexp"
)

type AuthService struct {
	sessionRepository db.SessionRepository
	userRepository    db.UserRepository
	sessionService    SessionService
	clock             Clock
	providers         map[string]*oidcProvider
}

func NewAuthService(
	sessionRepository db.SessionRepository,
	userRepository db.UserRepository,
	sessionService SessionService,
	clock Clock,
) *AuthService {
	return &AuthService{
		sessionRepository: sessionRepository,
		userRepository:    userRepository,
		sessionService:    sessionService,
		clock:             clock,
	}
}

func (s *AuthService) ConfigureOidc(ctx context.Context) error {
	googleProvider, err := oidc.NewProvider(ctx, "https://accounts.google.com")
	if err != nil {
		return err
	}
	googleConfig := &oauth2.Config{
		ClientID:     os.Getenv("REGISTRE_GOOGLE_CLIENT_ID"),
		ClientSecret: os.Getenv("REGISTRE_GOOGLE_CLIENT_SECRET"),
		RedirectURL:  os.Getenv("REGISTRE_GOOGLE_REDIRECT_URL"),
		Endpoint:     googleProvider.Endpoint(),
		Scopes:       []string{oidc.ScopeOpenID, "email"},
	}
	s.providers = map[string]*oidcProvider{
		"google": {
			provider: googleProvider,
			config:   googleConfig,
		},
	}
	return nil
}

const stateCookieName = "token"
const sessionIdCookieName = "token"
const redirectToCookieName = "redirectTo"

const cookiePath = "/api/auth/"

func (s *AuthService) Login(c echo.Context) error {
	providerName := c.Param("provider")
	provider, ok := s.providers[providerName]
	if !ok {
		return c.String(http.StatusNotFound, "no such provider")
	}
	state := rand.Text()
	c.SetCookie(&http.Cookie{
		Name:     stateCookieName,
		Value:    state,
		HttpOnly: true,
		Secure:   true,
		Path:     cookiePath,
		SameSite: http.SameSiteLaxMode,
	})

	redirectTo := c.QueryParam("redirectTo")
	if redirectTo != "" {
		c.SetCookie(&http.Cookie{
			Name:     redirectToCookieName,
			Value:    redirectTo,
			HttpOnly: true,
			Secure:   true,
			Path:     cookiePath,
			SameSite: http.SameSiteLaxMode,
		})
	}

	return c.Redirect(http.StatusFound, provider.config.AuthCodeURL(state))
}

func (s *AuthService) Callback(c echo.Context) error {
	ctx := c.Request().Context()
	providerName := c.Param("provider")
	provider, ok := s.providers[providerName]
	if !ok {
		return c.String(http.StatusNotFound, "no such provider")
	}
	stateCookie, err := c.Cookie(stateCookieName)
	if err != nil {
		return c.String(http.StatusBadRequest, "invalid state")
	}
	stateParam := c.QueryParam("state")
	if stateParam != stateCookie.Value {
		return c.String(http.StatusBadRequest, "invalid state")
	}
	c.SetCookie(&http.Cookie{
		Name:   stateCookieName,
		Path:   cookiePath,
		MaxAge: -1,
	})

	code := c.QueryParam("code")
	oauth2Token, err := provider.config.Exchange(ctx, code)
	if err != nil {
		c.Logger().Error(err)
		return c.NoContent(http.StatusInternalServerError)
	}
	rawIDToken, ok := oauth2Token.Extra("id_token").(string)
	if !ok {
		return c.String(http.StatusInternalServerError, "no id_token")
	}
	verifier := provider.provider.Verifier(s.oidcConfig(provider))
	idToken, err := verifier.Verify(ctx, rawIDToken)
	if err != nil {
		c.Logger().Error(err)
		return c.NoContent(http.StatusInternalServerError)
	}
	var claims struct {
		Email    string `json:"email"`
		Verified bool   `json:"email_verified"`
	}
	err = idToken.Claims(&claims)
	if err != nil {
		c.Logger().Error(err)
		return c.NoContent(http.StatusInternalServerError)
	}
	if !claims.Verified {
		return c.String(http.StatusForbidden, "email not verified")
	}

	user, err := s.userRepository.FindByEmail(ctx, claims.Email)
	if errors.Is(err, mongo.ErrNoDocuments) {
		return c.String(http.StatusForbidden, fmt.Sprintf("Sorry, %s, you have no registered user.", claims.Email))
	}
	if err != nil {
		c.Logger().Error(err)
		return c.NoContent(http.StatusInternalServerError)
	}

	session, err := s.sessionService.Create(ctx, user)
	if err != nil {
		c.Logger().Error(err)
		return c.NoContent(http.StatusInternalServerError)
	}
	c.SetCookie(&http.Cookie{
		Name:     sessionIdCookieName,
		Value:    session.Id,
		HttpOnly: true,
		Secure:   true,
		Path:     "/",
		SameSite: http.SameSiteLaxMode,
		Expires:  session.Expiry,
	})

	var redirectTo string
	redirectToCookie, err := c.Cookie(redirectToCookieName)
	if err == nil {
		redirectTo = redirectToCookie.Value
		c.SetCookie(&http.Cookie{
			Name:   redirectToCookieName,
			Path:   cookiePath,
			MaxAge: -1,
		})
	} else {
		redirectTo = "/"
	}

	return c.Redirect(http.StatusFound, redirectTo)
}

var authPathRegex = regexp.MustCompile(`^/api/auth/`)

func (s *AuthService) Process(next echo.HandlerFunc) echo.HandlerFunc {
	return func(c echo.Context) error {
		if authPathRegex.MatchString(c.Request().URL.Path) {
			return next(c)
		}

		cookie, err := c.Cookie(sessionIdCookieName)

		if err != nil {
			return c.NoContent(http.StatusForbidden)
		}
		session, err := s.sessionRepository.FindById(c.Request().Context(), cookie.Value)
		if errors.Is(err, mongo.ErrNoDocuments) {
			return c.NoContent(http.StatusForbidden)
		}
		if err != nil {
			return err
		}
		user, err := s.userRepository.FindById(c.Request().Context(), session.UserId)
		if err != nil {
			return err
		}
		ac := &types.AuthenticatedContext{
			Context: c,
			User:    user,
		}
		return next(ac)
	}
}

func (s *AuthService) oidcConfig(provider *oidcProvider) *oidc.Config {
	return &oidc.Config{
		ClientID: provider.config.ClientID,
		Now:      s.clock.Now,
	}
}

type oidcProvider struct {
	provider *oidc.Provider
	config   *oauth2.Config
}
