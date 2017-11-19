package fr.elimerl.registre.model.entités;

import static org.junit.Assert.assertEquals;

import fr.elimerl.registre.entities.Cartoonist;
import fr.elimerl.registre.entities.Comic;
import fr.elimerl.registre.entities.ScriptWriter;
import fr.elimerl.registre.entities.User;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe de test de la classe {@link Comic}.
 */
public class TestBandeDessinée {

    /** Titre de la bande dessinée testée. */
    private static final String TITRE = "Le démon de minuit";

    /** Un exemple d’{@code User} quelquonque. */
    private static final User CRÉATEUR =
	    new User("Etienne", "etienne@email");

    /** Loggeur de cette classe. */
    private static final Logger logger =
	    LoggerFactory.getLogger(TestBandeDessinée.class);

    /** {@code Comic} qui va être testée. */
    private Comic bandeDessinée;

    /**
     * Instancie la {@code Comic} qui va être testée.
     */
    @Before
    public void setUp() {
	bandeDessinée = new Comic(TITRE, CRÉATEUR);
    }

    /**
     * Teste les méthodes {@link Comic#setCartoonist(Cartoonist)
     * setCartoonist(Cartoonist)} et {@link Comic#getCartoonist()
     * getCartoonist()}.
     */
    @Test
    public void dessinateur() {
	logger.info("Test du dessinateur.");

	final Cartoonist dessinateur1 = new Cartoonist("Jigounov");
	final Cartoonist dessinateur2 = new Cartoonist("Alain Henriet");

	bandeDessinée.setCartoonist(dessinateur1);
	assertEquals("Le dessinateur n’a pas été défini correctement.",
		dessinateur1, bandeDessinée.getCartoonist());

	bandeDessinée.setCartoonist(dessinateur2);
	assertEquals("Le dessinateur n’a pas été modifié correctement.",
		dessinateur2, bandeDessinée.getCartoonist());
    }

    /**
     * Teste les méthodes {@link Comic#setScriptWriter(ScriptWriter)
     * setScriptWriter(ScriptWriter)} et {@link Comic#getScriptWriter()
     * getScriptWriter()}.
     */
    @Test
    public void scénariste() {
	logger.info("Test du scénariste.");

	final ScriptWriter scénariste1 = new ScriptWriter("Renard");
	final ScriptWriter scénariste2 = new ScriptWriter("Callède");

	bandeDessinée.setScriptWriter(scénariste1);
	assertEquals("Le scénariste n’a pas été défini correctement.",
		scénariste1, bandeDessinée.getScriptWriter());

	bandeDessinée.setScriptWriter(scénariste2);
	assertEquals("Le scénariste n’a pas été modifié correctement.",
		scénariste2, bandeDessinée.getScriptWriter());
    }

    /**
     * Teste les méthodes {@link Comic#setNumber(Integer)
     * setNumber(Integer)} et {@link Comic#getNumber() getNumber()}.
     */
    @Test
    public void numéro() {
	logger.info("Test du numéro.");

	final Integer numéro1 = Integer.valueOf(12);
	final Integer numéro2 = Integer.valueOf(4);

	bandeDessinée.setNumber(numéro1);
	assertEquals("Le numéro n’a pas été défini correctement.",
		numéro1, bandeDessinée.getNumber());

	bandeDessinée.setNumber(numéro2);
	assertEquals("Le numéro n’a pas été modifié correctement.",
		numéro2, bandeDessinée.getNumber());
    }

}
