import { Artwork } from './types';
import db from '../db';

export default function listArtworks(): Promise<Artwork[]> {
  return db.then(db => db.collection<Artwork>('artworks'))
      .then(c => c.find())
      .then(cursor => cursor.toArray());
}
