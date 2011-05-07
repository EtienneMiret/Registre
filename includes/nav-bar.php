<ul id="nav-bar">
  <li><?php if ($reg_page != PAGE_PROFIL) { ?><a href="<?php
    echo htmlspecialchars($reg_racine); ?>ChangerMdp">Mon profil</a><?php
	} else { ?>Mon profil<?php } ?>

  <li><?php if ($reg_page != PAGE_TOUT) { ?><a href="<?php
    echo htmlspecialchars($reg_racine); ?>Rechercher?q=">Toutes les références</a><?php
	} else { ?>Toutes les références<?php } ?>

  <li><?php if ($reg_page != PAGE_AJOUTER) { ?><a href="<?php
    echo htmlspecialchars($reg_racine); ?>Enregistrer">Ajouter une référence</a><?php
	} else { ?>Ajouter une référence<?php } ?>

  <li><?php if ($reg_page != PAGE_AIDE) { ?><a href="<?php
    echo htmlspecialchars($reg_racine); ?>Aide">Besoin d’aide&nbsp;?</a><?php
	} else { ?>Besoin d’aide&nbsp;?<?php } ?>

  <li><form id="form-recherche" action="<?php
    echo htmlspecialchars($reg_racine); ?>Rechercher" method="get">
      <input name="q"><button type="submit"><img src="<?php
	echo htmlspecialchars($reg_racine); ?>Loupe"></button>
    </form>

  <li><?php if ($reg_page != PAGE_RECHERCHE_AVANCEE) { ?><a href="<?php
    echo htmlspecialchars($reg_racine); ?>RechercheAvancee">Recherche avancée</a><?php
	} else { ?>Recherche avancée<?php } ?>

</ul>
