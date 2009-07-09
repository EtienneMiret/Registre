<?php
require('utilitaires.php');
require('connexion_bd.php');

$id = reg_session_verifier();

require('headers.php');

if ($id) {
    echo '<p>Bienvenue ' . htmlspecialchars($id) . ' !'; ?>
    <p><a href="Enregistrer">Enregistrer un nouveau film</a>.
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
