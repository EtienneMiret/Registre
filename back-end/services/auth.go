package services

import (
	"errors"
	"gihub.com/EtienneMiret/Registre/back-end/db"
	"gihub.com/EtienneMiret/Registre/back-end/types"
	"github.com/labstack/echo/v4"
	"go.mongodb.org/mongo-driver/v2/mongo"
	"net/http"
)

type AuthService struct {
	sessionRepository db.SessionRepository
	userRepository    db.UserRepository
}

func NewAuthService(
	sessionRepository db.SessionRepository,
	userRepository db.UserRepository,
) *AuthService {
	return &AuthService{
		sessionRepository: sessionRepository,
		userRepository:    userRepository,
	}
}

func (s *AuthService) Process(next echo.HandlerFunc) echo.HandlerFunc {
	return func(c echo.Context) error {
		cookie, err := c.Cookie("token")

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
