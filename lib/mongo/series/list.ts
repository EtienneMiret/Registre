import { ObjectID } from 'bson';
import db from '../db';
import { Series } from './types';

export default function listSeries(ids: Set<number>): Promise<Series[]> {
  if (ids.size === 0) {
    return Promise.resolve([]);
  }

  return db.then(db => db.collection<Series>('series'))
      .then(c => c.find({_id: {$in: Array.from(ids)}}))
      .then(cursor => cursor.toArray());
}
