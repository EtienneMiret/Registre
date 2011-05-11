<?php
require('config.php');
require('includes/definitions.php');
require('includes/initialiser.php');
require('includes/utilitaires.php');
require('includes/connexion_bd.php');

$reg_user = reg_authentifier();
$reg_titre_page = 'Recherche avancée';
$reg_page = PAGE_RECHERCHE_AVANCEE;

header('Content-Script-Type: application/ecmascript');
$reg_head[]='<style type="text/css"> </style>';
$reg_head[]='<script type="application/ecmascript" src="registre"></script>';
$reg_onload='masquerChampsInutilises(document.getElementById("type").value);';

require('includes/headers.php');
?>
	<form action="./" class="recherche-avancee">
		<p>Rechercher 
			<select name="type" id="type" onchange="masquerChampsInutilises(this.value);">
				<option value="" selected>toutes les références</option>
				<option value="Disque Blu-ray">les disques Blu-ray</option>
				<option value="DVD">les DVD</option>
				<option value="Cassette">les cassettes</option>
				<option value="Livre">les livres</option>
				<option value="BD">les BD</option>
			</select> qui vérifient les critères suivant :
		</p>
		<ul>
			<li class="form-line">
				<label for="q">N’importe quel champ contient :</label>
				<input type="text" name="q" id="q"/>
			</li>
			<li class="form-line">
				<label for="titre">Titre :</label>
				<input type="text" name="titre" id="titre">
			</li>
			<li class="film form-line">
				<label for="realisateur">Réalisateur :</label>
				<input type="text" name="realisateur" id="realisateur"/>
			</li>
			<li class="film form-line">
				<label for="acteur">Acteurs :</label>
				<input type="text" name="acteur" id="acteur"/>
			</li>
			<li class="film form-line">
				<label for="compositeur">Compositeur :</label>
				<input type="text" name="compositeur" id="compositeur"/>
			</li>
			<li class="livre form-line">
				<label for="auteur">Auteur :</label>
				<input type="text" name="auteur" id="auteur"/>
			</li>
			<li class="bd form-line">
				<label for="dessinateur">Dessinateur :</label>
				<input type="text" name="dessinateur" id="dessinateur"/>
			</li>
			<li class="bd form-line">
				<label for="scenariste">Scénariste :</label>
				<input type="text" name="scenariste" id="scenariste"/>
			</li>
			<li class="bd form-line">
				<label for="serie">Série :</label>
				<input type="text" name="serie" id="serie"/>
			</li>
			<li class="form-line">
				<label for="commentaire">Commentaire :</label>
				<input type="text" name="commentaire" id="commentaire"/>
			</li>
			<li class="form-line">
				<label for="proprietaire">Proprietaire :</label>
				<input type="text" name="proprietaire" id="proprietaire"/>
			</li>
			<li class="form-line">
				<label for="emplacement">Emplacement :</label> 
				<input type="text" name="emplacement" id="emplacement"/>
			</li>
			<li class="form-line">
				<label for="createur">Enregistré par :</label>
				<input type="text" name="createur" id="createur"/>
			</li>
			<li class="form-line">
				<label for="editeur">Dernière modification par :</label>
				<input type="text" name="editeur" id="editeur"/>
			</li>
		</ul>
		<button>Rechercher</button>
	</form>
	
<?php require('includes/footer.php'); ?>
