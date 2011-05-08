<?php
require('config.php');
require('includes/definitions.php');
require('includes/initialiser.php');
require('includes/utilitaires.php');
require('includes/connexion_bd.php');

$reg_user = reg_session_verifier();
$reg_titre_page = 'Accueil';
$reg_page = PAGE_HOME;

require('includes/headers.php');

if ($reg_user) {
    require('includes/nav-bar.php');
    require('includes/debut-contenu-principal.php'); ?>
    <h2>Bienvenue <?php echo htmlspecialchars($reg_user); ?> !</h2>

    <p>Liste de
	<a href="Rechercher?q=">toutes les références</a>.
    <p><a href="RechercheAvancee">Recherche avancée</a>.
    <p><a href="Enregistrer">Ajouter une référence</a>.
    <p><a href="ChangerMdp">Changer son mot de passe</a>.
    <p><a href="Aide">Aide</a>.
    <p><a href="Deconnexion">Déconnexion</a>.<?php
	
} else {
    require('includes/debut-contenu-principal.php');
    ?>
    
	<h2>Bienvenue dans le Registre de la famille Miret.</h2>
	<?php require('includes/formulaire-connexion.php');
} ?>

<?php require('includes/footer.php'); ?>
