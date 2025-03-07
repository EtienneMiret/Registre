package main

import (
	"context"
	"etienne.miret.io/registre/back/db"
	"etienne.miret.io/registre/back/services"
	"github.com/labstack/echo/v4"
	"github.com/labstack/echo/v4/middleware"
	"net/http"
)

func main() {
	e := echo.New()
	e.HideBanner = true
	e.HidePort = true
	e.Use(middleware.Logger())

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

	clock := services.NewClock()
	sessionService := services.NewSessionService(sessionRepository, clock)
	authService := services.NewAuthService(
		sessionRepository,
		userRepository,
		sessionService,
		clock,
	)
	err = authService.ConfigureOidc(context.Background())
	if err != nil {
		e.Logger.Fatal(err)
	}

	e.Use(authService.Process)

	e.GET("/", func(c echo.Context) error {
		return c.String(http.StatusOK, "Hello, World!")
	})
	e.GET("/api/auth/login/:provider", authService.Login)
	e.GET("/api/auth/callback/:provider", authService.Callback)

	e.Logger.Fatal(e.Start(":8080"))
}
