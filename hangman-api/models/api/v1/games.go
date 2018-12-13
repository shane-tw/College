package v1

type Game struct {
	Page     *int `json:"page,omitempty"`
	PageSize *int `json:"pageSize,omitempty"`
	Word     Word `json:"word"`

	ID           int    `json:"id"`
	GuessedCount int    `json:"guessedCount"`
	Status       string `json:"status"`
}
