<form action="Connexion" method="post" class="connexion">
<p><label>Nom : <input type="text" name="user"<?php
/* On tente de préremplir le nom d’utilisateur. */
if (isset($_POST['user'])) {
    echo ' value="' . htmlspecialchars($_POST['user']) . '"';
} elseif (isset($_COOKIE['UtilisateurRegistre'])) {
    echo ' value="' . htmlspecialchars($_COOKIE['UtilisateurRegistre']) . '"';
}
?>></label>
<p><label>Mot de passe : <input type="password" name="pwd"></label>
<p><label><input type="checkbox" name="rester"<?php
/* Si le formulaire vient d’être soumis, on récupère l’état de la case
 * dans la variable POST, et sinon dans le cookie. */
if (isset($_POST['user'])) {
    if (isset($_POST['rester'])) echo ' checked="yes"';
} else {
    if (isset($_COOKIE['UtilisateurRegistre'])) echo ' checked="yes"';
}
?>>Rester connecté après la fermeture de mon
    navigateur.</label>
<p><?php if (isset($_REQUEST['retour'])) { ?>
<input type="hidden" name="retour" value="<?php
    echo htmlspecialchars($_REQUEST['retour']); ?>">
<?php } ?><button type="submit">Connexion</button>
</form>
