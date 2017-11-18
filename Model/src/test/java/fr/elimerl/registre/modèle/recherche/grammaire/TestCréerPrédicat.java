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

import fr.elimerl.registre.entities.Fiche;
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
	vérifier(résultats, 1);
    }

    /**
     * Test de la méthode {@link RequêteSurChamp#créerPrédicat(CriteriaBuilder,
     * CriteriaQuery, Root)} sur le champ {@link Champ#TITRE TITRE}.
     */
    @Test
    public void testRequêteSurChampTitre() {
	journal.info("Création d’un prédicat à partir d’une requête sur le"
		+ " titre.");
	final RequêteSurChamp requêteSurChamp =
		new RequêteSurChamp(Champ.TITRE, new MotClé("la"));
	final List<Fiche> résultats = exécuter(requêteSurChamp);
	vérifier(résultats, 4, 5, 7);
    }

    /**
     * Test de la méthode {@link RequêteSurChamp#créerPrédicat(CriteriaBuilder,
     * CriteriaQuery, Root)} sur le champ {@link Champ#COMMENTAIRE COMMENTAIRE}.
     */
    @Test
    public void testRequêteSurChampCommentaire() {
	journal.info("Création d’un prédicat à partir d’une requête sur le"
		+ " commentaire.");
	final RequêteSurChamp requêteSurChamp =
		new RequêteSurChamp(Champ.COMMENTAIRE, new MotClé("super"));
	final List<Fiche> résultats = exécuter(requêteSurChamp);
	vérifier(résultats, 1);
    }

    /**
     * Test de la méthode {@link RequêteSurChamp#créerPrédicat(CriteriaBuilder,
     * CriteriaQuery, Root)} sur le champ {@link Champ#SÉRIE SÉRIE}.
     */
    @Test
    public void testRequêteSurChampSérie() {
	journal.info("Création d’un prédicat à partir d’une requête sur la"
		+ " série.");
	final RequêteSurChamp requêteSurChamp =
		new RequêteSurChamp(Champ.SÉRIE, new MotClé("warhammer"));
	final List<Fiche> résultats = exécuter(requêteSurChamp);
	vérifier(résultats, 4, 5);
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
	final RequêteSurChamp requêteSurChamp =
		new RequêteSurChamp(Champ.PROPRIÉTAIRE, new MotClé("etienne"));
	final List<Fiche> résultats = exécuter(requêteSurChamp);
	vérifier(résultats, 1, 2, 4, 5);
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
	final RequêteSurChamp requêteSurChamp =
		new RequêteSurChamp(Champ.EMPLACEMENT, new MotClé("verneuil"));
	final List<Fiche> résultats = exécuter(requêteSurChamp);
	vérifier(résultats, 0, 1, 2);
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
	final RequêteSurChamp requêteSurChamp =
		new RequêteSurChamp(Champ.RÉALISATEUR, new MotClé("besson"));
	final List<Fiche> résultats = exécuter(requêteSurChamp);
	vérifier(résultats, 3);
    }

    /**
     * Test de la méthode {@link RequêteSurChamp#créerPrédicat(CriteriaBuilder,
     * CriteriaQuery, Root)} sur le champ {@link Champ#ACTEUR ACTEUR}.
     */
    @Test
    public void testRequêteSurChampActeur() {
	journal.info("Création d’un prédicat à partir d’une requête sur les"
		+ " acteurs");
	final RequêteSurChamp requêteSurChamp =
		new RequêteSurChamp(Champ.ACTEUR, new MotClé("scarlett"));
	final List<Fiche> résultats = exécuter(requêteSurChamp);
	vérifier(résultats, 3);
    }

    /**
     * Test de la méthode {@link RequêteSurChamp#créerPrédicat(CriteriaBuilder,
     * CriteriaQuery, Root)} sur le champ {@link Champ#COMPOSITEUR COMPOSITEUR}.
     */
    @Test
    public void testRequêteSurChampCompositeur() {
	journal.info("Création d’un prédicat à partir d’une requête sur le"
		+ " compositeur.");
	final RequêteSurChamp requêteSurChamp =
		new RequêteSurChamp(Champ.COMPOSITEUR, new MotClé("howard"));
	final List<Fiche> résultats = exécuter(requêteSurChamp);
	vérifier(résultats, 1);
    }

    /**
     * Test de la méthode {@link RequêteSurChamp#créerPrédicat(CriteriaBuilder,
     * CriteriaQuery, Root)} sur le champ {@link Champ#DESSINATEUR DESSINATEUR}.
     */
    @Test
    public void testRequêteSurChampDessinateur() {
	journal.info("Création d’un prédicat à partir d’une requête sur le"
		+ " dessinateur.");
	final RequêteSurChamp requêteSurChamp =
		new RequêteSurChamp(Champ.DESSINATEUR, new MotClé("morris"));
	final List<Fiche> résultats = exécuter(requêteSurChamp);
	vérifier(résultats, 6, 7, 8);
    }

    /**
     * Test de la méthode {@link RequêteSurChamp#créerPrédicat(CriteriaBuilder,
     * CriteriaQuery, Root)} sur le champ {@link Champ#SCÉNARISTE SCÉNARISTE}.
     */
    @Test
    public void testRequêteSurChampScénariste() {
	journal.info("Création d’un prédicat à partir d’une requête sur le"
		+ " scénariste");
	final RequêteSurChamp requêteSurChamp =
		new RequêteSurChamp(Champ.SCÉNARISTE, new MotClé("renard"));
	final List<Fiche> résultats = exécuter(requêteSurChamp);
	vérifier(résultats, 0);
    }

    /**
     * Test de la méthode {@link RequêteSurChamp#créerPrédicat(CriteriaBuilder,
     * CriteriaQuery, Root)} sur le champ {@link Champ#AUTEUR AUTEUR}.
     */
    @Test
    public void testRequêteSurChampAuteur() {
	journal.info("Création d’un prédicat à partir d’une requête sur"
		+ " l’auteur");
	final RequêteSurChamp requêteSurChamp =
		new RequêteSurChamp(Champ.AUTEUR, new MotClé("thorpe"));
	final List<Fiche> résultats = exécuter(requêteSurChamp);
	vérifier(résultats, 4);
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
	vérifier(résultats, 1);
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
		new RequêteSurChamp(Champ.TITRE,
			new MotClé("honneur"),
			new MotClé("dette")
		),
		new RequêteSurChamp(Champ.PROPRIÉTAIRE,
			new MotClé("etienne")
		)
	);
	requête.where(requêteUtilisateur.créerPrédicat(constructeur, requête,
		fiche));
	final TypedQuery<Fiche> requêteJpa = em.createQuery(requête);
	final List<Fiche> résultats = requêteJpa.getResultList();
	vérifier(résultats, 1, 2, 4, 5);
    }

    /**
     * Teste la recherche de « luky » dans tous les champs.
     */
    @Test
    public void testLukyPartout() {
	journal.info("Création d’un prédicat pour la recherche de « luky »"
		+ " dans tous les champs.");
	final MotCléSimple motClé = new MotCléSimple(new MotClé("luky"));
	final List<Fiche> résultats = exécuter(motClé);
	vérifier(résultats, 6, 7, 8, 9);
    }

    /**
     * Teste la recherche de « luky » dans les titres.
     */
    @Test
    public void testLukyTitre() {
	journal.info("Création d’un prédicat pour la recherche de « luky »"
		+ " dans les titres.");
	final RequêteSurChamp requêteSurChamp =
		new RequêteSurChamp(Champ.TITRE, new MotClé("luky"));
	final List<Fiche> résultats = exécuter(requêteSurChamp);
	vérifier(résultats, 7, 9);
    }

    /**
     * Teste la recherche de « luky » dans les séries.
     */
    @Test
    public void testLukySérie() {
	journal.info("Création d’un prédicat pour la recherche de « luky »"
		+ " dans les séries");
	final RequêteSurChamp requêteSurChamp =
		new RequêteSurChamp(Champ.SÉRIE, new MotClé("luky"));
	final List<Fiche> résultats = exécuter(requêteSurChamp);
	vérifier(résultats, 6, 7, 8);
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

    /**
     * Vérifie que la liste de résultats trouvés correspond bien à ce qui est
     * attendu.
     * @param résultats liste de résultats trouvés.
     * @param attendus liste des id attendues, dans l’ordre.
     * @throws AssertionError si les résultats ne sont pas ceux attendus.
     */
    private static void vérifier(final List<Fiche> résultats,
	    final long... attendus) {
	journal.debug("Trouvé : {}.", résultats);
	assertEquals(attendus.length, résultats.size());
	for (int i = 0; i < attendus.length; i++) {
	    assertEquals(attendus[i], résultats.get(i).getId().longValue());
	}
    }

}
