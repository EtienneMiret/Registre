import db from '../db';
import { Member } from './types';

export async function findByEmail (email: string): Promise<Member | null> {
  return db.then(db => db.collection<Member>('members'))
      .then(members => members.findOne({emails: email}));
}
