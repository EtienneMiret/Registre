package services

import (
	"etienne.miret.io/registre/back/types"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
	"testing"
	"time"
)

func TestSessionService_Create(t *testing.T) {
	now := time.Date(2025, 3, 6, 16, 30, 0, 0, time.UTC)
	repository := &MockSessionRepository{}
	service := &sessionService{
		repository: repository,
		clock:      &MockClock{now: now},
	}
	user := &types.User{Id: "user"}
	repository.On("Save", t.Context(), mock.Anything).Return(nil)

	session, err := service.Create(t.Context(), user)

	expiry := time.Date(2025, 4, 5, 16, 30, 0, 0, time.UTC)
	assert.NoError(t, err)
	assert.NotNil(t, session)
	assert.Equal(t, user.Id, session.UserId)
	assert.True(t, expiry.Equal(session.Expiry))
	repository.AssertCalled(t, "Save", t.Context(), session)
}

func TestSessionService_Create_Error(t *testing.T) {
	now := time.Unix(0, 0)
	repository := &MockSessionRepository{}
	service := &sessionService{
		repository: repository,
		clock:      &MockClock{now: now},
	}
	user := &types.User{Id: "user"}
	repository.On("Save", t.Context(), mock.Anything).Return(assert.AnError)

	_, err := service.Create(t.Context(), user)

	assert.Equal(t, assert.AnError, err)
}
