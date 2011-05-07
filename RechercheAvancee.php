<?php
require('config.php');
require('includes/definitions.php');
require('includes/initialiser.php');
require('includes/utilitaires.php');
require('includes/connexion_bd.php');

$user = reg_authentifier();
$reg_titre_page = 'Recherche avancée';
$reg_page = PAGE_RECHERCHE_AVANCEE;

header('Content-Script-Type: application/ecmascript');
$reg_head[]='<style type="text/css"> </style>';
$reg_head[]='<script type="application/ecmascript" src="registre"></script>';
$reg_onload='masquerChampsInutilises(document.getElementById("type").value);';

require('includes/headers.php');
require('includes/nav-bar.php');
?>
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
  <li><label>N’importe quel champ contient : <input name="q"></label>
  <li><label>Titre : <input name="titre"></label>
  <li class="film"><label>Réalisateur : <input name="realisateur"></label>
  <li class="film"><label>Acteurs : <input name="acteur"></label>
  <li class="film"><label>Compositeur : <input name="compositeur"></label>
  <li class="livre"><label>Auteur : <input name="auteur"></label>
  <li class="bd"><label>Dessinateur : <input name="dessinateur"></label>
  <li class="bd"><label>Scénariste : <input name="scenariste"></label>
  <li class="bd"><label>Série : <input name="serie"></label>
  <li><label>Commentaire : <input name="commentaire"></label>
  <li><label>Proprietaire : <input name="proprietaire"></label>
  <li><label>Emplacement : <input name="emplacement"></label>
  <li><label>Enregistré par : <input name="createur"></label>
  <li><label>Dernière modification par : <input name="editeur"></label>
</ul>
<p><button>Rechercher</button>
</form>
