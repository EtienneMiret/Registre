package db

import (
	"context"
	"errors"
	"fmt"
	"gihub.com/EtienneMiret/Registre/back-end/types"
	"go.mongodb.org/mongo-driver/v2/bson"
	"go.mongodb.org/mongo-driver/v2/mongo"
)

const collectionName = "users"

type UserRepository struct {
	coll *mongo.Collection
}

func NewUserRepository(db *mongo.Database) *UserRepository {
	return &UserRepository{db.Collection(collectionName)}
}

func (r *UserRepository) List(ctx context.Context) ([]*types.User, error) {
	cursor, err := r.coll.Find(ctx, bson.D{})
	if err != nil {
		return nil, err
	}
	var result []*types.User

	err = cursor.All(ctx, &result)
	return result, err
}

func (r *UserRepository) Create(
	ctx context.Context,
	name string,
	emails ...string,
) (string, error) {
	user := &types.User{Name: name, Emails: emails}
	result, err := r.coll.InsertOne(ctx, user)
	if err != nil {
		return "", err
	}
	id, ok := result.InsertedID.(bson.ObjectID)
	if !ok {
		return "", errors.New(fmt.Sprintf("ID is not ObjectID: %T", result.InsertedID))
	}
	return id.Hex(), nil
}
