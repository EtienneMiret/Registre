package fr.elimerl.registre.modèle.services;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.Queue;

import fr.elimerl.registre.search.grammar.SearchQuery;
import fr.elimerl.registre.search.grammar.SimpleKeyword;
import fr.elimerl.registre.search.tokens.*;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.elimerl.registre.search.grammar.BracketedQuery;
import fr.elimerl.registre.search.grammar.FieldQuery;
import fr.elimerl.registre.search.tokens.Field;
import fr.elimerl.registre.services.ParseurDeRecherches;

/**
 * Cas de test JUnit pour la méthode
 * {@link ParseurDeRecherches#analyserGrammaticalement(Queue)}.
 */
public class TestAnalyseGrammaticale {

    /** Journal SLF4J de cette classe. */
    private static final Logger journal =
	    LoggerFactory.getLogger(TestAnalyseGrammaticale.class);

    /** Le parseur qu’on teste. */
    private ParseurDeRecherches parseur;

    /** La suite de signes à parser. */
    private Queue<Signe> signes;

    /**
     * Prépare l’environnement pour les tests.
     */
    @Before
    public void setUp() {
	parseur = new ParseurDeRecherches();
	signes = new LinkedList<Signe>();
    }

    /**
     * Vérifie qu’on peut parser une requête vide.
     */
    @Test
    public void requêteVide() {
	journal.info("Analyse grammaticale d’une requête vide.");

	final SearchQuery résultat = parseur.analyserGrammaticalement(signes);

	final SearchQuery attendue = new SearchQuery(true);
	assertEquals(attendue, résultat);
    }

    /**
     * Analyse une requête composée d’un seul mot clé. Attention : le test doit
     * passer que la requête obtenue soit une conjonction ou une disjonction.
     */
    @Test
    public void unSeulMotClé() {
	final Keyword motClé = new Keyword("coucou");
	signes.add(motClé);
	journal.info("Analyse grammaticale de la requête « {} ».", signes);

	final SearchQuery résultat = parseur.analyserGrammaticalement(signes);

	final SearchQuery attendue = new SearchQuery(true, new SimpleKeyword(motClé));
	assertEquals(attendue, résultat);
    }

    /**
     * Analyse une requête composée simplement de plusieurs mots-clés.
     */
    @Test
    public void conjonctionPlusieursMotsClés() {
	final Keyword toto = new Keyword("toto");
	final Keyword tata = new Keyword("tata");
	final Keyword titi = new Keyword("titi");
	signes.add(toto);
	signes.add(tata);
	signes.add(titi);
	journal.info("Analyse grammaticale de la requête « {} ».", signes);

	final SearchQuery résultat = parseur.analyserGrammaticalement(signes);

	final SearchQuery attendue = new SearchQuery(true,
		new SimpleKeyword(toto),
		new SimpleKeyword(tata),
		new SimpleKeyword(titi));
	assertEquals(attendue, résultat);
    }

    /**
     * Analyse une requête composée de plusieurs mots-clés séparés par
     * l’opérateur « ou ».
     */
    @Test
    public void disjonctionPlusieursMotsClés() {
	final Keyword toto = new Keyword("toto");
	final Keyword tata = new Keyword("tata");
	final Keyword titi = new Keyword("titi");
	signes.add(toto);
	signes.add(Operator.OR);
	signes.add(tata);
	signes.add(Operator.OR);
	signes.add(titi);
	journal.info("Analyse grammaticale de la requête « {} ».", signes);

	final SearchQuery résultat = parseur.analyserGrammaticalement(signes);

	final SearchQuery attendue = new SearchQuery(false,
		new SimpleKeyword(toto),
		new SimpleKeyword(tata),
		new SimpleKeyword(titi));
	assertEquals(attendue, résultat);
    }

    /**
     * Analyse une requête complexe.
     */
    @Test
    public void requêteComplexe1() {
	final Keyword maman = new Keyword("maman");
	final Keyword etienne = new Keyword("etienne");
	final Keyword grégoire = new Keyword("grégoire");
	final Keyword toto = new Keyword("toto");
	final Keyword tata = new Keyword("tata");
	final Keyword titi = new Keyword("titi");
	signes.add(Field.TITLE);
	signes.add(maman);
	signes.add(Operator.OR);
	signes.add(Field.COMMENT);
	signes.add(Parenthèse.OUVRANTE);
	signes.add(etienne);
	signes.add(grégoire);
	signes.add(Parenthèse.FERMANTE);
	signes.add(Operator.OR);
	signes.add(Parenthèse.OUVRANTE);
	signes.add(toto);
	signes.add(tata);
	signes.add(titi);
	signes.add(Parenthèse.FERMANTE);
	journal.info("Analyse grammaticale de la requête « {} ».", signes);

	final SearchQuery résultat = parseur.analyserGrammaticalement(signes);

	final SearchQuery attendue = new SearchQuery(false,
		new FieldQuery(Field.TITLE, maman),
		new FieldQuery(Field.COMMENT,
			etienne, grégoire),
		new BracketedQuery(
			new SearchQuery(true,
				new SimpleKeyword(toto),
				new SimpleKeyword(tata),
				new SimpleKeyword(titi)
			)
		)
	);
	assertEquals(attendue, résultat);
    }

    /**
     * Analyse une requête complexe.
     */
    @Test
    public void requêteComplexe2() {
	final Keyword a = new Keyword("a");
	final Keyword b = new Keyword("b");
	final Keyword c = new Keyword("c");
	final Keyword d = new Keyword("d");
	final Keyword e = new Keyword("e");
	final Keyword f = new Keyword("f");
	final Keyword g = new Keyword("g");
	final Keyword h = new Keyword("h");
	final Keyword i = new Keyword("i");
	signes.add(Parenthèse.OUVRANTE);
	signes.add(a);
	signes.add(Operator.OR);
	signes.add(b);
	signes.add(Operator.OR);
	signes.add(c);
	signes.add(Parenthèse.FERMANTE);
	signes.add(Parenthèse.OUVRANTE);
	signes.add(d);
	signes.add(Operator.OR);
	signes.add(e);
	signes.add(Operator.OR);
	signes.add(f);
	signes.add(Parenthèse.FERMANTE);
	signes.add(Parenthèse.OUVRANTE);
	signes.add(g);
	signes.add(Operator.OR);
	signes.add(h);
	signes.add(Operator.OR);
	signes.add(i);
	signes.add(Parenthèse.FERMANTE);
	journal.info("Analyse grammaticale de la requête « {} ».", signes);

	final SearchQuery résultat = parseur.analyserGrammaticalement(signes);

	final SearchQuery attendue = new SearchQuery(true,
		new BracketedQuery(new SearchQuery(false,
			new SimpleKeyword(a),
			new SimpleKeyword(b),
			new SimpleKeyword(c)
		)),
		new BracketedQuery(new SearchQuery(false,
			new SimpleKeyword(d),
			new SimpleKeyword(e),
			new SimpleKeyword(f)
		)),
		new BracketedQuery(new SearchQuery(false,
			new SimpleKeyword(g),
			new SimpleKeyword(h),
			new SimpleKeyword(i)
		))
	);
	assertEquals(attendue, résultat);
    }

}
