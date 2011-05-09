	<form action="Connexion" method="post" class="connexion">
		<div class="form-line">
			<label for="user">Nom :</label>
			<input type="text" name="user" id="user" <?php
			/* On tente de préremplir le nom d’utilisateur. */
			if (isset($_POST['user'])) {
				echo ' value="' . htmlspecialchars($_POST['user']) . '"';
			} elseif (isset($_COOKIE['UtilisateurRegistre'])) {
				echo ' value="' . htmlspecialchars($_COOKIE['UtilisateurRegistre']) . '"';
			}
			?> />
		</div>

		<div class="form-line">
			<label for="pwd">Mot de passe :</label>
			<input type="password" name="pwd" id="pwd"/>
		</div>

		<div class="form-line with-checkbox">
			<label for="rester">Rester connecté après la fermeture de mon navigateur.</label>
			<input type="checkbox" name="rester" id="rester" <?php
			/* Si le formulaire vient d’être soumis, on récupère l’état de la case
			 * dans la variable POST, et sinon dans le cookie. */
			if (isset($_POST['user'])) {
				if (isset($_POST['rester'])) echo ' checked="yes"';
			} else {
				if (isset($_COOKIE['UtilisateurRegistre'])) echo ' checked="yes"';
			}
			?> />
			<?php if (isset($_REQUEST['retour'])) { ?>
			<input type="hidden" name="retour" value="<?php
				echo htmlspecialchars($_REQUEST['retour']); ?>">
			<?php } ?>
		</div>
		
		<button type="submit">Connexion</button>
	</form>
