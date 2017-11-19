package fr.elimerl.registre.modèle.services;

import static org.junit.Assert.assertEquals;

import java.util.Queue;

import fr.elimerl.registre.search.tokens.*;
import org.junit.Before;
import org.junit.Test;

import fr.elimerl.registre.search.tokens.Field;
import fr.elimerl.registre.services.ParseurDeRecherches;

/**
 * Cas de test JUnit pour la méthode
 * {@link ParseurDeRecherches#analyserLexicalement(String)}.
 */
public class TestAnalyseLexicale {

    /** L’entier six. */
    private static final int SIX = 6;

    /** L’entier sept. */
    private static final int SEPT = 7;

    /** Le parseur qu’on teste. */
    private ParseurDeRecherches parseur;

    /**
     * Prépare l’environnement pour les tests.
     */
    @Before
    public void setUp() {
	parseur = new ParseurDeRecherches();
    }

    /**
     * Vérifie que l’analyse lexicale de parenthèses sans espaces fonctionne.
     */
    @Test
    public void analyserSimplesParenthèses() {
	final Queue<Signe> résultat = parseur.analyserLexicalement("(()()(");
	assertEquals(SIX, résultat.size());
	assertEquals(Parenthèse.OUVRANTE, résultat.poll());
	assertEquals(Parenthèse.OUVRANTE, résultat.poll());
	assertEquals(Parenthèse.FERMANTE, résultat.poll());
	assertEquals(Parenthèse.OUVRANTE, résultat.poll());
	assertEquals(Parenthèse.FERMANTE, résultat.poll());
	assertEquals(Parenthèse.OUVRANTE, résultat.poll());
    }

    /**
     * Vérifie que l’analyse lexicale de parenthèse avec espaces fonctionne.
     */
    @Test
    public void analyserParenthèsesAvecEspaces() {
	final Queue<Signe> résultat = parseur.analyserLexicalement(
		"  )(( )   (  ( ");
	assertEquals(SIX, résultat.size());
	assertEquals(Parenthèse.FERMANTE, résultat.poll());
	assertEquals(Parenthèse.OUVRANTE, résultat.poll());
	assertEquals(Parenthèse.OUVRANTE, résultat.poll());
	assertEquals(Parenthèse.FERMANTE, résultat.poll());
	assertEquals(Parenthèse.OUVRANTE, résultat.poll());
	assertEquals(Parenthèse.OUVRANTE, résultat.poll());
    }

    /**
     * Teste l’analyse léxicale de mot clés avec plein de lettres non-ascii.
     */
    @Test
    public void analyserUnicode() {
	final Queue<Signe> résultat = parseur.analyserLexicalement(
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
	final Queue<Signe> résultat = parseur.analyserLexicalement(
		"+ = or: / \t\n ( %ù §§ À \"bonjour\" ++--) **");
	assertEquals(SIX, résultat.size());
	assertEquals(Operator.OR, résultat.poll());
	assertEquals(Parenthèse.OUVRANTE, résultat.poll());
	assertEquals(new Keyword("ù"), résultat.poll());
	assertEquals(new Keyword("à"), résultat.poll());
	assertEquals(new Keyword("bonjour"), résultat.poll());
	assertEquals(Parenthèse.FERMANTE, résultat.poll());
    }

    /**
     * Vérifie l’analyse lexicale d’une requête réaliste.
     */
    @Test
    public void analyserVraieRequête() {
	final Queue<Signe> résultat = parseur.analyserLexicalement(
		"title:(Bonjour Madame) OR coucou");
	assertEquals(SEPT, résultat.size());
	assertEquals(Field.TITLE, résultat.poll());
	assertEquals(Parenthèse.OUVRANTE, résultat.poll());
	assertEquals(new Keyword("bonjour"), résultat.poll());
	assertEquals(new Keyword("madame"), résultat.poll());
	assertEquals(Parenthèse.FERMANTE, résultat.poll());
	assertEquals(Operator.OR, résultat.poll());
	assertEquals(new Keyword("coucou"), résultat.poll());
    }

}
