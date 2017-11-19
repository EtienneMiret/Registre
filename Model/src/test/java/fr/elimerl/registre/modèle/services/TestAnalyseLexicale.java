package fr.elimerl.registre.modèle.services;

import static org.junit.Assert.assertEquals;

import java.util.Queue;

import fr.elimerl.registre.search.tokens.*;
import org.junit.Before;
import org.junit.Test;

import fr.elimerl.registre.search.tokens.Field;
import fr.elimerl.registre.services.QueryParser;

/**
 * Cas de test JUnit pour la méthode
 * {@link QueryParser#tokenize(String)}.
 */
public class TestAnalyseLexicale {

    /** L’entier six. */
    private static final int SIX = 6;

    /** L’entier sept. */
    private static final int SEPT = 7;

    /** Le parseur qu’on teste. */
    private QueryParser parseur;

    /**
     * Prépare l’environnement pour les tests.
     */
    @Before
    public void setUp() {
	parseur = new QueryParser();
    }

    /**
     * Vérifie que l’analyse lexicale de parenthèses sans espaces fonctionne.
     */
    @Test
    public void analyserSimplesParenthèses() {
	final Queue<Token> résultat = parseur.tokenize("(()()(");
	assertEquals(SIX, résultat.size());
	assertEquals(Bracket.OPENING, résultat.poll());
	assertEquals(Bracket.OPENING, résultat.poll());
	assertEquals(Bracket.CLOSING, résultat.poll());
	assertEquals(Bracket.OPENING, résultat.poll());
	assertEquals(Bracket.CLOSING, résultat.poll());
	assertEquals(Bracket.OPENING, résultat.poll());
    }

    /**
     * Vérifie que l’analyse lexicale de parenthèse avec espaces fonctionne.
     */
    @Test
    public void analyserParenthèsesAvecEspaces() {
	final Queue<Token> résultat = parseur.tokenize(
		"  )(( )   (  ( ");
	assertEquals(SIX, résultat.size());
	assertEquals(Bracket.CLOSING, résultat.poll());
	assertEquals(Bracket.OPENING, résultat.poll());
	assertEquals(Bracket.OPENING, résultat.poll());
	assertEquals(Bracket.CLOSING, résultat.poll());
	assertEquals(Bracket.OPENING, résultat.poll());
	assertEquals(Bracket.OPENING, résultat.poll());
    }

    /**
     * Teste l’analyse léxicale de mot clés avec plein de lettres non-ascii.
     */
    @Test
    public void analyserUnicode() {
	final Queue<Token> résultat = parseur.tokenize(
		"ÉœÙ CŒUR Çédille àgrÂvÆ ÑñUbï Ïléà");
	assertEquals(SIX, résultat.size());
	assertEquals(new Keyword("éœù"), résultat.poll());
	assertEquals(new Keyword("cœur"), résultat.poll());
	assertEquals(new Keyword("çédille"), résultat.poll());
	assertEquals(new Keyword("àgrâvæ"), résultat.poll());
	assertEquals(new Keyword("ññubï"), résultat.poll());
	assertEquals(new Keyword("ïléà"), résultat.poll());
    }

    /**
     * Vérifie l’analyse lexicale d’une chaîne contenant plein de caractères
     * bizarres.
     */
    @Test
    public void analyserCaractèresBizarres() {
	final Queue<Token> résultat = parseur.tokenize(
		"+ = or: / \t\n ( %ù §§ À \"bonjour\" ++--) **");
	assertEquals(SIX, résultat.size());
	assertEquals(Operator.OR, résultat.poll());
	assertEquals(Bracket.OPENING, résultat.poll());
	assertEquals(new Keyword("ù"), résultat.poll());
	assertEquals(new Keyword("à"), résultat.poll());
	assertEquals(new Keyword("bonjour"), résultat.poll());
	assertEquals(Bracket.CLOSING, résultat.poll());
    }

    /**
     * Vérifie l’analyse lexicale d’une requête réaliste.
     */
    @Test
    public void analyserVraieRequête() {
	final Queue<Token> résultat = parseur.tokenize(
		"title:(Bonjour Madame) OR coucou");
	assertEquals(SEPT, résultat.size());
	assertEquals(Field.TITLE, résultat.poll());
	assertEquals(Bracket.OPENING, résultat.poll());
	assertEquals(new Keyword("bonjour"), résultat.poll());
	assertEquals(new Keyword("madame"), résultat.poll());
	assertEquals(Bracket.CLOSING, résultat.poll());
	assertEquals(Operator.OR, résultat.poll());
	assertEquals(new Keyword("coucou"), résultat.poll());
    }

}
