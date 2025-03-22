package db

import (
	"context"
	"etienne.miret.io/registre/back/types"
	"go.mongodb.org/mongo-driver/v2/bson"
	"go.mongodb.org/mongo-driver/v2/mongo"
	"go.mongodb.org/mongo-driver/v2/mongo/options"
)

const artworkCollection = "artworks"

type ArtworkRepository interface {
	Save(ctx context.Context, artwork *types.Artwork) error
}

func NewArtworkRepository(db *mongo.Database) (ArtworkRepository, error) {
	collection := db.Collection(artworkCollection)
	return &mongoArtworkRepository{collection}, nil
}

type mongoArtworkRepository struct {
	coll *mongo.Collection
}

func (r *mongoArtworkRepository) Save(
	ctx context.Context,
	artwork *types.Artwork,
) error {
	_, err := r.coll.UpdateOne(
		ctx,
		bson.M{"_id": artwork.Id},
		bson.M{"$set": artwork},
		options.UpdateOne().SetUpsert(true),
	)
	return err
}
