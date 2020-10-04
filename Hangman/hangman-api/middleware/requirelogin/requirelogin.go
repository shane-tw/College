package requirelogin

import (
	"github.com/labstack/echo"
	"github.com/labstack/echo-contrib/session"
)

func ApiHandler(next echo.HandlerFunc) echo.HandlerFunc {
	return func(c echo.Context) error {
		sess, _ := session.Get("session", c)
		if sess.Values["userID"] == nil {
			sess.Values["userID"] = 0
			sess.Save(c.Request(), c.Response())
			// return c.String(http.StatusUnauthorized, "API: Not logged in")
		}
		return next(c)
	}
}
