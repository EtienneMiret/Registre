package types

type Counter struct {
	Name  string `bson:"_id"`
	Value int    `bson:"value"`
}
