<?php
require('config.php');
require('includes/definitions.php');
require('includes/initialiser.php');
require('includes/utilitaires.php');
require('includes/connexion_bd.php');

$user = reg_session_verifier();
$reg_titre_page = 'Accueil';

require('includes/headers.php');

if ($user) {
    echo '<p>Bienvenue ' . htmlspecialchars($user) . ' !';
    ?>
    <p class="navigation">Liste de
	<a href="Rechercher?q=">toutes les références</a>.
    <p class="navigation"><a href="RechercheAvancee">Recherche avancée</a>.
    <p class="navigation"><a href="Enregistrer">Ajouter une référence</a>.
    <p class="navigation"><a href="ChangerMdp">Changer son mot de passe</a>.
    <p class="navigation"><a href="Aide">Aide</a>.
    <p class="navigation"><a href="Deconnexion">Déconnexion</a>.
    <?php
} else {
    ?>
    <p class="msg">Bienvenue dans le Registre de la famille Miret.
    <?php
    require('includes/formulaire-connexion.php');
}
?>
