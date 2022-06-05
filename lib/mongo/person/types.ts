import { ObjectID } from 'bson';

export interface Person {
  _id: ObjectID;
  name: string;
}
