package fr.elimerl.registre.model.services;

import fr.elimerl.registre.search.tokens.Bracket;
import fr.elimerl.registre.search.tokens.Field;
import fr.elimerl.registre.search.tokens.Keyword;
import fr.elimerl.registre.search.tokens.Operator;
import fr.elimerl.registre.search.tokens.Token;
import fr.elimerl.registre.services.QueryParser;
import org.junit.Before;
import org.junit.Test;

import java.util.Queue;

import static org.junit.Assert.assertEquals;

/**
 * JUnit test case for the {@link QueryParser#tokenize(String)} method.
 */
public class LexicalQueryParserTest {

  /** The parser under test. */
  private QueryParser parser;

  /**
   * Setup the test environment.
   */
  @Before
  public void setUp () {
    parser = new QueryParser ();
  }

  /**
   * Check brackets lexical analysis without spaces.
   */
  @Test
  public void simpleBrackets () {
    final Queue<Token> actual = parser.tokenize ("(()()(");
    assertEquals (6, actual.size ());
    assertEquals (Bracket.OPENING, actual.poll ());
    assertEquals (Bracket.OPENING, actual.poll ());
    assertEquals (Bracket.CLOSING, actual.poll ());
    assertEquals (Bracket.OPENING, actual.poll ());
    assertEquals (Bracket.CLOSING, actual.poll ());
    assertEquals (Bracket.OPENING, actual.poll ());
  }

  /**
   * Check brackets lexical analysis with spaces.
   */
  @Test
  public void bracketsWithSpaces () {
    final Queue<Token> actual = parser.tokenize (
        "  )(( )   (  ( ");
    assertEquals (6, actual.size ());
    assertEquals (Bracket.CLOSING, actual.poll ());
    assertEquals (Bracket.OPENING, actual.poll ());
    assertEquals (Bracket.OPENING, actual.poll ());
    assertEquals (Bracket.CLOSING, actual.poll ());
    assertEquals (Bracket.OPENING, actual.poll ());
    assertEquals (Bracket.OPENING, actual.poll ());
  }

  /**
   * Test the lexical analysis of keywords with many non-ASCII characters.
   */
  @Test
  public void unicode () {
    final Queue<Token> actual = parser.tokenize (
        "ÉœÙ CŒUR Çédille àgrÂvÆ ÑñUbï Ïléà");
    assertEquals (6, actual.size ());
    assertEquals (new Keyword ("éœù"), actual.poll ());
    assertEquals (new Keyword ("cœur"), actual.poll ());
    assertEquals (new Keyword ("çédille"), actual.poll ());
    assertEquals (new Keyword ("àgrâvæ"), actual.poll ());
    assertEquals (new Keyword ("ññubï"), actual.poll ());
    assertEquals (new Keyword ("ïléà"), actual.poll ());
  }

  /**
   * Test the lexical analysis of a string with a lot of punctuation.
   */
  @Test
  public void punctuation () {
    final Queue<Token> actual = parser.tokenize (
        "+ = or: / \t\n ( %ù §§ À \"bonjour\" ++--) **");
    assertEquals (6, actual.size ());
    assertEquals (Operator.OR, actual.poll ());
    assertEquals (Bracket.OPENING, actual.poll ());
    assertEquals (new Keyword ("ù"), actual.poll ());
    assertEquals (new Keyword ("à"), actual.poll ());
    assertEquals (new Keyword ("bonjour"), actual.poll ());
    assertEquals (Bracket.CLOSING, actual.poll ());
  }

  /**
   * Test the lexical analysis of a realistic query.
   */
  @Test
  public void realisticQuery () {
    final Queue<Token> actual = parser.tokenize (
        "title:(Bonjour Madame) OR coucou");
    assertEquals (7, actual.size ());
    assertEquals (Field.TITLE, actual.poll ());
    assertEquals (Bracket.OPENING, actual.poll ());
    assertEquals (new Keyword ("bonjour"), actual.poll ());
    assertEquals (new Keyword ("madame"), actual.poll ());
    assertEquals (Bracket.CLOSING, actual.poll ());
    assertEquals (Operator.OR, actual.poll ());
    assertEquals (new Keyword ("coucou"), actual.poll ());
  }

}
