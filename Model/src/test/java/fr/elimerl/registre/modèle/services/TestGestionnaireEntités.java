package fr.elimerl.registre.modèle.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import javax.annotation.Resource;

import fr.elimerl.registre.entities.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import fr.elimerl.registre.entities.Author;
import fr.elimerl.registre.services.GestionnaireEntités;

/**
 * Classe de teste pour le {@link GestionnaireEntités}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext.xml")
@Transactional("gestionnaireTransactions")
public class TestGestionnaireEntités {

    /** Loggeur de cette classe. */
    private static final Logger logger =
	    LoggerFactory.getLogger(TestGestionnaireEntités.class);

    /** Le gestionnaire d’entités testé, fournit par Spring. */
    @Resource(name = "gestionnaireEntités")
    private GestionnaireEntités gestionnaire;

    /**
     * Teste la méthode {@link GestionnaireEntités#fournirMot(String)
     * fournirMot(String)}.
     */
    @Test
    public void fournirMot() {
	logger.info("Recherche de mots.");

	final String chaîneUne = "une";
	final Mot une = gestionnaire.fournirMot(chaîneUne);
	assertNotNull(une);
	assertNotNull(une.getId());
	assertEquals(chaîneUne, une.getValeur());

	final String chaîneSuper = "super";
	final Mot super_ = gestionnaire.fournirMot(chaîneSuper);
	assertNotNull(super_);
	assertNotNull(super_.getId());
	assertEquals(chaîneSuper, super_.getValeur());

	final String chaîneSérie = "série";
	final Mot série = gestionnaire.fournirMot(chaîneSérie);
	assertNotNull(série);
	assertNotNull(série.getId());
	assertEquals(chaîneSérie, série.getValeur());

	final String chaîneBonjour = "bonjour";
	final Mot bonjour = gestionnaire.fournirMot(chaîneBonjour);
	assertNotNull(bonjour);
	assertEquals(chaîneBonjour, bonjour.getValeur());

	assertSame(une, gestionnaire.fournirMot(chaîneUne));
	assertSame(super_, gestionnaire.fournirMot(chaîneSuper));
	assertSame(série, gestionnaire.fournirMot(chaîneSérie));
	assertSame(bonjour, gestionnaire.fournirMot(chaîneBonjour));
    }

    /**
     * Teste la méthode {@link GestionnaireEntités#fournirActeur(String)
     * fournirActeur(String)}.
     */
    @Test
    public void fournirActeur() {
	logger.info("Recherche d’acteurs.");

	final String nomEvaMendes = "Eva Mendes";
	final Actor evaMendes = gestionnaire.fournirActeur(nomEvaMendes);
	assertNotNull(evaMendes);
	assertNotNull(evaMendes.getId());
	assertEquals(nomEvaMendes, evaMendes.getNom());

	final String nomWillSmith = "Will Smith";
	final Actor willSmith = gestionnaire.fournirActeur(nomWillSmith);
	assertNotNull(willSmith);
	assertNotNull(willSmith.getId());
	assertEquals(nomWillSmith, willSmith.getNom());

	final String nomFunès = "Louis de Funès";
	final Actor funès = gestionnaire.fournirActeur(nomFunès);
	assertNotNull(funès);
	assertEquals(nomFunès, funès.getNom());

	assertSame(evaMendes, gestionnaire.fournirActeur(nomEvaMendes));
	assertSame(willSmith, gestionnaire.fournirActeur(nomWillSmith));
	assertSame(funès, gestionnaire.fournirActeur(nomFunès));
    }

    /**
     * Teste la méthode {@link GestionnaireEntités#fournirAuteur(String)
     * fournirAuteur(String)}.
     */
    @Test
    public void fournirAuteur() {
	logger.info("Recherche d’auteurs.");

	final String nomHerbert = "Frank Hebert";
	final Author herbert = gestionnaire.fournirAuteur(nomHerbert);
	assertNotNull(herbert);
	assertEquals(nomHerbert, herbert.getNom());

	final String nomAsimov = "Isaac Asimov";
	final Author asimov = gestionnaire.fournirAuteur(nomAsimov);
	assertNotNull(asimov);
	assertNotNull(asimov.getId());
	assertEquals(nomAsimov, asimov.getNom());

	final String nomTolkien = "J.R.R. Tolkien";
	final Author tolkien = gestionnaire.fournirAuteur(nomTolkien);
	assertNotNull(tolkien);
	assertEquals(nomTolkien, tolkien.getNom());

	assertSame(herbert, gestionnaire.fournirAuteur(nomHerbert));
	assertSame(asimov, gestionnaire.fournirAuteur(nomAsimov));
	assertSame(tolkien, gestionnaire.fournirAuteur(nomTolkien));
    }

    /**
     * Teste la méthode {@link GestionnaireEntités#fournirCompositeur(String)
     * fournirCompositeur(String)}.
     */
    @Test
    public void fournirCompositeurs() {
	logger.info("Recherche de compositeurs.");

	final String nomHowardShore = "Howard Shore";
	final Compositeur howardShore =
		gestionnaire.fournirCompositeur(nomHowardShore);
	assertNotNull(howardShore);
	assertNotNull(howardShore.getId());
	assertEquals(nomHowardShore, howardShore.getNom());

	final String nomHansZimmer = "Hans Zimmer";
	final Compositeur hansZimmer =
		gestionnaire.fournirCompositeur(nomHansZimmer);
	assertNotNull(hansZimmer);
	assertNotNull(hansZimmer.getId());
	assertEquals(nomHansZimmer, hansZimmer.getNom());

	final String nomJohnWilliams = "John Williams";
	final Compositeur johnWilliams =
		gestionnaire.fournirCompositeur(nomJohnWilliams);
	assertNotNull(johnWilliams);
	assertEquals(nomJohnWilliams, johnWilliams.getNom());

	assertSame(howardShore,
		gestionnaire.fournirCompositeur(nomHowardShore));
	assertSame(hansZimmer, gestionnaire.fournirCompositeur(nomHansZimmer));
	assertSame(johnWilliams,
		gestionnaire.fournirCompositeur(nomJohnWilliams));
    }

    /**
     * Teste la méthode {@link GestionnaireEntités#fournirDessinateur(String)
     * fournirDessinateur(String)}.
     */
    @Test
    public void fournirDessinateur() {
	logger.info("Recherche de dessinateurs.");

	final String nomPhilippeBuchet = "Philippe Buchet";
	final Dessinateur philippeBuchet =
		gestionnaire.fournirDessinateur(nomPhilippeBuchet);
	assertNotNull(philippeBuchet);
	assertEquals(nomPhilippeBuchet, philippeBuchet.getNom());

	final String nomJigounov = "Jigounov";
	final Dessinateur jigounov =
		gestionnaire.fournirDessinateur(nomJigounov);
	assertNotNull(jigounov);
	assertNotNull(jigounov.getId());
	assertEquals(nomJigounov, jigounov.getNom());

	assertSame(philippeBuchet,
		gestionnaire.fournirDessinateur(nomPhilippeBuchet));
	assertSame(jigounov, gestionnaire.fournirDessinateur(nomJigounov));
    }

    /**
     * Teste la méthode {@link GestionnaireEntités#fournirEmplacement(String)
     * fournirEmplacement(String)}.
     */
    @Test
    public void fournirEmplacement() {
	logger.info("Recherche d’emplacements.");

	final String nomVerneuil = "Verneuil";
	final Emplacement verneuil =
		gestionnaire.fournirEmplacement(nomVerneuil);
	assertNotNull(verneuil);
	assertNotNull(verneuil.getId());
	assertEquals(nomVerneuil, verneuil.getNom());

	final String nomMarcigny = "Marcigny";
	final Emplacement marcigny =
		gestionnaire.fournirEmplacement(nomMarcigny);
	assertNotNull(marcigny);
	assertEquals(nomMarcigny, marcigny.getNom());

	final String nomLyon = "Lyon";
	final Emplacement lyon = gestionnaire.fournirEmplacement(nomLyon);
	assertNotNull(lyon);
	assertNotNull(lyon.getId());
	assertEquals(nomLyon, lyon.getNom());

	assertSame(verneuil, gestionnaire.fournirEmplacement(nomVerneuil));
	assertSame(marcigny, gestionnaire.fournirEmplacement(nomMarcigny));
	assertSame(lyon, gestionnaire.fournirEmplacement(nomLyon));
    }

    /**
     * Teste la méthode {@link GestionnaireEntités#fournirPropriétaire(String)
     * fournirPropriétaire(String)}.
     */
    @Test
    public void fournirPropriétaire() {
	logger.info("Recherche de propriétaires.");

	final String nomEloi = "Eloi";
	final Propriétaire eloi = gestionnaire.fournirPropriétaire(nomEloi);
	assertNotNull(eloi);
	assertEquals(nomEloi, eloi.getNom());

	final String nomGrégoire = "Grégoire";
	final Propriétaire grégoire =
		gestionnaire.fournirPropriétaire(nomGrégoire);
	assertNotNull(grégoire);
	assertNotNull(grégoire.getId());
	assertEquals(nomGrégoire, grégoire.getNom());

	final String nomQuentin = "Quentin";
	final Propriétaire quentin =
		gestionnaire.fournirPropriétaire(nomQuentin);
	assertNotNull(quentin);
	assertEquals(nomQuentin, quentin.getNom());

	assertSame(eloi, gestionnaire.fournirPropriétaire(nomEloi));
	assertSame(grégoire, gestionnaire.fournirPropriétaire(nomGrégoire));
	assertSame(quentin, gestionnaire.fournirPropriétaire(nomQuentin));
    }

    /**
     * Teste la méthode {@link GestionnaireEntités#fournirRéalisateur(String)
     * fournirRéalisateur(String)}.
     */
    @Test
    public void fournirRéalisateur() {
	logger.info("Recherche de réalisateurs.");

	final String nomSpielberg = "Steven Spielberg";
	final Réalisateur spielberg =
		gestionnaire.fournirRéalisateur(nomSpielberg);
	assertNotNull(spielberg);
	assertNotNull(spielberg.getId());
	assertEquals(nomSpielberg, spielberg.getNom());

	final String nomPeterJackson = "Peter Jackson";
	final Réalisateur peterJackson =
		gestionnaire.fournirRéalisateur(nomPeterJackson);
	assertNotNull(peterJackson);
	assertEquals(nomPeterJackson, peterJackson.getNom());

	assertSame(spielberg, gestionnaire.fournirRéalisateur(nomSpielberg));
	assertSame(peterJackson,
		gestionnaire.fournirRéalisateur(nomPeterJackson));
    }

    /**
     * Teste la méthode {@link GestionnaireEntités#fournirScénariste(String)
     * fournirScénariste(String)}.
     */
    @Test
    public void fournirScénariste() {
	logger.info("Recherche de scénaristes.");

	final String nomMorvan = "Jean-David Morvan";
	final Scénariste morvan = gestionnaire.fournirScénariste(nomMorvan);
	assertNotNull(morvan);
	assertEquals(nomMorvan, morvan.getNom());

	final String nomRenard = "Renard";
	final Scénariste renard = gestionnaire.fournirScénariste(nomRenard);
	assertNotNull(renard);
	assertNotNull(renard.getId());
	assertEquals(nomRenard, renard.getNom());

	assertSame(morvan, gestionnaire.fournirScénariste(nomMorvan));
	assertSame(renard, gestionnaire.fournirScénariste(nomRenard));
    }

    /**
     * Teste la méthode {@link GestionnaireEntités#fournirSérie(String)
     * fournirSérie(String)}.
     */
    @Test
    public void fournirSérie() {
	logger.info("Recherche de séries.");

	final String nomMerlin = "Merlin";
	final Série merlin = gestionnaire.fournirSérie(nomMerlin);
	assertNotNull(merlin);
	assertNotNull(merlin.getId());
	assertEquals(nomMerlin, merlin.getNom());

	final String nomNcis = "NCIS";
	final Série ncis = gestionnaire.fournirSérie(nomNcis);
	assertNotNull(ncis);
	assertEquals(nomNcis, ncis.getNom());

	assertSame(merlin, gestionnaire.fournirSérie(nomMerlin));
	assertSame(ncis, gestionnaire.fournirSérie(nomNcis));
    }

}
