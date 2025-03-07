package db

import (
	"errors"
	"etienne.miret.io/registre/back/types"
	"go.mongodb.org/mongo-driver/v2/bson"
	"go.mongodb.org/mongo-driver/v2/mongo"
	"testing"
	"time"
)

func TestNewSessionRepository(t *testing.T) {
	database, done := ConnectTestDb(t)
	defer done()

	// Should not fail if indexes exist
	_, err := NewSessionRepository(database)
	if err != nil {
		t.Fatal(err)
	}
	repository, err := NewSessionRepository(database)
	if err != nil {
		t.Fatal(err)
	}

	// Should delete expired sessions
	const id = "expired-session"
	session := &types.Session{
		Id:     id,
		Expiry: time.Unix(0, 0),
	}
	err = repository.Save(t.Context(), session)
	if err != nil {
		t.Fatal(err)
	}
	_, err = repository.FindById(t.Context(), id)
	if !errors.Is(err, mongo.ErrNoDocuments) {
		t.Fatal(err)
	}
}

func TestSessionRepository_Save(t *testing.T) {
	database, done := ConnectTestDb(t)
	defer done()
	repository, err := NewSessionRepository(database)
	if err != nil {
		t.Fatal(err)
	}

	session := &types.Session{
		UserId: "userId",
		Expiry: time.Unix(1<<40, 0),
	}

	err = repository.Save(t.Context(), session)

	if err != nil {
		t.Fatal(err)
	}
	if session.Id == "" {
		t.Error("session.Id should not be empty")
	}
	raw, err := repository.(*mongoSessionRepository).
		coll.FindOne(t.Context(), bson.M{"_id": session.Id}).Raw()
	if err != nil {
		t.Fatal(err)
	}
	str, ok := raw.Lookup("userId").StringValueOK()
	if !ok {
		t.Fatal("userId should exist")
	}
	if str != session.UserId {
		t.Error("userId should be " + session.UserId)
	}
	exp, ok := raw.Lookup("expiry").TimeOK()
	if !ok {
		t.Fatal("expiry should exist")
	}
	if exp != session.Expiry {
		t.Error("expiry should be " + session.Expiry.String())
	}
}

func TestSessionRepository_FindByID(t *testing.T) {
	database, done := ConnectTestDb(t)
	defer done()
	repository, err := NewSessionRepository(database)
	if err != nil {
		t.Fatal(err)
	}

	const (
		id0   = "foo"
		id1   = "bar"
		user0 = "Foo"
		user1 = "Bar"
	)
	_, err = repository.(*mongoSessionRepository).coll.InsertMany(
		t.Context(),
		[]interface{}{
			bson.M{"_id": id0, "userId": user0},
			bson.M{"_id": id1, "userId": user1},
		},
	)
	if err != nil {
		t.Fatal(err)
	}

	session0, err := repository.FindById(t.Context(), id0)
	if err != nil {
		t.Fatal(err)
	}
	session1, err := repository.FindById(t.Context(), id1)
	if err != nil {
		t.Fatal(err)
	}

	if session0.Id != id0 {
		t.Error("session0.Id should be " + id0)
	}
	if session1.Id != id1 {
		t.Error("session1.Id should be " + id1)
	}
	if session0.UserId != user0 {
		t.Error("session0.UserId should be " + user0)
	}
	if session1.UserId != user1 {
		t.Error("session1.UserId should be " + user1)
	}
}
