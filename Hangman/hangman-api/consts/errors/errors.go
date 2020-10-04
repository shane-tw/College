package errors

import apiModels "github.com/shanepm/hangman-api/models/api"

var (
	// General errors, starting at 1
	BadRequest      = apiModels.Error{Reason: "Couldn't understand request", Code: 1}
	DatabaseFailure = apiModels.Error{Reason: "Failed to connect to database", Code: 2}
	ItemNotFound    = apiModels.Error{Reason: "The requested item could not be found", Code: 3}

	// Validation errors, starting at 50
	MissingEmail           = apiModels.Error{Reason: "You need to supply an email address", Code: 50}
	MissingPassword        = apiModels.Error{Reason: "You need to supply a password", Code: 51}
	MissingFirstName       = apiModels.Error{Reason: "You need to supply a first name", Code: 52}
	MissingLastName        = apiModels.Error{Reason: "You need to supply a last name", Code: 53}
	MissingPasswordCurrent = apiModels.Error{Reason: "You need to supply the current password", Code: 54}
	MissingPasswordConfirm = apiModels.Error{Reason: "You need to supply the confirmation password", Code: 55}
	MissingLetter          = apiModels.Error{Reason: "You need to supply a letter", Code: 56}

	// Other errors, starting at 100
	InvalidLogin           = apiModels.Error{Reason: "Wrong login details provided", Code: 100}
	InvalidPasswordConfirm = apiModels.Error{Reason: "Wrong confirmation password", Code: 101}
	UserAlreadyExists      = apiModels.Error{Reason: "A user already exists with the supplied email", Code: 102}
	InvalidLetter          = apiModels.Error{Reason: "Letter must not exceed a single character", Code: 103}
	LetterAlreadyGuessed   = apiModels.Error{Reason: "You've guessed this letter already", Code: 104}
	LostAlready            = apiModels.Error{Reason: "You've lost already", Code: 105}
	GameAlreadyExists      = apiModels.Error{Reason: "You already have a game in progress", Code: 106}
)
