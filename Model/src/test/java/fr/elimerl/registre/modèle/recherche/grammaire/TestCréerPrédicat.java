package fr.elimerl.registre.modèle.recherche.grammaire;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import fr.elimerl.registre.modèle.entités.Fiche;
import fr.elimerl.registre.modèle.entités.Propriétaire;
import fr.elimerl.registre.modèle.recherche.signes.Champ;
import fr.elimerl.registre.modèle.recherche.signes.MotClé;

/**
 * Ce test JUnit teste les implémentations de la méthode
 * {@link Expression#créerPrédicat(CriteriaBuilder, CriteriaQuery, Root)} ainsi
 * que la méthode
 * {@link Requête#créerPrédicat(CriteriaBuilder, CriteriaQuery, Root)}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext.xml")
@Transactional("gestionnaireTransactions")
public class TestCréerPrédicat {

    /** Gestionnaire d’entités fournit par Spring. */
    @PersistenceContext(unitName = "Registre")
    private EntityManager em;

    /**
     * Test de la méthode
     * {@link Requête#créerPrédicat(CriteriaBuilder, CriteriaQuery, Root)}.
     */
    @Test
    public void testRequête() {
	final Requête requêteUtilisateur = new Requête(false,
		new MotCléSimple(new MotClé("coucou")),
		new RequêteSurChamp<String>(Champ.TITRE,
			new MotClé("honneur"),
			new MotClé("dette")
		),
		new RequêteSurChamp<Propriétaire>(Champ.PROPRIÉTAIRE,
			new MotClé("Etienne")
		)
	);
	final CriteriaBuilder constructeur = em.getCriteriaBuilder();
	final CriteriaQuery<Fiche> requête =
		constructeur.createQuery(Fiche.class);
	final Root<Fiche> fiche = requête.from(Fiche.class);
	requête.select(fiche);
	requête.where(requêteUtilisateur.créerPrédicat(constructeur, requête,
		fiche));
	final TypedQuery<Fiche> requêteJpa = em.createQuery(requête);
	final List<Fiche> résultats = requêteJpa.getResultList();
	assertEquals(2, résultats.size());
	assertEquals(1, résultats.get(0).getId().longValue());
	assertEquals(2, résultats.get(1).getId().longValue());
    }

}
