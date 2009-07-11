<?php

/* Connexion au serveur MySQL. */
$ok = mysql_connect($reg_serveur_bd, $reg_utilisateur_bd, $reg_mdp_bd);

if ( !$ok ) {
    reg_erreur_serveur('Impossible de se connecter au serveur MySQL : '
	. mysql_error());
}

/* Sélection de la base de données. */
$ok = mysql_select_db($reg_nom_bd);

if ( !$ok ) {
    reg_erreur_serveur('Impossible de séléctionner la base de données : '
	. mysql_error());
}

/* Définir l’encodage de charactères. */
$ok = mysql_set_charset('utf8');

if ( !$ok ) {
    reg_erreur_serveur('Erreur avec l’encodage de charactères : '
	. mysql_error());
}
?>
