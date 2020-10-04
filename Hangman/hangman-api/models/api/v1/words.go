package v1

type Word struct {
	Answer   string `json:"answer,omitempty"`
	Length   int    `json:"length"`
	Category string `json:"category"`
}
