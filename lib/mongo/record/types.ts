import { ObjectID } from 'bson';

export interface Record {
  _id: ObjectID;
  artwork: ObjectID; // Artwork
  comment: string;
  owner: ObjectID;
  location: ObjectID;
  alive: boolean;
  creator: ObjectID; // Member
  creation: Date;
  lastModifier: ObjectID; // Member
  lastModification: Date;
}
