import { ObjectID } from 'bson';

export interface Member {
  _id: ObjectID;
  username: string;
  accessGranted: boolean;
  emails: string[];
}
