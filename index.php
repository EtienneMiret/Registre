<?php
require('connexion_bd.php');
require('utilitaires.php');
require('header.php');

if (reg_verifier_mdp($_COOKIE['IDRegistre'], $_COOKIE['PwdRegistre'])) {
    echo '<p>Bienvenue ' . $_COOKIE['IDRegistre'] . ' !';
} else {
    ?>
    <p>Bienvenue dans le Registre de la famille Miret.
    <?php
    require('formulaire-connexion.php');
}
?>
