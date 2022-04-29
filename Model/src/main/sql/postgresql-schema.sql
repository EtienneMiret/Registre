-- Registre’s database schema, in PostgreSQL.

create table actors (
  id bigint generated always as identity primary key,
  name text unique not null
);

create table authors (
  id bigint generated always as identity primary key,
  name text unique not null
);

create table composers (
  id bigint generated always as identity primary key,
  name text unique not null
);

create table cartoonists (
  id bigint generated always as identity primary key,
  name text unique not null
);

create table locations (
  id bigint generated always as identity primary key,
  name text unique not null
);

create table owners (
  id bigint generated always as identity primary key,
  name text unique not null
);

create table directors (
  id bigint generated always as identity primary key,
  name text unique not null
);

create table script_writers (
  id bigint generated always as identity primary key,
  name text unique not null
);

create table series (
  id bigint generated always as identity primary key,
  name text unique not null
);

create table users (
  id bigint generated always as identity primary key,
  name text unique not null,
  email text unique not null
);

create table records (
  id bigint generated always as identity primary key,
  title text not null,
  dtype text not null,
  series bigint references series (id),
  number int,
  comment text,
  picture text,
  owner bigint references owners (id),
  location bigint references locations (id),
  action_style boolean,
  documentary_style boolean,
  fantasy_style boolean,
  war_style boolean,
  true_story_style boolean,
  historical_style boolean,
  humor_style boolean,
  detective_style boolean,
  romantic_style boolean,
  sf_style boolean,
  creator bigint not null references users (id),
  creation timestamp not null,
  last_modifier bigint not null references users (id),
  last_modification timestamp not null
);

create table movies (
  id bigint primary key references records (id),
  support text,
  director bigint references directors (id),
  composer bigint references composers (id)
);

create table comics (
  id bigint primary key references records (id),
  cartoonist bigint references cartoonists (id),
  script_writer bigint references script_writers (id)
);

create table books (
  id bigint primary key references records (id),
  author bigint references authors(id)
);

create table plays_in (
  actor bigint not null references actors (id),
  movie bigint not null references movies (id),
  primary key (movie, actor)
);

create table sessions (
  key text primary key,
  "user" bigint references users (id),
  expiration timestamp
);

create table dictionary (
  id bigint generated always as identity primary key,
  word text unique not null
);

create table index_ (
  id bigint generated always as identity primary key,
  word bigint not null references dictionary (id),
  field text not null,
  record bigint not null references records (id),
  unique (word, field, record)
);
