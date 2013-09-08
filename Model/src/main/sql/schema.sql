-- Schéma de la base de données de Registre, en HSQL.

set database collation fr_FR;

create table acteurs (
	id bigint generated always as identity primary key,
	nom varchar(200) unique not null
);

create table auteurs (
	id bigint generated always as identity primary key,
	nom varchar(200) unique not null
);

create table compositeurs (
	id bigint generated always as identity primary key,
	nom varchar(200) unique not null
);

create table dessinateurs (
	id bigint generated always as identity primary key,
	nom varchar(200) unique not null
);

create table emplacements (
	id bigint generated always as identity primary key,
	nom varchar(200) unique not null
);

create table proprietaires (
	id bigint generated always as identity primary key,
	nom varchar(200) unique not null
);

create table realisateurs (
	id bigint generated always as identity primary key,
	nom varchar(200) unique not null
);

create table scenaristes (
	id bigint generated always as identity primary key,
	nom varchar(200) unique not null
);

create table series (
	id bigint generated always as identity primary key,
	nom varchar(200) unique not null
);

create table utilisateurs (
	id bigint generated always as identity primary key,
	nom varchar(100) unique not null,
	email varchar(100) unique not null
);

create table fiches (
	id bigint generated always as identity primary key,
	titre varchar(200) unique not null,
	serie bigint references series (id),
	commentaire clob,
	image char(36),
	proprietaire bigint references proprietaires (id),
	emplacement bigint references emplacements (id),
	createur bigint references utilisateurs (id),
	creation datetime,
	dernier_editeur bigint references utilisateurs (id),
	derniere_edition datetime
);

create table films (
	id bigint primary key references fiches (id),
	support varchar(3),
	realisateur bigint references realisateurs (id),
	compositeur bigint references compositeurs (id),
	genre_action boolean,
	genre_documentaire boolean,
	genre_fantastique boolean,
	genre_guerre boolean,
	genre_histoire_vraie boolean,
	genre_historique boolean,
	genre_humour boolean,
	genre_policier boolean,
	genre_romantique boolean,
	genre_sf boolean
);

create table bandes_dessinees (
	id bigint primary key references fiches (id),
	dessinateur bigint references dessinateurs (id),
	scenariste bigint references scenaristes (id),
	numero int
);

create table livres (
	id bigint primary key references fiches(id),
	auteur bigint references auteurs(id),
	genre_fantastique boolean,
	genre_histoire_vraie boolean,
	genre_historique boolean,
	genre_humour boolean,
	genre_policier boolean,
	genre_romantique boolean,
	genre_sf boolean
);

create table joue_dans (
	acteur bigint not null references acteurs (id),
	film bigint not null references films (id),
	primary key (film, acteur)
);

create table sessions (
	clef char(20) primary key,
	utilisateur bigint references utilisateurs (id),
	expiration datetime
);

create table dictionaire (
	id bigint generated always as identity primary key,
	mot varchar(50) unique not null
);

create table "index" (
	id bigint generated always as identity primary key,
	mot bigint not null references dictionaire (id),
	champ varchar(20) not null,
	fiche bigint not null references fiches (id),
	unique (mot, champ, fiche)
);
