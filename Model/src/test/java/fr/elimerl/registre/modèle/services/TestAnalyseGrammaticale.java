package fr.elimerl.registre.modèle.services;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.Queue;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.elimerl.registre.modèle.recherche.grammaire.MotCléSimple;
import fr.elimerl.registre.modèle.recherche.grammaire.Requête;
import fr.elimerl.registre.modèle.recherche.grammaire.RequêteEntreParenthèse;
import fr.elimerl.registre.modèle.recherche.grammaire.RequêteSurChamp;
import fr.elimerl.registre.modèle.recherche.signes.Champ;
import fr.elimerl.registre.modèle.recherche.signes.MotClé;
import fr.elimerl.registre.modèle.recherche.signes.Opérateur;
import fr.elimerl.registre.modèle.recherche.signes.Parenthèse;
import fr.elimerl.registre.modèle.recherche.signes.Signe;

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

	final Requête résultat = parseur.analyserGrammaticalement(signes);

	final Requête attendue = new Requête(true);
	assertEquals(attendue, résultat);
    }

    /**
     * Analyse une requête composée d’un seul mot clé. Attention : le test doit
     * passer que la requête obtenue soit une conjonction ou une disjonction.
     */
    @Test
    public void unSeulMotClé() {
	final MotClé motClé = new MotClé("coucou");
	signes.add(motClé);
	journal.info("Analyse grammaticale de la requête « {} ».", signes);

	final Requête résultat = parseur.analyserGrammaticalement(signes);

	final Requête attendue = new Requête(true, new MotCléSimple(motClé));
	assertEquals(attendue, résultat);
    }

    /**
     * Analyse une requête composée simplement de plusieurs mots-clés.
     */
    @Test
    public void conjonctionPlusieursMotsClés() {
	final MotClé toto = new MotClé("toto");
	final MotClé tata = new MotClé("tata");
	final MotClé titi = new MotClé("titi");
	signes.add(toto);
	signes.add(tata);
	signes.add(titi);
	journal.info("Analyse grammaticale de la requête « {} ».", signes);

	final Requête résultat = parseur.analyserGrammaticalement(signes);

	final Requête attendue = new Requête(true,
		new MotCléSimple(toto),
		new MotCléSimple(tata),
		new MotCléSimple(titi));
	assertEquals(attendue, résultat);
    }

    /**
     * Analyse une requête composée de plusieurs mots-clés séparés par
     * l’opérateur « ou ».
     */
    @Test
    public void disjonctionPlusieursMotsClés() {
	final MotClé toto = new MotClé("toto");
	final MotClé tata = new MotClé("tata");
	final MotClé titi = new MotClé("titi");
	signes.add(toto);
	signes.add(Opérateur.OU);
	signes.add(tata);
	signes.add(Opérateur.OU);
	signes.add(titi);
	journal.info("Analyse grammaticale de la requête « {} ».", signes);

	final Requête résultat = parseur.analyserGrammaticalement(signes);

	final Requête attendue = new Requête(false,
		new MotCléSimple(toto),
		new MotCléSimple(tata),
		new MotCléSimple(titi));
	assertEquals(attendue, résultat);
    }

    /**
     * Analyse une requête complexe.
     */
    @Test
    public void requêteComplexe1() {
	final MotClé maman = new MotClé("maman");
	final MotClé etienne = new MotClé("etienne");
	final MotClé grégoire = new MotClé("grégoire");
	final MotClé toto = new MotClé("toto");
	final MotClé tata = new MotClé("tata");
	final MotClé titi = new MotClé("titi");
	signes.add(Champ.TITRE);
	signes.add(maman);
	signes.add(Opérateur.OU);
	signes.add(Champ.COMMENTAIRE);
	signes.add(Parenthèse.OUVRANTE);
	signes.add(etienne);
	signes.add(grégoire);
	signes.add(Parenthèse.FERMANTE);
	signes.add(Opérateur.OU);
	signes.add(Parenthèse.OUVRANTE);
	signes.add(toto);
	signes.add(tata);
	signes.add(titi);
	signes.add(Parenthèse.FERMANTE);
	journal.info("Analyse grammaticale de la requête « {} ».", signes);

	final Requête résultat = parseur.analyserGrammaticalement(signes);

	final Requête attendue = new Requête(false,
		new RequêteSurChamp(Champ.TITRE, maman),
		new RequêteSurChamp(Champ.COMMENTAIRE, etienne, grégoire),
		new RequêteEntreParenthèse(
			new Requête(true,
				new MotCléSimple(toto),
				new MotCléSimple(tata),
				new MotCléSimple(titi)
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
	final MotClé a = new MotClé("a");
	final MotClé b = new MotClé("b");
	final MotClé c = new MotClé("c");
	final MotClé d = new MotClé("d");
	final MotClé e = new MotClé("e");
	final MotClé f = new MotClé("f");
	final MotClé g = new MotClé("g");
	final MotClé h = new MotClé("h");
	final MotClé i = new MotClé("i");
	signes.add(Parenthèse.OUVRANTE);
	signes.add(a);
	signes.add(Opérateur.OU);
	signes.add(b);
	signes.add(Opérateur.OU);
	signes.add(c);
	signes.add(Parenthèse.FERMANTE);
	signes.add(Parenthèse.OUVRANTE);
	signes.add(d);
	signes.add(Opérateur.OU);
	signes.add(e);
	signes.add(Opérateur.OU);
	signes.add(f);
	signes.add(Parenthèse.FERMANTE);
	signes.add(Parenthèse.OUVRANTE);
	signes.add(g);
	signes.add(Opérateur.OU);
	signes.add(h);
	signes.add(Opérateur.OU);
	signes.add(i);
	signes.add(Parenthèse.FERMANTE);
	journal.info("Analyse grammaticale de la requête « {} ».", signes);

	final Requête résultat = parseur.analyserGrammaticalement(signes);

	final Requête attendue = new Requête(true,
		new RequêteEntreParenthèse(new Requête(false,
			new MotCléSimple(a),
			new MotCléSimple(b),
			new MotCléSimple(c)
		)),
		new RequêteEntreParenthèse(new Requête(false,
			new MotCléSimple(d),
			new MotCléSimple(e),
			new MotCléSimple(f)
		)),
		new RequêteEntreParenthèse(new Requête(false,
			new MotCléSimple(g),
			new MotCléSimple(h),
			new MotCléSimple(i)
		))
	);
	assertEquals(attendue, résultat);
    }

}
