<?php
require('connexion_bd.php');
require('utilitaires.php');
require('header.php');

if (reg_verifier_mdp($_COOKIE['IDRegistre'], $_COOKIE['PwdRegistre'])) {
    echo '<p>Bienvenue ' . htmlspecialchars($_COOKIE['IDRegistre']) . ' !'; ?>
    <p><a href="ChangerMdp">Changer son mot de passe</a>.
    <p><a href="Deconnexion">Déconnexion</a>.
    <?php
} else {
    ?>
    <p>Bienvenue dans le Registre de la famille Miret.
    <?php
    require('formulaire-connexion.php');
}
?>
