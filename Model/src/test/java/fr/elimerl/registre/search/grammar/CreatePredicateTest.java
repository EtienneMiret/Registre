package fr.elimerl.registre.search.grammar;

import fr.elimerl.registre.entities.Record;
import fr.elimerl.registre.search.tokens.Field;
import fr.elimerl.registre.search.tokens.Keyword;
import fr.elimerl.registre.search.tokens.Type;
import fr.elimerl.registre.services.Index;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * This JUnit test tests the various implementations of
 * {@link Expression#createPredicate(CriteriaBuilder, CriteriaQuery, Root)} as
 * well as
 * {@link SearchQuery#createPredicate(CriteriaBuilder, CriteriaQuery, Root)}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext.xml")
@Transactional("transactionManager")
public class CreatePredicateTest {

  /** SLF4J logger for this class. */
  private static final Logger logger =
      LoggerFactory.getLogger (CreatePredicateTest.class);

  /** Spring provided entity manager. */
  @PersistenceContext(unitName = "Registre")
  private EntityManager em;

  /** Spring provided indexation service. */
  @Resource(name = "index")
  private Index index;

  /** Query builder linked to {@link #em}. */
  private CriteriaBuilder builder;

  /** Main query. We test the building of its where clause. */
  private CriteriaQuery<Record> query;

  /** Root of the main query. */
  private Root<Record> root;

  /**
   * Setup the test environment.
   */
  @Before
  public void setUp () {
    builder = em.getCriteriaBuilder ();
    query = builder.createQuery (Record.class);
    root = query.from (Record.class);
    query.select (root);
    query.orderBy (builder.asc (root.get ("id")));

    /* Indexing all records. */
    final TypedQuery<Record> jpaQuery = em.createQuery (query);
    for (final Record fiche : jpaQuery.getResultList ()) {
      index.reindex (fiche);
    }
  }

  /**
   * Testing {@link SimpleKeyword#createPredicate(CriteriaBuilder,
   * CriteriaQuery, Root)}.
   */
  @Test
  public void simpleKeyword () {
    logger.info ("Creating a predicate from a simple keyword.");
    final SimpleKeyword keyword = new SimpleKeyword (new Keyword ("super"));
    final List<Record> results = execute (keyword);
    check (results, 1);
  }

  /**
   * Testing {@link FieldQuery#createPredicate(CriteriaBuilder, CriteriaQuery,
   * Root)} on the {@link Field#TITLE TITLE} field.
   */
  @Test
  public void fieldQueryOnTitle () {
    logger.info ("Creating a predicate from a field query on title.");
    final FieldQuery fieldQuery =
        new FieldQuery (Field.TITLE, new Keyword ("la"));
    final List<Record> results = execute (fieldQuery);
    check (results, 4, 5, 7);
  }

  /**
   * Testing {@link FieldQuery#createPredicate(CriteriaBuilder, CriteriaQuery,
   * Root)} on the {@link Field#COMMENT COMMENT} field.
   */
  @Test
  public void fieldQueryOnComment () {
    logger.info ("Creating a predicate from a field query on comment.");
    final FieldQuery fieldQuery =
        new FieldQuery (Field.COMMENT, new Keyword ("super"));
    final List<Record> results = execute (fieldQuery);
    check (results, 1);
  }

  /**
   * Testing {@link FieldQuery#createPredicate(CriteriaBuilder, CriteriaQuery,
   * Root)} on the {@link Field#SERIES SERIES} field.
   */
  @Test
  public void fieldQueryOnSeries () {
    logger.info ("Creating a predicate from a field query on series.");
    final FieldQuery fieldQuery =
        new FieldQuery (Field.SERIES, new Keyword ("warhammer"));
    final List<Record> results = execute (fieldQuery);
    check (results, 4, 5);
  }

  /**
   * Testing {@link FieldQuery#createPredicate(CriteriaBuilder, CriteriaQuery,
   * Root)} on the {@link Field#OWNER OWNER} field.
   */
  @Test
  public void fieldQueryOnOwner () {
    logger.info ("Creating a predicatee from a field query on owner.");
    final FieldQuery fieldQuery =
        new FieldQuery (Field.OWNER, new Keyword ("etienne"));
    final List<Record> results = execute (fieldQuery);
    check (results, 1, 2, 4, 5);
  }

  /**
   * Testing {@link FieldQuery#createPredicate(CriteriaBuilder, CriteriaQuery,
   * Root)} on the {@link Field#LOCATION LOCATION} field.
   */
  @Test
  public void fieldQueryOnLocation () {
    logger.info ("Creating a predicate from a field query on location.");
    final FieldQuery fieldQuery =
        new FieldQuery (Field.LOCATION, new Keyword ("verneuil"));
    final List<Record> results = execute (fieldQuery);
    check (results, 0, 1, 2);
  }

  /**
   * Testing {@link FieldQuery#createPredicate(CriteriaBuilder, CriteriaQuery,
   * Root)} on the {@link Field#DIRECTOR DIRECTOR} field.
   */
  @Test
  public void fieldQueryOnDirector () {
    logger.info ("Creating a predicate from a field query on director.");
    final FieldQuery fieldQuery =
        new FieldQuery (Field.DIRECTOR, new Keyword ("besson"));
    final List<Record> results = execute (fieldQuery);
    check (results, 3);
  }

  /**
   * Testing {@link FieldQuery#createPredicate(CriteriaBuilder, CriteriaQuery,
   * Root)} on the {@link Field#ACTOR ACTOR} field.
   */
  @Test
  public void fieldQueryOnActor () {
    logger.info ("Creating a predicate from a field query on actor.");
    final FieldQuery fieldQuery =
        new FieldQuery (Field.ACTOR, new Keyword ("scarlett"));
    final List<Record> results = execute (fieldQuery);
    check (results, 1, 3);
  }

  /**
   * Testing {@link FieldQuery#createPredicate(CriteriaBuilder, CriteriaQuery,
   * Root)} on the {@link Field#COMPOSER COMPOSER} field.
   */
  @Test
  public void fieldQueryOnComposer () {
    logger.info ("Creating a predicate from a field query on composer.");
    final FieldQuery fieldQuery =
        new FieldQuery (Field.COMPOSER, new Keyword ("howard"));
    final List<Record> results = execute (fieldQuery);
    check (results, 1);
  }

  /**
   * Testing {@link FieldQuery#createPredicate(CriteriaBuilder, CriteriaQuery,
   * Root)} on the {@link Field#CARTOONIST CARTOONIST} field.
   */
  @Test
  public void fieldQueryOnCartoonist () {
    logger.info ("Creating a predicate from a field query on cartoonist.");
    final FieldQuery fieldQuery =
        new FieldQuery (Field.CARTOONIST, new Keyword ("morris"));
    final List<Record> results = execute (fieldQuery);
    check (results, 6, 7, 8);
  }

  /**
   * Testing {@link FieldQuery#createPredicate(CriteriaBuilder, CriteriaQuery,
   * Root)} on the {@link Field#SCRIPT_WRITER SCRIPT_WRITER} field.
   */
  @Test
  public void fieldQueryOnScriptWriter () {
    logger.info ("Creating a predicate from a field query on script writer.");
    final FieldQuery fieldQuery =
        new FieldQuery (Field.SCRIPT_WRITER, new Keyword ("renard"));
    final List<Record> results = execute (fieldQuery);
    check (results, 0);
  }

  /**
   * Testing {@link FieldQuery#createPredicate(CriteriaBuilder, CriteriaQuery,
   * Root)} on the {@link Field#AUTHOR AUTHOR} field.
   */
  @Test
  public void fieldQueryOnAuthor () {
    logger.info ("Creating a predicate from a field query on author.");
    final FieldQuery fieldQuery =
        new FieldQuery (Field.AUTHOR, new Keyword ("thorpe"));
    final List<Record> results = execute (fieldQuery);
    check (results, 4);
  }

  /**
   * Testing {@link BracketedQuery#createPredicate(CriteriaBuilder,
   * CriteriaQuery, Root)}.
   */
  @Test
  public void bracketedQuery () {
    logger.info ("Creating a predicate from a bracketed query.");
    final BracketedQuery bracketedQuery =
        new BracketedQuery (
            new SearchQuery (true,
                new SimpleKeyword (new Keyword ("super"))
            )
        );
    final List<Record> results = execute (bracketedQuery);
    check (results, 1);
  }

  /**
   * Testing {@link SearchQuery#createPredicate(CriteriaBuilder,
   * CriteriaQuery, Root)}.
   */
  @Test
  public void searchQuery () {
    logger.info ("Creating a predicate from a complex query.");
    final SearchQuery userQuery = new SearchQuery (false,
        new SimpleKeyword (new Keyword ("coucou")),
        new FieldQuery (Field.TITLE,
            new Keyword ("honneur"),
            new Keyword ("dette")
        ),
        new FieldQuery (Field.OWNER,
            new Keyword ("etienne")
        )
    );
    query.where (userQuery.createPredicate (builder, query, root));
    final TypedQuery<Record> jpaQuery = em.createQuery (query);
    final List<Record> results = jpaQuery.getResultList ();
    check (results, 1, 2, 4, 5);
  }

  /**
   * Test searching “luky” in any field.
   */
  @Test
  public void lukyEverywhere () {
    logger.info ("Creating a predicate to search “luky” in all fields.");
    final SimpleKeyword keyword = new SimpleKeyword (new Keyword ("luky"));
    final List<Record> results = execute (keyword);
    check (results, 6, 7, 8, 9);
  }

  /**
   * Test searching “luky” in titles.
   */
  @Test
  public void lukyInTitles () {
    logger.info ("Creating a predicate to search “luky” in titles.");
    final FieldQuery fieldQuery =
        new FieldQuery (Field.TITLE, new Keyword ("luky"));
    final List<Record> results = execute (fieldQuery);
    check (results, 7, 9);
  }

  /**
   * Test searching “luky” in series.
   */
  @Test
  public void lukyInSeries () {
    logger.info ("Creating a predicate to search “luky” in series.");
    final FieldQuery fieldQuery =
        new FieldQuery (Field.SERIES, new Keyword ("luky"));
    final List<Record> results = execute (fieldQuery);
    check (results, 6, 7, 8);
  }

  /**
   * Test searching for books.
   */
  @Test
  public void books () {
    logger.info ("Creating a predicate to search for books.");
    TypeQuery typeQuery = new TypeQuery (Type.BOOK);
    final List<Record> results = execute (typeQuery);
    check (results, 2, 4, 5);
  }

  /**
   * Test searching for books and movies.
   */
  @Test
  public void booksAndMovies () {
    logger.info ("Creating a predicate to search for books and movies.");
    TypeQuery books = new TypeQuery (Type.BOOK);
    TypeQuery movies = new TypeQuery (Type.MOVIE);
    SearchQuery searchQuery = new SearchQuery (false, books, movies);
    final List<Record> results = execute (new BracketedQuery (searchQuery));
    check (results, 1, 2, 3, 4, 5, 9);
  }

  /**
   * Execute a query based on the given expression.
   *
   * @param expression
   *     expression used to create the where clause of the query.
   * @return the list of records for which the given expression is true.
   */
  private List<Record> execute (final Expression expression) {
    query.where (expression.createPredicate (builder, query, root));
    return em.createQuery (query).getResultList ();
  }

  /**
   * Check that the given result list matches the expected result.
   *
   * @param results
   *     list of actual results.
   * @param expected
   *     expected ids, in the expected order.
   * @throws AssertionError
   *     if the actual results don’t match the expected
   *     ones.
   */
  private static void check (final List<Record> results,
                             final long... expected) {
    logger.debug ("Found: {}.", results);
    assertEquals (expected.length, results.size ());
    for (int i = 0; i < expected.length; i++) {
      assertEquals (expected[i], results.get (i).getId ().longValue ());
    }
  }

}
