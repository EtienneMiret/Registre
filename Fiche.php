<?php
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
    require('headers.php');
    ?><p>Désolé, la référence que vous avez demandé n’existe pas.<?php
    exit(0);
}

require('headers.php');
echo "<dl>\n";

if (isset($general['titre'])) { ?>
    <dt>Titre</dt>
    <dd><?php echo htmlspecialchars($general['titre']) ?></dd>
<?php }
if (isset($general['type'])) { ?>
    <dt>Type</dt>
    <dd><?php echo htmlspecialchars($general['type']) ?></dd>
<?php }
if (isset($film['realisateur'])) { ?>
    <dt>Réalisateur</dt>
    <dd><?php echo htmlspecialchars($film['realisateur']) ?></dd>
<?php }
$acteur = mysql_fetch_assoc($acteurs);
if ($acteur) { ?>
    <dt>Acteurs</dt>
    <dd><ul>
<?php do { ?>
	    <li><?php echo htmlspecialchars($acteur['acteur']) ?></li>
<?php } while ($acteur = mysql_fetch_assoc($acteurs)); ?>
	</ul></dd>
<?php }
if (isset($general['commentaire'])) { ?>
    <dt>Commentaire</dt>
    <dd><?php echo htmlspecialchars($general['commentaire']) ?></dd>
<?php }
if (isset($general['proprietaire'])) { ?>
    <dt>Proprietaire</dt>
    <dd><?php echo htmlspecialchars($general['proprietaire']) ?></dd>
<?php }
if (isset($general['emplacement'])) { ?>
    <dt>Emplacement</dt>
    <dd><?php echo htmlspecialchars($general['emplacement']) ?></dd>
<?php }
if (isset($general['createur'])) { ?>
    <dt>Enregistré par</dt>
    <dd><?php echo htmlspecialchars($general['createur']) ?></dd>
<?php }
if (isset($general['creation'])) { ?>
    <dt>Enregistré le</dt>
    <dd><?php echo htmlspecialchars($general['creation']) ?></dd>
<?php }
if (isset($general['dernier_editeur'])) { ?>
    <dt>Dernière modification par</dt>
    <dd><?php echo htmlspecialchars($general['dernier_editeur']) ?></dd>
<?php }
if (isset($general['derniere_edition'])) { ?>
    <dt>Dernière modification le</dt>
    <dd><?php echo htmlspecialchars($general['derniere_edition']) ?></dd>
<?php }

echo "</dl>\n";
