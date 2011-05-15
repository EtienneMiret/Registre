<?php
require('config.php');
require('includes/definitions.php');
require('includes/initialiser.php');
require('includes/utilitaires.php');
require('includes/connexion_bd.php');

$reg_user = reg_authentifier();
$reg_page = PAGE_FICHE;
$id = 0;
if (isset($_GET['id'])) $id = (int) $_GET['id'];

if ($id == 0) reg_redirection_accueil();

$general = mysql_query('SELECT * FROM tout WHERE id=' . $id)
    or reg_erreur_mysql();
$film = mysql_query('SELECT * FROM films WHERE id=' . $id)
    or reg_erreur_mysql();
$acteurs = mysql_query('SELECT * FROM acteurs WHERE id=' . $id)
    or reg_erreur_mysql();
$livre = mysql_query('SELECT * from livres WHERE id=' . $id)
    or reg_erreur_mysql();
$bd = mysql_query('SELECT * from bd WHERE id=' . $id)
    or reg_erreur_mysql();

$general = mysql_fetch_assoc($general);
$film = mysql_fetch_assoc($film);
$livre = mysql_fetch_assoc($livre);
$bd = mysql_fetch_assoc($bd);

if (!$general) {
    $reg_titre_page = 'Référence inconnue';
    header('HTTP/1.1 404 Not Found');
    require('includes/headers.php');
    ?>
    <p class="msg msg-nok">Désolé, la référence que vous avez demandé n’existe pas.
    <?php
    exit(0);
}

$reg_titre_page = $general['titre'];
require('includes/headers.php');
?>


	<?php if (isset($general['titre'])) { ?>
	<h1><?php echo htmlspecialchars($general['titre']) ?></h1>
	<?php } ?>
	
	<div class="grid-200 box-data-side">
		<?php $picture = false; if ($picture == true) { ?>
		<img class="picture" width="200" src="<?php echo htmlspecialchars($reg_racine); ?>placeholder" />

		<?php } else { ?>
		<div class="picture picture-placeholder">
			<p>Pas encore d'image pour le moment.</p>
		</div>
		<?php } ?>
		
		<?php if (isset($film['genres'])) { ?>
		<p>Genres : <?php echo htmlspecialchars(ucfirst(str_replace(',', ', ', $film['genres']))); echo '.'; ?></p>

		<?php } if (isset($livre['genres'])) { ?>
		<p>Genres : <?php echo htmlspecialchars(ucfirst(str_replace(',',', ', $livre['genres']))); echo '.'; ?></p>
		<?php } ?>
	</div>


	<div class="grid-430 box-data-main">
		<div class="inner">
			<?php if (isset($film['realisateur'])) { ?>
			<p>Réalisé par <strong><?php echo htmlspecialchars($film['realisateur']) ?></strong></p>

			<?php } $acteur = mysql_fetch_assoc($acteurs); if ($acteur) { ?>
			<p>Avec
				<?php do { ?>
					<span><?php echo htmlspecialchars($acteur['acteur']) ?></span>,
				<?php } while ($acteur = mysql_fetch_assoc($acteurs)); ?>
			</p>

			<?php } if (isset($film['compositeur'])) { ?>
			<p>Compositeur : <?php echo htmlspecialchars($film['compositeur']) ?></p>

			<?php } if (isset($livre['auteur'])) { ?>
			<p>Écrit par <strong><?php echo htmlspecialchars($livre['auteur']) ?></strong></p>

			<?php } if (isset($bd['dessinateur'])) { ?>
			<p>Dessiné par <strong><?php echo htmlspecialchars($bd['dessinateur']) ?></strong></p>

			<?php } if (isset($bd['scenariste'])) { ?>
			<p>Scénarisé par <span><?php echo htmlspecialchars($bd['scenariste']) ?></span></p>

			<?php } if (isset($general['serie'])) { ?>
			<p>Série : <strong><?php echo htmlspecialchars($general['serie']) ?></strong></p>

			<?php } if (isset($bd['numero'])) { ?>
			<p>Numéro : <span><?php echo htmlspecialchars($bd['numero']) ?></span></p>
			<?php } ?>

			<ul>
				<li>
					<?php if (isset($general['type'])) { ?>
					<span><?php echo reg_afficher_type($general['type']) ?></span>

					<?php } if (isset($general['proprietaire'])) { ?>
					appartenant à <span><?php echo htmlspecialchars($general['proprietaire']) ?></span>.
				</li>

				<?php } if (isset($general['createur'])) { ?>
				<li>Enregistré par <?php echo htmlspecialchars($general['createur']) ?></li>

				<?php } if (isset($general['creation'])) { ?>
				<li>Enregistré le <?php echo htmlspecialchars($general['creation']) ?></li>

				<?php } if (isset($general['emplacement'])) { ?>
				<li>ce trouve actuellement ici : <?php echo htmlspecialchars($general['emplacement']) ?></li>
				<?php } if (isset($general['dernier_editeur'])) { ?>
				<li>
					Dernière modification par <?php echo htmlspecialchars($general['dernier_editeur']) ?>
					<?php } if (isset($general['derniere_edition'])) { ?>
					le <?php echo htmlspecialchars($general['derniere_edition']) ?>
				</li>
				<?php } if (isset($general['commentaire'])) { ?>
				<li>Commentaire : <?php echo str_replace("\n", '<br>', htmlspecialchars($general['commentaire'])) ?></li>
				<?php } ?>
			</ul>
		</div>

		<form action="<?php echo $reg_racine ?>Editer/<?php echo $id ?>" method="get" class="fiche">
			<button type="submit">Modifier</button>
		</form>
	</div>

<?php require('includes/footer.php'); ?>