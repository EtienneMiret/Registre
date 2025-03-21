package db

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

func TestCounterRepository_NextValue(t *testing.T) {
	database, done := ConnectTestDb(t)
	defer done()
	repository := NewCounterRepository(database)
	const (
		foo = "foo"
		bar = "bar"
	)

	foo0, err := repository.NextValue(t.Context(), foo)
	assert.NoError(t, err)
	foo1, err := repository.NextValue(t.Context(), foo)
	assert.NoError(t, err)
	bar0, err := repository.NextValue(t.Context(), bar)
	assert.NoError(t, err)
	foo2, err := repository.NextValue(t.Context(), foo)
	assert.NoError(t, err)
	assert.Equal(t, 1, foo0)
	assert.Equal(t, 2, foo1)
	assert.Equal(t, 3, foo2)
	assert.Equal(t, 1, bar0)
}
