			<nav id="main-nav">
				<ul>
					<li class="<?php if ($reg_page == 2) echo 'active'; ?>"><a href="<?php echo htmlspecialchars($reg_racine); ?>ChangerMdp">Mon profil</a></li>
					<li class="<?php if ($reg_page == 3) echo 'active'; ?>"><a href="<?php echo htmlspecialchars($reg_racine); ?>Rechercher?q=">Toutes les références</a></li>
					<li class="<?php if ($reg_page == 4) echo 'active'; ?>"><a href="<?php echo htmlspecialchars($reg_racine); ?>Enregistrer">Ajouter une référence</a></li>
					<li class="<?php if ($reg_page == 6) echo 'active'; ?>"><a class="no-border-right" href="<?php echo htmlspecialchars($reg_racine); ?>Aide">Besoin d’aide&nbsp;?</a></li>
					<li>
						<form id="form-recherche" action="<?php echo htmlspecialchars($reg_racine); ?>Rechercher" method="get">
						    <div>
							<input name="q" type="text" />
							<button type="submit">
								<span>Rechercher</span>
							</button>
						    </div>
						</form>
					</li>
					<li class="<?php if ($reg_page == 8) echo 'active'; ?>"><a href="<?php echo htmlspecialchars($reg_racine); ?>RechercheAvancee">Recherche avancée</a></li>
				</ul>
			</nav>
