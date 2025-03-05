package types

import "time"

type User struct {
	Id     string   `bson:"_id,omitempty"`
	Name   string   `bson:"name"`
	Emails []string `bson:"emails"`
}

type Session struct {
	Id     string    `bson:"_id"`
	UserId string    `bson:"userId"`
	Expiry time.Time `bson:"expiry"`
}
