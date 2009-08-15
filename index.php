<?php
require('config.php');
require('includes/utilitaires.php');
require('includes/connexion_bd.php');

$user = reg_session_verifier();
$reg_titre_page = 'Accueil';

require('includes/headers.php');

if ($user) {
    echo '<p>Bienvenue ' . htmlspecialchars($user) . ' !';
    require('includes/formulaire-recherche.php');
    ?>
    <p class="navigation">Liste de
	<a href="Rechercher?q=">tous les films</a> référencés.
    <p class="navigation"><a href="RechercheAvancee">Recherche avancée</a>.
    <p class="navigation"><a href="Enregistrer">Référencer un nouveau film</a>.
    <p class="navigation"><a href="ChangerMdp">Changer son mot de passe</a>.
    <p class="navigation"><a href="Deconnexion">Déconnexion</a>.
    <?php
} else {
    ?>
    <p class="msg">Bienvenue dans le Registre de la famille Miret.
    <?php
    require('includes/formulaire-connexion.php');
}
?>
