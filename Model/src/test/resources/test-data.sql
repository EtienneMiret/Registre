insert into users (id, name, email) values (
	(default, 'Etienne',  'etienne@email'),
	(default, 'Grégoire', 'gregoire@email'),
	(default, 'Claire',   'claire@email')
);

insert into actors (id, name) values (
	(default, 'Will Smith'),
	(default, 'Eva Mendes'),
	(default, 'George Clooney'),
	(default, 'Emma Watson'),
	(default, 'Scarlett Johansson'),
	(default, 'Anthony Head'),
	(default, 'Marilyn Monroe'),
	(default, 'Bradley James'),
	(default, 'Colin Morgan')
);

insert into authors (id, name) values (
	(default, 'Tom Clancy'),
	(default, 'Isaac Asimov'),
	(default, 'John Grisham'),
	(default, 'Gav Thorpe'),
	(default, 'Noick Kyme')
);

insert into composers (id, name) values (
	(default, 'Howard Shore'),
	(default, 'Hans Zimmer'),
	(default, 'Lisa Gerrard')
);

insert into cartoonists (id, name) values (
	(default, 'Jigounov'),
	(default, 'Alain Henriet'),
	(default, 'Morris')
);

insert into locations (id, name) values (
	(default, 'Verneuil'),
	(default, 'La Roche sur Yon'),
	(default, 'Lyon'),
	(default, 'Singapour'),
	(default, 'Poissy')
);

insert into owners (id, name) values (
	(default, 'Etienne'),
	(default, 'Grégoire'),
	(default, 'Claire')
);

insert into directors (id, name) values (
	(default, 'Steven Spielberg'),
	(default, 'George Lucas'),
	(default, 'Luc Besson')
);

insert into script_writers (id, name) values (
	(default, 'Jean Van Hamme'),
	(default, 'Renard'),
	(default, 'René Goscinny')
);

insert into series (id, name) values (
	(default, 'Boule et Bill'),
	(default, 'Merlin'),
	(default, 'Warhammer 40,000'),
	(default, 'Harry Potter'),
	(default, 'Luky Luke')
);

insert into records (id, alive, title, dtype, series, number, comment, picture, owner,
		location, creator, creation, last_modifier, last_modification) values (
	(default, true, 'Globe-trotters', 'Comic', 0, 12, null, null, 2,
		0, 0, '2012-12-25 22:18:30', 1, '2013-02-16 22:19:58'),
	(default, true, 'Merlin, Saison 1', 'Movie', 1, null, 'Une super série !', null, 0,
		0, 0, '2012-12-25 22:21:29', 2, '2013-02-26 22:22:06'),
	(default, true, 'Rainbow Six', 'Book', null, null, null, null, 0,
		0, 0, '2013-07-04 21:48:00', 0, '2013-07-04 21:48:00'),
	(default, true, 'Lucy', 'Movie', null, null, null, null, null,
		4, 0, '2014-08-11 21:28:30', 0, '2014-08-11 21:28:30'),
	(default, true, 'La Purge de Kadillus', 'Book', 2, null, null, null, 0,
		4, 0, '2014-08-11 21:33:45', 0, '2014-08-11 21:33:45'),
	(default, true, 'La Chute de Damnos', 'Book', 2, null, null, null, 0,
		4, 0, '2014-08-11 21:35:00', 0, '2014-08-11 21:35:00'),
	(default, true, 'Alerte aux Pieds-Bleus', 'Comic', 4, 10, null, null, 1,
		3, 2, '2014-08-11 21:37:00', 2, '2014-08-11 21:37:00'),
	(default, true, 'La Fiancée de Luky Luke', 'Comic', 4, 26, null, null, 1,
		3, 2, '2014-08-11 21:38:30', 2, '2014-08-11 21:38:30'),
	(default, true, 'Le Pony Express', 'Comic', 4, 29, null, null, 1,
		3, 2, '2014-08-11 21:40:00', 2, '2014-08-11 21:40:00'),
	(default, true, 'Luky Marines', 'Movie', null, null, 'Rien à voir avec Luky Luke.', null, 2,
		1, 0, '2014-08-11 21:42:00', 0, '2014-08-11 21:42:00')
);

insert into movies (id, support, director, composer) values (
	(1, 'BRD', null, 0),
	(3, 'DVD', 2, null),
	(9, 'K7', null, 1)
);

insert into comics (id, cartoonist, script_writer) values (
	(0, 0, 1),
	(6, 2, 2),
	(7, 2, 2),
	(8, 2, 2)
);

insert into books (id, author) values (
	(2, 0),
	(4, 3),
	(5, 4)
);

insert into plays_in (actor, movie) values (
	(0, 1),
	(3, 1),
	(4, 3)
);

insert into dictionary (id, word) values (
	(default, 'une'),
	(default, 'super'),
	(default, 'série')
);

insert into index_ (id, word, field, record) values (
	(default, 0, 'COMMENT', 1),
	(default, 1, 'COMMENT', 1),
	(default, 2, 'COMMENT', 1)
);
