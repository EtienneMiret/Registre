package controllers

import (
	"etienne.miret.io/registre/back/types"
	"github.com/labstack/echo/v4"
	"net/http"
)

type UserController struct {
}

func NewUserController() *UserController {
	return &UserController{}
}

func (*UserController) WhoAmI(c echo.Context) error {
	ac, ok := c.(*types.AuthenticatedContext)
	if !ok {
		return c.String(http.StatusNotFound, "Not authenticated")
	}
	return c.JSON(http.StatusOK, ac.User)
}
