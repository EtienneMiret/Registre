<?php
require('header.php');

if ($_POST["id"] == "Etienne" && $_POST["pwd"] == "dummypass") {
    ?>
    <p>Authentification réussie !
    <?php
} else {
    ?>
    <p>Echec de l’authentification.
    <?php
    require('formulaire-connexion.php');
}

?>
