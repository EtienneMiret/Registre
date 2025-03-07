package main

import (
	"etienne.miret.io/registre/back/db"
	"etienne.miret.io/registre/back/services"
	"net/http"

	"github.com/labstack/echo/v4"
)

func main() {
	e := echo.New()

	database, disconnect, err := db.Connect()
	if err != nil {
		e.Logger.Fatal(err)
	}
	defer disconnect(e.Logger)

	userRepository, err := db.NewUserRepository(database)
	if err != nil {
		e.Logger.Fatal(err)
	}
	sessionRepository, err := db.NewSessionRepository(database)
	if err != nil {
		e.Logger.Fatal(err)
	}

	authService := services.NewAuthService(sessionRepository, userRepository)

	e.Use(authService.Process)

	e.GET("/", func(c echo.Context) error {
		return c.String(http.StatusOK, "Hello, World!")
	})

	e.Logger.Fatal(e.Start(":8080"))
}
