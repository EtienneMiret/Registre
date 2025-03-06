package db

import (
	"context"
	"crypto/rand"
	"gihub.com/EtienneMiret/Registre/back-end/types"
	"go.mongodb.org/mongo-driver/v2/bson"
	"go.mongodb.org/mongo-driver/v2/mongo"
	"go.mongodb.org/mongo-driver/v2/mongo/options"
)

const sessionCollection = "sessions"

type SessionRepository struct {
	coll *mongo.Collection
}

func NewSessionRepository(db *mongo.Database) (*SessionRepository, error) {
	collection := db.Collection(sessionCollection)
	_, err := collection.Indexes().CreateMany(context.Background(), []mongo.IndexModel{
		{
			Keys: bson.D{{"userId", 1}},
		},
		{
			Keys:    bson.D{{"expiry", 1}},
			Options: options.Index().SetExpireAfterSeconds(0),
		},
	})
	return &SessionRepository{collection}, err
}

func (r *SessionRepository) Save(
	ctx context.Context,
	session *types.Session,
) error {
	session.Id = rand.Text()
	_, err := r.coll.InsertOne(ctx, session)
	return err
}

func (r *SessionRepository) FindByID(
	ctx context.Context,
	id string,
) (*types.Session, error) {
	session := &types.Session{}
	err := r.coll.FindOne(ctx, bson.M{"_id": id}).Decode(session)
	return session, err
}
