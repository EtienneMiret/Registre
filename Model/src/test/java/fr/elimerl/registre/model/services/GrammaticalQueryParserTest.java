package fr.elimerl.registre.model.services;

import fr.elimerl.registre.search.grammar.BracketedQuery;
import fr.elimerl.registre.search.grammar.FieldQuery;
import fr.elimerl.registre.search.grammar.SearchQuery;
import fr.elimerl.registre.search.grammar.SimpleKeyword;
import fr.elimerl.registre.search.tokens.Bracket;
import fr.elimerl.registre.search.tokens.Field;
import fr.elimerl.registre.search.tokens.Keyword;
import fr.elimerl.registre.search.tokens.Operator;
import fr.elimerl.registre.search.tokens.Token;
import fr.elimerl.registre.services.QueryParser;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.Queue;

import static org.junit.Assert.assertEquals;

/**
 * JUnit test case for the {@link QueryParser#analyze(Queue)}
 * method.
 */
public class GrammaticalQueryParserTest {

  /** SLF4J logger for this class. */
  private static final Logger logger =
      LoggerFactory.getLogger (GrammaticalQueryParserTest.class);

  /** The parser under test. */
  private QueryParser parser;

  /** The token sequence to parse. */
  private Queue<Token> tokens;

  /**
   * Setup the test environment.
   */
  @Before
  public void setUp () {
    parser = new QueryParser ();
    tokens = new LinkedList<Token> ();
  }

  /**
   * Check that an empty query can be parsed.
   */
  @Test
  public void emptyQuery () {
    logger.info ("Grammatical analysis of an empty query.");

    final SearchQuery actual = parser.analyze (tokens);

    final SearchQuery expected = new SearchQuery (true);
    assertEquals (expected, actual);
  }

  /**
   * Analysing a query made of a single keyword. The test must pass no matter
   * whether the query is a conjunction or a disjunction.
   */
  @Test
  public void singleKeyword () {
    final Keyword keyword = new Keyword ("coucou");
    tokens.add (keyword);
    logger.info ("Grammatical analysis of the query: “{}”.", tokens);

    final SearchQuery actual = parser.analyze (tokens);

    final SearchQuery expectedConjunction =
        new SearchQuery (true, new SimpleKeyword (keyword));
    final SearchQuery expectedDisjunction =
        new SearchQuery (false, new SimpleKeyword (keyword));
    assertEquals (expectedConjunction, actual);
    assertEquals (expectedDisjunction, actual);
  }

  /**
   * Analysing a query simply made of several keywords.
   */
  @Test
  public void severalKeywordsConjunction () {
    final Keyword toto = new Keyword ("toto");
    final Keyword tata = new Keyword ("tata");
    final Keyword titi = new Keyword ("titi");
    tokens.add (toto);
    tokens.add (tata);
    tokens.add (titi);
    logger.info ("Grammatical analysis of the query: “{}”.", tokens);

    final SearchQuery actual = parser.analyze (tokens);

    final SearchQuery expected = new SearchQuery (true,
        new SimpleKeyword (toto),
        new SimpleKeyword (tata),
        new SimpleKeyword (titi));
    assertEquals (expected, actual);
  }

  /**
   * Analysing a query made of several keywords joined by the “or” operator.
   */
  @Test
  public void severalKeywordsDisjunction () {
    final Keyword toto = new Keyword ("toto");
    final Keyword tata = new Keyword ("tata");
    final Keyword titi = new Keyword ("titi");
    tokens.add (toto);
    tokens.add (Operator.OR);
    tokens.add (tata);
    tokens.add (Operator.OR);
    tokens.add (titi);
    logger.info ("Grammatical analysis of the query: “{}”.", tokens);

    final SearchQuery actual = parser.analyze (tokens);

    final SearchQuery expected = new SearchQuery (false,
        new SimpleKeyword (toto),
        new SimpleKeyword (tata),
        new SimpleKeyword (titi));
    assertEquals (expected, actual);
  }

  /**
   * Analysing a complex query.
   */
  @Test
  public void complexQuery1 () {
    final Keyword maman = new Keyword ("maman");
    final Keyword etienne = new Keyword ("etienne");
    final Keyword gregoire = new Keyword ("grégoire");
    final Keyword toto = new Keyword ("toto");
    final Keyword tata = new Keyword ("tata");
    final Keyword titi = new Keyword ("titi");
    tokens.add (Field.TITLE);
    tokens.add (maman);
    tokens.add (Operator.OR);
    tokens.add (Field.COMMENT);
    tokens.add (Bracket.OPENING);
    tokens.add (etienne);
    tokens.add (gregoire);
    tokens.add (Bracket.CLOSING);
    tokens.add (Operator.OR);
    tokens.add (Bracket.OPENING);
    tokens.add (toto);
    tokens.add (tata);
    tokens.add (titi);
    tokens.add (Bracket.CLOSING);
    logger.info ("Grammatical analysis of the query: “{}”.", tokens);

    final SearchQuery actual = parser.analyze (tokens);

    final SearchQuery expected = new SearchQuery (false,
        new FieldQuery (Field.TITLE, maman),
        new FieldQuery (Field.COMMENT,
            etienne, gregoire),
        new BracketedQuery (
            new SearchQuery (true,
                new SimpleKeyword (toto),
                new SimpleKeyword (tata),
                new SimpleKeyword (titi)
            )
        )
    );
    assertEquals (expected, actual);
  }

  /**
   * Analysing a complex query.
   */
  @Test
  public void complexQuery2 () {
    final Keyword a = new Keyword ("a");
    final Keyword b = new Keyword ("b");
    final Keyword c = new Keyword ("c");
    final Keyword d = new Keyword ("d");
    final Keyword e = new Keyword ("e");
    final Keyword f = new Keyword ("f");
    final Keyword g = new Keyword ("g");
    final Keyword h = new Keyword ("h");
    final Keyword i = new Keyword ("i");
    tokens.add (Bracket.OPENING);
    tokens.add (a);
    tokens.add (Operator.OR);
    tokens.add (b);
    tokens.add (Operator.OR);
    tokens.add (c);
    tokens.add (Bracket.CLOSING);
    tokens.add (Bracket.OPENING);
    tokens.add (d);
    tokens.add (Operator.OR);
    tokens.add (e);
    tokens.add (Operator.OR);
    tokens.add (f);
    tokens.add (Bracket.CLOSING);
    tokens.add (Bracket.OPENING);
    tokens.add (g);
    tokens.add (Operator.OR);
    tokens.add (h);
    tokens.add (Operator.OR);
    tokens.add (i);
    tokens.add (Bracket.CLOSING);
    logger.info ("Grammatical analysis of the query: “{}”.", tokens);

    final SearchQuery actual = parser.analyze (tokens);

    final SearchQuery expected = new SearchQuery (true,
        new BracketedQuery (new SearchQuery (false,
            new SimpleKeyword (a),
            new SimpleKeyword (b),
            new SimpleKeyword (c)
        )),
        new BracketedQuery (new SearchQuery (false,
            new SimpleKeyword (d),
            new SimpleKeyword (e),
            new SimpleKeyword (f)
        )),
        new BracketedQuery (new SearchQuery (false,
            new SimpleKeyword (g),
            new SimpleKeyword (h),
            new SimpleKeyword (i)
        ))
    );
    assertEquals (expected, actual);
  }

}
