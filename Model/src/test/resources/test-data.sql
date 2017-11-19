insert into utilisateurs (id, nom, email) values (
	(default, 'Etienne',  'etienne@email'),
	(default, 'Grégoire', 'gregoire@email'),
	(default, 'Claire',   'claire@email')
);

insert into acteurs (id, nom) values (
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

insert into auteurs (id, nom) values (
	(default, 'Tom Clancy'),
	(default, 'Isaac Asimov'),
	(default, 'John Grisham'),
	(default, 'Gav Thorpe'),
	(default, 'Noick Kyme')
);

insert into compositeurs (id, nom) values (
	(default, 'Howard Shore'),
	(default, 'Hans Zimmer'),
	(default, 'Lisa Gerrard')
);

insert into dessinateurs (id, nom) values (
	(default, 'Jigounov'),
	(default, 'Alain Henriet'),
	(default, 'Morris')
);

insert into emplacements (id, nom) values (
	(default, 'Verneuil'),
	(default, 'La Roche sur Yon'),
	(default, 'Lyon'),
	(default, 'Singapour'),
	(default, 'Poissy')
);

insert into proprietaires (id, nom) values (
	(default, 'Etienne'),
	(default, 'Grégoire'),
	(default, 'Claire')
);

insert into realisateurs (id, nom) values (
	(default, 'Steven Spielberg'),
	(default, 'George Lucas'),
	(default, 'Luc Besson')
);

insert into scenaristes (id, nom) values (
	(default, 'Jean Van Hamme'),
	(default, 'Renard'),
	(default, 'René Goscinny')
);

insert into series (id, nom) values (
	(default, 'Boule et Bill'),
	(default, 'Merlin'),
	(default, 'Warhammer 40,000'),
	(default, 'Harry Potter'),
	(default, 'Luky Luke')
);

insert into fiches (id, titre, dtype, serie, commentaire, image, proprietaire,
		emplacement, createur, creation, dernier_editeur,
		derniere_edition) values (
	(default, 'Globe-trotters', 'Comic', 0, null, null, 2,
		0, 0, '2012-12-25 22:18:30', 1, '2013-02-16 22:19:58'),
	(default, 'Merlin, Saison 1', 'Movie', 1, 'Une super série !', null, 0,
		0, 0, '2012-12-25 22:21:29', 2, '2013-02-26 22:22:06'),
	(default, 'Rainbow Six', 'Book', null, null, null, 0,
		0, 0, '2013-07-04 21:48:00', 0, '2013-07-04 21:48:00'),
	(default, 'Lucy', 'Movie', null, null, null, null,
		4, 0, '2014-08-11 21:28:30', 0, '2014-08-11 21:28:30'),
	(default, 'La Purge de Kadillus', 'Book', 2, null, null, 0,
		4, 0, '2014-08-11 21:33:45', 0, '2014-08-11 21:33:45'),
	(default, 'La Chute de Damnos', 'Book', 2, null, null, 0,
		4, 0, '2014-08-11 21:35:00', 0, '2014-08-11 21:35:00'),
	(default, 'Alerte aux Pieds-Bleus', 'Comic', 4, null, null, 1,
		3, 2, '2014-08-11 21:37:00', 2, '2014-08-11 21:37:00'),
	(default, 'La Fiancée de Luky Luke', 'Comic', 4, null, null, 1,
		3, 2, '2014-08-11 21:38:30', 2, '2014-08-11 21:38:30'),
	(default, 'Le Pony Express', 'Comic', 4, null, null, 1,
		3, 2, '2014-08-11 21:40:00', 2, '2014-08-11 21:40:00'),
	(default, 'Luky Marines', 'Movie', null, 'Rien à voir avec Luky Luke.', null, 2,
		1, 0, '2014-08-11 21:42:00', 0, '2014-08-11 21:42:00')
);

insert into films (id, support, realisateur, compositeur) values (
	(1, 'BRD', null, 0),
	(3, 'DVD', 2, null),
	(9, 'K7', null, 1)
);

insert into bandes_dessinees (id, dessinateur, scenariste, numero) values (
	(0, 0, 1, 12),
	(6, 2, 2, 10),
	(7, 2, 2, 26),
	(8, 2, 2, 29)
);

insert into livres (id, auteur) values (
	(2, 0),
	(4, 3),
	(5, 4)
);

insert into joue_dans (acteur, film) values (
	(0, 1),
	(3, 1),
	(4, 3)
);

insert into dictionaire (id, mot) values (
	(default, 'une'),
	(default, 'super'),
	(default, 'série')
);

insert into index_ (id, mot, champ, fiche) values (
	(default, 0, 'COMMENT', 1),
	(default, 1, 'COMMENT', 1),
	(default, 2, 'COMMENT', 1)
);
