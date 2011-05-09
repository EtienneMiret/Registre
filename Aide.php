<?php
require('config.php');
require('includes/definitions.php');
require('includes/initialiser.php');
require('includes/utilitaires.php');
require('includes/connexion_bd.php');

$reg_user = reg_authentifier();
$reg_titre_page='Aide';
$reg_page=PAGE_AIDE;
require('includes/headers.php');
?>
<h2 id="Introduction">Introduction</h2>

<p>Registre est une base de données dans laquelle vous pouvez référencer votre
bibliothèque : livres, films et bandes-dessinées.

<p>Vous pouvez ainsi faire des recherches pour trouver quelles sont les
bandes-dessinées de telle série que vous avez déjà, ou pour trouver tous les
films que vous possèdez avec tel ou tel acteur.

<p>Registre peut aussi être utile pour noter des informations utiles à propos
d’un objet référencé comme par exemple l’endroit où il est rangé ou
le nom de la personne à qui vous l’avez prété.

<h2 id="Recherche">Recherche</h2>

<p>La fonction recherche vous permet de retrouver n’importe quelle référence en
tappant quelques mots.
Le formulaire de <a href="RechercheAvancee">recherche avancée</a> vous permet
de rechercher un mot clés dans un champ particulier.
Attention, il y a actuellement un problème avec la recherche d’acteurs :
vous ne devez entrer qu’un seul nom (au maximum) sinon il n’y aura aucun
résultats.

<p class="non-fini">La recherche avancée est également disponible depuis la
barre de recherche normale grâce à l’utilisation de certains mots-clés :
<ul>
  <li><em class="mc-recherche">titre:</em>
  <li><em class="mc-recherche">realisateur:</em>
  <li><em class="mc-recherche">acteur:</em>
  <li><em class="mc-recherche">compositeur:</em>
  <li><em class="mc-recherche">commentaire:</em>
  <li><em class="mc-recherche">proprietaire:</em>
  <li><em class="mc-recherche">emplacement:</em>
  <li><em class="mc-recherche">auteur:</em>
  <li><em class="mc-recherche">dessinateur:</em>
  <li><em class="mc-recherche">scenariste:</em>
  <li><em class="mc-recherche">serie:</em>
  <li><em class="mc-recherche">type:</em>
  <li><em class="mc-recherche">createur:</em>
  <li><em class="mc-recherche">editeur:</em>
</ul>
<p class="non-commence non-fini">Il est tout à fait possible d’utiliser
plusieurs mots-clés dans une recherche.
Par exemple, vous pouvez faire des recherches comme :
<ul>
  <li><a href="Rechercher?q=auteur:(Tom+Clancy)+titre:(Net Force)"
    >auteur:(Tom Clancy) titre:(Net Force)</a>
  <li><a href="Rechercher?q=James+Bond+acteur:Moore"
    >James Bond acteur:Moore</a>
</ul>

<h2 id="Enregistrement">Enregistrement/modification d’une fiche</h2>

<p>Lorsque vous voulez ajouter une nouvelle fiche ou modifier une fiche
existante, vous pouvez remplir un certain nombre de champs qui dépendent du
type de fiche que vous éditez.
Seuls les champs <em class="nom-champ">titre</em> et
<em class="nom-champ">type</em> sont obligatoires.

<p>L’<em class="nom-champ">emplacement</em> est le lieu de rangement de la
référence. Il faut penser à le mettre à jour quand on décide de déplacer des
livres ou des bandes-dessinées.

<p class="non-fini">Pour les films et les livres, on peut indiquer un ou
plusieurs genres.
Les genres existants sont :
<dl>
  <dt>Action :
  <dd>Les problèmes de l’histoires sont résolus par l’action, plus que par
    la discution ou la réflexion.
  <dt>Documentaire :
  <dd>Le film a pour objectif d’informer le spectateur sur un sujet quelquonque.
  <dt>Fantastique :
  <dd>L’œuvre met en scène des elfes, des orcs, des dragons, de la magie ou
    des inventions similaires.
  <dt>Film de guerre :
  <dd>L’action se déroule lors d’un conflit armé.
  <dt>Histoire vraie :
  <dd>L’histoire est inspirée de faits rééls.
  <dt>Historique :
  <dd>L’action se déroule à une époque passée du monde réel.
  <dt>Humour :
  <dd>L’œuvre est drôle ou amusante.
  <dt>Policier :
  <dd>L’histoire raconte une enquète criminelle.
  <dt>Romantique :
  <dd>L’intrigue est basés sur les sentiments amoureux des personnages.
  <dt>Science-fiction :
  <dd>L’histoire met en scène une technogie supérieure à la notre.
</dl>
<?php require('includes/footer.php'); ?>
