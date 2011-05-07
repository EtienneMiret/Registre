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
    require('includes/nav-bar.php');
    ?>
    <p class="msg nok">Désolé, la référence que vous avez demandé n’existe pas.
    <?php
    exit(0);
}

$reg_titre_page = $general['titre'];
require('includes/headers.php');
require('includes/nav-bar.php');
?>
<dl class="fiche">
<?php

if (isset($general['titre'])) { ?>
    <dt class="titre">Titre</dt>
    <dd class="titre"><?php echo htmlspecialchars($general['titre']) ?></dd>
<?php }
if (isset($general['type'])) { ?>
    <dt class="type">Type</dt>
    <dd class="type"><?php echo reg_afficher_type($general['type']) ?></dd>
<?php }
if (isset($film['realisateur'])) { ?>
    <dt class="realisateur">Réalisateur</dt>
    <dd class="realisateur"><?php echo htmlspecialchars($film['realisateur']) ?></dd>
<?php }
$acteur = mysql_fetch_assoc($acteurs);
if ($acteur) { ?>
    <dt class="acteurs">Acteurs</dt>
    <dd class="acteurs"><ul>
<?php do { ?>
	    <li><?php echo htmlspecialchars($acteur['acteur']) ?></li>
<?php } while ($acteur = mysql_fetch_assoc($acteurs)); ?>
	</ul></dd>
<?php }
if (isset($film['compositeur'])) { ?>
    <dt class="compositeur">Compositeur</dt>
    <dd class="compositeur"><?php echo htmlspecialchars($film['compositeur']) ?></dd>
<?php }
if (isset($film['genres'])) { ?>
    <dt class="genres">Genres</dt>
    <dd class="genres"><?php
	echo htmlspecialchars(ucfirst(str_replace(',', ', ', $film['genres'])));	echo '.';
    ?></dd>
<?php }
if (isset($livre['auteur'])) { ?>
    <dt class="auteur">Auteur</dt>
    <dd class="auteur"><?php echo htmlspecialchars($livre['auteur']) ?></dd>
<?php }
if (isset($livre['genres'])) { ?>
    <dt class="genres">Genres</dt>
    <dd class="genres"><?php
	echo htmlspecialchars(ucfirst(str_replace(',',', ',$livre['genres'])));
	echo '.';
    ?></dd>
<?php }
if (isset($bd['dessinateur'])) { ?>
    <dt class="dessinateur">Dessinateur</dt>
    <dd class="dessinateur"><?php echo htmlspecialchars($bd['dessinateur']) ?></dd>
<?php }
if (isset($bd['scenariste'])) { ?>
    <dt class="scenariste">Scénariste</dt>
    <dd class="scenariste"><?php echo htmlspecialchars($bd['scenariste']) ?></dd>
<?php }
if (isset($bd['serie'])) { ?>
    <dt class="serie">Série</dt>
    <dd class="serie"><?php echo htmlspecialchars($bd['serie']) ?></dd>
<?php }
if (isset($bd['numero'])) { ?>
    <dt class="numero">Numéro</dt>
    <dd class="numero"><?php echo htmlspecialchars($bd['numero']) ?></dd>
<?php }
if (isset($general['commentaire'])) { ?>
    <dt class="commentaire">Commentaire</dt>
    <dd class="commentaire"><?php echo str_replace("\n", '<br>', htmlspecialchars($general['commentaire'])) ?></dd>
<?php }
if (isset($general['proprietaire'])) { ?>
    <dt class="proprietaire">Proprietaire</dt>
    <dd class="proprietaire"><?php echo htmlspecialchars($general['proprietaire']) ?></dd>
<?php }
if (isset($general['emplacement'])) { ?>
    <dt class="emplacement">Emplacement</dt>
    <dd class="emplacement"><?php echo htmlspecialchars($general['emplacement']) ?></dd>
<?php }
if (isset($general['createur'])) { ?>
    <dt class="createur">Enregistré par</dt>
    <dd class="createur"><?php echo htmlspecialchars($general['createur']) ?></dd>
<?php }
if (isset($general['creation'])) { ?>
    <dt class="creation">Enregistré le</dt>
    <dd class="creation"><?php echo htmlspecialchars($general['creation']) ?></dd>
<?php }
if (isset($general['dernier_editeur'])) { ?>
    <dt class="dernier_editeur">Dernière modification par</dt>
    <dd class="dernier_editeur"><?php echo htmlspecialchars($general['dernier_editeur']) ?></dd>
<?php }
if (isset($general['derniere_edition'])) { ?>
    <dt class="derniere_edition">Dernière modification le</dt>
    <dd class="derniere_edition"><?php echo htmlspecialchars($general['derniere_edition']) ?></dd>
<?php } ?>
</dl>

<form action="<?php echo $reg_racine ?>Editer/<?php echo $id ?>" method="get"
    class="fiche">
<p><button type="submit">Modifier</button>
</form>
