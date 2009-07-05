<?php
    $mysql = mysql_connect( ':/private/var/mysql/mysql.sock', 'registre', 'somepass' )
	or die('Impossible de se connecter : ' . mysql_error());
    mysql_select_db('Registre')
	or die('Impossible de séléctionner la base de données : ' . mysql_error());
?>
