import { MongoClient, MongoClientOptions } from "mongodb"

const uri = process.env.MONGODB_URI;
const options: MongoClientOptions = {
  appName: 'registre'
}

if (!uri) {
  throw new Error("Please add your Mongo URI to .env.local")
}

const client = new MongoClient (uri, options);
const clientPromise = client.connect ();

export default clientPromise;
