insert into tout (titre, type) values (
  ('Hello', 'DVD'),                         -- 0
  ('World', 'disque Blu-ray'),              -- 1
  ('How are you?', 'livre'),                -- 2
  ('I like this', 'BD'),                    -- 3
  ('I’m the best', 'DVD'),                  -- 4
  ('I’m the worst', 'DVD'),                 -- 5
  ('Happy Birthday', 'BD'),                 -- 6
  ('Merry Christmas', 'disque Blu-ray'),    -- 7
  ('To DELETE', 'DVD'),                     -- 8
  ('Happy new year!', 'livre')              -- 9
);

insert into films (id, realisateur, compositeur) values (
  (0, 'Spielberg', 'Andrew'),
  (1, 'Spielberg', null),
  (4, 'Me', 'Me'),
  (5, null, null),
  (7, null, 'Howard')
);

insert into acteurs (id, acteur) values (
  (0, 'Alice'),
  (0, 'Bob'),
  (0, 'Clara'),
  (4, 'Me'),
  (7, 'Alice'),
  (7, 'Bob')
);

insert into bd (id, dessinateur, scenariste, numero) values (
  (3, 'Alan', 'Turing', 42),
  (6, null, null, null)
);

insert into livres (id, auteur) values (
  (2, 'Foo'),
  (9, 'Foo')
);

update tout set serie = 'Me' where id in (3, 4, 5);
update tout set serie = 'Wish' where id in (6, 7, 9);
update tout set proprietaire = 'Alice' where id in (1, 2, 9);
update tout set proprietaire = 'Bob' where id in (4, 5);
update tout set emplacement = 'Paris' where id in (2, 3);
update tout set emplacement = 'New-York' where id in (5, 7);
update tout set commentaire = 'Hello World!' where id = 0;
update tout set commentaire = 'This is bad' where id = 5;
update tout set image = 'YES' where id in (0, 1, 6, 7, 9);
update tout set createur = 'Système' where id in (0, 1, 2, 3);
update tout set createur = 'Alice' where id in (4, 6, 7);
update tout set createur = 'Bob' where id in (5, 9);
update tout set creation = '2017-01-12 22:33:41' where id in (0, 1, 2, 3);
update tout set creation = '2017-02-14 07:12:28' where id = 4;
update tout set creation = '2017-03-19 14:32:25' where id = 5;
update tout set creation = '2017-04-21 19:56:02' where id = 6;
update tout set creation = '2017-04-29 15:17:43' where id = 7;
update tout set creation = '2017-05-06 04:23:12' where id = 9;
update tout set dernier_editeur = 'Alice' where id in (3, 4, 5);
update tout set dernier_editeur = 'Bob' where id in (1, 2, 9);
update tout set derniere_edition = '2017-07-01 14:28:05' where id = 1;
update tout set derniere_edition = '2017-07-02 11:25:31' where id = 2;
update tout set derniere_edition = '2017-07-03 12:29:34' where id = 3;
update tout set derniere_edition = '2017-07-04 05:18:21' where id = 4;
update tout set derniere_edition = '2017-07-05 16:14:30' where id = 5;
update tout set derniere_edition = '2017-07-08 15:14:32' where id = 9;

update films set genres = 'action,fantastique' where id = 0;
update films set genres = 'film de guerre,historique' where id = 1;
update livres set genres = 'policier' where id = 2;

delete from tout where id = 8;
