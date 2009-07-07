<?php
require('utilitaires.php');
require('connexion_bd.php');

header('HTTP/1.1 303 See Other');
header('Location: http://ibook-g4.elimerl.fr/Registre/');
reg_session_fermer();

?>
