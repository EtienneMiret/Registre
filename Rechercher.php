<?php
require('config.php');
require('includes/definitions.php');
require('includes/initialiser.php');
require('includes/utilitaires.php');
require('includes/connexion_bd.php');

$reg_user = reg_authentifier();
$reg_titre_page = 'Recherche';
$reg_page = PAGE_RECHERCHE;

if (!isset($_GET['q'])) {
    $q = '';
} else {
    $q = $_GET['q'];
}

/* Construction de la chaine de recherche en incluant les paramètres de
 * recherche avancée. */

if (isset($_GET['titre']) && $_GET['titre'] <> '')
    $q.= ' titre:(' . $_GET['titre'] . ')';
if (isset($_GET['realisateur']) && $_GET['realisateur'] <> '')
    $q.= ' realisateur:(' . $_GET['realisateur'] . ')';
if (isset($_GET['acteur']) && $_GET['acteur'] <> '')
    $q.= ' acteur:(' . $_GET['acteur'] . ')';
if (isset($_GET['compositeur']) && $_GET['compositeur'] <> '')
    $q.= ' compositeur:(' . $_GET['compositeur'] . ')';
if (isset($_GET['commentaire']) && $_GET['commentaire'] <> '')
    $q.= ' commentaire:(' . $_GET['commentaire'] . ')';
if (isset($_GET['proprietaire']) && $_GET['proprietaire'] <> '')
    $q.= ' proprietaire:(' . $_GET['proprietaire'] . ')';
if (isset($_GET['emplacement']) && $_GET['emplacement'] <> '')
    $q.= ' emplacement:(' . $_GET['emplacement'] . ')';
if (isset($_GET['type']) && $_GET['type'] <> '')
    $q.= ' type:(' . $_GET['type'] . ')';
if (isset($_GET['createur']) && $_GET['createur'] <> '')
    $q.= ' createur:(' . $_GET['createur'] . ')';
if (isset($_GET['editeur']) && $_GET['editeur'] <> '')
    $q.= ' editeur:(' . $_GET['editeur'] . ')';
if (isset($_GET['auteur']) && $_GET['auteur'] <> '')
    $q.= ' auteur:(' . $_GET['auteur'] . ')';
if (isset($_GET['dessinateur']) && $_GET['dessinateur'] <> '')
    $q.= ' dessinateur:(' . $_GET['dessinateur'] . ')';
if (isset($_GET['scenariste']) && $_GET['scenariste'] <> '')
    $q.= ' scenariste:(' . $_GET['scenariste'] . ')';
if (isset($_GET['serie']) && $_GET['serie'] <> '')
    $q.= ' serie:(' . $_GET['serie'] . ')';
// Suppression d’éventuelles espaces au début.
$q = preg_replace('/^\s+/', '', $q);

/* Si besoin, on redéfinit $reg_page. */
if ($q == '') $reg_page = PAGE_TOUT;

define('REG_RECH_TOUT', 0);
define('REG_RECH_TITRE', 1);
define('REG_RECH_REALISATEUR', 2);
define('REG_RECH_ACTEUR', 3);
define('REG_RECH_COMPOSITEUR', 4);
define('REG_RECH_COMMENTAIRE', 5);
define('REG_RECH_PROPRIETAIRE', 6);
define('REG_RECH_EMPLACEMENT', 7);
define('REG_RECH_AUTEUR', 8);
define('REG_RECH_DESSINATEUR', 9);
define('REG_RECH_SCENARISTE', 10);
define('REG_RECH_SERIE', 11);
define('REG_RECH_TYPE', 1000);
define('REG_RECH_CREATEUR', 1001);
define('REG_RECH_EDITEUR', 1002);

unset($cle_sans_parenthese);
$cle_sans_parenthese[REG_RECH_TITRE]	    = '/^titre:([^\)\s]*)/';
$cle_sans_parenthese[REG_RECH_REALISATEUR]  = '/^realisateur:([^\)\s]*)/';
$cle_sans_parenthese[REG_RECH_ACTEUR]	    = '/^acteur:([^\)\s]*)/';
$cle_sans_parenthese[REG_RECH_COMPOSITEUR]  = '/^compositeur:([^\)\s]*)/';
$cle_sans_parenthese[REG_RECH_COMMENTAIRE]  = '/^commentaire:([^\)\s]*)/';
$cle_sans_parenthese[REG_RECH_PROPRIETAIRE] = '/^proprietaire:([^\)\s]*)/';
$cle_sans_parenthese[REG_RECH_EMPLACEMENT]  = '/^emplacement:([^\)\s]*)/';
$cle_sans_parenthese[REG_RECH_AUTEUR]	    = '/^auteur:([^\)\s]*)/';
$cle_sans_parenthese[REG_RECH_DESSINATEUR]  = '/^dessinateur:([^\)\s]*)/';
$cle_sans_parenthese[REG_RECH_SCENARISTE]   = '/^scenariste:([^\)\s]*)/';
$cle_sans_parenthese[REG_RECH_SERIE]	    = '/^serie:([^\)\s]*)/';
$cle_sans_parenthese[REG_RECH_TYPE]	    = '/^type:([^\)\s]*)/';
$cle_sans_parenthese[REG_RECH_CREATEUR]	    = '/^createur:([^\)\s]*)/';
$cle_sans_parenthese[REG_RECH_EDITEUR]	    = '/^editeur:([^\)\s]*)/';

