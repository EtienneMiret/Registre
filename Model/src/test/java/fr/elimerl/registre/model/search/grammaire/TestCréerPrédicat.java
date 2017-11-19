package fr.elimerl.registre.model.search.grammaire;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fr.elimerl.registre.entities.Record;
import fr.elimerl.registre.search.grammar.*;
import fr.elimerl.registre.search.tokens.Field;
import fr.elimerl.registre.search.tokens.Keyword;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import fr.elimerl.registre.search.grammar.SimpleKeyword;
import fr.elimerl.registre.services.Index;

/**
 * Ce test JUnit teste les implémentations de la méthode
 * {@link Expression#createPredicate(CriteriaBuilder, CriteriaQuery, Root)} ainsi
 * que la méthode
 * {@link SearchQuery#createPredicate(CriteriaBuilder, CriteriaQuery, Root)}.
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
    private Index indexeur;

    /** Constructeur de requêtes lié à {@link #em}. */
    private CriteriaBuilder constructeur;

    /** Requête principale. On teste la construction de sa clause where. */
    private CriteriaQuery<Record> requête;

    /** La racine de la requête principale. */
    private Root<Record> fiche;

    /**
     * Prépare l’environnement pour les tests.
     */
    @Before
    public void setUp() {
	constructeur = em.getCriteriaBuilder();
	requête = constructeur.createQuery(Record.class);
	fiche = requête.from(Record.class);
	requête.select(fiche);
	requête.orderBy(constructeur.asc(fiche.get("id")));

	/* Indexation de toutes les fiches. */
	final TypedQuery<Record> requêteJpa = em.createQuery(requête);
	for (final Record fiche : requêteJpa.getResultList()) {
	    indexeur.reindex(fiche);
	}
    }

    /**
     * Test de la méthode
     * {@link SimpleKeyword#createPredicate(CriteriaBuilder, CriteriaQuery, Root)}.
     */
    @Test
    public void testMotCléSimple() {
	journal.info("Création d’un prédicat à partir d’un mot clé simple.");
	final SimpleKeyword motClé = new SimpleKeyword(new Keyword("super"));
	final List<Record> résultats = exécuter(motClé);
	vérifier(résultats, 1);
    }

    /**
     * Test de la méthode {@link FieldQuery#createPredicate(CriteriaBuilder,
     * CriteriaQuery, Root)} sur le champ {@link Field#TITLE TITLE}.
     */
    @Test
    public void testRequêteSurChampTitre() {
	journal.info("Création d’un prédicat à partir d’une requête sur le"
		+ " titre.");
	final FieldQuery requêteSurChamp =
		new FieldQuery(Field.TITLE, new Keyword("la"));
	final List<Record> résultats = exécuter(requêteSurChamp);
	vérifier(résultats, 4, 5, 7);
    }

    /**
     * Test de la méthode {@link FieldQuery#createPredicate(CriteriaBuilder,
     * CriteriaQuery, Root)} sur le champ {@link Field#COMMENT COMMENT}.
     */
    @Test
    public void testRequêteSurChampCommentaire() {
	journal.info("Création d’un prédicat à partir d’une requête sur le"
		+ " commentaire.");
	final FieldQuery requêteSurChamp =
		new FieldQuery(Field.COMMENT, new Keyword("super"));
	final List<Record> résultats = exécuter(requêteSurChamp);
	vérifier(résultats, 1);
    }

    /**
     * Test de la méthode {@link FieldQuery#createPredicate(CriteriaBuilder,
     * CriteriaQuery, Root)} sur le champ {@link Field#SERIES SERIES}.
     */
    @Test
    public void testRequêteSurChampSérie() {
	journal.info("Création d’un prédicat à partir d’une requête sur la"
		+ " série.");
	final FieldQuery requêteSurChamp =
		new FieldQuery(Field.SERIES, new Keyword("warhammer"));
	final List<Record> résultats = exécuter(requêteSurChamp);
	vérifier(résultats, 4, 5);
    }

    /**
     * Test de la méthode {@link FieldQuery#createPredicate(CriteriaBuilder,
     * CriteriaQuery, Root)} sur le champ {@link Field#OWNER OWNER}.
     */
    @Test
    public void testRequêteSurChampPropriétaire() {
	journal.info("Création d’un prédicat à partir d’une requête sur le"
		+ " propriétaire.");
	final FieldQuery requêteSurChamp =
		new FieldQuery(Field.OWNER, new Keyword("etienne"));
	final List<Record> résultats = exécuter(requêteSurChamp);
	vérifier(résultats, 1, 2, 4, 5);
    }

    /**
     * Test de la méthode {@link FieldQuery#createPredicate(CriteriaBuilder,
     * CriteriaQuery, Root)} sur le champ {@link Field#LOCATION LOCATION}.
     */
    @Test
    public void testRequêteSurChampEmplacement() {
	journal.info("Création d’un prédicat à partir d’une requête sur"
		+ " l’emplacement.");
	final FieldQuery requêteSurChamp =
		new FieldQuery(Field.LOCATION, new Keyword("verneuil"));
	final List<Record> résultats = exécuter(requêteSurChamp);
	vérifier(résultats, 0, 1, 2);
    }

    /**
     * Test de la méthode {@link FieldQuery#createPredicate(CriteriaBuilder,
     * CriteriaQuery, Root)} sur le champ {@link Field#DIRECTOR DIRECTOR}.
     */
    @Test
    public void testRequêteSurChampRéalisateur() {
	journal.info("Création d’un prédicat à partir d’une requête sur le"
		+ " réalisateur.");
	final FieldQuery requêteSurChamp =
		new FieldQuery(Field.DIRECTOR, new Keyword("besson"));
	final List<Record> résultats = exécuter(requêteSurChamp);
	vérifier(résultats, 3);
    }

    /**
     * Test de la méthode {@link FieldQuery#createPredicate(CriteriaBuilder,
     * CriteriaQuery, Root)} sur le champ {@link Field#ACTOR ACTOR}.
     */
    @Test
    public void testRequêteSurChampActeur() {
	journal.info("Création d’un prédicat à partir d’une requête sur les"
		+ " acteurs");
	final FieldQuery requêteSurChamp =
		new FieldQuery(Field.ACTOR, new Keyword("scarlett"));
	final List<Record> résultats = exécuter(requêteSurChamp);
	vérifier(résultats, 3);
    }

    /**
     * Test de la méthode {@link FieldQuery#createPredicate(CriteriaBuilder,
     * CriteriaQuery, Root)} sur le champ {@link Field#COMPOSER COMPOSER}.
     */
    @Test
    public void testRequêteSurChampCompositeur() {
	journal.info("Création d’un prédicat à partir d’une requête sur le"
		+ " compositeur.");
	final FieldQuery requêteSurChamp =
		new FieldQuery(Field.COMPOSER, new Keyword("howard"));
	final List<Record> résultats = exécuter(requêteSurChamp);
	vérifier(résultats, 1);
    }

    /**
     * Test de la méthode {@link FieldQuery#createPredicate(CriteriaBuilder,
     * CriteriaQuery, Root)} sur le champ {@link Field#CARTOONIST CARTOONIST}.
     */
    @Test
    public void testRequêteSurChampDessinateur() {
	journal.info("Création d’un prédicat à partir d’une requête sur le"
		+ " dessinateur.");
	final FieldQuery requêteSurChamp =
		new FieldQuery(Field.CARTOONIST, new Keyword("morris"));
	final List<Record> résultats = exécuter(requêteSurChamp);
	vérifier(résultats, 6, 7, 8);
    }

    /**
     * Test de la méthode {@link FieldQuery#createPredicate(CriteriaBuilder,
     * CriteriaQuery, Root)} sur le champ {@link Field#SCRIPT_WRITER SCRIPT_WRITER}.
     */
    @Test
    public void testRequêteSurChampScénariste() {
	journal.info("Création d’un prédicat à partir d’une requête sur le"
		+ " scénariste");
	final FieldQuery requêteSurChamp =
		new FieldQuery(Field.SCRIPT_WRITER, new Keyword("renard"));
	final List<Record> résultats = exécuter(requêteSurChamp);
	vérifier(résultats, 0);
    }

    /**
     * Test de la méthode {@link FieldQuery#createPredicate(CriteriaBuilder,
     * CriteriaQuery, Root)} sur le champ {@link Field#AUTHOR AUTHOR}.
     */
    @Test
    public void testRequêteSurChampAuteur() {
	journal.info("Création d’un prédicat à partir d’une requête sur"
		+ " l’auteur");
	final FieldQuery requêteSurChamp =
		new FieldQuery(Field.AUTHOR, new Keyword("thorpe"));
	final List<Record> résultats = exécuter(requêteSurChamp);
	vérifier(résultats, 4);
    }

    /**
     * Test de la méthode {@link BracketedQuery#createPredicate(
     * CriteriaBuilder, CriteriaQuery, Root)}.
     */
    @Test
    public void testRequêteEntreParenthèse() {
	journal.info("Création d’un prédicat à partir d’une requête entre "
		+ "parenthèses.");
	final BracketedQuery requêteEntreParenthèse =
		new BracketedQuery(
			new SearchQuery(true,
				new SimpleKeyword(new Keyword("super"))
			)
		);
	final List<Record> résultats = exécuter(requêteEntreParenthèse);
	vérifier(résultats, 1);
    }

    /**
     * Test de la méthode
     * {@link SearchQuery#createPredicate(CriteriaBuilder, CriteriaQuery, Root)}.
     */
    @Test
    public void testRequête() {
	journal.info("Création d’un prédicat à partir d’une requête complexe.");
	final SearchQuery requêteUtilisateur = new SearchQuery(false,
		new SimpleKeyword(new Keyword("coucou")),
		new FieldQuery(Field.TITLE,
			new Keyword("honneur"),
			new Keyword("dette")
		),
		new FieldQuery(Field.OWNER,
			new Keyword("etienne")
		)
	);
	requête.where(requêteUtilisateur.createPredicate(constructeur, requête,
		fiche));
	final TypedQuery<Record> requêteJpa = em.createQuery(requête);
	final List<Record> résultats = requêteJpa.getResultList();
	vérifier(résultats, 1, 2, 4, 5);
    }

    /**
     * Teste la recherche de « luky » dans tous les champs.
     */
    @Test
    public void testLukyPartout() {
	journal.info("Création d’un prédicat pour la recherche de « luky »"
		+ " dans tous les champs.");
	final SimpleKeyword motClé = new SimpleKeyword(new Keyword("luky"));
	final List<Record> résultats = exécuter(motClé);
	vérifier(résultats, 6, 7, 8, 9);
    }

    /**
     * Teste la recherche de « luky » dans les titres.
     */
    @Test
    public void testLukyTitre() {
	journal.info("Création d’un prédicat pour la recherche de « luky »"
		+ " dans les titres.");
	final FieldQuery requêteSurChamp =
		new FieldQuery(Field.TITLE, new Keyword("luky"));
	final List<Record> résultats = exécuter(requêteSurChamp);
	vérifier(résultats, 7, 9);
    }

    /**
     * Teste la recherche de « luky » dans les séries.
     */
    @Test
    public void testLukySérie() {
	journal.info("Création d’un prédicat pour la recherche de « luky »"
		+ " dans les séries");
	final FieldQuery requêteSurChamp =
		new FieldQuery(Field.SERIES, new Keyword("luky"));
	final List<Record> résultats = exécuter(requêteSurChamp);
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
    private List<Record> exécuter(final Expression expression) {
	requête.where(expression.createPredicate(constructeur, requête, fiche));
	return em.createQuery(requête).getResultList();
    }

    /**
     * Vérifie que la liste de résultats trouvés correspond bien à ce qui est
     * attendu.
     * @param résultats liste de résultats trouvés.
     * @param attendus liste des id attendues, dans l’ordre.
     * @throws AssertionError si les résultats ne sont pas ceux attendus.
     */
    private static void vérifier(final List<Record> résultats,
	    final long... attendus) {
	journal.debug("Trouvé : {}.", résultats);
	assertEquals(attendus.length, résultats.size());
	for (int i = 0; i < attendus.length; i++) {
	    assertEquals(attendus[i], résultats.get(i).getId().longValue());
	}
    }

}
