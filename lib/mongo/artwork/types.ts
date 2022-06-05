import { ObjectID } from 'bson';

export const enum ArtworkType {
  Movie,
  Comic,
  Book,
}

interface BaseArtwork {
  _id: ObjectID;
  type: ArtworkType;
  title: string;
  series?: ObjectID;
  season?: number;
  episode?: number;
  comment?: string;
  picture?: string;
}

export const enum Support {
  K7,
  DVD,
  DEM,
  BRD
}

export interface Movie extends BaseArtwork {
  type: ArtworkType.Movie;
  support: Support;
  director: ObjectID[]; // Person
  actors: ObjectID[]; // Person
  composer: ObjectID[]; // Person
}

export interface Comic extends BaseArtwork {
  type: ArtworkType.Comic;
  cartoonist: ObjectID[]; // Person
  scriptWriter: ObjectID[]; // Person
}

export interface Book extends BaseArtwork {
  type: ArtworkType.Book;
  author: ObjectID[]; // Person
}

export type Artwork = Movie | Comic | Book;
