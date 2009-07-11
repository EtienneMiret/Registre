<?php

/* Ficher de configuration générale de registre. */

/* Le serveur MySQL.
 * Il s’agit soit d’une addresse internet de la forme "hostname:port", soit du
 * chemin vers une socket locale, sous la forme : ":/path/to/socket". */
$reg_serveur_bd = ':/private/var/mysql/mysql.sock';

/* Nom de la base de donnée. */
$reg_nom_bd = 'Registre_dev';

/* Nom d’utilisateur pour se connecter à la base de donnée. */
$reg_utilisateur_bd = 'registre_dev';

/* Mot de passe de l’utilisateur $reg_utilisateur_bd. */
$reg_mdp_bd = 'dummypass';

/* Addresse de la racine.
 * Il s’agit de du chemin vers la page d’accueil de Registre dans l’URL
 * (après le nom du serveur). */
$reg_accueil = '/Registre-DEV/';

/* Nom de cette instance de Registre. */
$reg_nom = 'Registre DÉVELOPPEMENT';
