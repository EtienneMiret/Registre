package db

import (
	"context"
	"go.mongodb.org/mongo-driver/v2/bson"
	"go.mongodb.org/mongo-driver/v2/mongo"
	"strconv"
	"testing"
)

func TestUserRepository_List_empty(t *testing.T) {
	database, disconnect := ConnectTestDb(t)
	defer disconnect()
	repository, err := NewUserRepository(database)
	if err != nil {
		t.Fatal(err)
	}

	list, err := repository.List(t.Context())

	if err != nil {
		t.Fatal(err)
	}
	if len(list) != 0 {
		t.Error("list should be empty")
	}
}

func TestUserRepository_Create_1_mail(t *testing.T) {
	database, disconnect := ConnectTestDb(t)
	defer disconnect()
	repository, err := NewUserRepository(database)
	if err != nil {
		t.Fatal(err)
	}

	const name = "Étienne"
	const email = "etienne.miret@ens-lyon.org"

	id, err := repository.Create(
		t.Context(),
		name,
		email,
	)

	if err != nil {
		t.Fatal(err)
	}
	if len(id) == 0 {
		t.Error("id should not be empty")
	}
	raw, err := findOne(t.Context(), repository.(*mongoUserRepository).coll, id)
	if err != nil {
		t.Fatal(err)
	}
	str, ok := raw.Lookup("name").StringValueOK()
	if !ok {
		t.Fatal("name should exist")
	}
	if str != name {
		t.Error("name should be " + name)
	}
	arr, ok := raw.Lookup("emails").ArrayOK()
	if !ok {
		t.Fatal("emails should exist")
	}
	values, err := arr.Values()
	if err != nil {
		t.Fatal(err)
	}
	if len(values) != 1 {
		t.Fatal("emails should have one element")
	}
	str, ok = values[0].StringValueOK()
	if !ok {
		t.Fatal("emails should contain a string")
	}
	if str != email {
		t.Error("email should be " + email)
	}
}

func TestUserRepository_Create_0_mail(t *testing.T) {
	database, disconnect := ConnectTestDb(t)
	defer disconnect()
	repository, err := NewUserRepository(database)
	if err != nil {
		t.Fatal(err)
	}

	const name = "Quentin"

	id, err := repository.Create(t.Context(), name)

	if err != nil {
		t.Fatal(err)
	}
	raw, err := findOne(t.Context(), repository.(*mongoUserRepository).coll, id)
	if err != nil {
		t.Fatal(err)
	}
	arr, ok := raw.Lookup("emails").ArrayOK()
	if !ok {
		t.Fatal("emails should exist")
	}
	values, err := arr.Values()
	if err != nil {
		t.Fatal(err)
	}
	if len(values) != 0 {
		t.Error("emails should be empty")
	}
}

func TestUserRepository_List_7(t *testing.T) {
	database, disconnect := ConnectTestDb(t)
	defer disconnect()
	repository, err := NewUserRepository(database)
	if err != nil {
		t.Fatal(err)
	}

	const n = 7

	for i := 0; i < n; i++ {
		_, err := repository.Create(t.Context(), strconv.Itoa(i))
		if err != nil {
			t.Fatal(err)
		}
	}

	list, err := repository.List(t.Context())
	if err != nil {
		t.Fatal(err)
	}
	if len(list) != n {
		t.Error("list should have length", n)
	}
}

func TestUserRepository_FindById(t *testing.T) {
	database, disconnect := ConnectTestDb(t)
	defer disconnect()
	repository, err := NewUserRepository(database)
	if err != nil {
		t.Fatal(err)
	}

	const name0 = "Étienne"
	const name1 = "Quentin"

	id0 := bson.NewObjectID()
	_, err = repository.(*mongoUserRepository).coll.InsertOne(t.Context(), bson.M{
		"_id":    id0,
		"name":   name0,
		"emails": bson.A{},
	})
	if err != nil {
		t.Fatal(err)
	}
	id1 := bson.NewObjectID()
	_, err = repository.(*mongoUserRepository).coll.InsertOne(t.Context(), bson.M{
		"_id":    id1,
		"name":   name1,
		"emails": bson.A{},
	})
	if err != nil {
		t.Fatal(err)
	}

	user0, err := repository.FindById(t.Context(), id0.Hex())
	if err != nil {
		t.Fatal(err)
	}
	user1, err := repository.FindById(t.Context(), id1.Hex())
	if err != nil {
		t.Fatal(err)
	}
	if user0 == nil {
		t.Fatal("user0 should not be nil")
	}
	if user1 == nil {
		t.Fatal("user1 should not be nil")
	}
	if user0.Name != name0 {
		t.Error("user0 name should be " + name0)
	}
	if user1.Name != name1 {
		t.Error("user1 name should be " + name1)
	}
	if len(user0.Emails) != 0 {
		t.Error("user0 emails should be empty")
	}
	if len(user1.Emails) != 0 {
		t.Error("user1 emails should be empty")
	}
}

func TestUserRepository_FindByEmail(t *testing.T) {
	database, disconnect := ConnectTestDb(t)
	defer disconnect()
	repository, err := NewUserRepository(database)
	if err != nil {
		t.Fatal(err)
	}

	const name = "Étienne"
	const email = "etienne.miret@ens-lyon.org"

	_, err = repository.(*mongoUserRepository).coll.InsertOne(t.Context(), bson.M{
		"name":   name,
		"emails": bson.A{email},
	})
	if err != nil {
		t.Fatal(err)
	}

	user, err := repository.FindByEmail(t.Context(), email)
	if err != nil {
		t.Fatal(err)
	}
	if user == nil {
		t.Fatal("user should not be nil")
	}
	if user.Name != name {
		t.Error("user.name should be " + name)
	}
	if len(user.Emails) != 1 {
		t.Fatal("user emails should have length", 1)
	}
	if user.Emails[0] != email {
		t.Error("user email should be " + email)
	}
}

func findOne(
	ctx context.Context,
	coll *mongo.Collection,
	id string,
) (bson.Raw, error) {
	objId, err := bson.ObjectIDFromHex(id)
	if err != nil {
		return nil, err
	}
	return coll.FindOne(ctx, bson.M{"_id": objId}).Raw()
}
