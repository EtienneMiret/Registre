package db

import (
	"context"
	"etienne.miret.io/registre/back/types"
	"go.mongodb.org/mongo-driver/v2/bson"
	"go.mongodb.org/mongo-driver/v2/mongo"
	"go.mongodb.org/mongo-driver/v2/mongo/options"
)

const counterCollection = "counters"

type CounterRepository interface {
	NextValue(ctx context.Context, name string) (int, error)
}

type mongoCounterRepository struct {
	coll *mongo.Collection
}

func NewCounterRepository(db *mongo.Database) CounterRepository {
	collection := db.Collection(counterCollection)
	return &mongoCounterRepository{collection}
}

func (r *mongoCounterRepository) NextValue(
	ctx context.Context,
	name string,
) (int, error) {
	counter := types.Counter{}
	err := r.coll.FindOneAndUpdate(
		ctx,
		bson.M{"_id": name},
		bson.M{"$inc": bson.M{"value": 1}},
		options.FindOneAndUpdate().SetUpsert(true).SetReturnDocument(options.After),
	).Decode(&counter)
	return counter.Value, err
}
