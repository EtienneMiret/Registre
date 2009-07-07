<?php
require('connexion_bd.php');
require('utilitaires.php');

if (reg_verifier_mdp($_POST['id'], $_POST['pwd'])) {
    header('HTTP/1.1 303 See Other');
    header("Location: http://ibook-g4.elimerl.fr/Registre/");
    setcookie('IDRegistre', $_POST['id'], 0, '/Registre/');
    setcookie('PwdRegistre', $_POST['pwd'], 0, '/Registre/');
} else {
    require('headers.php');
    ?>
    <p>Echec de l’authentification.
    <?php
    require('formulaire-connexion.php');
}

?>
