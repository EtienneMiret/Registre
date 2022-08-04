export const enum ArtworkType {
  Movie,
  Comic,
  Book,
}

interface BaseArtwork {
  _id: number;
  type: ArtworkType;
  title: string;
  series?: number; // ID of a series
  season?: number;
  episode?: number;
  comment?: string;
  picture?: string;
}

export interface Movie extends BaseArtwork {
  type: ArtworkType.Movie;
  director: number[]; // Person
  actors: number[]; // Person
  composer: number[]; // Person
}

export interface Comic extends BaseArtwork {
  type: ArtworkType.Comic;
  cartoonist: number[]; // Person
  scriptWriter: number[]; // Person
}

export interface Book extends BaseArtwork {
  type: ArtworkType.Book;
  author: number[]; // Person
}

export type Artwork = Movie | Comic | Book;
