package db

import (
	"go.mongodb.org/mongo-driver/v2/mongo"
	"go.mongodb.org/mongo-driver/v2/mongo/options"
	"testing"
)

const testDbName = "test"

func ConnectTestDb(t *testing.T) (*mongo.Database, func()) {
	client, err := connect(t.Context(), options.Client())
	if err != nil {
		t.Fatal(err)
	}
	database := client.Database(testDbName)
	return database, func() {
		err := database.Drop(t.Context())
		if err != nil {
			t.Fatal(err)
		}
		err = client.Disconnect(t.Context())
		if err != nil {
			t.Fatal(err)
		}
	}
}
