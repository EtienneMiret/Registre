package controllers

import (
	"etienne.miret.io/registre/back/types"
	"github.com/labstack/echo/v4"
	"github.com/stretchr/testify/assert"
	"net/http"
	"net/http/httptest"
	"testing"
)

func TestUserController_WhoAmI(t *testing.T) {
	e := echo.New()
	req := httptest.NewRequest(http.MethodGet, "/users/@me", nil)
	rec := httptest.NewRecorder()
	ac := &types.AuthenticatedContext{
		Context: e.NewContext(req, rec),
		User: &types.User{
			Id:     "foo",
			Name:   "Étienne",
			Emails: []string{"etienne.miret@ens-lyon.org"},
		},
	}
	userController := &UserController{}

	err := userController.WhoAmI(ac)

	assert.NoError(t, err)
	assert.Equal(t, http.StatusOK, rec.Code)
	assert.JSONEq(t, `{"id":"foo","name":"Étienne"}`, rec.Body.String())
}
