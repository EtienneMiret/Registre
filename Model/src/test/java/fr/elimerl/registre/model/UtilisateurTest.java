package fr.elimerl.registre.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test de la classe {@link Utilisateur}.
 */
public class UtilisateurTest {

    /** Nom de l’utilisateur dans ces tests. */
    private static final String NOM = "Etienne";

    /** Mot de passe initial de l’utilisateur pour ces tests. */
    private static final String MOT_DE_PASSE_INITIAL = "qwerty";

    /**
     * Exemples de mots de passe. Tous différents de
     * {@link #MOT_DE_PASSE_INITIAL} et différents les uns des autres.
     */
    private static final String[] MOTS_DE_PASSE =
	    new String[] {"azerty", "AZERTY", "Eoqk", "9Juj", "a2(;Xz" };

    /** Un seul mot de passe encodé de manières différentes. */
    private static final String[] MOT_DE_PASSE_ENCODÉ =
	    new String[] {
		"stéèàñÑçff",
		"ﬆéèàñÑçﬀ",
		// st     é     è     à     ñ      Ñ    ff
		"\ufb06\u00e9\u00e8\u00e0\u00f1\u00d1ç\ufb00",
		// st  e   ´  e   `  a   `  n   ~  N   ~  c   ¸    ff
		"\ufb06e\u0301e\u0300a\u0300n\u0303N\u0303c\u0327\ufb00" };

    /** Loggeur de cette classe. */
    private static final Logger logger =
	    LoggerFactory.getLogger(UtilisateurTest.class);

    /**
     * Teste la création d’un utilisateur, ainsi que les méthodes
     * {@link Utilisateur#getNom() getNom()} et
     * {@link Utilisateur#vérifierMdp(String) vérifierMdp()}.
     */
    @Test
    public void testCréation() {
	logger.info("Test création.");
	final Utilisateur utilisateur =
		new Utilisateur(NOM, MOT_DE_PASSE_INITIAL);
	assertEquals("Il y a un problème dans le nom de l’utilisateur.",
		NOM, utilisateur.getNom());
	assertTrue("Problème de vérification du mot de passe.",
		utilisateur.vérifierMdp(MOT_DE_PASSE_INITIAL));
	for (final String exempleMdp : MOTS_DE_PASSE) {
	    assertFalse("Un faux mot de passe est accepté.",
		    utilisateur.vérifierMdp(exempleMdp));
	}
    }

    /**
     * Teste le changement de mot de passe avec la méthode
     * {@link Utilisateur#définirMdp(String) définirMdp(String)}.
     */
    @Test
    public void testDéfinirMotDePasse() {
	logger.info("Test définir mot de passe.");
	final Utilisateur utilisateur =
		new Utilisateur(NOM, MOT_DE_PASSE_INITIAL);
	for (final String mdp : MOTS_DE_PASSE) {
	    utilisateur.définirMdp(mdp);
	    for (final String mdpTest : MOTS_DE_PASSE) {
		if (mdp.equals(mdpTest)) {
		    assertTrue("Le bon mot de passe n’est pas reconnu.",
			    utilisateur.vérifierMdp(mdpTest));
		} else {
		    assertFalse("Un faux mot de passe est accepté.",
			    utilisateur.vérifierMdp(mdpTest));
		}
	    }
	}
    }

    /**
     * Teste la prise en charge des mots de passe contenant des caractères
     * non-ascii. On veut nottament qu’un même mot de passe encodé de manières
     * différentes soit reconnu.
     */
    @Test
    public void testMotDePasseEncodé() {
	logger.info("Test mot de passe encodé.");
	final Utilisateur utilisateur =
		new Utilisateur(NOM, MOT_DE_PASSE_INITIAL);
	for (int i = 0; i < MOT_DE_PASSE_ENCODÉ.length; i++) {
	    utilisateur.définirMdp(MOT_DE_PASSE_ENCODÉ[i]);
	    for (int j = 0; j < MOT_DE_PASSE_ENCODÉ.length; j++) {
		assertTrue("Problème de vérification des mots de passe "
			+ "non-ascii : " + i + "≠" + j,
			utilisateur.vérifierMdp(MOT_DE_PASSE_ENCODÉ[j]));
	    }
	}
    }

}
