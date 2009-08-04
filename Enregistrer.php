<?php
require('config.php');
require('includes/utilitaires.php');
require('includes/connexion_bd.php');

$user = reg_authentifier();
$reg_titre_page = 'Enregistrement d’une nouvelle référence';

$titre='';
$proprietaire='';
$type='DVD';
$emplacement='';
$commentaire='';
$realisateur='';
$compositeur='';
$acteurs='';
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
if (isset($_POST['acteurs'])) $acteurs=$_POST['acteurs'];

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
	. reg_mysql_quote_string($user) . ', NOW(), NULL, NULL)');
    if (!$ok) reg_erreur_mysql();

    $id = mysql_insert_id();
    if (!$id) reg_erreur_mysql();

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

    $acteurs = preg_split("/ *, */", $acteurs, -1, PREG_SPLIT_NO_EMPTY);
    foreach ($acteurs as $acteur) {
	$ok = mysql_query('INSERT INTO acteurs VALUES(' . $id .', "'
	    . mysql_real_escape_string($acteur) .'")');
	if (!$ok) reg_erreur_mysql();
    }

    $titre='';
    $proprietaire='';
    $type='DVD';
    $emplacement='';
    $commentaire='';
    $realisateur='';
    $compositeur='';
    $acteurs='';
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
    ?><p class="msg ok">Votre film a été enregistré sous le
<a href="Fiche/<?php echo $id; ?>">numéro <?php echo $id; ?></a>.
<?php } else {
    require('includes/headers.php');
    ?><p><em class="erreur">Vous devez indiquer un titre.</em>
<?php } ?>

<p class="navigation">Retour à l’<a href="<?php echo $reg_racine; ?>">accueil</a>.

<form action="Enregistrer" method="post" class="enregistrer">
<dl class="enregistrer">
    <dt class="titre">Titre
    <dd class="titre"><input name="titre" type="text"
	value="<?php echo htmlspecialchars($titre); ?>">
    <dt class="type">Type
    <dd class="type"><select name="type">
	<option<?php if($type=='DVD') echo ' selected';?>>DVD</option>
	<option<?php if($type=='Cassette') echo ' selected';?>>Cassette</option>
    </select>
    <dt class="realisateur">Réalisateur
    <dd class="realisateur"><input name="realisateur" type="text"
	value="<?php echo htmlspecialchars($realisateur); ?>">
    <dt class="acteurs">Acteurs
    <dd class="acteurs">Veuillez indiquez la liste des acteurs, séparés par des virgules :<br>
	<input name="acteurs" type="text">
    <dt class="compositeur">Compositeur
    <dd class="compositeur"><input name="compositeur" type="text"
	value="<?php echo htmlspecialchars($compositeur); ?>">
    <dt class="commentaire">Commentaire
    <dd class="commentaire"><textarea name="commentaire" rows="4" cols="60"><?php
	echo htmlspecialchars($commentaire);
    ?></textarea>
    <dt class="genres">Genres
    <dd class="genres"><ul>
	<li><input name="action" type="checkbox"<?php
	    if ($g_action) echo ' checked'; ?>>Action</li>
	<li><input name="docu" type="checkbox"<?php
	    if ($g_docu) echo ' checked'; ?>>Documentaire</li>
	<li><input name="fantastique" type="checkbox"<?php
	    if ($g_fantastique) echo ' checked'; ?>>Fantastique</li>
	<li><input name="guerre" type="checkbox"<?php
	    if ($g_guerre) echo ' checked'; ?>>Film de guerre</li>
	<li><input name="vrai" type="checkbox"<?php
	    if ($g_vrai) echo ' checked'; ?>>Histoire vraie</li>
	<li><input name="historique" type="checkbox"<?php
	    if ($g_historique) echo ' checked'; ?>>Historique</li>
	<li><input name="humour" type="checkbox"<?php
	    if ($g_humour) echo ' checked'; ?>>Humour</li>
	<li><input name="policier" type="checkbox"<?php
	    if ($g_policier) echo ' checked'; ?>>Policier</li>
	<li><input name="romantique" type="checkbox"<?php
	    if ($g_romantique) echo ' checked'; ?>>Romantique</li>
	<li><input name="SF" type="checkbox"<?php
	    if ($g_SF) echo ' checked'; ?>>Science-fiction</li>
    </ul>
    <dt class="proprietaire">Propriétaire
    <dd class="proprietaire"><input name="proprietaire" type="text"
	value="<?php echo htmlspecialchars($proprietaire); ?>">
    <dt class="emplacement">Emplacement
    <dd class="emplacement"><input name="emplacement" type="text"
	value="<?php echo htmlspecialchars($emplacement); ?>">
</dl>
<p><button type="submit">Enregistrer</button>
</form>
