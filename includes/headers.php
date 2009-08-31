<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<?php if ($reg_titre_page=='Accueil') { ?>
<title><?php echo $reg_nom; ?></title>
<?php } else { ?>
<title><?php echo htmlspecialchars($reg_titre_page); ?> - <?php echo $reg_nom ?></title>
<?php } ?>
<link rel="stylesheet" type="text/css" href="<?php echo $reg_racine; ?>main">
<?php
if (isset($reg_head)) {
    foreach($reg_head as $balise) {
	echo $balise . PHP_EOL;
    }
}
?>
<body<?php if (isset($reg_onload)) {
    echo ' onload="' . htmlspecialchars($reg_onload) . '"';
} ?>>
<h1><?php echo htmlspecialchars($reg_titre_page); ?></h1>
