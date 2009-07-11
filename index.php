<?php
require('utilitaires.php');
require('connexion_bd.php');

$user = reg_session_verifier();
$reg_titre_page = 'Accueil';

require('headers.php');

if ($user) {
    echo '<p>Bienvenue ' . htmlspecialchars($user) . ' !';
    require('formulaire-recherche.php');
    ?>
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
