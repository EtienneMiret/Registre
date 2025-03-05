package db

import (
	"context"
	"crypto/rand"
	"gihub.com/EtienneMiret/Registre/back-end/types"
	"go.mongodb.org/mongo-driver/v2/bson"
	"go.mongodb.org/mongo-driver/v2/mongo"
)

const sessionCollection = "sessions"

type SessionRepository struct {
	coll *mongo.Collection
}

func NewSessionRepository(db *mongo.Database) *SessionRepository {
	return &SessionRepository{db.Collection(sessionCollection)}
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
