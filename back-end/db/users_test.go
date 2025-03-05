package db

import (
	"strconv"
	"testing"
)

func TestUserRepository(t *testing.T) {
	client, err := connect("mongodb://localhost:27017")
	if err != nil {
		t.Fatal(err)
	}
	defer func() {
		err := client.Disconnect(t.Context())
		if err != nil {
			t.Fatal(err)
		}
	}()

	database := client.Database("test")
	defer func() {
		err = database.Drop(t.Context())
		if err != nil {
			t.Fatal(err)
		}
	}()

	repository := NewUserRepository(database)

	t.Run("List empty", func(t *testing.T) {
		err = database.Drop(t.Context())
		if err != nil {
			t.Fatal(err)
		}

		list, err := repository.List(t.Context())
		if err != nil {
			t.Fatal(err)
		}
		if len(list) != 0 {
			t.Fail()
		}
	})

	t.Run("Create 1 mail", func(t *testing.T) {
		id, err := repository.Create(
			t.Context(),
			"Étienne",
			"etienne.miret@ens-lyon.org",
		)
		if err != nil {
			t.Fatal(err)
		}
		if len(id) == 0 {
			t.Fail()
		}
	})

	t.Run("Create 0 mail", func(t *testing.T) {
		_, err := repository.Create(t.Context(), "Quentin")
		if err != nil {
			t.Fatal(err)
		}
	})

	t.Run("Create 3 mails", func(t *testing.T) {
		_, err := repository.Create(
			t.Context(),
			"Foo",
			"foo@example.com",
			"foo@example.org",
			"admin@example.org",
		)
		if err != nil {
			t.Fatal(err)
		}
	})

	t.Run("List 7", func(t *testing.T) {
		const n = 7
		err = database.Drop(t.Context())
		if err != nil {
			t.Fatal(err)
		}

		for i := 0; i < n; i++ {
			_, err := repository.Create(t.Context(), strconv.Itoa(i))
			if err != nil {
				t.Fatal(err)
			}
		}

		list, err := repository.List(t.Context())
		if err != nil {
			t.Fatal(err)
		}
		if len(list) != n {
			t.Fail()
		}
	})
}
