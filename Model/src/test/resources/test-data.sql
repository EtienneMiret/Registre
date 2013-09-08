insert into utilisateurs (id, nom, email) values (
	(default, 'Etienne',  'etienne@email'),
	(default, 'Grégoire', 'gregoire@email'),
	(default, 'Claire',   'claire@email')
);

insert into acteurs (id, nom) values (
	(default, 'Will Smith'),
	(default, 'Eva Mendes'),
	(default, 'George Clooney'),
	(default, 'Emma Watson')
);

insert into auteurs (id, nom) values (
	(default, 'Tom Clancy'),
	(default, 'Isaac Asimov'),
	(default, 'John Grisham')
);

insert into compositeurs (id, nom) values (
	(default, 'Howard Shore'),
	(default, 'Hans Zimmer'),
	(default, 'Lisa Gerrard')
);

insert into dessinateurs (id, nom) values (
	(default, 'Jigounov'),
	(default, 'Alain Henriet')
);

insert into emplacements (id, nom) values (
	(default, 'Verneuil'),
	(default, 'La Roche sur Yon'),
	(default, 'Lyon'),
	(default, 'Singapour')
);

insert into proprietaires (id, nom) values (
	(default, 'Etienne'),
	(default, 'Grégoire'),
	(default, 'Claire')
);

insert into realisateurs (id, nom) values (
	(default, 'Steven Spielberg'),
	(default, 'George Lucas')
);

insert into scenaristes (id, nom) values (
	(default, 'Jean Van Hamme'),
	(default, 'Renard')
);

insert into series (id, nom) values (
	(default, 'Boule et Bill'),
	(default, 'Merlin')
);

insert into fiches (id, titre, serie, commentaire, image, proprietaire,
		emplacement, createur, creation, dernier_editeur,
		derniere_edition) values (
	(default, 'Globe-trotters', 0, null, null, 2,
		0, 0, '2012-12-25 22:18:30', 1, '2013-02-16 22:19:58'),
	(default, 'Merlin, Saison 1', 1, 'Une super série !', null, 0,
		0, 0, '2012-12-25 22:21:29', 2, '2013-02-26 22:22:06')
);

insert into films (id, support, realisateur, compositeur) values (
	(1, 'BRD', null, 0)
);

insert into bandes_dessinees (id, dessinateur, scenariste, numero) values (
	(0, 0, 1, 12)
);

insert into joue_dans (acteur, film) values (
	(0, 1),
	(3, 1)
);

insert into dictionaire (id, mot) values (
	(default, 'une'),
	(default, 'super'),
	(default, 'série')
);

insert into "index" (id, mot, champ, fiche) values (
	(default, 0, 'COMMENTAIRE', 1),
	(default, 1, 'COMMENTAIRE', 1),
	(default, 2, 'COMMENTAIRE', 1)
);
