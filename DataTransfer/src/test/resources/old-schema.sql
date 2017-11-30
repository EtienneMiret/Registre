create table tout (
  id bigint generated always as identity primary key,
  titre varchar (80) not null,
  serie varchar (80),
  proprietaire varchar (20) default null,
  type varchar (20) not null,
  emplacement varchar (80),
  commentaire longvarchar,
  image varchar (3),
  createur varchar (20),
  creation timestamp,
  dernier_editeur varchar (20),
  derniere_edition timestamp
);

create table films (
  id bigint primary key,
  realisateur varchar (20),
  compositeur varchar (20),
  genres varchar (200)
);

create table acteurs (
  id bigint not null,
  acteur varchar (20)
);

create table bd (
  id bigint primary key,
  dessinateur varchar (20),
  scenariste varchar (20),
  numero bigint
);

create table livres (
  id bigint primary key,
  auteur varchar (20),
  genres varchar (200)
);
