package db

import (
	"context"
	"go.mongodb.org/mongo-driver/v2/mongo"
	"go.mongodb.org/mongo-driver/v2/mongo/options"
	"os"
)

func Connect() (*mongo.Database, func() error, error) {
	username, userOk := os.LookupEnv("REGISTRE_DB_USERNAME")
	password, pwdOk := os.LookupEnv("REGISTRE_DB_PASSWORD")
	mongoUrl, urlOk := os.LookupEnv("REGISTRE_DB_URL")
	database, dbOk := os.LookupEnv("REGISTRE_DB_NAME")

	opts := options.Client()
	if urlOk {
		opts.ApplyURI(mongoUrl)
	}
	if userOk && pwdOk {
		opts.SetAuth(options.Credential{
			Username: username,
			Password: password,
		})
	}
	if !dbOk {
		database = "registre"
	}

	client, err := connect(context.Background(), opts)
	if err != nil {
		return nil, nil, err
	}
	return client.Database(database), func() error {
		return client.Disconnect(context.Background())
	}, nil
}

func connect(
	ctx context.Context,
	opts *options.ClientOptions,
) (*mongo.Client, error) {
	opts.BSONOptions = &options.BSONOptions{
		ObjectIDAsHexString: true,
		NilSliceAsEmpty:     true,
	}
	return mongo.Connect(opts)
}
