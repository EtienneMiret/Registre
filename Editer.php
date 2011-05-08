<?php
require('config.php');
require('includes/definitions.php');
require('includes/initialiser.php');
require('includes/utilitaires.php');
require('includes/connexion_bd.php');

/* Nombre de lignes vides à la fin de la liste des acteurs. */
define("LIGNES_ACTEURS_VIDES", 2);

$reg_user = reg_authentifier();

header('Content-Script-Type: application/ecmascript');
$reg_head[]='<script type="application/ecmascript" src="'.$reg_racine.'registre"></script>';
$reg_onload='ajouterBoutonListeActeurs();';

$id = 0;
if (isset($_GET['id'])) $id = (int) $_GET['id'];

if ($id == 0) reg_redirection_accueil();

$general = mysql_query('SELECT * FROM tout WHERE id=' . $id)
    or reg_erreur_mysql();
$film = mysql_query('SELECT * FROM films WHERE id=' . $id)
    or reg_erreur_mysql();
$liste_a = mysql_query('SELECT * FROM acteurs WHERE id=' . $id)
    or reg_erreur_mysql();
$livre = mysql_query('SELECT * FROM livres WHERE id=' . $id)
    or reg_erreur_mysql();
$bd = mysql_query('SELECT * FROM bd WHERE id=' . $id)
    or reg_erreur_mysql();

$general = mysql_fetch_assoc($general);
$film = mysql_fetch_assoc($film);
$livre = mysql_fetch_assoc($livre);
$bd = mysql_fetch_assoc($bd);

if (!$general) reg_redirection_accueil();

$acteurs_origine=array();
while($ligne = mysql_fetch_assoc($liste_a)) {
    $acteurs_origine[]=$ligne['acteur'];
}
$tableau_genres=array();
if (isset($film['genres'])) $tableau_genres=explode(',', $film['genres']);
if (isset($livre['genres'])) $tableau_genres=explode(',', $livre['genres']);

$titre=$general['titre'];
$proprietaire=$general['proprietaire'];
$type=$general['type'];
$emplacement=$general['emplacement'];
$commentaire=$general['commentaire'];
$realisateur=$film['realisateur'];
$compositeur=$film['compositeur'];
$acteurs=$acteurs_origine;
$auteur=$livre['auteur'];
$dessinateur=$bd['dessinateur'];
$scenariste=$bd['scenariste'];
$serie=$bd['serie'];
$numero=$bd['numero'];

$g_action=in_array('action',$tableau_genres);
$g_docu=in_array('documentaire',$tableau_genres);
$g_fantastique=in_array('fantastique',$tableau_genres);
$g_guerre=in_array('film de guerre',$tableau_genres);
$g_vrai=in_array('histoire vraie',$tableau_genres);
$g_historique=in_array('historique',$tableau_genres);
$g_humour=in_array('humour',$tableau_genres);
$g_policier=in_array('policier',$tableau_genres);
$g_romantique=in_array('romantique',$tableau_genres);
$g_SF=in_array('science-fiction',$tableau_genres);

$reg_titre_page = 'Modification - ' . $titre;

if (isset($_POST['titre'])) $titre=$_POST['titre'];
if (isset($_POST['proprietaire'])) $proprietaire=$_POST['proprietaire'];
if (isset($_POST['emplacement'])) $emplacement=$_POST['emplacement'];
if (isset($_POST['commentaire'])) $commentaire=$_POST['commentaire'];
if (isset($_POST['realisateur'])) $realisateur=$_POST['realisateur'];
if (isset($_POST['compositeur'])) $compositeur=$_POST['compositeur'];
if (isset($_POST['auteur'])) $auteur=$_POST['auteur'];
if (isset($_POST['dessinateur'])) $dessinateur=$_POST['dessinateur'];
if (isset($_POST['scenariste'])) $scenariste=$_POST['scenariste'];
if (isset($_POST['serie'])) $serie=$_POST['serie'];
if (isset($_POST['numero'])) $numero=$_POST['numero'];
if (isset($_POST['acteur0'])) $acteurs = array();
for ($i=0; isset($_POST['acteur'.$i]); $i++) $acteurs[$i]=$_POST['acteur'.$i];

/* Ajout des lignes vides à la fin de la liste d’acteurs. */
for ($i=0; $i < LIGNES_ACTEURS_VIDES; $i++) {
    $acteurs[] = '';
}

$reg_head[]='<script type="application/ecmascript">nombreLignesActeurs=' .
    count($acteurs) . '</script>';

