<?php
require('utilitaires.php');
require('connexion_bd.php');

$user = reg_authentifier();
$reg_titre_page = 'Recherche';

if (!isset($_GET['q']) or $_GET['q']=='') {
    require('headers.php');
    require('formulaire-recherche.php');
    ?>
    <p class="navigation">Retour à l’<a href="/Registre/">accueil</a>.
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
?>
<p class="navigation">Retour à l’<a href="/Registre/">accueil</a>.
<?php
require('formulaire-recherche.php');

$ligne = mysql_fetch_assoc($res);

if (!$ligne) {
    echo '<p class="msg nok">Désolé, aucun résultat correspondant à votre';
    echo ' recherche n’a été trouvé.' . "\n";
} else {
    echo "<table class=\"resultats-recherche\">\n";
    do {
	echo '  <tr><td><a href="/Registre/Fiche/' . $ligne['id'] .'">';
	echo htmlspecialchars($ligne['titre']) . "</a>\n";
    } while($ligne = mysql_fetch_assoc($res));
    echo "</table>\n";
} ?>
<p class="navigation">Retour à l’<a href="/Registre/">accueil</a>.
<p class="note">Recherche effectuée en <?php
 echo round($heure_fin-$heure_debut,3); ?>
 secondes.
