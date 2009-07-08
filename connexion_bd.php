<?php

/* Connexion au serveur MySQL. */
$ok = mysql_connect(':/private/var/mysql/mysql.sock', 'registre', 'somepass');

if ( !$ok ) {
    require('headers.php'); ?>
    <p><em class="error">Impossible de se connecter au serveur MySQL : <?php
    echo htmlspecialchars(mysql_error()) ?></em>.<?php
    exit(1);
}

/* Sélection de la base de données. */
$ok = mysql_select_db('Registre');

if ( !$ok ) {
    require ('headers.php');
    ?><p><em class="error">Impossible de séléctionner la base de données : <?php
    echo htmlspecialchars(mysql_error()) ?></em>.<?php
    exit(1);
}

/* Définir l’encodage de charactères. */
$ok = mysql_set_charset('utf8');

if ( !$ok ) {
    require ('headers.php');
    ?><p><em class="error">Erreur avec l’encodage de charactères : <?php
    echo htmlspecialchars(mysql_error()) ?></em>.<?php
    exit(1);
}
?>
