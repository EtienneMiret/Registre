export const enum Support {
  K7,
  DVD,
  DEM,
  BRD
}

export interface Record {
  _id: number;
  support?: Support;
  artwork: number; // Artwork
  comment: string;
  owner?: number; // Person
  location?: number; // Location
  alive: boolean;
  creator: number; // Member
  creation: Date;
  lastModifier: number; // Member
  lastModification: Date;
}
