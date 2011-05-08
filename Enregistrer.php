<?php
require('config.php');
require('includes/definitions.php');
require('includes/initialiser.php');
require('includes/utilitaires.php');
require('includes/connexion_bd.php');

/* Nombre de lignes par défaut pour enregistrer des acteurs. */
define("NOMBRE_LIGNES_ACTEURS", 3);

$reg_user = reg_authentifier();
$reg_titre_page = 'Enregistrement d’une nouvelle référence';
$reg_page = PAGE_AJOUTER;

header('Content-Script-Type: application/ecmascript');
$reg_head[]='<style type="text/css">.genres.livre { display: none; }</style>';
$reg_head[]='<script type="application/ecmascript" src="registre"></script>';
$reg_onload='masquerChampsInutilises(document.getElementById("type").value); '
    . 'ajouterBoutonListeActeurs();';

$titre='';
$proprietaire='';
$type='DVD';
$emplacement='';
$commentaire='';
$realisateur='';
$compositeur='';
$acteurs=array();
$auteur='';
$dessinateur='';
$scenariste='';
$serie='';
$numero='';

for ($i=0; $i < NOMBRE_LIGNES_ACTEURS; $i++) $acteurs[]='';

$g_action=FALSE;
$g_docu=FALSE;
$g_fantastique=FALSE;
$g_guerre=FALSE;
$g_vrai=FALSE;
$g_historique=FALSE;
$g_humour=FALSE;
$g_policier=FALSE;
$g_romantique=FALSE;
$g_SF=FALSE;

if (isset($_POST['titre'])) $titre=$_POST['titre'];
if (isset($_POST['proprietaire'])) $proprietaire=$_POST['proprietaire'];
if (isset($_POST['type'])) $type=$_POST['type'];
if (isset($_POST['emplacement'])) $emplacement=$_POST['emplacement'];
if (isset($_POST['commentaire'])) $commentaire=$_POST['commentaire'];
if (isset($_POST['realisateur'])) $realisateur=$_POST['realisateur'];
if (isset($_POST['compositeur'])) $compositeur=$_POST['compositeur'];
if (isset($_POST['auteur'])) $auteur=$_POST['auteur'];
if (isset($_POST['dessinateur'])) $dessinateur=$_POST['dessinateur'];
if (isset($_POST['scenariste'])) $scenariste=$_POST['scenariste'];
if (isset($_POST['serie'])) $serie=$_POST['serie'];
if (isset($_POST['numero'])) $numero=$_POST['numero'];
if (isset($_POST['acteur0'])) {
    $acteurs=array();
    for ($i=0; isset($_POST['acteur'.$i]); $i++)
	$acteurs[$i]=$_POST['acteur'.$i];
}

if (!in_array($type, array('Disque Blu-ray', 'DVD', 'Cassette', 'Livre', 'BD')))
    $type='DVD';
if ($type=='BD' && !preg_match('/^\d*$/', $numero)) $numero='';

$reg_head[]='<script type="application/ecmascript">nombreLignesActeurs='
    . count($acteurs) . '</script>';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $g_action=isset($_POST['action']);
    $g_docu=isset($_POST['docu']);
    $g_fantastique=isset($_POST['fantastique']);
    $g_guerre=isset($_POST['guerre']);
    $g_vrai=isset($_POST['vrai']);
    $g_historique=isset($_POST['historique']);
    $g_humour=isset($_POST['humour']);
    $g_policier=isset($_POST['policier']);
    $g_romantique=isset($_POST['romantique']);
    $g_SF=isset($_POST['SF']);
}

