<form action="Connexion" method="post" class="connexion">
<p>Nom : <input type="text" name="user">
<p>Mot de passe : <input type="password" name="pwd">
<p><?php if (isset($_REQUEST['retour'])) { ?>
<input type="hidden" name="retour" value="<?php
    echo htmlspecialchars($_REQUEST['retour']); ?>">
<?php } ?><button type="submit">Connexion</button>
</form>
