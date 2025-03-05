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
		const name = "Étienne"
		const email = "etienne.miret@ens-lyon.org"
		id, err := repository.Create(
			t.Context(),
			name,
			email,
		)
		if err != nil {
			t.Fatal(err)
		}
		if len(id) == 0 {
			t.Fail()
		}

		user, err := repository.FindById(t.Context(), id)
		if err != nil {
			t.Fatal(err)
		}
		if user.Name != name {
			t.Fail()
		}
		if len(user.Emails) != 1 {
			t.Fail()
		}
		if user.Emails[0] != email {
			t.Fail()
		}
	})

	t.Run("Create 0 mail", func(t *testing.T) {
		const name = "Quentin"
		id, err := repository.Create(t.Context(), name)
		if err != nil {
			t.Fatal(err)
		}

		user, err := repository.FindById(t.Context(), id)
		if err != nil {
			t.Fatal(err)
		}
		if user.Name != name {
			t.Fail()
		}
		if len(user.Emails) != 0 {
			t.Fail()
		}
	})

	t.Run("Create 2 mails", func(t *testing.T) {
		const name = "Foo"
		var emails = []string{"foo@example.com", "foo@example.org"}
		id, err := repository.Create(
			t.Context(),
			name,
			emails...,
		)
		if err != nil {
			t.Fatal(err)
		}

		user, err := repository.FindById(t.Context(), id)
		if err != nil {
			t.Fatal(err)
		}
		if user.Name != name {
			t.Fail()
		}
		if len(user.Emails) != len(emails) {
			t.Fail()
		}
		for i, email := range user.Emails {
			if emails[i] != email {
				t.Fail()
			}
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
