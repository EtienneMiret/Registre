<?php
require('utilitaires.php');
require('connexion_bd.php');
require('headers.php');

$id = reg_session_verifier();

if ($id) {
    echo '<p>Bienvenue ' . htmlspecialchars($id) . ' !'; ?>
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
