package db

type User struct {
	ID           int     `gorm:"AUTO_INCREMENT;primary_key"`
	Email        string  `gorm:"type:varchar(255);unique;not null"`
	FirstName    string  `gorm:"type:varchar(50);not null"`
	LastName     string  `gorm:"type:varchar(50);not null"`
	PasswordHash []byte  `gorm:"type:binary(60);not null"`
	AvatarURL    *string `gorm:"type:varchar(255)"`
	Games        []Game  `gorm:"foreignkey:UserID"`
}

type Category struct {
	ID   int    `gorm:"AUTO_INCREMENT;primary_key"`
	Name string `gorm:"type:varchar(255);unique;not null"`
}

type Word struct {
	ID         int    `gorm:"AUTO_INCREMENT;primary_key"`
	Word       string `gorm:"type:varchar(255);unique;not null"`
	CategoryID int
	Category   Category `gorm:"foreignkey:CategoryID"`
}

type Guess struct {
	ID      int    `gorm:"AUTO_INCREMENT;primary_key"`
	Letter  string `gorm:"type:varchar(8);not null;unique_index:idx_letter_game"`
	GameID  int    `gorm:"unique_index:idx_letter_game"`
	Game    Game   `gorm:"foreignkey:GameID"`
	Correct *bool
}

type Game struct {
	ID     int `gorm:"AUTO_INCREMENT;primary_key"`
	WordID int
	Word   Word `gorm:"foreignkey:WordID"`
	UserID int
	User   User   `gorm:"foreignkey:UserID"`
	Status string `gorm:"type:varchar(15);not null"` // active, won, lost
}
