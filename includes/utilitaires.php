<?php

/* Affiche le message d’erreur dans une page HTML avec un code 500.
 * Même contraintes que header : pas de données envoyées avant.
 */
function reg_erreur_serveur($msg) {
    global $reg_nom, $reg_racine;
    header('HTTP/1.1 500 Server Error');
    $reg_titre_page = 'Erreur interne du serveur';
    require('headers.php');
    echo '<p><em class="erreur">' . htmlspecialchars($msg) . '</em>';
    exit (1);
}

/* Fonction a appelé lorsque mysql_query renvoie faux. Affiche un message
 * d’erreur adapté avec un code 500.
 */
function reg_erreur_mysql() {
    reg_erreur_serveur('Erreur de requête MySQL : ' . mysql_error());
}

/* Renvoie vers la page indiquée et termine le script.
 * Même contraintes que header : pas de données envoyées avant.
 */
function reg_redirection($path) {
    header('HTTP/1.1 303 See Other');
    if (isset($_SERVER['HTTPS'])) {
	$proto='https';
    } else {
	$proto='http';
    }
    header('Location: '.$proto.'://'.$_SERVER['HTTP_HOST'].$path);
    exit(0);
}

/* Renvoie vers la page d’accueil et termine le script.
 * Même contraintes que header : pas de données envoyées avant.
 */
function reg_redirection_accueil() {
    global $reg_racine;
    reg_redirection($reg_racine);
}

/* Vérifer des identifiants utilisateurs. Ceux-ci ont été fournis par
 * l’utilisateurs et peuvent contenir n’importe quoi.
 * Même contraintes que header : pas de données envoyées avant.
 * Renvoie le nom d’utilisateur si les identifiants sont valides, faux sinon.
 * Le nom d’utilisateur renvoyé est celui de la base de donnée. La casse et
 * les accents peuvent être différent de ceux du nom donné en argument.
 */
function reg_verifier_mdp($nom, $mdp) {

    $res = mysql_query ('SELECT * FROM utilisateurs WHERE nom="'
	. mysql_real_escape_string ( $nom ) . '"');
    if (!$res) reg_erreur_mysql();
    
    $ligne = mysql_fetch_assoc($res);
    if (!$ligne) return false;

    $sel = $ligne['sel'];
    $hash = $ligne['mdp'];
    $nom = $ligne['nom'];

    if ( hash("md5", $sel . ':' . $mdp) == $hash ) {
	return $nom;
    } else {
	return FALSE;
    }
}

/* Crée une nouvelle session pour l'utilisateur donné en paramètre.
 * Même contraintes que header : pas de données envoyées avant.
 */
function reg_session_creer($nom, $persistante) {
    global $reg_racine;
    /* L'identifiant de session fait 20 charactères, correspondant à l'encodage
     * en base 64 de 15 octets. */
    $id_session = base64_encode(exec('/usr/bin/openssl rand 15 2> /dev/null'));
    if ($persistante) {
	$expiration_1 = time() + 86400 * 14;
	$expiration_2 = time() + 86400 * 28;
	setcookie('SessionRegistre', $id_session, $expiration_1, $reg_racine);
	setcookie('UtilisateurRegistre', $nom, $expiration_2, $reg_racine);
	$expiration = date('Y-m-d H:i:s', $expiration_1);
    } else {
	setcookie('SessionRegistre', $id_session, 0, $reg_racine);
	setcookie('UtilisateurRegistre', '');
	$expiration = date('Y-m-d H:i:s', time() + 7200);
    }
    $ok = mysql_query('INSERT INTO sessions VALUES("'
	. mysql_real_escape_string($id_session) .'", "'
	. mysql_real_escape_string($nom) . '", "'
	. mysql_real_escape_string($expiration) . '")');
    if (!$ok) reg_erreur_mysql();
}

/* Vérifie que la requète fait partie d'une session valide.
 * Même contraintes que header : pas de données envoyées avant.
 * Renvoie le nom de l'utilisateur s'il est bien authentifié, faux sinon.
 */
function reg_session_verifier() {
    if (!isset($_COOKIE['SessionRegistre'])) return FALSE;
	// Pas de cookie de session

    $id_session = $_COOKIE['SessionRegistre'];
    $res = mysql_query('SELECT nom, expiration FROM sessions WHERE clef="'
	. mysql_real_escape_string($id_session) . '"');
    if (!$res) reg_erreur_mysql();
    $ligne = mysql_fetch_assoc($res);

    if (!$ligne) return FALSE; // Cookie de session invalide

    $expiration = strtotime($ligne['expiration']);

    if ( $expiration < time() ) return FALSE; // Cookie de session expiré

    if ( $expiration < time() + 6000 ) {
	// Si la session est vielle, on met à jour se date d’expiration.
	$ok = mysql_query('UPDATE sessions SET expiration="'
	    . date('Y-m-d H:i:s', time() + 7200) . '" WHERE clef="'
	    . mysql_real_escape_string($id_session) . '"');
	if (!$ok) reg_erreur_mysql();
    }

    return $ligne['nom'];
}

/* Détruit la session courrante.
 * Même contraintes que header : pas de données envoyées avant.
 */
function reg_session_fermer() {
    global $reg_racine;
    if (isset($_COOKIE['SessionRegistre'])) {
	$ok = mysql_query('DELETE FROM sessions WHERE clef="'
	    . mysql_real_escape_string($_COOKIE['SessionRegistre']) . '"');
	if (!$ok) reg_erreur_mysql();
	setcookie('SessionRegistre', '', 0, $reg_racine);
	setcookie('UtilisateurRegistre', '');
    }
}

/* Renvoie vers la page d’acceuil si l’utilisateurs n’est pas authentifié.
 * Si l’utilisateur est correctement authentifié, retourne son nom.
 * Même contraintes que header : pas de données envoyées avant.
 */
function reg_authentifier() {
    global $reg_racine;
    $res = reg_session_verifier();
    if (!$res) {
	reg_redirection($reg_racine . 'Connexion?retour=' .
	    urlencode($_SERVER['REQUEST_URI']));
    } else {
	return $res;
    }
}

/* Entoure l’argument de guillemets et le protège s’il est non null et non vide,
 * renvoie 'NULL' sinon.
 * Le résultat peut être injecté directement dans une requête MySQL.
 */
function reg_mysql_quote_string($string) {
    if (is_null($string) or $string=='') {
	return 'NULL';
    } else {
	return '"' . mysql_real_escape_string($string) . '"';
    }
}

/* Transforme un type MySQL en type affichable.
 */
function reg_afficher_type($string) {
    return htmlspecialchars(ucfirst($string));
}
