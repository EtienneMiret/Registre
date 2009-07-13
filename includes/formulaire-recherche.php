<form action="<?php echo $reg_racine; ?>Rechercher" method="get"
    class="recherche">
<p>Rechercher un film dans la base de donnée :<br>
<input name="q" type="text"<?php
if(isset($q)) echo ' value="' . htmlspecialchars($q) . '"' ?>>
<button type="submit">Rechercher</button>
</form>
