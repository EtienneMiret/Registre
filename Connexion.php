<?php
require('config.php');
require('includes/definitions.php');
require('includes/initialiser.php');
require('includes/utilitaires.php');
require('includes/connexion_bd.php');

$reg_titre_page = 'Connexion';
$reg_page = PAGE_CONNEXION;

if (isset($_REQUEST['retour'])
    && strpos($_REQUEST['retour'], '/') === 0
    && strpos($_REQUEST['retour'], "\n") === FALSE
    && strpos($_REQUEST['retour'], "\r") === FALSE)
{
    $retour=$_REQUEST['retour'];
} else {
    $retour=$reg_racine;
}

if (reg_session_verifier()) {
    reg_redirection($retour);
}

if (!isset($_POST['user']) || !isset($_POST['pwd'])) {
    require('includes/headers.php');
?>
      <p class="msg msg-ok">Veuillez vous identifier
<?php } elseif ($nom=reg_verifier_mdp($_POST['user'], $_POST['pwd'])) {
    reg_session_creer($nom, isset($_POST['rester']));
    reg_redirection($retour);
} else {
    require('includes/headers.php');
?>
      <p class="msg msg-nok">Echec de l’authentification.
<?php } ?>

      <form action="Connexion" method="post" class="connexion">
	<div class="form-line">
	  <label for="user">Nom :</label>
	  <input type="text" name="user" id="user"<?php
/* On tente de préremplir le nom d’utilisateur. */
if (isset($_POST['user'])) {
    echo ' value="' . htmlspecialchars($_POST['user']) . '"';
} elseif (isset($_COOKIE['UtilisateurRegistre'])) {
    echo ' value="' . htmlspecialchars($_COOKIE['UtilisateurRegistre']) . '"';
} ?>>
	</div>

	<div class="form-line">
	  <label for="pwd">Mot de passe :</label>
	  <input type="password" name="pwd" id="pwd">
	</div>

	<div class="form-line with-checkbox">
	  <label for="rester">Rester connecté après la fermeture de mon navigateur.</label>
	  <input type="checkbox" name="rester" id="rester"<?php
/* Si le formulaire vient d’être soumis, on récupère l’état de la case
 * dans la variable POST, et sinon dans le cookie. */
if (isset($_POST['user'])) {
    if (isset($_POST['rester'])) echo ' checked';
} else {
    if (isset($_COOKIE['UtilisateurRegistre'])) echo ' checked';
} ?>>
<?php if (isset($_REQUEST['retour'])) { ?>
	  <input type="hidden" name="retour" value="<?php echo htmlspecialchars($_REQUEST['retour']); ?>">
<?php } ?>
	</div>
		
	<button type="submit">Connexion</button>
      </form>
<?php require('includes/footer.php'); ?>
