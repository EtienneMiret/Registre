package db

import (
	"gihub.com/EtienneMiret/Registre/back-end/types"
	"go.mongodb.org/mongo-driver/v2/bson"
	"testing"
	"time"
)

func TestSessionRepository_Save(t *testing.T) {
	database, done := ConnectTestDb(t)
	defer done()
	repository := NewSessionRepository(database)

	session := &types.Session{
		UserId: "userId",
		Expiry: time.Unix(1<<40, 0),
	}

	err := repository.Save(t.Context(), session)

	if err != nil {
		t.Fatal(err)
	}
	if session.Id == "" {
		t.Fail()
	}
	raw, err := repository.coll.FindOne(t.Context(), bson.M{"_id": session.Id}).Raw()
	if err != nil {
		t.Fatal(err)
	}
	str, ok := raw.Lookup("userId").StringValueOK()
	if !ok {
		t.Fail()
	}
	if str != session.UserId {
		t.Fail()
	}
	exp, ok := raw.Lookup("expiry").TimeOK()
	if !ok {
		t.Fail()
	}
	if exp != session.Expiry {
		t.Fail()
	}
}

func TestSessionRepository_FindByID(t *testing.T) {
	database, done := ConnectTestDb(t)
	defer done()
	repository := NewSessionRepository(database)

	const (
		id0   = "foo"
		id1   = "bar"
		user0 = "Foo"
		user1 = "Bar"
	)
	_, err := repository.coll.InsertMany(t.Context(), []interface{}{
		bson.M{"_id": id0, "userId": user0},
		bson.M{"_id": id1, "userId": user1},
	})
	if err != nil {
		t.Fatal(err)
	}

	session0, err := repository.FindByID(t.Context(), id0)
	if err != nil {
		t.Fatal(err)
	}
	session1, err := repository.FindByID(t.Context(), id1)
	if err != nil {
		t.Fatal(err)
	}

	if session0.Id != id0 {
		t.Fail()
	}
	if session1.Id != id1 {
		t.Fail()
	}
	if session0.UserId != user0 {
		t.Fail()
	}
	if session1.UserId != user1 {
		t.Fail()
	}
}
