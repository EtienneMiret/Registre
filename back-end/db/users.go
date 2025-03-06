package db

import (
	"context"
	"errors"
	"fmt"
	"gihub.com/EtienneMiret/Registre/back-end/types"
	"go.mongodb.org/mongo-driver/v2/bson"
	"go.mongodb.org/mongo-driver/v2/mongo"
)

const userCollection = "users"

type UserRepository interface {
	List(ctx context.Context) ([]*types.User, error)
	Create(ctx context.Context, name string, emails ...string) (string, error)
	FindById(ctx context.Context, id string) (*types.User, error)
	FindByEmail(ctx context.Context, email string) (*types.User, error)
}

type mongoUserRepository struct {
	coll *mongo.Collection
}

func NewUserRepository(db *mongo.Database) (UserRepository, error) {
	collection := db.Collection(userCollection)
	_, err := collection.Indexes().CreateMany(context.Background(), []mongo.IndexModel{
		{
			Keys: bson.D{{"emails", 1}},
		},
	})
	return &mongoUserRepository{collection}, err
}

func (r *mongoUserRepository) List(ctx context.Context) ([]*types.User, error) {
	cursor, err := r.coll.Find(ctx, bson.D{})
	if err != nil {
		return nil, err
	}
	var result []*types.User

	err = cursor.All(ctx, &result)
	return result, err
}

func (r *mongoUserRepository) Create(
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

func (r *mongoUserRepository) FindById(
	ctx context.Context,
	id string,
) (*types.User, error) {
	var res types.User
	objId, err := bson.ObjectIDFromHex(id)
	if err != nil {
		return nil, err
	}
	err = r.coll.FindOne(ctx, bson.M{"_id": objId}).Decode(&res)
	return &res, err
}

func (r *mongoUserRepository) FindByEmail(
	ctx context.Context,
	email string,
) (*types.User, error) {
	var res types.User
	err := r.coll.FindOne(ctx, bson.M{"emails": email}).Decode(&res)
	return &res, err
}
