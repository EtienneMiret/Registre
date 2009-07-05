<?php

/* Vérifer des identifiants utilisateurs. Ceux-ci ont été fournis par
 * l’utilisateurs et peuvent contenir n’importequoi.
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

?>
