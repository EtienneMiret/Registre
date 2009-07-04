<?php

if ($_POST['id'] == "Etienne" && $_POST['pwd'] == "dummypass") {
    header('HTTP/1.1 303 See Other');
    header("Location: http://ibook-g4.elimerl.fr/Registre/");
    setcookie('IDRegistre', $_POST['id'], 0, '/Registre/');
    setcookie('PwdRegistre', $_POST['pwd'], 0, '/Registre/');
} else {
    require('header.php');
    ?>
    <p>Echec de l’authentification.
    <?php
    require('formulaire-connexion.php');
}

?>
