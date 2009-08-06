<?php
require('config.php');
require('includes/utilitaires.php');
require('includes/connexion_bd.php');

$user = reg_authentifier();
$reg_titre_page = 'Recherche';

if (!isset($_GET['q'])) {
    $reg_titre_page = 'Recherche avancée';
    header('Content-Script-Type: application/ecmascript');
    require('includes/headers.php'); ?>
<p class="navigation">Retour à l’<a href="<?php echo $reg_racine; ?>">accueil</a>.
<form onsubmit="rechercheAvancee();">
<script type='application/ecmascript' src='recherche-avancee'></script>
<p>Rechercher : <input id="tout">
<p>Titre : <input id="titre">
<p>Réalisateur : <input id="realisateur">
<p>Acteurs : <input id="acteur">
<p>Compositeur : <input id="compositeur">
<p>Commentaire : <input id="commentaire">
<p>Proprietaire : <input id="proprietaire">
<p>Emplacement : <input id="emplacement">
<p><button>Rechercher</button>
<form>
<p class="navigation">Retour à l’<a href="<?php echo $reg_racine; ?>">accueil</a>.
<?php exit(0);
}

$q=$_GET['q'];
$termes=explode(' ', $q);

$query = 'SELECT id,titre '
    . 'FROM tout LEFT JOIN acteurs USING(id) JOIN films USING(id) WHERE ';
foreach ($termes as $k) {
    if (preg_match('/^titre:/', $k)) {
	$k = preg_replace('/^titre:/', '', $k);
	$query .= 'tout.titre LIKE "%' . mysql_real_escape_string($k)
	    . '%" AND ';
    } elseif (preg_match('/^realisateur:/', $k)) {
	$k = preg_replace('/^realisateur:/', '', $k);
	$query .= 'films.realisateur LIKE "%' . mysql_real_escape_string($k)
	    . '%" AND ';
    } elseif (preg_match('/^acteur:/', $k)) {
	$k = preg_replace('/^acteur:/', '', $k);
	$query .= 'acteurs.acteur LIKE "%' . mysql_real_escape_string($k)
	    . '%" AND ';
    } elseif (preg_match('/^compositeur:/', $k)) {
	$k = preg_replace('/^compositeur:/', '', $k);
	$query .= 'films.compositeur LIKE "%' . mysql_real_escape_string($k)
	    . '%" AND ';
    } elseif (preg_match('/^proprietaire:/', $k)) {
	$k = preg_replace('/^proprietaire:/', '', $k);
	$query .= 'tout.proprietaire LIKE "%' . mysql_real_escape_string($k)
	    . '%" AND ';
    } elseif (preg_match('/^emplacement:/', $k)) {
	$k = preg_replace('/^emplacement:/', '', $k);
	$query .= 'tout.emplacement LIKE "%' . mysql_real_escape_string($k)
	    . '%" AND ';
    } elseif (preg_match('/^commentaire:/', $k)) {
	$k = preg_replace('/^commentaire:/', '', $k);
	$query .= 'tout.commentaire LIKE "%' . mysql_real_escape_string($k)
	    . '%" AND ';
    } elseif ($k<>'') {
	$query .=  '(tout.type="' . mysql_real_escape_string($k)
	    .  '" OR tout.titre LIKE "%' . mysql_real_escape_string($k)
	    . '%" OR films.realisateur LIKE "%' . mysql_real_escape_string($k)
	    . '%" OR acteurs.acteur LIKE "%' . mysql_real_escape_string($k)
	    . '%" OR films.compositeur LIKE "%' . mysql_real_escape_string($k)
	    . '%" OR tout.proprietaire LIKE "%' . mysql_real_escape_string($k)
	    . '%" OR tout.emplacement LIKE "%' . mysql_real_escape_string($k) 
	    . '%" OR tout.commentaire LIKE "%' . mysql_real_escape_string($k)
	. '%") AND ';
    }
}
$query .='TRUE GROUP BY id ORDER BY titre,id';

$heure_debut = microtime(true);
$res = mysql_query($query);
$heure_fin = microtime(true);
if (!$res) reg_erreur_mysql();

require('includes/headers.php');
?>
<p class="navigation">Retour à l’<a href="<?php echo $reg_racine; ?>">accueil</a>.
<?php
require('includes/formulaire-recherche.php');

$ligne = mysql_fetch_assoc($res);

if (!$ligne) { ?>
<p class="msg nok">Désolé, aucun résultat correspondant à votre recherche n’a
    été trouvé.
<?php } else { ?>
<p class="msg ok resultats-recherche">Votre recherche a renvoyé
    <?php echo mysql_num_rows($res);?> résultats :
<table class="resultats-recherche">
<?php
    do {
	echo '  <tr><td><a href="'. $reg_racine . 'Fiche/'. $ligne['id'] .'">';
	echo htmlspecialchars($ligne['titre']) . "</a>\n";
    } while($ligne = mysql_fetch_assoc($res));
    echo "</table>\n";
} ?>
<p class="navigation">Retour à l’<a href="<?php echo $reg_racine; ?>">accueil</a>.
<p class="note">Recherche effectuée en <?php
 echo round($heure_fin-$heure_debut,3); ?>
 secondes.
