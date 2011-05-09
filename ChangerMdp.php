<?php
require('config.php');
require('includes/definitions.php');
require('includes/initialiser.php');
require('includes/utilitaires.php');
require('includes/connexion_bd.php');

$reg_user = reg_authentifier();
$reg_titre_page = 'Changer de mot de passe';
$reg_page = PAGE_PROFIL;

$old_pwd = null;
$new_pwd1 = null;
$new_pwd2 = null;

if (isset($_POST['old_pwd']))  $old_pwd = $_POST['old_pwd'];
if (isset($_POST['new_pwd1'])) $new_pwd1 = $_POST['new_pwd1'];
if (isset($_POST['new_pwd2'])) $new_pwd2 = $_POST['new_pwd2'];

if( !is_null($old_pwd) or !is_null($new_pwd1) or !is_null($new_pwd2) )
{
    if(!reg_verifier_mdp($reg_user, $old_pwd)) {
	require('includes/headers.php');
	?>
	<p class="msg msg-nok">Votre ancien mot de passe est faux.</p>
	<?php
    } elseif ( $new_pwd1 <> $new_pwd2 ) {
	require('includes/headers.php');
	?>
	<p class="msg msg-nok">Les deux nouveaux mots de passe sont différents.</p>
	<?php
    } else {
	$sel = dechex(mt_rand(0, 0xffff)) . dechex(mt_rand(0, 0xffff));
	$mdp = hash('md5', $sel . ':' . $new_pwd1 );
	$ok = mysql_query("UPDATE utilisateurs SET sel='$sel', mdp='$mdp' WHERE nom='" .
	    mysql_real_escape_string($reg_user) . "'");
	if ( !$ok ) {
	    require('includes/headers.php');
	    ?>
	    <p class="msg msg-nok">Erreur MysQL lors de la mise à jour du mot de passe :
	    <?php echo htmlspecialchars(mysql_error()); ?>.</p>
	    <?php
	} else {
	    require('includes/headers.php');
	    ?>
<p class="msg msg-ok">Votre mot de passe a été changé.
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
		<div class="form-row">
			<label for="old_pwd">Ancien mot de passe :</label>
			<input type="password" name="old_pwd" id="old_pwd">
		</div>

		<div class="form-row">
			<label for="new_pwd1">Nouveau mot de passe :</label>
			<input type="password" name="new_pwd1" id="new_pwd1">
		</div>

		<div class="form-row">
			<label for="new_pwd2">Répétez le nouveau mot de passe :</label>
			<input type="text" name="new_pwd2" id="new_pwd2">
		</div>

		<button type="submit">Changer</button>
	</form>

	<?php require('includes/footer.php'); ?>
