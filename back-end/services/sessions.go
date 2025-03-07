package services

import (
	"context"
	"etienne.miret.io/registre/back/db"
	"etienne.miret.io/registre/back/types"
	"time"
)

const sessionDuration = time.Hour * 24 * 30

type SessionService interface {
	Create(ctx context.Context, user *types.User) (*types.Session, error)
}

type sessionService struct {
	repository db.SessionRepository
	clock      Clock
}

func NewSessionService(repository db.SessionRepository, clock Clock) SessionService {
	return &sessionService{
		repository: repository,
		clock:      clock,
	}
}

func (s *sessionService) Create(
	ctx context.Context,
	user *types.User,
) (*types.Session, error) {
	session := &types.Session{
		UserId: user.Id,
		Expiry: s.clock.Now().Add(sessionDuration),
	}
	err := s.repository.Save(ctx, session)
	return session, err
}
