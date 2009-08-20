<?php
require('config.php');
require('includes/utilitaires.php');
require('includes/connexion_bd.php');

$reg_titre_page = 'Connexion';

if (isset($_REQUEST['retour'])
    && strpos($_REQUEST['retour'], '/') === 0
    && strpos($_REQUEST['retour'], "\n") === FALSE
    && strpos($_REQUEST['retour'], "\r") === FALSE)
{
    $retour=$_REQUEST['retour'];
} else {
    $retour=$reg_racine;
}

if (!isset($_POST['user']) || !isset($_POST['pwd'])) {
    require('includes/headers.php'); ?>
<p class="msg">Veuillez vous identifier
<?php
    require('includes/formulaire-connexion.php');
} elseif ($nom=reg_verifier_mdp($_POST['user'], $_POST['pwd'])) {
    reg_session_creer($nom, isset($_POST['rester']));
    reg_redirection($retour);
} else {
    require('includes/headers.php');
    ?>
    <p class="msg nok">Echec de l’authentification.
    <?php
    require('includes/formulaire-connexion.php');
}

?>
