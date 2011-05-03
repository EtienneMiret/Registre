<?php
require('config.php');
require('includes/definitions.php');
require('includes/initialiser.php');
require('includes/utilitaires.php');
require('includes/connexion_bd.php');

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
	require('includes/headers.php');
	?>
	<p><em class="erreur">Votre ancien mot de passe est faux.</em>
	<?php
    } elseif ( $new_pwd1 <> $new_pwd2 ) {
	require('includes/headers.php');
	?>
	<p><em class="erreur">Les deux nouveaux mots de passe sont différents.</em>
	<?php
    } else {
	$sel = dechex(mt_rand(0, 0xffff)) . dechex(mt_rand(0, 0xffff));
	$mdp = hash('md5', $sel . ':' . $new_pwd1 );
	$ok = mysql_query("UPDATE utilisateurs SET sel='$sel', mdp='$mdp' WHERE nom='" .
	    mysql_real_escape_string($user) . "'");
	if ( !$ok ) {
	    require('includes/headers.php'); ?>
	    <p><em class="erreur">Erreur MysQL lors de la mise à jour du mot de passe :
	    <?php echo htmlspecialchars(mysql_error()); ?>.</em>
	    <?php
	} else {
	    require('includes/headers.php'); ?>
<p class="msg ok">Votre mot de passe a été changé.
<p class="navigation">Retour à l’<a href="<?php echo $reg_racine; ?>">accueil</a>.
	    <?php
	    exit (0);
	}
    }
} else {
    require('includes/headers.php');
}

?>

<h2>Changer de mot de passe</h2>
<form action="ChangerMdp" method="post" class="changer-mdp">
<p>Ancien mot de passe : <input type="password" name="old_pwd">
<p>Nouveau mot de passe : <input type="password" name="new_pwd1">
<p>Répétez le nouveau mot de passe : <input type="password" name="new_pwd2">
<p><button type="submit">Changer</button>
</form>
