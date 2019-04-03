package fr.elimerl.registre.services;

import fr.elimerl.registre.search.grammar.BracketedQuery;
import fr.elimerl.registre.search.grammar.Expression;
import fr.elimerl.registre.search.grammar.FieldQuery;
import fr.elimerl.registre.search.grammar.SearchQuery;
import fr.elimerl.registre.search.grammar.SimpleKeyword;
import fr.elimerl.registre.search.tokens.Bracket;
import fr.elimerl.registre.search.tokens.Field;
import fr.elimerl.registre.search.tokens.Keyword;
import fr.elimerl.registre.search.tokens.Operator;
import fr.elimerl.registre.search.tokens.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;

/**
 * Singleton service responsible for parsing users’ queries.
 */
public class QueryParser {

  /** SLF4J logger for this class. */
  private static final Logger logger =
      LoggerFactory.getLogger (QueryParser.class);

  /** A zero-sized {@link Expression}s array. */
  private static final Expression[] EXPRESSIONS = new Expression[0];

  /** A zero-sized {@link Keyword}s array. */
  private static final Keyword[] KEYWORDS = new Keyword[0];

  /**
   * Make the lexical analysis then the grammatical analysis of the given user
   * query. Returns something no matter what the query contains.
   *
   * @param query
   *     user supplied query. May contain anything.
   * @return the query resulting from analysis of the given text.
   */
  public SearchQuery parse (final String query) {
    return analyze (tokenize (query));
  }

  /**
   * Make the lexical analysis of the given user query.
   *
   * @param query
   *     user supplied query. May contain anything.
   * @return the queue of tokens present in the query.
   */
  public Queue<Token> tokenize (final String query) {
    final Queue<Token> result = new LinkedList<Token> ();
    int i = 0;
    while (i < query.length ()) {
      Token token = null;
      {
        final Matcher matcher =
            Bracket.OPENING.getPattern ().matcher (query);
        if (matcher.find (i)) {
          token = Bracket.OPENING;
          i = matcher.end ();
        }
      }
      if (token == null) {
        final Matcher matcher =
            Bracket.CLOSING.getPattern ().matcher (query);
        if (matcher.find (i)) {
          token = Bracket.CLOSING;
          i = matcher.end ();
        }
      }
      final Iterator<Operator> operators = Operator.all.iterator ();
      while (token == null && operators.hasNext ()) {
        final Operator operator = operators.next ();
        final Matcher matcher =
            operator.getPattern ().matcher (query);
        if (matcher.find (i)) {
          token = operator;
          i = matcher.end ();
        }
      }
      final Iterator<Field> fields = Field.all.iterator ();
      while (token == null && fields.hasNext ()) {
        final Field field = fields.next ();
        final Matcher matcher = field.getPattern ().matcher (query);
        if (matcher.find (i)) {
          token = field;
          i = matcher.end ();
        }
      }
      if (token == null) {
        final Matcher matcher = Keyword.PATTERN.matcher (query);
        if (matcher.find (i)) {
          token = new Keyword (matcher.group (1).toLowerCase ());
          i = matcher.end ();
        }
      }
      if (token == null) {
        i++;
      } else {
        result.add (token);
      }
    }
    return result;
  }

