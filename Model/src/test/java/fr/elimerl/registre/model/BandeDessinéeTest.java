package fr.elimerl.registre.model;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe de test de la classe {@link BandeDessinée}.
 */
public class BandeDessinéeTest {

    /** Titre de la bande dessinée testée. */
    private static final String TITRE = "Le démon de minuit";

    /** Un exemple d’{@code Utilisateur} quelquonque. */
    private static final Utilisateur CRÉATEUR =
	    new Utilisateur("Etienne", "*******");

    /** Loggeur de cette classe. */
    private static final Logger logger =
	    LoggerFactory.getLogger(BandeDessinéeTest.class);

    /** {@code BandeDessinée} qui va être testée. */
    private BandeDessinée bandeDessinée;

    /**
     * Instancie la {@code BandeDessinée} qui va être testée.
     */
    @Before
    public void setUp() {
	bandeDessinée = new BandeDessinée(TITRE, CRÉATEUR);
    }

    /**
     * Teste les méthodes {@link BandeDessinée#setDessinateur(Dessinateur)
     * setDessinateur(Dessinateur)} et {@link BandeDessinée#getDessinateur()
     * getDessinateur()}.
     */
    @Test
    public void dessinateur() {
	logger.info("Test du dessinateur.");

	final Dessinateur dessinateur1 = new Dessinateur("Jigounov");
	final Dessinateur dessinateur2 = new Dessinateur("Alain Henriet");

	bandeDessinée.setDessinateur(dessinateur1);
	assertEquals("Le dessinateur n’a pas été défini correctement.",
		dessinateur1, bandeDessinée.getDessinateur());

	bandeDessinée.setDessinateur(dessinateur2);
	assertEquals("Le dessinateur n’a pas été modifié correctement.",
		dessinateur2, bandeDessinée.getDessinateur());
    }

    /**
     * Teste les méthodes {@link BandeDessinée#setScénariste(Scénariste)
     * setScénariste(Scénariste)} et {@link BandeDessinée#getScénariste()
     * getScénariste()}.
     */
    @Test
    public void scénariste() {
	logger.info("Test du scénariste.");

	final Scénariste scénariste1 = new Scénariste("Renard");
	final Scénariste scénariste2 = new Scénariste("Callède");

	bandeDessinée.setScénariste(scénariste1);
	assertEquals("Le scénariste n’a pas été défini correctement.",
		scénariste1, bandeDessinée.getScénariste());

	bandeDessinée.setScénariste(scénariste2);
	assertEquals("Le scénariste n’a pas été modifié correctement.",
		scénariste2, bandeDessinée.getScénariste());
    }

    /**
     * Teste les méthodes {@link BandeDessinée#setNuméro(Integer)
     * setNuméro(Integer)} et {@link BandeDessinée#getNuméro() getNuméro()}.
     */
    @Test
    public void numéro() {
	logger.info("Test du numéro.");

	final Integer numéro1 = Integer.valueOf(12);
	final Integer numéro2 = Integer.valueOf(4);

	bandeDessinée.setNuméro(numéro1);
	assertEquals("Le numéro n’a pas été défini correctement.",
		numéro1, bandeDessinée.getNuméro());

	bandeDessinée.setNuméro(numéro2);
	assertEquals("Le numéro n’a pas été modifié correctement.",
		numéro2, bandeDessinée.getNuméro());
    }

}
