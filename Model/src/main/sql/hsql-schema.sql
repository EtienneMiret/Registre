-- Registre’s database schema, in HSQL.

set database collation fr_FR;

create table actors (
	id bigint generated always as identity primary key,
	name varchar(200) unique not null
);

create table authors (
	id bigint generated always as identity primary key,
	name varchar(200) unique not null
);

create table composers (
	id bigint generated always as identity primary key,
	name varchar(200) unique not null
);

create table cartoonists (
	id bigint generated always as identity primary key,
	name varchar(200) unique not null
);

create table locations (
	id bigint generated always as identity primary key,
	name varchar(200) unique not null
);

create table owners (
	id bigint generated always as identity primary key,
	name varchar(200) unique not null
);

create table directors (
	id bigint generated always as identity primary key,
	name varchar(200) unique not null
);

create table script_writers (
	id bigint generated always as identity primary key,
	name varchar(200) unique not null
);

create table series (
	id bigint generated always as identity primary key,
	name varchar(200) unique not null
);

create table users (
	id bigint generated always as identity primary key,
	name varchar(100) unique not null,
	email varchar(100) unique not null
);

create table records (
	id bigint generated always as identity primary key,
	title varchar(200) not null,
	dtype varchar(20) not null,
	series bigint references series (id),
	comment clob,
	picture varchar(50),
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
	creation datetime not null,
	last_modifier bigint not null references users (id),
	last_modification datetime not null
);

create table movies (
	id bigint primary key references records (id),
	support varchar(3),
	director bigint references directors (id),
	composer bigint references composers (id)
);

create table comics (
	id bigint primary key references records (id),
	cartoonist bigint references cartoonists (id),
	script_writer bigint references script_writers (id),
	number int
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
	key char(20) primary key,
	user bigint references users (id),
	expiration datetime
);

create table dictionary (
	id bigint generated always as identity primary key,
	word varchar(50) unique not null
);

create table index_ (
	id bigint generated always as identity primary key,
	word bigint not null references dictionary (id),
	field varchar(20) not null,
	record bigint not null references records (id),
	unique (word, field, record)
);
