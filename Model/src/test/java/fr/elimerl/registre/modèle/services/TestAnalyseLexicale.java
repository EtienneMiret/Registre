package fr.elimerl.registre.modèle.services;

import static org.junit.Assert.assertEquals;

import java.util.Queue;

import org.junit.Before;
import org.junit.Test;

import fr.elimerl.registre.search.signes.Champ;
import fr.elimerl.registre.search.signes.MotClé;
import fr.elimerl.registre.search.signes.Opérateur;
import fr.elimerl.registre.search.signes.Parenthèse;
import fr.elimerl.registre.search.signes.Signe;
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
	assertEquals(new MotClé("éœù"), résultat.poll());
	assertEquals(new MotClé("cœur"), résultat.poll());
	assertEquals(new MotClé("çédille"), résultat.poll());
	assertEquals(new MotClé("àgrâvæ"), résultat.poll());
	assertEquals(new MotClé("ññubï"), résultat.poll());
	assertEquals(new MotClé("ïléà"), résultat.poll());
    }

    /**
     * Vérifie l’analyse lexicale d’une chaîne contenant plein de caractères
     * bizarres.
     */
    @Test
    public void analyserCaractèresBizarres() {
	final Queue<Signe> résultat = parseur.analyserLexicalement(
		"+ = ou: / \t\n ( %ù §§ À \"bonjour\" ++--) **");
	assertEquals(SIX, résultat.size());
	assertEquals(Opérateur.OU, résultat.poll());
	assertEquals(Parenthèse.OUVRANTE, résultat.poll());
	assertEquals(new MotClé("ù"), résultat.poll());
	assertEquals(new MotClé("à"), résultat.poll());
	assertEquals(new MotClé("bonjour"), résultat.poll());
	assertEquals(Parenthèse.FERMANTE, résultat.poll());
    }

    /**
     * Vérifie l’analyse lexicale d’une requête réaliste.
     */
    @Test
    public void analyserVraieRequête() {
	final Queue<Signe> résultat = parseur.analyserLexicalement(
		"title:(Bonjour Madame) OU coucou");
	assertEquals(SEPT, résultat.size());
	assertEquals(Champ.TITRE, résultat.poll());
	assertEquals(Parenthèse.OUVRANTE, résultat.poll());
	assertEquals(new MotClé("bonjour"), résultat.poll());
	assertEquals(new MotClé("madame"), résultat.poll());
	assertEquals(Parenthèse.FERMANTE, résultat.poll());
	assertEquals(Opérateur.OU, résultat.poll());
	assertEquals(new MotClé("coucou"), résultat.poll());
    }

}
