<?php

header('HTTP/1.1 303 See Other');
header('Location: http://ibook-g4.elimerl.fr/Registre/');
setcookie('IDRegistre', '', 0, '/Registre/');
setcookie('PwdRegistre', '', 0, '/Registre/');

?>