if ($type=='BD' && !preg_match('/^\d*$/', $numero)) $numero = '';

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
    require('includes/nav-bar.php');
    require('includes/debut-contenu-principal.php');
} elseif ($titre<>'') {
    $ok = mysql_query('LOCK TABLES tout WRITE, films WRITE, acteurs WRITE,' .
	'livres WRITE, bd WRITE');
    if (!$ok) reg_erreur_mysql();

    $ok = mysql_query('UPDATE tout ' .
	'SET titre=' . reg_mysql_quote_string($titre) .
	', proprietaire=' . reg_mysql_quote_string($proprietaire) .
	', emplacement=' . reg_mysql_quote_string($emplacement) .
	', commentaire=' . reg_mysql_quote_string($commentaire) .
	', dernier_editeur=' . reg_mysql_quote_string($reg_user) .
	', derniere_edition=NOW() WHERE id=' . $id);
    if (!$ok) reg_erreur_mysql();

    switch ($type) {

	/* Mise à jour d’un film. */

	case 'disque Blu-ray':
	case 'DVD':
	case 'cassette':
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

	    $ok = mysql_query('UPDATE films ' .
		'SET realisateur=' . reg_mysql_quote_string($realisateur) .
		', compositeur=' . reg_mysql_quote_string($compositeur) .
		', genres=' . reg_mysql_quote_string($genres) .
		' WHERE id=' . $id);
	    if (!$ok) reg_erreur_mysql();

	    if ($acteurs <> $acteurs_origine) {
		$ok = mysql_query('DELETE FROM acteurs WHERE id=' . $id);
		if (!$ok) reg_erreur_mysql();
		foreach ($acteurs as $acteur) {
		    if($acteur <> '') {
			$ok = mysql_query('INSERT INTO acteurs VALUES(' . $id .
			    ', "' . mysql_real_escape_string($acteur) .'")');
			if (!$ok) reg_erreur_mysql();
		    }
		}
	    }
	    break;

	/* Mise à jour d’un livre. */

	case 'livre':
	    $genres = '';
	    if ($g_fantastique) $genres .= 'fantastique,';
	    if ($g_vrai) $genres .= 'histoire vraie,';
	    if ($g_historique) $genres .= 'historique,';
	    if ($g_humour) $genres .= 'humour,';
	    if ($g_policier) $genres .= 'policier,';
	    if ($g_romantique) $genres .= 'romantique,';
	    if ($g_SF) $genres .= 'science-fiction,';

	    $ok = mysql_query('UPDATE livres ' .
		'SET auteur=' . reg_mysql_quote_string($auteur) .
		', genres=' . reg_mysql_quote_string($genres) .
		' WHERE id=' . $id);
	    if (!$ok) reg_erreur_mysql();
	    break;

	/* Mise à jour d’une bande-dessinée. */

	case 'BD':
	    $ok = mysql_query('UPDATE bd ' .
		'SET dessinateur=' .  reg_mysql_quote_string($dessinateur) .
		', scenariste=' . reg_mysql_quote_string($scenariste) .
		', serie=' . reg_mysql_quote_string($serie) .
		', numero=' . reg_mysql_quote_string($numero) .
		' WHERE id=' . $id);
	    if (!$ok) reg_erreur_mysql();
	    break;

	default:
    }

    $ok = mysql_query('UNLOCK TABLES');
    if (!$ok) reg_erreur_mysql();

    reg_redirection($reg_racine . 'Fiche/' . $id);
} else {
    require('includes/headers.php');
    require('includes/nav-bar.php');
    require('includes/debut-contenu-principal.php');
    ?><p><em class="erreur">Vous devez indiquer un titre.</em>
<?php } ?>

<p class="navigation"><a href="<?php echo $reg_racine; ?>Fiche/<?php echo $id; ?>">Annuler</a> les modifications.

<form action="<?php echo $reg_racine; ?>Editer/<?php echo $id; ?>" method="post" class="editer">
<dl class="editer">
    <dt class="titre"><label for="titre">Titre</label>
    <dd class="titre"><input name="titre" type="text" id="titre"
	value="<?php echo htmlspecialchars($titre); ?>">
    <dt class="type">Type
    <dd class="type"><?php echo reg_afficher_type($type) . "\n"; ?>
