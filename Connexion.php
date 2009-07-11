<?php
require('utilitaires.php');
require('connexion_bd.php');

$reg_titre_page = 'Connexion';

if (!isset($_POST['user']) || !isset($_POST['pwd'])) {
    require('headers.php');
    require('formulaire-connexion.php');
} elseif (reg_verifier_mdp($_POST['user'], $_POST['pwd'])) {
    reg_session_creer($_POST['user']);
    reg_redirection_accueil();
} else {
    require('headers.php');
    ?>
    <p>Echec de l’authentification.
    <?php
    require('formulaire-connexion.php');
}

?>