if ($_SERVER['REQUEST_METHOD'] <> 'POST') {
    require('includes/headers.php');
} elseif ($titre<>'') {
    $ok = mysql_query('INSERT INTO tout VALUES( NULL, '
	. reg_mysql_quote_string($titre) . ', '
	. reg_mysql_quote_string($proprietaire) . ', '
	. reg_mysql_quote_string($type) . ', '
	. reg_mysql_quote_string($emplacement) . ', '
	. reg_mysql_quote_string($commentaire) . ', '
	. reg_mysql_quote_string($reg_user) . ', NOW(), NULL, NULL)');
    if (!$ok) reg_erreur_mysql();

    $id = mysql_insert_id();
    if (!$id) reg_erreur_mysql();

    switch($type) {

	/* Enregistrement d’un film. */

	case 'Disque Blu-ray':
	case 'DVD':
	case 'Cassette':
	    $genres = '';
	    if ($g_action) $genres .= 'action,';
	    if ($g_docu) $genres .= 'documentaire,';
	    if ($g_fantastique) $genres .= 'fantastique,';
	    if ($g_guerre) $genres .= 'film de guerre,';
	    if ($g_vrai) $genres .= 'histoire vraie,';
	    if ($g_historique) $genres .= 'historique,';
	    if ($g_humour) $genres .= 'humour,';
	    if ($g_policier) $genres .= 'policier,';
	    if ($g_romantique) $genres .= 'romantique,';
	    if ($g_SF) $genres .= 'science-fiction,';

	    $ok = mysql_query('INSERT INTO films VALUES(' . $id . ', '
		. reg_mysql_quote_string($realisateur) . ', '
		. reg_mysql_quote_string($compositeur) . ', '
		. reg_mysql_quote_string($genres) . ')');
	    if (!$ok) reg_erreur_mysql();

	    foreach ($acteurs as $acteur) {
		if ($acteur <> '') {
		    $ok = mysql_query('INSERT INTO acteurs VALUES(' . $id .', "'
			. mysql_real_escape_string($acteur) .'")');
		    if (!$ok) reg_erreur_mysql();
		}
	    }
	    break;

	/* Enregistrement d’un livre. */

	case 'Livre':
	    $genres = '';
	    if ($g_fantastique) $genres .= 'fantastique,';
	    if ($g_vrai) $genres .= 'histoire vraie,';
	    if ($g_historique) $genres .= 'historique,';
	    if ($g_humour) $genres .= 'humour,';
	    if ($g_policier) $genres .= 'policier,';
	    if ($g_romantique) $genres .= 'romantique,';
	    if ($g_SF) $genres .= 'science-fiction,';

	    $ok = mysql_query('INSERT INTO livres VALUES(' . $id . ', '
		. reg_mysql_quote_string($auteur) . ', '
		. reg_mysql_quote_string($genres) . ')');
	    break;

	/* Enregistrement d’une bande-dessinée. */

	case 'BD':
	    $ok = mysql_query('INSERT INTO bd VALUES(' . $id . ', '
		. reg_mysql_quote_string($dessinateur) . ', '
		. reg_mysql_quote_string($scenariste) . ', '
		. reg_mysql_quote_string($serie) . ', '
		. reg_mysql_quote_string($numero) . ')');
	    if (!$ok) reg_erreur_mysql();
	    break;

	default:
	    reg_erreur_serveur('Le type « ' . $type . ' » est inconnu.');
    }

    $titre='';
    $numero='';

    $g_action=FALSE;
    $g_docu=FALSE;
    $g_fantastique=FALSE;
    $g_guerre=FALSE;
    $g_vrai=FALSE;
    $g_historique=FALSE;
    $g_humour=FALSE;
    $g_policier=FALSE;
    $g_romantique=FALSE;
    $g_SF=FALSE;

    require('includes/headers.php');
?>



	<p class="msg ok">Votre <?php echo reg_type_dans_phrase($type);?> a été
	<?php
		if (is_null(reg_type_masculin($type))) { // genre de $type inconnu
			echo 'référencé(e) ';
		} elseif (reg_type_masculin($type)) { // $type est masculin
			echo 'référencé ';
		} else { // $type est féminin
			echo 'référencée ';
		}
	?>
	sous le <a href="Fiche/<?php echo $id; ?>">numéro <?php echo $id; ?></a>.
	<?php
		} else {
			require('includes/headers.php'); ?>
			<p><em class="erreur">Vous devez indiquer un titre.</em><?php
		}
	?>

	<h2>Ajouter une référence</h2>

	<form action="Enregistrer" method="post">
		<div class="form-row">
			<label for="titre">Titre :</label>
			<input name="titre" type="text" id="titre" value="<?php echo htmlspecialchars($titre); ?>" />
		</div>

		<div class="form-row">
			<label for="type">Type :</label>
			<select name="type" id="type" onchange="masquerChampsInutilises(this.value)">
				<option <?php if($type=='Disque Blu-ray') echo ' selected'; ?>>Disque Blu-ray</option>
				<option <?php if($type=='DVD') echo ' selected';?>>DVD</option>
				<option <?php if($type=='Cassette') echo ' selected';?>>Cassette</option>
				<option <?php if($type=='Livre') echo ' selected';?>>Livre</option>
				<option <?php if($type=='BD') echo ' selected';?>>BD</option>
			</select>
		</div>

		<div class="form-row">
			<label for="realisateur">Réalisateur :</label>
			<input name="realisateur" type="text" id="realisateur" value="<?php echo htmlspecialchars($realisateur); ?>" />
		</div>

		<div class="form-row">
			<label>Acteurs :</label>
			<ul id="liste-acteurs">
			<?php foreach($acteurs as $i => $a) { ?>
				<li><input name="acteur<?php echo $i; ?>" type="text" value="<?php echo htmlspecialchars($a); ?>" /></li>
			<?php } ?>
			</ul>
		</div>

		<div class="form-row">
			<label for="compositeur">Compositeur :</label>
			<input name="compositeur" type="text" id="compositeur" value="<?php echo htmlspecialchars($compositeur); ?>" />
		</div>

		<div class="form-row">
			<span class="label">Genres :</span>
			<ul id="liste-genres" class="with-checkbox">
				<li>
					<label for="action">Action</label>
					<input name="action" type="checkbox" id="action" <?php if ($g_action) echo 'checked'; ?>>
				</li>
				<li>
					<label for="docu">Documentaire</label>
					<input name="docu" type="checkbox" id="docu" <?php if ($g_docu) echo 'checked'; ?>>
				</li>
				<li>
					<label for="fantastique">Fantastique</label>
					<input name="fantastique" type="checkbox" id="fantastique" <?php if ($g_fantastique) echo 'checked'; ?>>
				</li>
				<li>
					<label for="guerre">Film de guerre</label>
					<input name="guerre" type="checkbox" id="guerre" <?php if ($g_guerre) echo 'checked'; ?>>
				</li>
				<li>
					<label for="vrai">Histoire vraie</label>
					<input name="vrai" type="checkbox" id="vrai" <?php if ($g_vrai) echo 'checked'; ?>>
				</li>
				<li>
					<label for="historique">Historique</label>
					<input name="historique" type="checkbox" id="historique" <?php if ($g_historique) echo 'checked'; ?>>
				</li>
				<li>
					<label for="humour">Humour</label>
					<input name="humour" type="checkbox" id="humour" <?php if ($g_humour) echo 'checked'; ?>>
				</li>
				<li>
					<label for="policier">Policier</label>
					<input name="policier" type="checkbox" id="policier" <?php if ($g_policier) echo 'checked'; ?>>
				</li>
				<li>
					<label for="romantique">Romantique</label>
					<input name="romantique" type="checkbox" id="romantique" <?php if ($g_romantique) echo 'checked'; ?>>
				</li>
				<li>
					<label for="SF">Science-fiction</label>
					<input name="SF" type="checkbox" id="SF" <?php if ($g_SF) echo 'checked'; ?>>
				</li>
			</ul>
		</div>

		<div class="form-row">
			<label for="auteur">Auteur :</label>
			<input name="auteur" type="text" id="auteur" value="<?php echo htmlspecialchars($auteur); ?>" />
		</div>

		<div class="form-row">
			<label for="dessinateur">Dessinateur :</label>
			<input name="dessinateur" type="text" id="dessinateur" value="<?php echo htmlspecialchars($dessinateur); ?>" />
		</div>

		<div class="form-row">
			<label for="scenariste">Scénariste :</label>
			<input name="scenariste" type="text" id="scenariste" value="<?php echo htmlspecialchars($scenariste); ?>" />
		</div>

		<div class="form-row">
			<label for="serie">Série :</label>
			<input name="serie" type="text" id="serie" value="<?php echo htmlspecialchars($serie); ?>" />
		</div>

		<div class="form-row">
			<label for="numero">Numéro :</label>
			<input name="numero" type="text" id="numero" value="<?php echo htmlspecialchars($numero); ?>" />
		</div>

		<div class="form-row">
			<label for="commentaire">Commentaire :</label>
			<textarea name="commentaire" id="commentaire">
				<?php echo htmlspecialchars($commentaire);?>
			</textarea>
		</div>

		<div class="form-row">
			<label for="proprietaire">Propriétaire :</label>
			<input name="proprietaire" type="text" id="proprietaire" value="<?php echo htmlspecialchars($proprietaire); ?>" />
		</div>

		<div class="form-row">
			<label for="emplacement">Emplacement :</label>
			<input name="emplacement" type="text" id="emplacement" value="<?php echo htmlspecialchars($emplacement); ?>" />
		</div>

		<button type="submit">Enregistrer</button>
	</form>

<?php require('includes/footer.php'); ?>