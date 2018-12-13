package v1

import (
	"math"
	"net/http"
	"regexp"
	"strconv"
	"strings"

	"github.com/go-sql-driver/mysql"
	"github.com/jinzhu/gorm"
	"github.com/shanepm/hangman-api/consts/errors"
	apiModels "github.com/shanepm/hangman-api/models/api"
	v1Models "github.com/shanepm/hangman-api/models/api/v1"
	dbModels "github.com/shanepm/hangman-api/models/db"

	"github.com/labstack/echo"
	"github.com/labstack/echo-contrib/session"
)

func RouteGames(g *echo.Group) {
	g.GET("/games", GetGames)
	g.GET("/games/:id", GetGames)
	g.POST("/games", AddGame)
	g.GET("/games/:id/guesses", GetGuesses)
	g.POST("/games/:id/guesses", AddGuess)
}

func GetGames(c echo.Context) error {
	db := c.Get("db").(*gorm.DB)

	criteria := new(v1Models.Game)
	err := c.Bind(criteria)
	if err != nil {
		return c.JSON(http.StatusBadRequest, apiModels.FailedResponse{Errors: []apiModels.Error{errors.BadRequest}})
	}

	sess, _ := session.Get("session", c)
	userID := sess.Values["userID"].(int)

	if c.Param("id") == "current" {
		if userID == 0 {
			return c.JSON(http.StatusNotFound, apiModels.FailedResponse{Errors: []apiModels.Error{errors.ItemNotFound}})
		}
		criteria.Status = "active"
	} else if len(c.Param("id")) > 0 {
		if criteriaID, err := strconv.Atoi(c.Param("id")); err == nil {
			criteria.ID = criteriaID
		} else {
			return c.JSON(http.StatusNotFound, apiModels.FailedResponse{Errors: []apiModels.Error{errors.ItemNotFound}})
		}
	}

	defaultPage := 1
	if criteria.Page == nil {
		criteria.Page = &defaultPage
	}

	defaultPageSize := 50
	if criteria.PageSize == nil {
		criteria.PageSize = &defaultPageSize
	}
	*criteria.PageSize = int(math.Min(math.Max(0, float64(*criteria.PageSize)), 100))
	pageOffset := (*criteria.Page - 1) * *criteria.PageSize

	criteriaGame := dbModels.Game{UserID: sess.Values["userID"].(int), Status: criteria.Status}

	gamesCount := 0
	errs := db.Model(&dbModels.Game{}).Where(&criteriaGame).Count(&gamesCount).GetErrors()
	if len(errs) > 0 {
		return c.JSON(http.StatusServiceUnavailable, apiModels.FailedResponse{Errors: []apiModels.Error{errors.DatabaseFailure}})
	}

	dbGames := []dbModels.Game{}
	errs = db.Where(&criteriaGame).Limit(*criteria.PageSize).Offset(pageOffset).Preload("Word").Preload("Word.Category").Order("id desc").Find(&dbGames).GetErrors()
	if len(errs) > 0 {
		return c.JSON(http.StatusServiceUnavailable, apiModels.FailedResponse{Errors: []apiModels.Error{errors.DatabaseFailure}})
	}

	games := []v1Models.Game{}
	for _, dbGame := range dbGames {
		guessedCount := 0
		if errs := db.Model(&dbModels.Guess{}).Where(&dbModels.Guess{GameID: dbGame.ID}).Count(&guessedCount).GetErrors(); len(errs) > 0 {
			return c.JSON(http.StatusServiceUnavailable, apiModels.FailedResponse{Errors: []apiModels.Error{errors.DatabaseFailure}})
		}

		game := v1Models.Game{
			ID: dbGame.ID,
			Word: v1Models.Word{
				Length:   len(dbGame.Word.Word),
				Category: dbGame.Word.Category.Name,
			},
			GuessedCount: guessedCount,
			Status:       dbGame.Status,
		}
		if dbGame.Status != "active" {
			game.Word.Answer = dbGame.Word.Word
		}
		games = append(games, game)
	}

	jsonData := map[string]interface{}{}
	if len(c.Param("id")) > 0 {
		if len(games) == 0 {
			return c.JSON(http.StatusNotFound, apiModels.FailedResponse{Errors: []apiModels.Error{errors.ItemNotFound}})
		}
		jsonData["game"] = games[0]
	} else {
		jsonData["games"] = games
		c.Response().Header().Add("X-Records", strconv.Itoa(gamesCount))
		if *criteria.PageSize > 0 {
			c.Response().Header().Add("X-Page", strconv.Itoa(*criteria.Page))
			c.Response().Header().Add("X-Pages", strconv.Itoa(int(math.Ceil(float64(gamesCount)/float64(*criteria.PageSize)))))
		}
	}

	return c.JSON(http.StatusOK, jsonData)
}

