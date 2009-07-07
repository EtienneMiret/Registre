<?php

/* Vérifer des identifiants utilisateurs. Ceux-ci ont été fournis par
 * l’utilisateurs et peuvent contenir n’importe quoi.
 * Renvoie un vrai si les identifiants sont valides, faux sinon.
 */
function reg_verifier_mdp($nom, $mdp) {

    $res = mysql_query ('SELECT sel, mdp FROM utilisateurs WHERE nom="'
	. mysql_real_escape_string ( $nom ) . '"');
    if (!$res) die('Requête invalide : ' . mysql_error());
    
    $ligne = mysql_fetch_assoc($res);
    if (!$ligne) return false;

    $sel = $ligne['sel'];
    $hash = $ligne['mdp'];

    return ( hash("md5", $sel . ':' . $mdp) == $hash );
}

/* Renvoie vers la page d’acceuil si l’utilisateurs n’est pas authentifié.
 * Même contraintes que header : pas de données envoyées avant.
 */
function reg_verifier_authentification() {
    if (!reg_verifier_mdp($_COOKIE['IDRegistre'], $_COOKIE['PwdRegistre'])) {
	header('HTTP/1.1 303 See Other');
	header('Location: http://ibook-g4.elimerl.fr/Registre/');
	exit(0);
    }
}
?>
