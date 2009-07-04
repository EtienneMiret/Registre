<?php require('header.php'); ?>
<p>Bienvenue dans le Registre de la famille Miret.
<?php
if ($_COOKIE['IDRegistre']=='Etienne' && $_COOKIE['PwdRegistre']=='dummypass' )
{
    ?>
    <p>Bienvenue Etienne !
    <?php
} else {
    require('formulaire-connexion.php');
}
?>
