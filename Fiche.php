<?php
require('config.php');
require('utilitaires.php');
require('connexion_bd.php');

$user = reg_authentifier();
$id = 0;
if (isset($_GET['id'])) $id = (int) $_GET['id'];

if ($id == 0) reg_redirection_accueil();

$general = mysql_query('SELECT * FROM tout WHERE id=' . $id)
    or reg_mysql_error();
$film = mysql_query('SELECT * FROM films WHERE id=' . $id)
    or reg_mysql_error();
$acteurs = mysql_query('SELECT * FROM acteurs WHERE id=' . $id)
    or reg_mysql_error();

$general = mysql_fetch_assoc($general);
$film = mysql_fetch_assoc($film);

if ($general xor $film) reg_erreur_serveur('Base de donnée corrompue !');

if (!$general) {
    $reg_titre_page = 'Référence inconnue';
    require('headers.php');
    require('formulaire-recherche.php');
    ?>
    <p class="msg nok">Désolé, la référence que vous avez demandé n’existe pas.
    <p class="navigation">Retour à l’<a href="<?php echo $reg_accueil ?>">accueil</a>.
    <?php
    exit(0);
}

$reg_titre_page = $general['titre'];
require('headers.php');
require('formulaire-recherche.php');
?>
<p class="navigation">Retour à l’<a href="<?php echo $reg_accueil; ?>">accueil</a>.
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
$acteur = mysql_fetch_assoc($acteurs);
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

<form action="<?php echo $reg_accueil ?>Editer/<?php echo $id ?>" method="get"
    class="fiche">
<p><button type="submit">Modifier</button>
</form>
