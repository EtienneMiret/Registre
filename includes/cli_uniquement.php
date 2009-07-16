<?php

if (php_sapi_name() <> "cli") {
    header('HTTP/1.1 403 Forbidden');
    header('Content-Type: text/plain; charset=UTF-8'); ?>
Ce script doit être utilisé en ligne de commande.
<?php exit(1); } ?>
