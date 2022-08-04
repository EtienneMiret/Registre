export interface Record {
  _id: number;
  artwork: number; // Artwork
  comment: string;
  owner: number; // Person
  location: number; // Location
  alive: boolean;
  creator: number; // Member
  creation: Date;
  lastModifier: number; // Member
  lastModification: Date;
}
