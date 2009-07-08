<?php
require('utilitaires.php');
require('connexion_bd.php');

if (!isset($_POST['id']) || !isset($_POST['pwd'])) {
    require('headers.php');
    require('formulaire-connexion.php');
} elseif (reg_verifier_mdp($_POST['id'], $_POST['pwd'])) {
    reg_session_creer($_POST['id']);
    reg_redirection_accueil();
} else {
    require('headers.php');
    ?>
    <p>Echec de l’authentification.
    <?php
    require('formulaire-connexion.php');
}

?>
