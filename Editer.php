<?php
require('config.php');
require('includes/utilitaires.php');
require('includes/connexion_bd.php');

$user = reg_authentifier();

$id = 0;
if (isset($_GET['id'])) $id = (int) $_GET['id'];

if ($id == 0) reg_redirection_accueil();

$general = mysql_query('SELECT * FROM tout WHERE id=' . $id)
    or reg_mysql_error();
$film = mysql_query('SELECT * FROM films WHERE id=' . $id)
    or reg_mysql_error();
$liste_a = mysql_query('SELECT * FROM acteurs WHERE id=' . $id)
    or reg_mysql_error();

$general = mysql_fetch_assoc($general);
$film = mysql_fetch_assoc($film);

$tableau_acteurs=array();
while($ligne = mysql_fetch_assoc($liste_a)) {
    $tableau_acteurs[]=$ligne['acteur'];
}

$titre=$general['titre'];
$proprietaire=$general['proprietaire'];
$type=$general['type'];
$emplacement=$general['emplacement'];
$commentaire=$general['commentaire'];
$realisateur=$film['realisateur'];
$compositeur=$film['compositeur'];
$acteurs=implode(', ', $tableau_acteurs);

if (isset($_POST['titre'])) $titre=$_POST['titre'];
if (isset($_POST['proprietaire'])) $proprietaire=$_POST['proprietaire'];
if (isset($_POST['type'])) $type=$_POST['type'];
if (isset($_POST['emplacement'])) $emplacement=$_POST['emplacement'];
if (isset($_POST['commentaire'])) $commentaire=$_POST['commentaire'];
if (isset($_POST['realisateur'])) $realisateur=$_POST['realisateur'];
if (isset($_POST['compositeur'])) $compositeur=$_POST['compositeur'];
if (isset($_POST['acteurs'])) $acteurs=$_POST['acteurs'];

$reg_titre_page = 'Modification - ' . $titre;

if ($_SERVER['REQUEST_METHOD'] <> 'POST') {
    require('includes/headers.php');
} elseif ($titre<>'') {
    $ok = mysql_query('LOCK TABLES tout WRITE, films WRITE, acteurs WRITE');
    if (!$ok) reg_erreur_mysql();

    $ok = mysql_query('UPDATE tout ' .
	'SET titre=' . reg_mysql_quote_string($titre) .
	', proprietaire=' . reg_mysql_quote_string($proprietaire) .
	', emplacement=' . reg_mysql_quote_string($emplacement) .
	', commentaire=' . reg_mysql_quote_string($commentaire) .
	', dernier_editeur=' . reg_mysql_quote_string($user) .
	', derniere_edition=NOW() WHERE id=' . $id);
    if (!$ok) reg_erreur_mysql();

    $ok = mysql_query('UPDATE films SET realisateur=' .
	reg_mysql_quote_string($realisateur) .
	', compositeur=' . reg_mysql_quote_string($compositeur) .
	' WHERE id=' . $id);
    if (!$ok) reg_erreur_mysql();

    $acteurs = preg_split("/ *, */", $acteurs, -1, PREG_SPLIT_NO_EMPTY);
    if (implode(", ", $acteurs) <> implode(", ", $tableau_acteurs)) {
	$ok = mysql_query('DELETE FROM acteurs WHERE id=' . $id);
	if (!$ok) reg_erreur_mysql();
	foreach ($acteurs as $acteur) {
	    $ok = mysql_query('INSERT INTO acteurs VALUES(' . $id .', "' .
		mysql_real_escape_string($acteur) .'")');
	    if (!$ok) reg_erreur_mysql();
	}
    }

    $ok = mysql_query('UNLOCK TABLES');
    if (!$ok) reg_erreur_mysql();

    reg_redirection($reg_racine . 'Fiche/' . $id);
} else {
    require('includes/headers.php');
    ?><p><em class="erreur">Vous devez indiquer un titre.</em>
<?php } ?>

<p class="navigation">Retour à l’<a href="<?php echo $reg_racine; ?>">accueil</a>.
<p class="navigation"><a href="<?php echo $reg_racine; ?>Fiche/<?php echo $id; ?>">Annuler</a> les modifications.

<form action="<?php echo $reg_racine; ?>Editer/<?php echo $id; ?>" method="post" class="editer">
<dl class="editer">
    <dt class="titre">Titre
    <dd class="titre"><input name="titre" type="text"
	value="<?php echo htmlspecialchars($titre); ?>">
    <dt class="type">Type
    <dd class="type"><?php echo reg_afficher_type($type) . "\n"; ?>
    <dt class="realisateur">Réalisateur
    <dd class="realisateur"><input name="realisateur" type="text"
	value="<?php echo htmlspecialchars($realisateur); ?>">
    <dt class="acteurs">Acteurs
    <dd class="acteurs">Veuillez indiquez la liste des acteurs, séparés par des virgules :<br>
	<input name="acteurs" type="text"
	value="<?php echo htmlspecialchars($acteurs); ?>">
    <dt class="compositeur">Compositeur
    <dd class="compositeur"><input name="compositeur" type="text"
	value="<?php echo htmlspecialchars($compositeur); ?>">
    <dt class="commentaire">Commentaire
    <dd class="commentaire"><textarea name="commentaire" rows="4" cols="60"><?php
	echo htmlspecialchars($commentaire);
    ?></textarea>
    <dt class="proprietaire">Propriétaire
    <dd class="proprietaire"><input name="proprietaire" type="text"
	value="<?php echo htmlspecialchars($proprietaire); ?>">
    <dt class="emplacement">Emplacement
    <dd class="emplacement"><input name="emplacement" type="text"
	value="<?php echo htmlspecialchars($emplacement); ?>">
</dl>
<p><button type="submit">Enregistrer les modifications</button>
</form>
