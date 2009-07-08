<?php
require('utilitaires.php');
require('connexion_bd.php');

reg_session_fermer();
header('HTTP/1.1 303 See Other');
header('Location: http://ibook-g4.elimerl.fr/Registre/');

?>
