<?php
require('utilitaires.php');
require('connexion_bd.php');

$user = reg_authentifier();

if (!isset($_GET['q']) or $_GET['q']=='') {
    require('headers.php');
    require('formulaire-recherche.php');
    ?>
    <p>Retour à l’<a href="/Registre/">accueil</a>.
    <?php
    exit(0);
}

$q=$_GET['q'];
$termes=explode(' ', $q);

$query = 'SELECT id,titre '
    . 'FROM tout LEFT JOIN acteurs USING(id) JOIN films USING(id) WHERE ';
foreach ($termes as $k) {
    if ($k<>'') {
	$query .=  '(tout.type="' . mysql_real_escape_string($k)
	    .  '" OR tout.titre LIKE "%' . mysql_real_escape_string($k)
	    . '%" OR films.realisateur LIKE "%' . mysql_real_escape_string($k)
	    . '%" OR acteurs.acteur LIKE "%' . mysql_real_escape_string($k)
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

require('headers.php');
require('formulaire-recherche.php');

$ligne = mysql_fetch_assoc($res);

if (!$ligne) {
    echo '<p>Désolé, aucun résultat correspondant à votre recherche';
    echo ' n’a été trouvé.' . "\n";
} else {
    echo "<table>\n";
    do {
	echo '  <tr><th><a href="/Registre/Fiche/' . $ligne['id'] .'">';
	echo htmlspecialchars($ligne['titre']) . "</a>\n";
    } while($ligne = mysql_fetch_assoc($res));
    echo "</table>\n";
} ?>
<p>Retour à l’<a href="/Registre/">accueil</a>.
<p>Recherche effectuée en <?php echo round($heure_fin-$heure_debut,3); ?>
 secondes.