unset($cle_avec_parenchese);
$cle_avec_parenthese[REG_RECH_TITRE]	    = '/^titre:\(/';
$cle_avec_parenthese[REG_RECH_REALISATEUR]  = '/^realisateur:\(/';
$cle_avec_parenthese[REG_RECH_ACTEUR]	    = '/^acteur:\(/';
$cle_avec_parenthese[REG_RECH_COMPOSITEUR]  = '/^compositeur:\(/';
$cle_avec_parenthese[REG_RECH_COMMENTAIRE]  = '/^commentaire:\(/';
$cle_avec_parenthese[REG_RECH_PROPRIETAIRE] = '/^proprietaire:\(/';
$cle_avec_parenthese[REG_RECH_EMPLACEMENT]  = '/^emplacement:\(/';
$cle_avec_parenthese[REG_RECH_AUTEUR]	    = '/^auteur:\(/';
$cle_avec_parenthese[REG_RECH_DESSINATEUR]  = '/^dessinateur:\(/';
$cle_avec_parenthese[REG_RECH_SCENARISTE]   = '/^scenariste:\(/';
$cle_avec_parenthese[REG_RECH_SERIE]	    = '/^serie:\(/';
$cle_avec_parenthese[REG_RECH_TYPE]	    = '/^type:\(/';
$cle_avec_parenthese[REG_RECH_CREATEUR]	    = '/^createur:\(/';
$cle_avec_parenthese[REG_RECH_EDITEUR]	    = '/^editeur:\(/';

/* Analyse de la recherche et construction des tableaux $termes et $types. */

$rech=$q;
$type_actif=REG_RECH_TOUT;
$termes=array();
$types=array();
while ( $rech <> '') {

    /* Espaces */
    $espaces='/^\s+/';
    if (preg_match($espaces, $rech)) {
	$rech = preg_replace($espaces, '', $rech);
	continue;
    }

    /* Mot-clé avec parenthèse ouvrante */
    foreach ($cle_avec_parenthese as $i => $k) {
	if (preg_match($k, $rech)) {
	    $rech = preg_replace($k, '', $rech);
	    $type_actif = $i;
	    continue 2;
	}
    }

    /* Mot-clé sans parenthèse ouvrante */
    foreach ($cle_sans_parenthese as $i => $k) {
	if (preg_match($k, $rech, $matches)) {
	    $rech = preg_replace($k, '', $rech);
	    $types[] = $i;
	    $termes[] = $matches[1];
	    continue 2;
	}
    }

    /* Parenthèse fermante */
    $parenthese_fermante='/^\)/';
    if (preg_match($parenthese_fermante, $rech)) {
	$rech = preg_replace($parenthese_fermante, '', $rech);
	$type_actif=REG_RECH_TOUT;
	continue;
    }

    /* Terme recherché */
    $terme='/^[^\)\s]+/';
    if (preg_match($terme, $rech, $matches)) {
	$rech = preg_replace($terme, '', $rech);
	$types[]=$type_actif;
	$termes[]=$matches[0];
	continue;
    }

    /* Rien trouvé */
    reg_erreur_serveur('Impossible d’analyser la recherche : ' . $q);

}

/* Recherche des acteurs et constructions du tableau $films_acteur. */

$films_acteur=Array();
foreach ($termes as $i => $k) {
    $films_acteur[$i]=Array();
    if ($type[$i]==REG_RECH_TOUT || $type[$i]=REG_RECH_ACTEUR) {
	$res = mysql_query('SELECT id FROM acteurs WHERE acteur LIKE "%' .
	    mysql_real_escape_string($k) . '%" GROUP BY id');
	if (!$res) reg_erreur_mysql();
	while ($ligne=mysql_fetch_assoc($res)) {
	    $films_acteur[$i][]=$ligne['id'];
	}
    }
}

/* Construction de la requête MySQL à partir des tableaux $termes et $types. */

$query = 'SELECT id,titre,type '
    . 'FROM tout LEFT JOIN films USING(id) '
    . 'LEFT JOIN livres USING(id) LEFT JOIN bd USING(id) WHERE ';
