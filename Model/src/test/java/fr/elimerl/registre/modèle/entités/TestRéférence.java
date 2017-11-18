package fr.elimerl.registre.modèle.entités;

import static org.junit.Assert.assertEquals;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

import fr.elimerl.registre.entities.Fiche;
import fr.elimerl.registre.entities.Film;
import fr.elimerl.registre.entities.Mot;
import fr.elimerl.registre.entities.Référence;
import fr.elimerl.registre.entities.Utilisateur;
import fr.elimerl.registre.entities.Film.Support;
import fr.elimerl.registre.entities.Référence.Champ;

/**
 * Test JUnit pour la classe {@link Référence}.
 */
public class TestRéférence {

    /**
     * Teste le constructeur et les getteurs.
     */
    public void test() {
	final Mot mot = new Mot("Demain");
	final Champ champ = Champ.TITRE;
	final Fiche fiche = new Film("Demain ne meurt jamais",
		new Utilisateur("Etienne", "etienne@email"), Support.DVD);
	final Référence référence = new Référence(mot, champ, fiche);
	assertEquals(mot, référence.getMot());
	assertEquals(champ, référence.getChamp());
	assertEquals(fiche, référence.getFiche());
    }

    /**
     * Teste les méthodes {@link Référence#equals(Object) equals(Object)} et
     * {@link Référence#hashCode() hashCode()}.
     */
    @Test
    public void equalsEtHashCode() {
	final EqualsVerifier<Référence> equalsVerifier =
		EqualsVerifier.forClass(Référence.class);
	equalsVerifier.suppress(Warning.STRICT_INHERITANCE);
	equalsVerifier.verify();
    }

}
