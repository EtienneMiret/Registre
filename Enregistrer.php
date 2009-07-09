<?php
require('utilitaires.php');
require('connexion_bd.php');

$user = reg_authentifier();

$titre='';
$proprietaire='';
$type='DVD';
$emplacement='';
$commentaire='';
$realisateur='';

if (isset($_POST['titre'])) $titre=$_POST['titre'];
if (isset($_POST['proprietaire'])) $proprietaire=$_POST['proprietaire'];
if (isset($_POST['type'])) $type=$_POST['type'];
if (isset($_POST['emplacement'])) $emplacement=$_POST['emplacement'];
if (isset($_POST['commentaire'])) $commentaire=$_POST['commentaire'];
if (isset($_POST['realisateur'])) $realisateur=$_POST['realisateur'];

if ($_SERVER['REQUEST_METHOD'] <> 'POST') {
    require('headers.php');
} elseif ($titre<>'') {
    $ok = mysql_query('INSERT INTO tout VALUES( NULL, '
	. reg_mysql_quote_string($titre) . ', '
	. reg_mysql_quote_string($proprietaire) . ', '
	. reg_mysql_quote_string($type) . ', '
	. reg_mysql_quote_string($emplacement) . ', '
	. reg_mysql_quote_string($commentaire) . ', '
	. reg_mysql_quote_string($user) . ', NOW(), '
	. reg_mysql_quote_string($user) . ', NOW())');
    if (!$ok) reg_erreur_mysql();

    $id = mysql_insert_id();
    if (!$id) reg_erreur_mysql();

    $ok = mysql_query('INSERT INTO films VALUES(' . $id . ', '
	. reg_mysql_quote_string($realisateur) . ')');
    if (!$ok) reg_erreur_mysql();

    $titre='';
    $proprietaire='';
    $type='DVD';
    $emplacement='';
    $commentaire='';
    $realisateur='';

    require('headers.php');
    ?><p>Votre films a été enregistré sous le
<a href="Fiche/<?php echo $id; ?>">numéro <?php echo $id; ?></a>.
<?php } else {
    require('headers.php');
    ?><p><em class="erreur">Vous devez indiquer un titre.</em>
<?php } ?>

<form action="Enregistrer" method="post">
<dl>
    <dt>Titre
    <dd><input name="titre" type="text"
	value="<?php echo htmlspecialchars($titre); ?>">
    <dt>Type
    <dd><select name="type">
	<option<?php if($type=='DVD') echo ' selected';?>>DVD</option>
	<option<?php if($type=='Cassette') echo ' selected';?>>Cassette</option>
    </select>
    <dt>Réalisateur
    <dd><input name="realisateur" type="text"
	value="<?php echo htmlspecialchars($realisateur); ?>">
    <dt>Commentaire
    <dd><textarea name="commentaire" rows="4" cols="40"><?php
	echo htmlspecialchars($commentaire);
    ?></textarea>
    <dt>Propriétaire
    <dd><input name="proprietaire" type="text"
	value="<?php echo htmlspecialchars($proprietaire); ?>">
    <dt>Emplacement
    <dd><input name="emplacement" type="text"
	value="<?php echo htmlspecialchars($emplacement); ?>">
</dl>
<p><button type="submit">Enregistrer</button>
</form>
