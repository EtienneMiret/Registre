package types

type User struct {
	Id     string   `bson:"_id,omitempty"`
	Name   string   `bson:"name"`
	Emails []string `bson:"emails"`
}
