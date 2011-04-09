<?php

/* Ficher de configuration générale de registre. */

/* Le serveur MySQL.
 * Il s’agit soit d’une addresse internet de la forme "hostname:port", soit du
 * chemin vers une socket locale, sous la forme : ":/path/to/socket". */
$reg_serveur_bd = 'localhost:3306';

/* Nom de la base de données. */
$reg_nom_bd = 'registre';

/* Nom d’utilisateur pour se connecter à la base de données. */
$reg_utilisateur_bd = 'registre';

/* Mot de passe de l’utilisateur $reg_utilisateur_bd. */
$reg_mdp_bd = '';

/* Nom d'utilisateur pour administrer la base de données. Doit avoir les
 * droits CREATE et ALTER. */
$reg_utilisateur_admin_bd = $reg_utilisateur_bd;

/* Mot de passe de l'utilisateur $reg_utilisateur_admin_bd */
$reg_mdp_admin_bd = $reg_mdp_bd;

/* Addresse de la racine.
 * Il s’agit de du chemin vers la page d’accueil de Registre dans l’URL
 * (après le nom du serveur). */
$reg_racine = '/Registre/';

/* Nom de cette instance de Registre. */
$reg_nom = 'Registre';
