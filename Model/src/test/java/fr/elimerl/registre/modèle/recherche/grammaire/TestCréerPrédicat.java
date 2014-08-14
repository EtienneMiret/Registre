package fr.elimerl.registre.modèle.recherche.grammaire;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.annotation.Resource;
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

import fr.elimerl.registre.entités.Acteur;
import fr.elimerl.registre.entités.Auteur;
import fr.elimerl.registre.entités.Compositeur;
import fr.elimerl.registre.entités.Dessinateur;
import fr.elimerl.registre.entités.Emplacement;
import fr.elimerl.registre.entités.Fiche;
import fr.elimerl.registre.entités.Propriétaire;
import fr.elimerl.registre.entités.Réalisateur;
import fr.elimerl.registre.entités.Scénariste;
import fr.elimerl.registre.entités.Série;
import fr.elimerl.registre.recherche.grammaire.Expression;
import fr.elimerl.registre.recherche.grammaire.MotCléSimple;
import fr.elimerl.registre.recherche.grammaire.Requête;
import fr.elimerl.registre.recherche.grammaire.RequêteEntreParenthèse;
import fr.elimerl.registre.recherche.grammaire.RequêteSurChamp;
import fr.elimerl.registre.recherche.signes.Champ;
import fr.elimerl.registre.recherche.signes.MotClé;
import fr.elimerl.registre.services.Indexeur;

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

    /** Le service d’indexation, fournit par Spring. */
    @Resource(name = "indexeur")
    private Indexeur indexeur;

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
	requête.orderBy(constructeur.asc(fiche.get("id")));

	/* Indexation de toutes les fiches. */
	final TypedQuery<Fiche> requêteJpa = em.createQuery(requête);
	for (final Fiche fiche : requêteJpa.getResultList()) {
	    indexeur.réindexer(fiche);
	}
    }

    /**
     * Test de la méthode
     * {@link MotCléSimple#créerPrédicat(CriteriaBuilder, CriteriaQuery, Root)}.
     */
    @Test
    public void testMotCléSimple() {
	journal.info("Création d’un prédicat à partir d’un mot clé simple.");
	final MotCléSimple motClé = new MotCléSimple(new MotClé("super"));
	final List<Fiche> résultats = exécuter(motClé);
	assertEquals(1, résultats.size());
	assertEquals(1, résultats.get(0).getId().longValue());
    }

    /**
     * Test de la méthode {@link RequêteSurChamp#créerPrédicat(CriteriaBuilder,
     * CriteriaQuery, Root)} sur le champ {@link Champ#TITRE TITRE}.
     */
    @Test
    public void testRequêteSurChampTitre() {
	journal.info("Création d’un prédicat à partir d’une requête sur le"
		+ " titre.");
	final RequêteSurChamp<String> requêteSurChamp =
		new RequêteSurChamp<String>(Champ.TITRE, new MotClé("la"));
	final List<Fiche> résultats = exécuter(requêteSurChamp);
	assertEquals(3, résultats.size());
	assertEquals(4, résultats.get(0).getId().longValue());
	assertEquals(5, résultats.get(1).getId().longValue());
	assertEquals(7, résultats.get(2).getId().longValue());
    }

    /**
     * Test de la méthode {@link RequêteSurChamp#créerPrédicat(CriteriaBuilder,
     * CriteriaQuery, Root)} sur le champ {@link Champ#COMMENTAIRE COMMENTAIRE}.
     */
    @Test
    public void testRequêteSurChampCommentaire() {
	journal.info("Création d’un prédicat à partir d’une requête sur le"
		+ " commentaire.");
	final RequêteSurChamp<String> requêteSurChamp =
		new RequêteSurChamp<String>(Champ.COMMENTAIRE,
			new MotClé("super"));
	final List<Fiche> résultats = exécuter(requêteSurChamp);
	assertEquals(1, résultats.size());
	assertEquals(1, résultats.get(0).getId().longValue());
    }

    /**
     * Test de la méthode {@link RequêteSurChamp#créerPrédicat(CriteriaBuilder,
     * CriteriaQuery, Root)} sur le champ {@link Champ#SÉRIE SÉRIE}.
     */
    @Test
    public void testRequêteSurChampSérie() {
	journal.info("Création d’un prédicat à partir d’une requête sur la"
		+ " série.");
	final RequêteSurChamp<Série> requêteSurChamp =
		new RequêteSurChamp<Série>(Champ.SÉRIE,
			new MotClé("warhammer"));
	final List<Fiche> résultats = exécuter(requêteSurChamp);
	assertEquals(2, résultats.size());
	assertEquals(4, résultats.get(0).getId().longValue());
	assertEquals(5, résultats.get(1).getId().longValue());
    }

    /**
     * Test de la méthode {@link RequêteSurChamp#créerPrédicat(CriteriaBuilder,
     * CriteriaQuery, Root)} sur le champ {@link Champ#PROPRIÉTAIRE
     * PROPRIÉTAIRE}.
     */
    @Test
    public void testRequêteSurChampPropriétaire() {
	journal.info("Création d’un prédicat à partir d’une requête sur le"
		+ " propriétaire.");
	final RequêteSurChamp<Propriétaire> requêteSurChamp =
		new RequêteSurChamp<Propriétaire>(Champ.PROPRIÉTAIRE,
			new MotClé("etienne"));
	final List<Fiche> résultats = exécuter(requêteSurChamp);
	assertEquals(4, résultats.size());
	assertEquals(1, résultats.get(0).getId().longValue());
	assertEquals(2, résultats.get(1).getId().longValue());
	assertEquals(4, résultats.get(2).getId().longValue());
	assertEquals(5, résultats.get(3).getId().longValue());
    }

    /**
     * Test de la méthode {@link RequêteSurChamp#créerPrédicat(CriteriaBuilder,
     * CriteriaQuery, Root)} sur le champ {@link Champ#EMPLACEMENT
     * EMPLACEMENT}.
     */
    @Test
    public void testRequêteSurChampEmplacement() {
	journal.info("Création d’un prédicat à partir d’une requête sur"
		+ " l’emplacement.");
	final RequêteSurChamp<Emplacement> requêteSurChamp =
		new RequêteSurChamp<Emplacement>(Champ.EMPLACEMENT,
			new MotClé("verneuil"));
	final List<Fiche> résultats = exécuter(requêteSurChamp);
	assertEquals(3, résultats.size());
	assertEquals(0, résultats.get(0).getId().longValue());
	assertEquals(1, résultats.get(1).getId().longValue());
	assertEquals(2, résultats.get(2).getId().longValue());
    }

    /**
     * Test de la méthode {@link RequêteSurChamp#créerPrédicat(CriteriaBuilder,
     * CriteriaQuery, Root)} sur le champ {@link Champ#RÉALISATEUR
     * RÉALISATEUR}.
     */
    @Test
    public void testRequêteSurChampRéalisateur() {
	journal.info("Création d’un prédicat à partir d’une requête sur le"
		+ " réalisateur.");
	final RequêteSurChamp<Réalisateur> requêteSurChamp =
		new RequêteSurChamp<Réalisateur>(Champ.RÉALISATEUR,
			new MotClé("besson"));
	final List<Fiche> résultats = exécuter(requêteSurChamp);
	assertEquals(1, résultats.size());
	assertEquals(3, résultats.get(0).getId().longValue());
    }

    /**
     * Test de la méthode {@link RequêteSurChamp#créerPrédicat(CriteriaBuilder,
     * CriteriaQuery, Root)} sur le champ {@link Champ#ACTEUR ACTEUR}.
     */
    @Test
    public void testRequêteSurChampActeur() {
	journal.info("Création d’un prédicat à partir d’une requête sur les"
		+ " acteurs");
	final RequêteSurChamp<Acteur> requêteSurChamp =
		new RequêteSurChamp<Acteur>(Champ.ACTEUR,
			new MotClé("scarlett"));
	final List<Fiche> résultats = exécuter(requêteSurChamp);
	assertEquals(1, résultats.size());
	assertEquals(3, résultats.get(0).getId().longValue());
    }

    /**
     * Test de la méthode {@link RequêteSurChamp#créerPrédicat(CriteriaBuilder,
     * CriteriaQuery, Root)} sur le champ {@link Champ#COMPOSITEUR COMPOSITEUR}.
     */
    @Test
    public void testRequêteSurChampCompositeur() {
	journal.info("Création d’un prédicat à partir d’une requête sur le"
		+ " compositeur.");
	final RequêteSurChamp<Compositeur> requêteSurChamp =
		new RequêteSurChamp<Compositeur>(Champ.COMPOSITEUR,
			new MotClé("howard"));
	final List<Fiche> résultats = exécuter(requêteSurChamp);
	assertEquals(1, résultats.size());
	assertEquals(1, résultats.get(0).getId().longValue());
    }

    /**
     * Test de la méthode {@link RequêteSurChamp#créerPrédicat(CriteriaBuilder,
     * CriteriaQuery, Root)} sur le champ {@link Champ#DESSINATEUR DESSINATEUR}.
     */
    @Test
    public void testRequêteSurChampDessinateur() {
	journal.info("Création d’un prédicat à partir d’une requête sur le"
		+ " dessinateur.");
	final RequêteSurChamp<Dessinateur> requêteSurChamp =
		new RequêteSurChamp<Dessinateur>(Champ.DESSINATEUR,
			new MotClé("morris"));
	final List<Fiche> résultats = exécuter(requêteSurChamp);
	assertEquals(3, résultats.size());
	assertEquals(6, résultats.get(0).getId().longValue());
	assertEquals(7, résultats.get(1).getId().longValue());
	assertEquals(8, résultats.get(2).getId().longValue());
    }

    /**
     * Test de la méthode {@link RequêteSurChamp#créerPrédicat(CriteriaBuilder,
     * CriteriaQuery, Root)} sur le champ {@link Champ#SCÉNARISTE SCÉNARISTE}.
     */
    @Test
    public void testRequêteSurChampScénariste() {
	journal.info("Création d’un prédicat à partir d’une requête sur le"
		+ " scénariste");
	final RequêteSurChamp<Scénariste> requêteSurChamp =
		new RequêteSurChamp<Scénariste>(Champ.SCÉNARISTE,
			new MotClé("renard"));
	final List<Fiche> résultats = exécuter(requêteSurChamp);
	assertEquals(1, résultats.size());
	assertEquals(0, résultats.get(0).getId().longValue());
    }

    /**
     * Test de la méthode {@link RequêteSurChamp#créerPrédicat(CriteriaBuilder,
     * CriteriaQuery, Root)} sur le champ {@link Champ#AUTEUR AUTEUR}.
     */
    @Test
    public void testRequêteSurChampAuteur() {
	journal.info("Création d’un prédicat à partir d’une requête sur"
		+ " l’auteur");
	final RequêteSurChamp<Auteur> requêteSurChamp =
		new RequêteSurChamp<Auteur>(Champ.AUTEUR,
			new MotClé("thorpe"));
	final List<Fiche> résultats = exécuter(requêteSurChamp);
	assertEquals(1, résultats.size());
	assertEquals(5, résultats.get(0).getId().longValue());
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
	final List<Fiche> résultats = exécuter(requêteEntreParenthèse);
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
			new MotClé("etienne")
		)
	);
	requête.where(requêteUtilisateur.créerPrédicat(constructeur, requête,
		fiche));
	final TypedQuery<Fiche> requêteJpa = em.createQuery(requête);
	final List<Fiche> résultats = requêteJpa.getResultList();
	assertEquals(4, résultats.size());
	assertEquals(1, résultats.get(0).getId().longValue());
	assertEquals(2, résultats.get(1).getId().longValue());
	assertEquals(4, résultats.get(2).getId().longValue());
	assertEquals(5, résultats.get(3).getId().longValue());
    }

    /**
     * Éxécute une recherche basée sur l’expression donnée.
     *
     * @param expression
     *            expression servant à créer la clause where de la recherche à
     *            faire.
     * @return la liste des fiches pour lesquelles l’expression données est
     *         vraie.
     */
    private List<Fiche> exécuter(final Expression expression) {
	requête.where(expression.créerPrédicat(constructeur, requête, fiche));
	return em.createQuery(requête).getResultList();
    }

}
