<?php
require('header.php');
if ($_COOKIE['IDRegistre']=='Etienne' && $_COOKIE['PwdRegistre']=='dummypass' )
{
    ?>
    <p>Bienvenue Etienne !
    <?php
} else {
    ?>
    <p>Bienvenue dans le Registre de la famille Miret.
    <?php
    require('formulaire-connexion.php');
}
?>
