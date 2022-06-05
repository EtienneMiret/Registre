import { Member } from '../../types';
import db from '../db';

export async function findByEmail (email: string): Promise<Member | null> {
  return db.then(db => db.collection('members'))
      .then(members => members.findOne({emails: email}))
      .then(doc => doc as (Member | null));
}
