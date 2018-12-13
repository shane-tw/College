package main

import (
	"fmt"

	_ "github.com/go-sql-driver/mysql"
	"github.com/gorilla/sessions"
	"github.com/jinzhu/gorm"
	"github.com/labstack/echo"
	"github.com/labstack/echo-contrib/session"
	"github.com/labstack/echo/middleware"
	"github.com/shanepm/hangman-api/controllers/api"
	"github.com/shanepm/hangman-api/middleware/initdb"
	dbModels "github.com/shanepm/hangman-api/models/db"
)

func main() {
	e := echo.New()

	e.Use(middleware.Logger())
	e.Use(middleware.Recover())
	e.Use(session.Middleware(sessions.NewCookieStore([]byte("u81h2ij3l;1,'23;'ll!231u98@"))))
	e.Use(middleware.CORSWithConfig(middleware.CORSConfig{
		AllowOrigins:     []string{"http://localhost:8080"},
		AllowHeaders:     []string{echo.HeaderOrigin, echo.HeaderContentType, echo.HeaderAccept},
		AllowCredentials: true,
	}))

	db, err := gorm.Open("mysql", "hangmanuser:mari4db@/hangmandb")
	if err != nil {
		fmt.Println("Error: Failed to establish database connection.")
		return
	}
	defer db.Close()
	db.LogMode(true)
	db.AutoMigrate(&dbModels.User{}, &dbModels.Category{}, &dbModels.Word{}, &dbModels.Guess{}, &dbModels.Game{})
	populateWordsAndCategories(db)
	e.Use(initdb.InitDbHandler(db))

	api.Route(e)
	e.Logger.Fatal(e.Start(":4040"))
}

func populateWordsAndCategories(db *gorm.DB) {
	db.FirstOrCreate(&dbModels.Category{ID: 1, Name: "Sports"})
	db.FirstOrCreate(&dbModels.Word{ID: 1, Word: "Hockey", CategoryID: 1})
	db.FirstOrCreate(&dbModels.Word{ID: 2, Word: "Football", CategoryID: 1})
	db.FirstOrCreate(&dbModels.Word{ID: 3, Word: "Hurling", CategoryID: 1})
	db.FirstOrCreate(&dbModels.Word{ID: 4, Word: "Camogie", CategoryID: 1})
	db.FirstOrCreate(&dbModels.Word{ID: 5, Word: "Handball", CategoryID: 1})
	db.FirstOrCreate(&dbModels.Word{ID: 6, Word: "Jousting", CategoryID: 1})
	db.FirstOrCreate(&dbModels.Word{ID: 7, Word: "Fencing", CategoryID: 1})
	db.FirstOrCreate(&dbModels.Word{ID: 8, Word: "Running", CategoryID: 1})

	db.FirstOrCreate(&dbModels.Category{ID: 2, Name: "Animal"})
	db.FirstOrCreate(&dbModels.Word{ID: 9, Word: "Crocodile", CategoryID: 2})
	db.FirstOrCreate(&dbModels.Word{ID: 10, Word: "Giraffe", CategoryID: 2})
	db.FirstOrCreate(&dbModels.Word{ID: 11, Word: "Rhinoceros", CategoryID: 2})
	db.FirstOrCreate(&dbModels.Word{ID: 12, Word: "Monkey", CategoryID: 2})
	db.FirstOrCreate(&dbModels.Word{ID: 13, Word: "Gorilla", CategoryID: 2})
	db.FirstOrCreate(&dbModels.Word{ID: 14, Word: "Leopard", CategoryID: 2})
	db.FirstOrCreate(&dbModels.Word{ID: 15, Word: "Squirrel", CategoryID: 2})
	db.FirstOrCreate(&dbModels.Word{ID: 16, Word: "Kitten", CategoryID: 2})

	db.FirstOrCreate(&dbModels.Category{ID: 3, Name: "Country"})
	db.FirstOrCreate(&dbModels.Word{ID: 17, Word: "Ireland", CategoryID: 3})
	db.FirstOrCreate(&dbModels.Word{ID: 18, Word: "Canada", CategoryID: 3})
	db.FirstOrCreate(&dbModels.Word{ID: 19, Word: "Australia", CategoryID: 3})
	db.FirstOrCreate(&dbModels.Word{ID: 20, Word: "Austria", CategoryID: 3})
	db.FirstOrCreate(&dbModels.Word{ID: 21, Word: "Morocco", CategoryID: 3})
	db.FirstOrCreate(&dbModels.Word{ID: 22, Word: "France", CategoryID: 3})
	db.FirstOrCreate(&dbModels.Word{ID: 23, Word: "Spain", CategoryID: 3})
	db.FirstOrCreate(&dbModels.Word{ID: 24, Word: "Russia", CategoryID: 3})
}
