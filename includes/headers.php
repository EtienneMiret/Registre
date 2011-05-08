<!DOCTYPE html>
<html lang="fr">
<head>
	<meta charset="utf-8" />
	<?php if ($reg_titre_page=='Accueil' or $reg_titre_page=='') { ?>
	<title><?php echo htmlspecialchars($reg_nom); ?></title>
	<?php } else { ?>
	<title><?php echo htmlspecialchars($reg_titre_page); ?> - <?php echo htmlspecialchars($reg_nom) ?></title>
	<?php } ?>
	<link rel="shortcut icon" href="<?php echo htmlspecialchars($reg_racine); ?>favicon"/>
	<link rel="stylesheet" type="text/css" href="<?php echo htmlspecialchars($reg_racine); ?>main"/>
	<?php
		foreach($reg_head as $balise) {
			echo $balise . PHP_EOL;
		}
	?>
<body class="<?php echo reg_current_page($reg_page); ?>" <?php if ($reg_onload) { echo ' onload="' . htmlspecialchars($reg_onload) . '"'; } ?>>

	<div id="wrapper">
		<header id="header">
			<div class="row">
				<?php if($reg_page == 1) : ?>
				<h1 class="logo"><img width="149" height="71" src="<?php echo htmlspecialchars($reg_racine); ?>logo-registre" alt="Registre" /></h1>
				<?php else : ?>
				<a class="logo" href="<?php echo $reg_racine; ?>"><img width="149" height="71" src="<?php echo htmlspecialchars($reg_racine); ?>logo-registre" alt="Registre" /></a>
				<?php endif; ?>
				<?php if ($reg_user) { ?>
				<div id="welcome-log">
					<span>Bienvenue <?php echo htmlspecialchars($reg_user); ?> !</span> |
					<a href="<?php echo htmlspecialchars($reg_racine); ?>Deconnexion">Déconnexion</a>
				</div>
				<?php } ?>
			</div>

			<nav id="main-nav">