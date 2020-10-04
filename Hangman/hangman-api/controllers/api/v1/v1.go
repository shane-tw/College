package v1

import (
	"github.com/labstack/echo"
	"github.com/shanepm/hangman-api/middleware/requirelogin"
)

func Route(g *echo.Group) {
	api := g.Group("/v1")
	RouteSession(api)
	api.POST("/users", AddUser)

	lgn := api.Group("")
	lgn.Use(requirelogin.ApiHandler)
	RouteUsers(lgn)
	RouteGames(lgn)
}
