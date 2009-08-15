<?php
require('config.php');
require('includes/utilitaires.php');
require('includes/connexion_bd.php');

$user = reg_authentifier();
$reg_titre_page = 'Recherche avancée';

require('includes/headers.php');
?>
<p class="navigation">Retour à l’<a href="<?php echo $reg_racine; ?>">accueil</a>.
<form action="Rechercher" class="recherche-avancee">
<p>Dans n’importe quel champ : <input name="q">
<p>Titre : <input name="titre">
<p>Réalisateur : <input name="realisateur">
<p>Acteurs : <input name="acteur">
<p>Compositeur : <input name="compositeur">
<p>Commentaire : <input name="commentaire">
<p>Proprietaire : <input name="proprietaire">
<p>Emplacement : <input name="emplacement">
<p>Enregistré par : <input name="createur">
<p>Dernière modification par : <input name="editeur">
<p><button>Rechercher</button>
</form>
<p class="navigation">Retour à l’<a href="<?php echo $reg_racine; ?>">accueil</a>.
