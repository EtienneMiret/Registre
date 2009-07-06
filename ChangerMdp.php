<?php
require('connexion_bd.php');
require('utilitaires.php');

reg_verifier_authentification();

$id = $_COOKIE['IDRegistre'];
$old_pwd = $_POST['old_pwd'];
$new_pwd1 = $_POST['new_pwd1'];
$new_pwd2 = $_POST['new_pwd2'];

if( !is_null($old_pwd) or !is_null($new_pwd1) or !is_null($new_pwd2) )
{
    if(!reg_verifier_mdp($id, $old_pwd)) {
	require('header.php');
	?>
	<p><em class="erreur">Votre ancien mot de passe est faux.</em>
	<?php
    } elseif ( $new_pwd1 <> $new_pwd2 ) {
	require('header.php');
	?>
	<p><em class="erreur">Les deux nouveaux mots de passe sont différents.</em>
	<?php
    } else {
	$sel = dechex(mt_rand(0, 0xffff)) . dechex(mt_rand(0, 0xffff));
	$mdp = hash('md5', $sel . ':' . $new_pwd1 );
	$ok = mysql_query("UPDATE utilisateurs SET sel='$sel', mdp='$mdp' WHERE nom='" .
	    mysql_real_escape_string($id) . "';");
	if ( !$ok ) {
	    require('header.php'); ?>
	    <p><em class="erreur">Erreur MysQL lors de la mise à jour du mot de passe :
	    <?php echo htmlspecialchars(mysql_error()); ?>.</em>
	    <?php
	} else {
	    setcookie('PwdRegistre', $new_pwd1, 0, '/Registre/');
	    require('header.php'); ?>
	    <p>Votre mot de passe a été changé.
	    <?php
	}
    }
} else {
    require('header.php');
}

?>
<p>Retour à l’<a href="./">accueil</a>.

<form action="ChangerMdp" method="post">
<p>Ancien mot de passe : <input type="password" name="old_pwd">
<p>Nouveau mot de passe : <input type="password" name="new_pwd1">
<p>Répétez le nouveau mot de passe : <input type="password" name="new_pwd2">
<p><button type="submit">Changer</button>
</form>
