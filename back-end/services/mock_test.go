package services

import (
	"context"
	"gihub.com/EtienneMiret/Registre/back-end/types"
	"github.com/stretchr/testify/mock"
	"time"
)

type MockClock struct {
	now time.Time
}

func (c *MockClock) Now() time.Time {
	return c.now
}

type MockSessionRepository struct {
	mock.Mock
}

func (m *MockSessionRepository) Save(
	ctx context.Context,
	session *types.Session,
) error {
	args := m.Called(ctx, session)
	return args.Error(0)
}

func (m *MockSessionRepository) FindById(
	ctx context.Context,
	id string,
) (*types.Session, error) {
	args := m.Called(ctx, id)
	sess := args.Get(0)
	if sess == nil {
		return nil, args.Error(1)
	}
	return sess.(*types.Session), args.Error(1)
}
