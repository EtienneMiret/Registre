<?php
require('config.php');
require('includes/initialiser.php');
require('includes/utilitaires.php');
require('includes/connexion_bd.php');

$user = reg_authentifier();
$reg_titre_page = 'Enregistrement d’une nouvelle référence';

header('Content-Script-Type: application/ecmascript');
$reg_head[]='<style type="text/css">.genres.livre { display: none; }</style>';
$reg_head[]='<script type="application/ecmascript" src="registre"></script>';
$reg_onload='masquerChampsInutilises(document.getElementById("type").value);';

$titre='';
$proprietaire='';
$type='DVD';
$emplacement='';
$commentaire='';
$realisateur='';
$compositeur='';
$acteurs='';
$auteur='';
$dessinateur='';
$scenariste='';
$serie='';
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

if (isset($_POST['titre'])) $titre=$_POST['titre'];
if (isset($_POST['proprietaire'])) $proprietaire=$_POST['proprietaire'];
if (isset($_POST['type'])) $type=$_POST['type'];
if (isset($_POST['emplacement'])) $emplacement=$_POST['emplacement'];
if (isset($_POST['commentaire'])) $commentaire=$_POST['commentaire'];
if (isset($_POST['realisateur'])) $realisateur=$_POST['realisateur'];
if (isset($_POST['compositeur'])) $compositeur=$_POST['compositeur'];
if (isset($_POST['acteurs'])) $acteurs=$_POST['acteurs'];
if (isset($_POST['auteur'])) $auteur=$_POST['auteur'];
if (isset($_POST['dessinateur'])) $dessinateur=$_POST['dessinateur'];
if (isset($_POST['scenariste'])) $scenariste=$_POST['scenariste'];
if (isset($_POST['serie'])) $serie=$_POST['serie'];
if (isset($_POST['numero'])) $numero=$_POST['numero'];

if (!in_array($type, array('DVD', 'Cassette', 'Livre', 'BD'))) $type='DVD';
if ($type=='BD' && !preg_match('/^\d*$/', $numero)) $numero='';

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

    switch($type) {

	/* Enregistrement d’un film. */

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

	    $acteurs = preg_split("/ *, */", $acteurs, -1, PREG_SPLIT_NO_EMPTY);
	    foreach ($acteurs as $acteur) {
		$ok = mysql_query('INSERT INTO acteurs VALUES(' . $id .', "'
		    . mysql_real_escape_string($acteur) .'")');
		if (!$ok) reg_erreur_mysql();
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
    $proprietaire='';
    $emplacement='';
    $commentaire='';
    $realisateur='';
    $compositeur='';
    $acteurs='';
    $auteur='';
    $dessinateur='';
    $scenariste='';
    $serie='';
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
    ?><p class="msg ok">Votre <?php echo $type;?> a été référencé sous
le <a href="Fiche/<?php echo $id; ?>">numéro <?php echo $id; ?></a>.
<?php } else {
    require('includes/headers.php');
    ?><p><em class="erreur">Vous devez indiquer un titre.</em>
<?php } ?>

<p class="navigation">Retour à l’<a href="<?php echo $reg_racine; ?>">accueil</a>.
<p class="navigation"><a href="Aide#Enregistrement">Aide</a>.

<form action="Enregistrer" method="post" class="enregistrer">
<dl class="enregistrer">
    <dt class="titre">Titre
    <dd class="titre"><input name="titre" type="text"
	value="<?php echo htmlspecialchars($titre); ?>">
    <dt class="type">Type
    <dd class="type"><select name="type" id="type"
	onchange="masquerChampsInutilises(this.value)">
	<option<?php if($type=='DVD') echo ' selected';?>>DVD</option>
	<option<?php if($type=='Cassette') echo ' selected';?>>Cassette</option>
	<option<?php if($type=='Livre') echo ' selected';?>>Livre</option>
	<option<?php if($type=='BD') echo ' selected';?>>BD</option>
    </select>
    <dt class="film realisateur">Réalisateur
    <dd class="film realisateur"><input name="realisateur" type="text"
	value="<?php echo htmlspecialchars($realisateur); ?>">
    <dt class="film acteurs">Acteurs
    <dd class="film acteurs">Veuillez indiquez la liste des acteurs, séparés par des virgules :<br>
	<input name="acteurs" type="text">
    <dt class="film compositeur">Compositeur
    <dd class="film compositeur"><input name="compositeur" type="text"
	value="<?php echo htmlspecialchars($compositeur); ?>">
    <dt class="film genres">Genres
    <dd class="film genres"><ul>
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
    <dt class="livre auteur">Auteur
    <dd class="livre auteur"><input name="auteur" type="text"
	value="<?php echo htmlspecialchars($auteur); ?>">
    <dt class="livre genres">Genres
    <dd class="livre genres"><ul>
	<li><input name="fantastique" type="checkbox"<?php
	    if ($g_fantastique) echo ' checked'; ?>>Fantastique</li>
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
    <dt class="bd dessinateur">Dessinateur
    <dd class="bd dessinateur"><input name="dessinateur" type="text"
	value="<?php echo htmlspecialchars($dessinateur); ?>">
    <dt class="bd scenariste">Scénariste
    <dd class="bd scenariste"><input name="scenariste" type="text"
	value="<?php echo htmlspecialchars($scenariste); ?>">
    <dt class="bd serie">Série
    <dd class="bd serie"><input name="serie" type="text"
	value="<?php echo htmlspecialchars($serie); ?>">
    <dt class="bd numero">Numéro
    <dd class="bd numero"><input name="numero" type="text"
	value="<?php echo htmlspecialchars($numero); ?>">
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
<p><button type="submit">Enregistrer</button>
</form>
