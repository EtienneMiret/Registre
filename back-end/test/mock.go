package test

import (
	"context"
	"etienne.miret.io/registre/back/types"
	"github.com/stretchr/testify/mock"
	"time"
)

func NewMockClock(now time.Time) *MockClock {
	return &MockClock{now: now}
}

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

type MockUserRepository struct {
	mock.Mock
}

func (m *MockUserRepository) List(ctx context.Context) ([]*types.User, error) {
	args := m.Called(ctx)
	users := args.Get(0)
	if users == nil {
		return nil, args.Error(1)
	}
	return users.([]*types.User), args.Error(1)
}

func (m *MockUserRepository) Create(ctx context.Context, name string, emails ...string) (string, error) {
	args := m.Called(ctx, name, emails)
	return args.String(0), args.Error(1)
}

func (m *MockUserRepository) FindById(ctx context.Context, id string) (*types.User, error) {
	args := m.Called(ctx, id)
	user := args.Get(0)
	if user == nil {
		return nil, args.Error(1)
	}
	return user.(*types.User), args.Error(1)
}

func (m *MockUserRepository) FindByEmail(ctx context.Context, email string) (*types.User, error) {
	args := m.Called(ctx, email)
	user := args.Get(0)
	if user == nil {
		return nil, args.Error(1)
	}
	return user.(*types.User), args.Error(1)
}
