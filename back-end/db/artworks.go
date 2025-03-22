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
	FindById(ctx context.Context, id int64) (*types.Artwork, error)
	FindAll(ctx context.Context) (chan *types.Artwork, error)
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

func (r *mongoArtworkRepository) FindById(
	ctx context.Context,
	id int64,
) (*types.Artwork, error) {
	artwork := &types.Artwork{}
	err := r.coll.FindOne(ctx, bson.M{"_id": id}).Decode(artwork)
	return artwork, err
}

func (r *mongoArtworkRepository) FindAll(
	ctx context.Context,
) (chan *types.Artwork, error) {
	result := make(chan *types.Artwork)
	cursor, err := r.coll.Find(ctx, bson.M{})
	if err != nil {
		return result, err
	}
	stream := func() {
		defer close(result)
		for cursor.Next(ctx) {
			artwork := &types.Artwork{}
			if cursor.Decode(artwork) != nil {
				return
			}
			select {
			case result <- artwork:
			case <-ctx.Done():
				return
			}
		}
	}
	go stream()
	return result, nil
}
