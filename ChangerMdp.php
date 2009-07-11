<?php
require('utilitaires.php');
require('connexion_bd.php');

$user = reg_authentifier();
$reg_titre_page = 'Changer de mot de passe';

$old_pwd = null;
$new_pwd1 = null;
$new_pwd2 = null;

if (isset($_POST['old_pwd']))  $old_pwd = $_POST['old_pwd'];
if (isset($_POST['new_pwd1'])) $new_pwd1 = $_POST['new_pwd1'];
if (isset($_POST['new_pwd2'])) $new_pwd2 = $_POST['new_pwd2'];

if( !is_null($old_pwd) or !is_null($new_pwd1) or !is_null($new_pwd2) )
{
    if(!reg_verifier_mdp($user, $old_pwd)) {
	require('headers.php');
	?>
	<p><em class="erreur">Votre ancien mot de passe est faux.</em>
	<?php
    } elseif ( $new_pwd1 <> $new_pwd2 ) {
	require('headers.php');
	?>
	<p><em class="erreur">Les deux nouveaux mots de passe sont différents.</em>
	<?php
    } else {
	$sel = dechex(mt_rand(0, 0xffff)) . dechex(mt_rand(0, 0xffff));
	$mdp = hash('md5', $sel . ':' . $new_pwd1 );
	$ok = mysql_query("UPDATE utilisateurs SET sel='$sel', mdp='$mdp' WHERE nom='" .
	    mysql_real_escape_string($user) . "'");
	if ( !$ok ) {
	    require('headers.php'); ?>
	    <p><em class="erreur">Erreur MysQL lors de la mise à jour du mot de passe :
	    <?php echo htmlspecialchars(mysql_error()); ?>.</em>
	    <?php
	} else {
	    require('headers.php'); ?>
	    <p class="msg ok">Votre mot de passe a été changé.
	    <?php
	}
    }
} else {
    require('headers.php');
}

?>
<p class="navigation"><a href="/Registre/">Annuler</a>

<form action="ChangerMdp" method="post" class="changer-mdp">
<p>Ancien mot de passe : <input type="password" name="old_pwd">
<p>Nouveau mot de passe : <input type="password" name="new_pwd1">
<p>Répétez le nouveau mot de passe : <input type="password" name="new_pwd2">
<p><button type="submit">Changer</button>
</form>
