-- Registre’s database schema, in HSQL.

set database collation fr_FR;

create table actors (
	id bigint generated always as identity primary key,
	nom varchar(200) unique not null
);

create table authors (
	id bigint generated always as identity primary key,
	nom varchar(200) unique not null
);

create table composers (
	id bigint generated always as identity primary key,
	nom varchar(200) unique not null
);

create table cartoonists (
	id bigint generated always as identity primary key,
	nom varchar(200) unique not null
);

create table locations (
	id bigint generated always as identity primary key,
	nom varchar(200) unique not null
);

create table owners (
	id bigint generated always as identity primary key,
	nom varchar(200) unique not null
);

create table directors (
	id bigint generated always as identity primary key,
	nom varchar(200) unique not null
);

create table script_writers (
	id bigint generated always as identity primary key,
	nom varchar(200) unique not null
);

create table series (
	id bigint generated always as identity primary key,
	nom varchar(200) unique not null
);

create table users (
	id bigint generated always as identity primary key,
	nom varchar(100) unique not null,
	email varchar(100) unique not null
);

create table records (
	id bigint generated always as identity primary key,
	titre varchar(200) not null,
	dtype varchar(20) not null,
	serie bigint references series (id),
	commentaire clob,
	image char(36),
	proprietaire bigint references owners (id),
	emplacement bigint references locations (id),
	genre_action boolean,
	genre_documentaire boolean,
	genre_fantastique boolean,
	genre_guerre boolean,
	genre_histoire_vraie boolean,
	genre_historique boolean,
	genre_humour boolean,
	genre_policier boolean,
	genre_romantique boolean,
	genre_sf boolean,
	createur bigint references users (id) not null,
	creation datetime not null,
	dernier_editeur bigint references users (id) not null,
	derniere_edition datetime not null
);

create table movies (
	id bigint primary key references records (id),
	support varchar(3),
	realisateur bigint references directors (id),
	compositeur bigint references composers (id)
);

create table comics (
	id bigint primary key references records (id),
	dessinateur bigint references cartoonists (id),
	scenariste bigint references script_writers (id),
	numero int
);

create table books (
	id bigint primary key references records (id),
	auteur bigint references authors(id)
);

create table plays_in (
	acteur bigint not null references actors (id),
	film bigint not null references movies (id),
	primary key (film, acteur)
);

create table sessions (
	clef char(20) primary key,
	utilisateur bigint references users (id),
	expiration datetime
);

create table dictionary (
	id bigint generated always as identity primary key,
	mot varchar(50) unique not null
);

create table index_ (
	id bigint generated always as identity primary key,
	mot bigint not null references dictionary (id),
	champ varchar(20) not null,
	fiche bigint not null references records (id),
	unique (mot, champ, fiche)
);