func AddGame(c echo.Context) error {
	db := c.Get("db").(*gorm.DB)

	dbWord := new(dbModels.Word)
	if errs := db.Limit(1).Order("RAND()").Preload("Category").First(&dbWord, &dbModels.Word{}).GetErrors(); len(errs) > 0 {
		return c.JSON(http.StatusServiceUnavailable, apiModels.FailedResponse{Errors: []apiModels.Error{errors.DatabaseFailure}})
	}

	sess, _ := session.Get("session", c)
	userID := sess.Values["userID"].(int)

	activeCount := 0
	if errs := db.Model(&dbModels.Game{}).Where(&dbModels.Game{UserID: userID, Status: "active"}).Count(&activeCount).GetErrors(); len(errs) > 0 {
		return c.JSON(http.StatusServiceUnavailable, apiModels.FailedResponse{Errors: []apiModels.Error{errors.DatabaseFailure}})
	}

	if activeCount > 0 && userID > 0 {
		return c.JSON(http.StatusConflict, apiModels.FailedResponse{Errors: []apiModels.Error{errors.GameAlreadyExists}})
	}

	gameToCreate := dbModels.Game{Status: "active", Word: *dbWord, UserID: userID}
	if errs := db.Set("gorm:association_autoupdate", false).Create(&gameToCreate).GetErrors(); len(errs) > 0 {
		return c.JSON(http.StatusServiceUnavailable, apiModels.FailedResponse{Errors: []apiModels.Error{errors.DatabaseFailure}})
	}

	return c.JSON(http.StatusCreated, map[string]interface{}{
		"game": v1Models.Game{
			ID: gameToCreate.ID,
			Word: v1Models.Word{
				Length:   len(gameToCreate.Word.Word),
				Category: gameToCreate.Word.Category.Name,
			},
			GuessedCount: 0,
			Status:       gameToCreate.Status,
		},
	})
}

func AddGuess(c echo.Context) error {
	db := c.Get("db").(*gorm.DB)

	criteria := new(v1Models.Guess)
	err := c.Bind(criteria)
	if err != nil {
		return c.JSON(http.StatusBadRequest, apiModels.FailedResponse{Errors: []apiModels.Error{errors.BadRequest}})
	}

	criteria.GameStatus = "active"
	if len(c.Param("id")) > 0 && c.Param("id") != "current" {
		if criteriaID, err := strconv.Atoi(c.Param("id")); err == nil {
			criteria.GameID = criteriaID
		} else {
			return c.JSON(http.StatusNotFound, apiModels.FailedResponse{Errors: []apiModels.Error{errors.ItemNotFound}})
		}
	}

	if criteria.Letter == "" {
		return c.JSON(http.StatusBadRequest, apiModels.FailedResponse{Errors: []apiModels.Error{errors.MissingLetter}})
	}
	if len(criteria.Letter) > 1 {
		return c.JSON(http.StatusBadRequest, apiModels.FailedResponse{Errors: []apiModels.Error{errors.InvalidLetter}})
	}

	dbGame := new(dbModels.Game)
	res := db.Preload("Word").First(&dbGame, dbModels.Game{ID: criteria.GameID, Status: criteria.GameStatus})
	if res.RecordNotFound() {
		return c.JSON(http.StatusNotFound, apiModels.FailedResponse{Errors: []apiModels.Error{errors.ItemNotFound}})
	} else if len(res.GetErrors()) > 0 {
		return c.JSON(http.StatusServiceUnavailable, apiModels.FailedResponse{Errors: []apiModels.Error{errors.DatabaseFailure}})
	}

	correct := true
	correctGuessCount := 0
	correctGuesses := []dbModels.Guess{}
	if errs := db.Model(&dbModels.Guess{}).Where(&dbModels.Guess{GameID: dbGame.ID, Correct: &correct}).Find(&correctGuesses).GetErrors(); len(errs) > 0 {
		return c.JSON(http.StatusServiceUnavailable, apiModels.FailedResponse{Errors: []apiModels.Error{errors.DatabaseFailure}})
	}
	for _, correctGuess := range correctGuesses {
		re := regexp.MustCompile("(?i)" + regexp.QuoteMeta(correctGuess.Letter))
		correctGuessCount += len(re.FindAllStringIndex(dbGame.Word.Word, -1))
	}

	incorrect := false
	incorrectGuessCount := 0
	if errs := db.Model(&dbModels.Guess{}).Where(&dbModels.Guess{GameID: dbGame.ID, Correct: &incorrect}).Count(&incorrectGuessCount).GetErrors(); len(errs) > 0 {
		return c.JSON(http.StatusServiceUnavailable, apiModels.FailedResponse{Errors: []apiModels.Error{errors.DatabaseFailure}})
	}
	if incorrectGuessCount >= 9 {
		return c.JSON(http.StatusBadRequest, apiModels.FailedResponse{Errors: []apiModels.Error{errors.LostAlready}})
	}

	criteria.Letter = strings.ToLower(criteria.Letter)
	re := regexp.MustCompile("(?i)" + regexp.QuoteMeta(criteria.Letter))
	indexes := []int{}
	for _, index := range re.FindAllStringIndex(dbGame.Word.Word, -1) {
		indexes = append(indexes, index[0])
	}

	correctGuess := len(indexes) > 0
	if err := db.Create(&dbModels.Guess{Letter: criteria.Letter, Correct: &correctGuess, GameID: dbGame.ID}).Error; err != nil {
		if mysqlError, ok := err.(*mysql.MySQLError); ok && mysqlError.Number == 1062 {
			return c.JSON(http.StatusConflict, apiModels.FailedResponse{Errors: []apiModels.Error{errors.LetterAlreadyGuessed}})
		}
		return c.JSON(http.StatusServiceUnavailable, apiModels.FailedResponse{Errors: []apiModels.Error{errors.DatabaseFailure}})
	}

	if correctGuess {
		correctGuessCount += len(indexes)
	} else {
		incorrectGuessCount++
	}

	errs := []error{}
	if correctGuessCount >= len(dbGame.Word.Word) {
		dbGame.Status = "won"
		errs = db.Save(&dbGame).GetErrors()
	} else if incorrectGuessCount >= 9 {
		dbGame.Status = "lost"
		errs = db.Save(&dbGame).GetErrors()
	}
	if len(errs) > 0 {
		return c.JSON(http.StatusServiceUnavailable, apiModels.FailedResponse{Errors: []apiModels.Error{errors.DatabaseFailure}})
	}

	guess := v1Models.Guess{
		GameStatus: dbGame.Status,
		Letter:     criteria.Letter,
		Correct:    &correctGuess,
		Indexes:    indexes,
	}
	if dbGame.Status != "active" {
		guess.Answer = dbGame.Word.Word
	}

	return c.JSON(http.StatusCreated, map[string]interface{}{
		"guess": guess,
	})
}