  /**
   * Build a query from the token resulting from the lexical analysis. Consume
   * the tokens from the queue that mean something, and stop as soon as it
   * isn’t possible to build a query anymore.
   *
   * @param tokens
   *     the queue of tokens obtained from the lexical analysis.
   * @return the query resulting from the grammatical analysis of the given
   * tokens.
   */
  public SearchQuery analyze (final Queue<Token> tokens) {
    final List<Expression> expressions = new ArrayList<Expression> ();
    boolean conjunction;
    if (!tokens.isEmpty () && tokens.peek () != Bracket.CLOSING) {
      try {
        expressions.add (analyzeExpression (tokens));
        conjunction = tokens.peek () != Operator.OR;
        if (!conjunction) {
          tokens.poll (); // Il faut consommer l’opérateur.
        }
      } catch (final ParseException e) {
        logger.warn ("Incorrect query grammar.", e);
        conjunction = true; // No matter.
        tokens.clear (); // Force analysis end.
      }
    } else {
      conjunction = true;
    }
    while (!tokens.isEmpty () && tokens.peek () != Bracket.CLOSING) {
      try {
        expressions.add (analyzeExpression (tokens));
        /*
         * If this is a disjunction and we’re not at the end, there must
         * be an “or” operator.
         */
        if (!conjunction
            && !tokens.isEmpty ()
            && tokens.peek () != Bracket.CLOSING) {
          final Token nextToken = tokens.poll ();
          if (nextToken != Operator.OR) {
            logger.warn ("Incorrect query grammar: “or” expected,"
                + " {} found.", nextToken);
            tokens.clear (); // Force analysis end.
          }
        }
      } catch (final ParseException e) {
        logger.warn ("Incorrect query grammar.", e);
        tokens.clear (); // Force analysis end.
      }
    }
    return new SearchQuery (conjunction, expressions.toArray (EXPRESSIONS));
  }

  /**
   * Read the given token sequence and try to build an expression out of the
   * first ones. The used tokens are consumed.
   *
   * @param tokens
   *     token sequence to analyze.
   * @return the expression present at the beginning of the given token
   * sequence.
   * @throws ParseException
   *     if the beginning of the given token sequence cannot represent
   *     an expression.
   */
  private Expression analyzeExpression (final Queue<Token> tokens)
      throws ParseException {
    final Expression result;
    final Token firstToken = tokens.poll ();
    if (firstToken == Bracket.OPENING) {
      final SearchQuery subQuery = analyze (tokens);
      result = new BracketedQuery (subQuery);
      final Token nextToken = tokens.poll ();
      if (nextToken != Bracket.CLOSING) {
        throw new ParseException (
            "“)” expected, “" + nextToken + "” found.",
            -1
        );
      }
    } else if (firstToken instanceof Keyword) {
      result = new SimpleKeyword ((Keyword) firstToken);
    } else if (firstToken instanceof Field) {
      result = analyzeField ((Field) firstToken, tokens);
    } else {
      throw new ParseException ("Expression beginning expected, “"
          + firstToken + "” found.", -1);
    }
    return result;
  }

  /**
   * Reads the given token sequence and try to read a field query. In case of
   * success, the used tokens are consumed.
   *
   * @param field
   *     the field we might be searching.
   * @param tokens
   *     the token sequence to analyze. Must start with a keyword or
   *     a bracketed keywords sequence.
   * @return a query on the given field, with the keywords found at the
   * beginning of the token sequence.
   * @throws ParseException
   *     if the token sequence doesn’t start with a keyword, nor a
   *     bracketed keyword list.
   */
  private static FieldQuery analyzeField (final Field field,
      final Queue<Token> tokens) throws ParseException {
    final FieldQuery result;
    final Token firstToken = tokens.poll ();
    if (firstToken == Bracket.OPENING) {
      final List<Keyword> keywords = new ArrayList<Keyword> ();
      while (tokens.peek () instanceof Keyword) {
        keywords.add ((Keyword) tokens.poll ());
      }
      final Token nextToken = tokens.poll ();
      if (nextToken != Bracket.CLOSING) {
        throw new ParseException (
            "“)” expected, “" + nextToken + "” found.",
            -1
        );
      }
      result = new FieldQuery (field, keywords.toArray (KEYWORDS));
    } else if (firstToken instanceof Keyword) {
      result = new FieldQuery (field, (Keyword) firstToken);
    } else {
      throw new ParseException (
          "Keyword or “(” expected. “" + firstToken + "” found.",
          -1
      );
    }
    return result;
  }

}
