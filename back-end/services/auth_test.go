package services

import (
	"etienne.miret.io/registre/back/types"
	"github.com/labstack/echo/v4"
	"github.com/stretchr/testify/assert"
	"go.mongodb.org/mongo-driver/v2/mongo"
	"net/http"
	"net/http/httptest"
	"testing"
)

func TestAuthService_Process_ok(t *testing.T) {
	const token = "foobar"
	const userId = "someUser"
	sessionRepository := &MockSessionRepository{}
	userRepository := &MockUserRepository{}
	authService := AuthService{
		sessionRepository: sessionRepository,
		userRepository:    userRepository,
	}
	e := echo.New()
	req := httptest.NewRequest(http.MethodGet, "/", nil)
	rec := httptest.NewRecorder()
	c := e.NewContext(req, rec)
	session := &types.Session{
		UserId: userId,
	}
	user := &types.User{}
	sessionRepository.On("FindById", req.Context(), token).Return(session, nil)
	userRepository.On("FindById", req.Context(), userId).Return(user, nil)
	req.Header.Set("Cookie", "token="+token)

	ok := false
	err := authService.Process(func(ctx echo.Context) error {
		_, ok = ctx.(*types.AuthenticatedContext)
		return ctx.NoContent(http.StatusNoContent)
	})(c)

	assert.NoError(t, err)
	assert.Equal(t, http.StatusNoContent, rec.Code)
	assert.True(t, ok)
}

func TestAuthService_Process_no_token(t *testing.T) {
	sessionRepository := &MockSessionRepository{}
	userRepository := &MockUserRepository{}
	authService := AuthService{
		sessionRepository: sessionRepository,
		userRepository:    userRepository,
	}
	e := echo.New()
	req := httptest.NewRequest(http.MethodGet, "/", nil)
	rec := httptest.NewRecorder()
	c := e.NewContext(req, rec)

	ok := true
	err := authService.Process(func(ctx echo.Context) error {
		ok = false
		return ctx.NoContent(http.StatusNoContent)
	})(c)

	assert.NoError(t, err)
	assert.Equal(t, http.StatusForbidden, rec.Code)
	assert.True(t, ok)
}

func TestAuthService_Process_invalid_token(t *testing.T) {
	const token = "foobar"
	sessionRepository := &MockSessionRepository{}
	userRepository := &MockUserRepository{}
	authService := AuthService{
		sessionRepository: sessionRepository,
		userRepository:    userRepository,
	}
	e := echo.New()
	req := httptest.NewRequest(http.MethodGet, "/", nil)
	rec := httptest.NewRecorder()
	c := e.NewContext(req, rec)
	req.Header.Set("Cookie", "token="+token)
	sessionRepository.On("FindById", req.Context(), token).Return(
		nil,
		mongo.ErrNoDocuments,
	)

	ok := true
	err := authService.Process(func(ctx echo.Context) error {
		ok = false
		return ctx.NoContent(http.StatusNoContent)
	})(c)

	assert.NoError(t, err)
	assert.Equal(t, http.StatusForbidden, rec.Code)
	assert.True(t, ok)
}