<?php
switch($type) {
    case 'disque Blu-ray':
    case 'DVD':
    case 'cassette': /* Films. */
?>
    <dt class="realisateur"><label for="realisateur">Réalisateur</label>
    <dd class="realisateur"><input name="realisateur" type="text"
	id="realisateur" value="<?php echo htmlspecialchars($realisateur); ?>">
    <dt class="acteurs">Acteurs
    <dd class="acteurs"><ul id="liste-acteurs">
<?php foreach($acteurs as $i => $a) { ?>
	<li><input name="acteur<?php echo $i; ?>" type="text"
	    value="<?php echo htmlspecialchars($a); ?>">
<?php } ?>
    </ul>
    <dt class="compositeur"><label for="compositeur">Compositeur</label>
    <dd class="compositeur"><input name="compositeur" type="text"
	id="compositeur" value="<?php echo htmlspecialchars($compositeur); ?>">
    <dt class="genres">Genres
    <dd class="genres"><ul>
	<li><label><input name="action" type="checkbox"<?php
	    if ($g_action) echo ' checked'; ?>>Action</label></li>
	<li><label><input name="docu" type="checkbox"<?php
	    if ($g_docu) echo ' checked'; ?>>Documentaire</label></li>
	<li><label><input name="fantastique" type="checkbox"<?php
	    if ($g_fantastique) echo ' checked'; ?>>Fantastique</label></li>
	<li><label><input name="guerre" type="checkbox"<?php
	    if ($g_guerre) echo ' checked'; ?>>Film de guerre</label></li>
	<li><label><input name="vrai" type="checkbox"<?php
	    if ($g_vrai) echo ' checked'; ?>>Histoire vraie</label></li>
	<li><label><input name="historique" type="checkbox"<?php
	    if ($g_historique) echo ' checked'; ?>>Historique</label></li>
	<li><label><input name="humour" type="checkbox"<?php
	    if ($g_humour) echo ' checked'; ?>>Humour</label></li>
	<li><label><input name="policier" type="checkbox"<?php
	    if ($g_policier) echo ' checked'; ?>>Policier</label></li>
	<li><label><input name="romantique" type="checkbox"<?php
	    if ($g_romantique) echo ' checked'; ?>>Romantique</label></li>
	<li><label><input name="SF" type="checkbox"<?php
	    if ($g_SF) echo ' checked'; ?>>Science-fiction</label></li>
    </ul>
<?php
	break;
    case 'livre': /* Livres. */
?>
    <dt class="auteur"><label for="auteur">Auteur</label>
    <dd class="auteur"><input name="auteur" type="text" id="auteur"
	value="<?php echo htmlspecialchars($auteur); ?>">
    <dt class="genres">Genres
    <dd class="genres"><ul>
	<li><label><input name="fantastique" type="checkbox"<?php
	    if ($g_fantastique) echo ' checked'; ?>>Fantastique</label></li>
	<li><label><input name="vrai" type="checkbox"<?php
	    if ($g_vrai) echo ' checked'; ?>>Histoire vraie</label></li>
	<li><label><input name="historique" type="checkbox"<?php
	    if ($g_historique) echo ' checked'; ?>>Historique</label></li>
	<li><label><input name="humour" type="checkbox"<?php
	    if ($g_humour) echo ' checked'; ?>>Humour</label></li>
	<li><label><input name="policier" type="checkbox"<?php
	    if ($g_policier) echo ' checked'; ?>>Policier</label></li>
	<li><label><input name="romantique" type="checkbox"<?php
	    if ($g_romantique) echo ' checked'; ?>>Romantique</label></li>
	<li><label><input name="SF" type="checkbox"<?php
	    if ($g_SF) echo ' checked'; ?>>Science-fiction</label></li>
    </ul>
<?php
	break;
    case 'BD': /* Bandes-dessinées. */
?>
    <dt class="dessinateur"><label for="dessinateur">Dessinateur</label>
    <dd class="dessinateur"><input name="dessinateur" type="text"
	id="dessinateur" value="<?php echo htmlspecialchars($dessinateur); ?>">
    <dt class="scenariste"><label for="scenariste">Scénariste</label>
    <dd class="scenariste"><input name="scenariste" type="text"
	id="scenariste" value="<?php echo htmlspecialchars($scenariste); ?>">
    <dt class="serie"><label for="serie">Série</label>
    <dd class="serie"><input name="serie" type="text" id="serie"
	value="<?php echo htmlspecialchars($serie); ?>">
    <dt class="numero"><label for="numero">Numéro</label>
    <dd class="numero"><input name="numero" type="text" id="numero"
	value="<?php echo htmlspecialchars($numero); ?>">
<?php
	break;
    default:
}
?>
    <dt class="commentaire"><label for="commentaire">Commentaire</label>
    <dd class="commentaire">
      <textarea name="commentaire" id="commentaire" rows="4" cols="60"><?php
	echo htmlspecialchars($commentaire);
    ?></textarea>
    <dt class="proprietaire"><label for="propriétaire">Propriétaire</label>
    <dd class="proprietaire"><input name="proprietaire" type="text"
	id="proprietaire" value="<?php echo htmlspecialchars($proprietaire); ?>">
    <dt class="emplacement"><label for="emplacement">Emplacement</label>
    <dd class="emplacement"><input name="emplacement" type="text"
	id="emplacement" value="<?php echo htmlspecialchars($emplacement); ?>">
</dl>
<p><button type="submit">Enregistrer les modifications</button>
</form>
<?php require('includes/footer.php'); ?>
