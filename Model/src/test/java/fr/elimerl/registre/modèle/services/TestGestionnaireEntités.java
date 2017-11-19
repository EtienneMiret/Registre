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
import fr.elimerl.registre.services.RegistreEntityManager;

/**
 * Classe de teste pour le {@link RegistreEntityManager}.
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
    private RegistreEntityManager gestionnaire;

    /**
     * Teste la méthode {@link RegistreEntityManager#supplyWord(String)
     * supplyWord(String)}.
     */
    @Test
    public void fournirMot() {
	logger.info("Recherche de mots.");

	final String chaîneUne = "une";
	final Word une = gestionnaire.supplyWord(chaîneUne);
	assertNotNull(une);
	assertNotNull(une.getId());
	assertEquals(chaîneUne, une.getValue());

	final String chaîneSuper = "super";
	final Word super_ = gestionnaire.supplyWord(chaîneSuper);
	assertNotNull(super_);
	assertNotNull(super_.getId());
	assertEquals(chaîneSuper, super_.getValue());

	final String chaîneSérie = "série";
	final Word série = gestionnaire.supplyWord(chaîneSérie);
	assertNotNull(série);
	assertNotNull(série.getId());
	assertEquals(chaîneSérie, série.getValue());

	final String chaîneBonjour = "bonjour";
	final Word bonjour = gestionnaire.supplyWord(chaîneBonjour);
	assertNotNull(bonjour);
	assertEquals(chaîneBonjour, bonjour.getValue());

	assertSame(une, gestionnaire.supplyWord(chaîneUne));
	assertSame(super_, gestionnaire.supplyWord(chaîneSuper));
	assertSame(série, gestionnaire.supplyWord(chaîneSérie));
	assertSame(bonjour, gestionnaire.supplyWord(chaîneBonjour));
    }

    /**
     * Teste la méthode {@link RegistreEntityManager#supplyActor(String)
     * supplyActor(String)}.
     */
    @Test
    public void fournirActeur() {
	logger.info("Recherche d’acteurs.");

	final String nomEvaMendes = "Eva Mendes";
	final Actor evaMendes = gestionnaire.supplyActor(nomEvaMendes);
	assertNotNull(evaMendes);
	assertNotNull(evaMendes.getId());
	assertEquals(nomEvaMendes, evaMendes.getName());

	final String nomWillSmith = "Will Smith";
	final Actor willSmith = gestionnaire.supplyActor(nomWillSmith);
	assertNotNull(willSmith);
	assertNotNull(willSmith.getId());
	assertEquals(nomWillSmith, willSmith.getName());

	final String nomFunès = "Louis de Funès";
	final Actor funès = gestionnaire.supplyActor(nomFunès);
	assertNotNull(funès);
	assertEquals(nomFunès, funès.getName());

	assertSame(evaMendes, gestionnaire.supplyActor(nomEvaMendes));
	assertSame(willSmith, gestionnaire.supplyActor(nomWillSmith));
	assertSame(funès, gestionnaire.supplyActor(nomFunès));
    }

    /**
     * Teste la méthode {@link RegistreEntityManager#supplyAuthor(String)
     * supplyAuthor(String)}.
     */
    @Test
    public void fournirAuteur() {
	logger.info("Recherche d’auteurs.");

	final String nomHerbert = "Frank Hebert";
	final Author herbert = gestionnaire.supplyAuthor(nomHerbert);
	assertNotNull(herbert);
	assertEquals(nomHerbert, herbert.getName());

	final String nomAsimov = "Isaac Asimov";
	final Author asimov = gestionnaire.supplyAuthor(nomAsimov);
	assertNotNull(asimov);
	assertNotNull(asimov.getId());
	assertEquals(nomAsimov, asimov.getName());

	final String nomTolkien = "J.R.R. Tolkien";
	final Author tolkien = gestionnaire.supplyAuthor(nomTolkien);
	assertNotNull(tolkien);
	assertEquals(nomTolkien, tolkien.getName());

	assertSame(herbert, gestionnaire.supplyAuthor(nomHerbert));
	assertSame(asimov, gestionnaire.supplyAuthor(nomAsimov));
	assertSame(tolkien, gestionnaire.supplyAuthor(nomTolkien));
    }

    /**
     * Teste la méthode {@link RegistreEntityManager#supplyComposer(String)
     * supplyComposer(String)}.
     */
    @Test
    public void fournirCompositeurs() {
	logger.info("Recherche de compositeurs.");

	final String nomHowardShore = "Howard Shore";
	final Composer howardShore =
		gestionnaire.supplyComposer(nomHowardShore);
	assertNotNull(howardShore);
	assertNotNull(howardShore.getId());
	assertEquals(nomHowardShore, howardShore.getName());

	final String nomHansZimmer = "Hans Zimmer";
	final Composer hansZimmer =
		gestionnaire.supplyComposer(nomHansZimmer);
	assertNotNull(hansZimmer);
	assertNotNull(hansZimmer.getId());
	assertEquals(nomHansZimmer, hansZimmer.getName());

	final String nomJohnWilliams = "John Williams";
	final Composer johnWilliams =
		gestionnaire.supplyComposer(nomJohnWilliams);
	assertNotNull(johnWilliams);
	assertEquals(nomJohnWilliams, johnWilliams.getName());

	assertSame(howardShore,
		gestionnaire.supplyComposer(nomHowardShore));
	assertSame(hansZimmer, gestionnaire.supplyComposer(nomHansZimmer));
	assertSame(johnWilliams,
		gestionnaire.supplyComposer(nomJohnWilliams));
    }

    /**
     * Teste la méthode {@link RegistreEntityManager#supplyCartoonist(String)
     * supplyCartoonist(String)}.
     */
    @Test
    public void fournirDessinateur() {
	logger.info("Recherche de dessinateurs.");

	final String nomPhilippeBuchet = "Philippe Buchet";
	final Cartoonist philippeBuchet =
		gestionnaire.supplyCartoonist(nomPhilippeBuchet);
	assertNotNull(philippeBuchet);
	assertEquals(nomPhilippeBuchet, philippeBuchet.getName());

	final String nomJigounov = "Jigounov";
	final Cartoonist jigounov =
		gestionnaire.supplyCartoonist(nomJigounov);
	assertNotNull(jigounov);
	assertNotNull(jigounov.getId());
	assertEquals(nomJigounov, jigounov.getName());

	assertSame(philippeBuchet,
		gestionnaire.supplyCartoonist(nomPhilippeBuchet));
	assertSame(jigounov, gestionnaire.supplyCartoonist(nomJigounov));
    }

    /**
     * Teste la méthode {@link RegistreEntityManager#supplyLocation(String)
     * supplyLocation(String)}.
     */
    @Test
    public void fournirEmplacement() {
	logger.info("Recherche d’emplacements.");

	final String nomVerneuil = "Verneuil";
	final Location verneuil =
		gestionnaire.supplyLocation(nomVerneuil);
	assertNotNull(verneuil);
	assertNotNull(verneuil.getId());
	assertEquals(nomVerneuil, verneuil.getName());

	final String nomMarcigny = "Marcigny";
	final Location marcigny =
		gestionnaire.supplyLocation(nomMarcigny);
	assertNotNull(marcigny);
	assertEquals(nomMarcigny, marcigny.getName());

	final String nomLyon = "Lyon";
	final Location lyon = gestionnaire.supplyLocation(nomLyon);
	assertNotNull(lyon);
	assertNotNull(lyon.getId());
	assertEquals(nomLyon, lyon.getName());

	assertSame(verneuil, gestionnaire.supplyLocation(nomVerneuil));
	assertSame(marcigny, gestionnaire.supplyLocation(nomMarcigny));
	assertSame(lyon, gestionnaire.supplyLocation(nomLyon));
    }

    /**
     * Teste la méthode {@link RegistreEntityManager#supplyOwner(String)
     * supplyOwner(String)}.
     */
    @Test
    public void fournirPropriétaire() {
	logger.info("Recherche de propriétaires.");

	final String nomEloi = "Eloi";
	final Owner eloi = gestionnaire.supplyOwner(nomEloi);
	assertNotNull(eloi);
	assertEquals(nomEloi, eloi.getName());

	final String nomGrégoire = "Grégoire";
	final Owner grégoire =
		gestionnaire.supplyOwner(nomGrégoire);
	assertNotNull(grégoire);
	assertNotNull(grégoire.getId());
	assertEquals(nomGrégoire, grégoire.getName());

	final String nomQuentin = "Quentin";
	final Owner quentin =
		gestionnaire.supplyOwner(nomQuentin);
	assertNotNull(quentin);
	assertEquals(nomQuentin, quentin.getName());

	assertSame(eloi, gestionnaire.supplyOwner(nomEloi));
	assertSame(grégoire, gestionnaire.supplyOwner(nomGrégoire));
	assertSame(quentin, gestionnaire.supplyOwner(nomQuentin));
    }

    /**
     * Teste la méthode {@link RegistreEntityManager#supplyDirector(String)
     * supplyDirector(String)}.
     */
    @Test
    public void fournirRéalisateur() {
	logger.info("Recherche de réalisateurs.");

	final String nomSpielberg = "Steven Spielberg";
	final Director spielberg =
		gestionnaire.supplyDirector(nomSpielberg);
	assertNotNull(spielberg);
	assertNotNull(spielberg.getId());
	assertEquals(nomSpielberg, spielberg.getName());

	final String nomPeterJackson = "Peter Jackson";
	final Director peterJackson =
		gestionnaire.supplyDirector(nomPeterJackson);
	assertNotNull(peterJackson);
	assertEquals(nomPeterJackson, peterJackson.getName());

	assertSame(spielberg, gestionnaire.supplyDirector(nomSpielberg));
	assertSame(peterJackson,
		gestionnaire.supplyDirector(nomPeterJackson));
    }

    /**
     * Teste la méthode {@link RegistreEntityManager#supplyScriptWriter(String)
     * supplyScriptWriter(String)}.
     */
    @Test
    public void fournirScénariste() {
	logger.info("Recherche de scénaristes.");

	final String nomMorvan = "Jean-David Morvan";
	final ScriptWriter morvan = gestionnaire.supplyScriptWriter(nomMorvan);
	assertNotNull(morvan);
	assertEquals(nomMorvan, morvan.getName());

	final String nomRenard = "Renard";
	final ScriptWriter renard = gestionnaire.supplyScriptWriter(nomRenard);
	assertNotNull(renard);
	assertNotNull(renard.getId());
	assertEquals(nomRenard, renard.getName());

	assertSame(morvan, gestionnaire.supplyScriptWriter(nomMorvan));
	assertSame(renard, gestionnaire.supplyScriptWriter(nomRenard));
    }

    /**
     * Teste la méthode {@link RegistreEntityManager#supplySeries(String)
     * supplySeries(String)}.
     */
    @Test
    public void fournirSérie() {
	logger.info("Recherche de séries.");

	final String nomMerlin = "Merlin";
	final Series merlin = gestionnaire.supplySeries(nomMerlin);
	assertNotNull(merlin);
	assertNotNull(merlin.getId());
	assertEquals(nomMerlin, merlin.getName());

	final String nomNcis = "NCIS";
	final Series ncis = gestionnaire.supplySeries(nomNcis);
	assertNotNull(ncis);
	assertEquals(nomNcis, ncis.getName());

	assertSame(merlin, gestionnaire.supplySeries(nomMerlin));
	assertSame(ncis, gestionnaire.supplySeries(nomNcis));
    }

}
