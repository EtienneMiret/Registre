package db

import (
	"etienne.miret.io/registre/back/types"
	"github.com/stretchr/testify/assert"
	"go.mongodb.org/mongo-driver/v2/bson"
	"testing"
)

func TestArtworkRepository_Save(t *testing.T) {
	database, done := ConnectTestDb(t)
	defer done()
	repository, err := NewArtworkRepository(database)
	assert.NoError(t, err)

	tolkien := types.Participant{
		Person: 38,
		Role:   types.Author,
	}
	artwork := &types.Artwork{
		Id:           42,
		Type:         types.Book,
		Name:         "The Fellowship of The Ring",
		Series:       12,
		Number:       1,
		Description:  "The Lord of the Rings: Volume 1",
		Picture:      "aExTufGHn",
		Participants: []types.Participant{tolkien},
	}

	err = repository.Save(t.Context(), artwork)

	assert.NoError(t, err)
	raw, err := database.Collection(artworkCollection).
		FindOne(t.Context(), bson.M{"_id": artwork.Id}).Raw()
	assert.NoError(t, err)
	assert.NotNil(t, raw)
	id, ok := raw.Lookup("_id").Int64OK()
	assert.True(t, ok, "id should be int64")
	assert.Equal(t, artwork.Id, id)
	artworkType, ok := raw.Lookup("type").StringValueOK()
	assert.True(t, ok, "type should be string")
	assert.Equal(t, string(artwork.Type), artworkType)
	name, ok := raw.Lookup("name").StringValueOK()
	assert.True(t, ok, "name should be string")
	assert.Equal(t, artwork.Name, name)
	series, ok := raw.Lookup("series").Int64OK()
	assert.True(t, ok, "series should be int64")
	assert.Equal(t, artwork.Series, series)
	number, ok := raw.Lookup("number").Int32OK()
	assert.True(t, ok, "number should be int32")
	assert.Equal(t, artwork.Number, number)
	description, ok := raw.Lookup("description").StringValueOK()
	assert.True(t, ok, "description should be string")
	assert.Equal(t, artwork.Description, description)
	picture, ok := raw.Lookup("picture").StringValueOK()
	assert.True(t, ok, "picture should be string")
	assert.Equal(t, artwork.Picture, picture)
	participants, ok := raw.Lookup("participants").ArrayOK()
	assert.True(t, ok, "participants should be an array")
	p0, ok := participants.Index(0).DocumentOK()
	assert.True(t, ok, "participants should be an array of documents")
	person, ok := p0.Lookup("person").Int64OK()
	assert.True(t, ok, "person should be int64")
	assert.Equal(t, tolkien.Person, person)
	role, ok := p0.Lookup("role").StringValueOK()
	assert.True(t, ok, "role should be string")
	assert.Equal(t, string(tolkien.Role), role)
}

func TestArtworkRepository_FindById(t *testing.T) {
	database, done := ConnectTestDb(t)
	defer done()
	repository, err := NewArtworkRepository(database)
	assert.NoError(t, err)

	id := int64(43)
	artworkType := types.Book
	name := "The Two Towers"
	series := int64(12)
	number := int32(2)
	description := "The Lord of the Rings: Volume 2"
	picture := "aExTufGHn"
	tolkienId := int64(38)
	tolkienRole := types.Author
	_, err = database.Collection(artworkCollection).InsertOne(t.Context(), bson.M{
		"_id":          id,
		"type":         artworkType,
		"name":         name,
		"series":       series,
		"number":       number,
		"description":  description,
		"picture":      picture,
		"participants": bson.A{bson.M{"person": tolkienId, "role": tolkienRole}},
	})
	assert.NoError(t, err)

	res, err := repository.FindById(t.Context(), id)
	assert.NoError(t, err)
	assert.NotNil(t, res)
	assert.Equal(t, id, res.Id)
	assert.Equal(t, artworkType, res.Type)
	assert.Equal(t, name, res.Name)
	assert.Equal(t, series, res.Series)
	assert.Equal(t, number, res.Number)
	assert.Equal(t, description, res.Description)
	assert.Equal(t, picture, res.Picture)
	assert.Equal(t, 1, len(res.Participants), "should have one participant")
	assert.Equal(t, tolkienId, res.Participants[0].Person)
	assert.Equal(t, tolkienRole, res.Participants[0].Role)
}

func TestArtworkRepository_FindAll(t *testing.T) {
	database, done := ConnectTestDb(t)
	defer done()
	repository, err := NewArtworkRepository(database)
	assert.NoError(t, err)

	ids := []int64{42, 43, 44, 45, 46, 47, 48, 49, 50, 51}
	for _, id := range ids {
		_, err := database.Collection(artworkCollection).InsertOne(t.Context(), bson.M{
			"_id": id,
		})
		assert.NoError(t, err)
	}

	producer := repository.FindAll(t.Context())
	found := make([]*types.Artwork, 0)
	for artwork, err := producer(); artwork != nil && err != nil; artwork, err = producer() {
		assert.NoError(t, err)
		found = append(found, artwork)
	}
	assert.Len(t, found, len(ids))
}
