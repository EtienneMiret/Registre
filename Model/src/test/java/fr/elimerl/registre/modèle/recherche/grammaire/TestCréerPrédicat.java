package fr.elimerl.registre.modèle.recherche.grammaire;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    /** Journal SLF4J pour cette classe. */
    private static final Logger journal =
	    LoggerFactory.getLogger(TestCréerPrédicat.class);

    /** Gestionnaire d’entités fournit par Spring. */
    @PersistenceContext(unitName = "Registre")
    private EntityManager em;

    /** Constructeur de requêtes lié à {@link #em}. */
    private CriteriaBuilder constructeur;

    /** Requête principale. On teste la construction de sa clause where. */
    private CriteriaQuery<Fiche> requête;

    /** La racine de la requête principale. */
    private Root<Fiche> fiche;

    /**
     * Prépare l’environnement pour les tests.
     */
    @Before
    public void setUp() {
	constructeur = em.getCriteriaBuilder();
	requête = constructeur.createQuery(Fiche.class);
	fiche = requête.from(Fiche.class);
	requête.select(fiche);
    }

    /**
     * Test de la méthode
     * {@link MotCléSimple#créerPrédicat(CriteriaBuilder, CriteriaQuery, Root)}.
     */
    @Test
    public void testMotCléSimple() {
	journal.info("Création d’un prédicat à partir d’un mot clé simple.");
	final MotCléSimple motClé = new MotCléSimple(new MotClé("super"));
	requête.where(motClé.créerPrédicat(constructeur, requête, fiche));
	final TypedQuery<Fiche> requêteJpa = em.createQuery(requête);
	final List<Fiche> résultats = requêteJpa.getResultList();
	assertEquals(1, résultats.size());
	assertEquals(1, résultats.get(0).getId().longValue());
    }

    /**
     * Test de la méthode {@link RequêteEntreParenthèse#créerPrédicat(
     * CriteriaBuilder, CriteriaQuery, Root)}.
     */
    @Test
    public void testRequêteEntreParenthèse() {
	journal.info("Création d’un prédicat à partir d’une requête entre "
		+ "parenthèses.");
	final RequêteEntreParenthèse requêteEntreParenthèse =
		new RequêteEntreParenthèse(
			new Requête(true,
				new MotCléSimple(new MotClé("super"))
			)
		);
	requête.where(requêteEntreParenthèse.créerPrédicat(constructeur,
		requête, fiche));
	final TypedQuery<Fiche> requêteJpa = em.createQuery(requête);
	final List<Fiche> résultats = requêteJpa.getResultList();
	assertEquals(1, résultats.size());
	assertEquals(1, résultats.get(0).getId().longValue());
    }

    /**
     * Test de la méthode
     * {@link Requête#créerPrédicat(CriteriaBuilder, CriteriaQuery, Root)}.
     */
    @Test
    public void testRequête() {
	journal.info("Création d’un prédicat à partir d’une requête complexe.");
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
	requête.where(requêteUtilisateur.créerPrédicat(constructeur, requête,
		fiche));
	final TypedQuery<Fiche> requêteJpa = em.createQuery(requête);
	final List<Fiche> résultats = requêteJpa.getResultList();
	assertEquals(2, résultats.size());
	assertEquals(1, résultats.get(0).getId().longValue());
	assertEquals(2, résultats.get(1).getId().longValue());
    }

}
