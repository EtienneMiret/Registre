<?php
require('utilitaires.php');
require('connexion_bd.php');

if (reg_verifier_mdp($_POST['id'], $_POST['pwd'])) {
    header('HTTP/1.1 303 See Other');
    header("Location: http://ibook-g4.elimerl.fr/Registre/");
    reg_session_creer($_POST['id']);
} else {
    require('headers.php');
    ?>
    <p>Echec de l’authentification.
    <?php
    require('formulaire-connexion.php');
}

?>
