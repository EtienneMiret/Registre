package db

import (
	"go.mongodb.org/mongo-driver/v2/mongo"
	"go.mongodb.org/mongo-driver/v2/mongo/options"
)

func connect(url string) (*mongo.Client, error) {
	opts := options.Client()
	opts.BSONOptions = &options.BSONOptions{
		ObjectIDAsHexString: true,
		NilSliceAsEmpty: true,
	}
	opts.ApplyURI(url)
	return mongo.Connect(opts)
}
