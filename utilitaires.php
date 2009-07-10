<?php

/* Affiche le message d’erreur dans une page HTML avec un code 500.
 * Même contraintes que header : pas de données envoyées avant.
 */
function reg_erreur_serveur($msg) {
    header('HTTP/1.1 500 Server Error');
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
    reg_redirection('/Registre/');
}

/* Vérifer des identifiants utilisateurs. Ceux-ci ont été fournis par
 * l’utilisateurs et peuvent contenir n’importe quoi.
 * Même contraintes que header : pas de données envoyées avant.
 * Renvoie un vrai si les identifiants sont valides, faux sinon.
 */
function reg_verifier_mdp($nom, $mdp) {

    $res = mysql_query ('SELECT sel, mdp FROM utilisateurs WHERE nom="'
	. mysql_real_escape_string ( $nom ) . '"');
    if (!$res) reg_erreur_mysql();
    
    $ligne = mysql_fetch_assoc($res);
    if (!$ligne) return false;

    $sel = $ligne['sel'];
    $hash = $ligne['mdp'];

    return ( hash("md5", $sel . ':' . $mdp) == $hash );
}

/* Crée une nouvelle session pour l'utilisateur donné en paramètre.
 * Même contraintes que header : pas de données envoyées avant.
 */
function reg_session_creer($nom) {
    /* L'identifiant de session fait 20 charactères, correspondant à l'encodage
     * en base 64 de 15 octets. */
    $id_session = base64_encode(exec('/usr/bin/openssl rand 15 2> /dev/null'));
    $expiration = date('Y-m-d H:i:s', time() + 7200);
    $ok = mysql_query('INSERT INTO sessions VALUES("'
	. mysql_real_escape_string($id_session) .'", "'
	. mysql_real_escape_string($nom) . '", "'
	. mysql_real_escape_string($expiration) . '")');
    if (!$ok) reg_erreur_mysql();
    setcookie('SessionRegistre', $id_session, 0, '/Registre/');
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
    if (isset($_COOKIE['SessionRegistre'])) {
	$ok = mysql_query('DELETE FROM sessions WHERE clef="'
	    . mysql_real_escape_string($_COOKIE['SessionRegistre']) . '"');
	if (!$ok) reg_erreur_mysql();
	setcookie('SessionRegistre', '', 0, '/Registre/');
    }
}

/* Renvoie vers la page d’acceuil si l’utilisateurs n’est pas authentifié.
 * Si l’utilisateur est correctement authentifié, retourne son nom.
 * Même contraintes que header : pas de données envoyées avant.
 */
function reg_authentifier() {
    $res = reg_session_verifier();
    if (!$res) {
	reg_redirection_accueil();
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
