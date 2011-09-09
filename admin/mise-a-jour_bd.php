<?php
require('../includes/cli_uniquement.php');
require('../config.php');

?>
Ce script met à jour la base de données pour la version courante de registre.
La base de données DOIT avoir été préalablement créée, éventuellement pour une
ancienne version de Registre.

<?php

/* Connexion à la base de données. */
mysql_connect($reg_serveur_bd, $reg_utilisateur_admin_bd, $reg_mdp_admin_bd)
    or die('Erreur de connexion à la base de données : '.mysql_error().PHP_EOL);
mysql_select_db($reg_nom_bd)
    or die('Erreur de connexion à la base de données : '.mysql_error().PHP_EOL);
mysql_set_charset('utf8')
    or die('Erreur lors de la définition de l\'encodage de caractères : '
    . mysql_error().PHP_EOL);

/* Récupération des informations sur l'état de la base de données. */
$tables = mysql_query('SHOW TABLES;')
    or die ('Erreur MySQL : ' . mysql_error() . PHP_EOL);
$films = mysql_query('DESCRIBE films;')
    or die ('Erreur MySQL : ' . mysql_error() . PHP_EOL);
$tout = mysql_query('DESCRIBE tout;')
    or die ('Erreur MySQL : ' . mysql_error() . PHP_EOL);

/* Mise à jour de la base de données. */

/* Champ 'compositeur' dans la table 'films' (changement c99aaacb928d). */
echo 'Champ \'compositeur\' dans la table \'films\'...';
while ($ligne=mysql_fetch_assoc($films)) {
    if ($ligne['Field']=='compositeur') break;
}
if ($ligne) {
    echo ' ok.'.PHP_EOL;
} else {
    mysql_query('ALTER TABLE films ADD compositeur varchar(20)')
	or die ('Erreur MySQL : ' . mysql_error() . PHP_EOL);
    echo ' ajouté.'.PHP_EOL;
}

/* Champ 'genres' dans la table 'films'. */
mysql_data_seek($films, 0);
echo 'Champ \'genres\' dans la table \'films\'...';
while ($ligne=mysql_fetch_assoc($films)) {
    if ($ligne['Field']=='genres') break;
}
if ($ligne) {
    echo ' ok.'.PHP_EOL;
} else {
    mysql_query('ALTER TABLE films ADD genres SET("action","documentaire",' .
	    '"fantastique","film de guerre","histoire vraie","historique",' .
	    '"humour","policier","romantique","science-fiction")')
	or die ('Erreur MySQL : ' . mysql_error() . PHP_EOL);
    echo ' ajouté.'.PHP_EOL;
}

/* Table 'bd'. */
mysql_data_seek($tables, 0);
echo 'Table \'bd\'...';
while ($ligne=mysql_fetch_row($tables)) {
    if ($ligne[0]=='bd') break;
}
if ($ligne) {
    echo ' ok.' . PHP_EOL;
} else {
    mysql_query('CREATE TABLE bd (id INT UNSIGNED PRIMARY KEY,' .
	    ' dessinateur VARCHAR(20), scenariste VARCHAR(20),' .
	    ' serie VARCHAR(80), numero INT UNSIGNED) DEFAULT CHARSET utf8;')
	or die ('Erreur MySQL : ' . mysql_error() . PHP_EOL);
    echo ' ajouté.' . PHP_EOL;
}

/* Table 'livres'. */
mysql_data_seek($tables, 0);
echo 'Table \'livres\'...';
while ($ligne=mysql_fetch_row($tables)) {
    if ($ligne[0]=='livres') break;
}
if ($ligne) {
    echo ' ok.' . PHP_EOL;
} else {
    mysql_query('CREATE TABLE livres (id INT UNSIGNED PRIMARY KEY,' .
	    ' auteur VARCHAR(20), genres SET("fantastique","histoire vraie",' .
	    '"historique","humour","policier","romantique",' .
	    '"science-fiction")) DEFAULT CHARACTER SET utf8;')
	or die ('Erreur MySQL : ' . mysql_error() . PHP_EOL);
    echo ' ajouté.' . PHP_EOL;
}

/* Ajout des disques Blu-ray. */
mysql_data_seek($tout, 0);
echo 'Prise en charge des Blu-ray...';
while ($ligne=mysql_fetch_assoc($tout)) {
    if ($ligne['Field'] == 'type') break;
}
if (is_null($ligne))
    die('Erreur : pas de champ \'type\' dans la table \'tout\' !' . PHP_EOL);
if (strpos($ligne['Type'], 'disque Blu-ray')!==FALSE) {
    echo ' ok.' . PHP_EOL;
} else {
    mysql_query('ALTER TABLE tout CHANGE type ' .
	    'type ENUM("disque Blu-ray","DVD","cassette","livre","BD");')
	or die ('Erreur MysQL : ' . mysql_error() . PHP_EOL);
    echo ' ajoutée.' . PHP_EOL;
}

/* Champs « série » généralisé (changement 8b9943c2142c). */
mysql_data_seek($tout, 0);
echo 'Champ \'serie\' dans la table \'tout\'...';
while ($ligne=mysql_fetch_assoc($tout)) {
    if ($ligne['Field']=='serie') break;
}
if ($ligne) {
    echo ' ok.'.PHP_EOL;
} else {
    mysql_query('ALTER TABLE tout ADD serie VARCHAR(80) AFTER titre')
        or die ('Erreur MySQL : ' . mysql_error() . PHP_EOL);
    mysql_query('UPDATE tout,bd SET tout.serie=bd.serie WHERE tout.id=bd.id')
        or die ('Erreur MySQL : ' . mysql_error() . PHP_EOL);
    mysql_query('ALTER TABLE bd DROP serie')
        or die ('Erreur MySQL : ' . mysql_error() . PHP_EOL);
    echo ' déplacé.'.PHP_EOL;
}

/* Ajout du champ « image ». */
mysql_data_seek($tout, 0);
echo 'Champ \'image\'...';
while ($ligne=mysql_fetch_assoc($tout)) {
    if ($ligne['Field']=='image') break;
}
if ($ligne) {
    echo ' ok.' . PHP_EOL;
} else {
    mysql_query('ALTER TABLE tout ADD image enum(\'YES\') default NULL
		AFTER commentaire');
	or die ('Erreur MySQL : ' . mysql_error() . PHP_EOL)
    echo ' ajouté.' . PHP_EOL;
}