func GetGuesses(c echo.Context) error {
	db := c.Get("db").(*gorm.DB)

	criteria := new(v1Models.Guess)
	err := c.Bind(criteria)
	if err != nil {
		return c.JSON(http.StatusBadRequest, apiModels.FailedResponse{Errors: []apiModels.Error{errors.BadRequest}})
	}

	if c.Param("id") == "current" {
		criteria.GameStatus = "active"
	} else if len(c.Param("id")) > 0 {
		if criteriaID, err := strconv.Atoi(c.Param("id")); err == nil {
			criteria.GameID = criteriaID
		} else {
			return c.JSON(http.StatusNotFound, apiModels.FailedResponse{Errors: []apiModels.Error{errors.ItemNotFound}})
		}
	}

	defaultPage := 1
	if criteria.Page == nil {
		criteria.Page = &defaultPage
	}

	defaultPageSize := 50
	if criteria.PageSize == nil {
		criteria.PageSize = &defaultPageSize
	}
	*criteria.PageSize = int(math.Min(math.Max(0, float64(*criteria.PageSize)), 100))
	pageOffset := (*criteria.Page - 1) * *criteria.PageSize

	dbGame := new(dbModels.Game)
	res := db.Preload("Word").First(&dbGame, dbModels.Game{ID: criteria.GameID, Status: criteria.GameStatus})
	if res.RecordNotFound() {
		return c.JSON(http.StatusNotFound, apiModels.FailedResponse{Errors: []apiModels.Error{errors.ItemNotFound}})
	} else if len(res.GetErrors()) > 0 {
		return c.JSON(http.StatusServiceUnavailable, apiModels.FailedResponse{Errors: []apiModels.Error{errors.DatabaseFailure}})
	}

	criteriaGuess := dbModels.Guess{GameID: dbGame.ID, Correct: criteria.Correct}

	guessCount := 0
	errs := db.Model(&dbModels.Guess{}).Where(&criteriaGuess).Count(&guessCount).GetErrors()
	if len(errs) > 0 {
		return c.JSON(http.StatusServiceUnavailable, apiModels.FailedResponse{Errors: []apiModels.Error{errors.DatabaseFailure}})
	}

	dbGuesses := []dbModels.Guess{}
	errs = db.Where(&criteriaGuess).Limit(*criteria.PageSize).Offset(pageOffset).Find(&dbGuesses).GetErrors()
	if len(errs) > 0 {
		return c.JSON(http.StatusServiceUnavailable, apiModels.FailedResponse{Errors: []apiModels.Error{errors.DatabaseFailure}})
	}

	guesses := []v1Models.Guess{}
	for _, dbGuess := range dbGuesses {
		re := regexp.MustCompile("(?i)" + regexp.QuoteMeta(dbGuess.Letter))
		indexes := []int{}
		for _, index := range re.FindAllStringIndex(dbGame.Word.Word, -1) {
			indexes = append(indexes, index[0])
		}

		guesses = append(guesses, v1Models.Guess{
			Letter:  dbGuess.Letter,
			Correct: dbGuess.Correct,
			Indexes: indexes,
		})
	}

	c.Response().Header().Add("X-Records", strconv.Itoa(guessCount))
	if *criteria.PageSize > 0 {
		c.Response().Header().Add("X-Page", strconv.Itoa(*criteria.Page))
		c.Response().Header().Add("X-Pages", strconv.Itoa(int(math.Ceil(float64(guessCount)/float64(*criteria.PageSize)))))
	}

	return c.JSON(http.StatusOK, map[string]interface{}{
		"guesses": guesses,
	})
}
