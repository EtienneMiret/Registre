<?php
require('config.php');
require('includes/initialiser.php');
require('includes/utilitaires.php');
require('includes/connexion_bd.php');

$user = reg_authentifier();
$reg_titre_page = 'Recherche avancée';

header('Content-Script-Type: application/ecmascript');
$reg_head[]='<style type="text/css"> </style>';
$reg_head[]='<script type="application/ecmascript" src="registre"></script>';
$reg_onload='masquerChampsInutilises(document.getElementById("type").value);';

require('includes/headers.php');
?>
<p class="navigation">Retour à l’<a href="<?php echo $reg_racine; ?>">accueil</a>.
<p class="navigation"><a href="Aide#Recherche">Aide</a>.
<form action="Rechercher" class="recherche-avancee">
<p class="non-fini">Rechercher <select name="type" id="type"
    onchange="masquerChampsInutilises(this.value);">
  <option value="" selected>toutes les références</option>
  <option value="Disque Blu-ray">les disques Blu-ray</option>
  <option value="DVD">les DVD</option>
  <option value="Cassette">les cassettes</option>
  <option value="Livre">les livres</option>
  <option value="BD">les BD</option>
</select> qui vérifient les critères suivant :

<ul>
  <li>N’importe quel champ contient : <input name="q">
  <li>Titre : <input name="titre">
  <li class="film">Réalisateur : <input name="realisateur">
  <li class="film">Acteurs : <input name="acteur">
  <li class="film">Compositeur : <input name="compositeur">
  <li class="livre">Auteur : <input name="auteur">
  <li class="bd">Dessinateur : <input name="dessinateur">
  <li class="bd">Scénariste : <input name="scenariste">
  <li class="bd">Série : <input name="serie">
  <li>Commentaire : <input name="commentaire">
  <li>Proprietaire : <input name="proprietaire">
  <li>Emplacement : <input name="emplacement">
  <li>Enregistré par : <input name="createur">
  <li>Dernière modification par : <input name="editeur">
</ul>
<p><button>Rechercher</button>
</form>
<p class="navigation">Retour à l’<a href="<?php echo $reg_racine; ?>">accueil</a>.