foreach ($termes as $i => $k) {
    switch ($types[$i]) {
	case REG_RECH_TITRE:
	    $query .= 'tout.titre LIKE "%' . mysql_real_escape_string($k)
		. '%" AND ';
	    break;
	case REG_RECH_REALISATEUR:
	    $query .= 'films.realisateur LIKE "%' . mysql_real_escape_string($k)
		. '%" AND ';
	    break;
	case REG_RECH_ACTEUR:
	    $query .= '(';
	    foreach ($films_acteur[$i] as $id) {
		$query .= 'id=' . $id . ' OR ';
	    }
	    $query .= 'FALSE) AND ';
	    break;
	case REG_RECH_COMPOSITEUR:
	    $query .= 'films.compositeur LIKE "%' . mysql_real_escape_string($k)
		. '%" AND ';
	    break;
	case REG_RECH_PROPRIETAIRE:
	    $query .= 'tout.proprietaire LIKE "%' . mysql_real_escape_string($k)
		. '%" AND ';
	    break;
	case REG_RECH_EMPLACEMENT:
	    $query .= 'tout.emplacement LIKE "%' . mysql_real_escape_string($k)
		. '%" AND ';
	    break;
	case REG_RECH_COMMENTAIRE:
	    $query .= 'tout.commentaire LIKE "%' . mysql_real_escape_string($k)
		. '%" AND ';
	    break;
	case REG_RECH_AUTEUR:
	    $query .= 'livres.auteur LIKE "%' . mysql_real_escape_string($k)
		. '%" AND ';
	    break;
	case REG_RECH_DESSINATEUR:
	    $query .= 'bd.dessinateur LIKE "%' . mysql_real_escape_string($k)
		. '%" AND ';
	    break;
	case REG_RECH_SCENARISTE:
	    $query .= 'bd.scenariste LIKE "%' . mysql_real_escape_string($k)
		. '%" AND ';
	    break;
	case REG_RECH_SERIE:
	    $query .= 'bd.serie LIKE "%' . mysql_real_escape_string($k)
		. '%" AND ';
	    break;
	case REG_RECH_TYPE:
	    $query .= 'tout.type LIKE "%' . mysql_real_escape_string($k)
		. '%" AND ';
	    break;
	case REG_RECH_CREATEUR:
	    $query .= 'tout.createur LIKE "%' . mysql_real_escape_string($k)
		. '%" AND ';
	    break;
	case REG_RECH_EDITEUR:
	    $query .= 'tout.dernier_editeur LIKE "%' .
		mysql_real_escape_string($k) . '%" AND ';
	    break;
	case REG_RECH_TOUT:
	    $query .=  '(tout.type LIKE "%' . mysql_real_escape_string($k)
	    . '%" OR tout.titre LIKE "%' . mysql_real_escape_string($k)
	    . '%" OR films.realisateur LIKE "%' . mysql_real_escape_string($k)
	    . '%" OR films.compositeur LIKE "%' . mysql_real_escape_string($k)
	    . '%" OR tout.proprietaire LIKE "%' . mysql_real_escape_string($k)
	    . '%" OR tout.emplacement LIKE "%' . mysql_real_escape_string($k)
	    . '%" OR tout.commentaire LIKE "%' . mysql_real_escape_string($k)
	    . '%" OR livres.auteur LIKE "%' . mysql_real_escape_string($k)
	    . '%" OR bd.dessinateur LIKE "%' . mysql_real_escape_string($k)
	    . '%" OR bd.scenariste LIKE "%' . mysql_real_escape_string($k)
	    . '%" OR bd.serie LIKE "%' . mysql_real_escape_string($k)
	    . '%" OR ';
	    foreach ($films_acteur[$i] as $id) {
		$query .= 'id=' . $id . ' OR ';
	    }
	    $query .= 'FALSE) AND ';
	    break;
	default:
	    reg_erreur_serveur('Recherche avec un numéro de type inconnu : '
		. $types[$i]);
    }
}
$query .='TRUE ORDER BY titre,id';

$heure_debut = microtime(true);
$res = mysql_query($query);
$heure_fin = microtime(true);
if (!$res) reg_erreur_mysql();

require('includes/headers.php');

$ligne = mysql_fetch_assoc($res);

if (!$ligne) { ?>
	<p class="msg msg-nok">Désolé, aucun résultat correspondant à votre recherche n’a été trouvé.</p>
<?php } else { ?>
<?php if ($q == '') { ?>
	<p class="msg msg-ok">Il y a <span><?php echo mysql_num_rows($res);?></span> références en tout :</p>
<?php } else { ?>
	<p class="msg msg-ok">Votre recherche a renvoyé <span><?php echo mysql_num_rows($res);?></span> résultats :</p>
<?php } ?>

	<table border="0" cellspacing="0" cellpadding="0">

		<col span="1" style="width: 760px;"/>
		<col span="1" style="width: 200px;"/>

		<thead>
			<tr>
				<th>Références</th>
				<th class="last">Types</th>
			</tr>
		</thead>

		<tbody>
		<?php do { ?>
			<tr>
				<td>
					<a href="<?php echo $reg_racine; ?>Fiche/<?php echo $ligne['id']; ?>">
						<?php echo htmlspecialchars($ligne['titre']); ?>
					</a>
				</td>
				<td class="last"><?php echo reg_afficher_type($ligne['type']) . PHP_EOL; ?></td>
			</tr>
		<?php } while($ligne = mysql_fetch_assoc($res)); ?>
		</tbody>

	</table>
<?php } ?>

	<p>Retour à l’<a href="<?php echo $reg_racine; ?>">accueil</a>.</p>
	<p>Recherche effectuée en <?php echo round($heure_fin-$heure_debut,3); ?> secondes.</p>

<?php require('includes/footer.php'); ?>























