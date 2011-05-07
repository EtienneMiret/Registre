<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<?php if ($reg_titre_page=='Accueil' or $reg_titre_page=='') { ?>
<title><?php echo htmlspecialchars($reg_nom); ?></title>
<?php } else { ?>
<title><?php echo htmlspecialchars($reg_titre_page); ?> - <?php
    echo htmlspecialchars($reg_nom) ?></title>
<?php } ?>
<link rel="stylesheet" type="text/css" href="<?php
    echo htmlspecialchars($reg_racine); ?>main">
<link rel="stylesheet" type="text/css" href="<?php
    echo htmlspecialchars($reg_racine); ?>v2">
<?php foreach($reg_head as $balise) {
    echo $balise . PHP_EOL;
} ?>
<body<?php if ($reg_onload) {
    echo ' onload="' . htmlspecialchars($reg_onload) . '"';
} ?>>
<?php if ($reg_user) { ?>
<div id="bouton-connexion">
Bienvenue <?php echo htmlspecialchars($reg_user); ?> ! |
<a href="<?php
    echo htmlspecialchars($reg_racine); ?>Deconnexion">Déconnexion</a>
</div>
<?php } ?>
<h1>Registre</h1>
<hr>
