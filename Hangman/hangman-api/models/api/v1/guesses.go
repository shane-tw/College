package v1

type Guess struct {
	Page       *int   `json:"page,omitempty"`
	PageSize   *int   `json:"pageSize,omitempty"`
	GameID     int    `json:",omitempty"`
	GameStatus string `json:"gameStatus,omitempty"`

	Letter  string `json:"letter"`
	Correct *bool  `json:"correct"`
	Indexes []int  `json:"indexes,omitempty"`
	Answer  string `json:"answer,omitempty"`
}
