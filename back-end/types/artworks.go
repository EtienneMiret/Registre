package types

type Artwork struct {
	Id           int64         `bson:"_id" json:"id"`
	Type         Type          `bson:"type" json:"type"`
	Name         string        `bson:"name" json:"name"`
	Series       int64         `bson:"series,omitempty" json:"series,omitempty"`
	Number       int32         `bson:"number,omitempty" json:"number,omitempty"`
	Description  string        `bson:"description,omitempty" json:"description,omitempty"`
	Picture      string        `bson:"picture,omitempty" json:"picture,omitempty"`
	Participants []Participant `bson:"participants" json:"participants"`
}

type Series struct {
	Id   int64  `bson:"_id" json:"id"`
	Name string `bson:"name" json:"name"`
}

type Person struct {
	Id   int64  `bson:"_id" json:"id"`
	Name string `bson:"name" json:"name"`
}

// The Role of a Participant is how they took part in the making of an Artwork.
type Role string

const (
	Director     Role = "director"
	Actor        Role = "actor"
	Composer     Role = "composer"
	Cartoonist   Role = "cartoonist"
	ScriptWriter Role = "script-writer"
	Author       Role = "author"
	Designer     Role = "designer"
)

// A Participant is a Person that took part in the making of an Artwork.
type Participant struct {
	Person int64 `bson:"person" json:"person"`
	Role   Role  `bson:"role" json:"role"`
}

// A Type of Artwork.
type Type string

const (
	Movie Type = "movie"
	Comic Type = "comic"
	Book  Type = "book"
	Game  Type = "game"
)
