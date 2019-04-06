package fr.elimerl.registre.services;

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

/**
 * Test class for the {@link RegistreEntityManager} class.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext.xml")
@Transactional("transactionManager")
public class RegistreEntityManagerTest {

    /** This class’ logger. */
    private static final Logger logger =
	    LoggerFactory.getLogger(RegistreEntityManagerTest.class);

    /** The entity manager under test, provided by Spring. */
    @Resource(name = "registreEntityManager")
    private RegistreEntityManager entityManager;

    /**
     * Testing the {@link RegistreEntityManager#supplyWord(String)} method.
     */
    @Test
    public void supplyWord() {
	logger.info("Lookup words.");

	final String uneString = "une";
	final Word une = entityManager.supplyWord(uneString);
	assertNotNull(une);
	assertNotNull(une.getId());
	assertEquals(uneString, une.getValue());

	final String superString = "super";
	final Word super_ = entityManager.supplyWord(superString);
	assertNotNull(super_);
	assertNotNull(super_.getId());
	assertEquals(superString, super_.getValue());

	final String serieString = "série";
	final Word serie = entityManager.supplyWord(serieString);
	assertNotNull(serie);
	assertNotNull(serie.getId());
	assertEquals(serieString, serie.getValue());

	final String bonjourString = "bonjour";
	final Word bonjour = entityManager.supplyWord(bonjourString);
	assertNotNull(bonjour);
	assertEquals(bonjourString, bonjour.getValue());

	assertSame(une, entityManager.supplyWord(uneString));
	assertSame(super_, entityManager.supplyWord(superString));
	assertSame(serie, entityManager.supplyWord(serieString));
	assertSame(bonjour, entityManager.supplyWord(bonjourString));
    }

    /**
     * Testing the {@link RegistreEntityManager#supplyActor(String)} method.
     */
    @Test
    public void supplyActor() {
	logger.info("Lookup actors.");

	final String evaMendesName = "Eva Mendes";
	final Actor evaMendes = entityManager.supplyActor(evaMendesName);
	assertNotNull(evaMendes);
	assertNotNull(evaMendes.getId());
	assertEquals(evaMendesName, evaMendes.getName());

	final String willSmithName = "Will Smith";
	final Actor willSmith = entityManager.supplyActor(willSmithName);
	assertNotNull(willSmith);
	assertNotNull(willSmith.getId());
	assertEquals(willSmithName, willSmith.getName());

	final String funesName = "Louis de Funès";
	final Actor funes = entityManager.supplyActor(funesName);
	assertNotNull(funes);
	assertEquals(funesName, funes.getName());

	assertSame(evaMendes, entityManager.supplyActor(evaMendesName));
	assertSame(willSmith, entityManager.supplyActor(willSmithName));
	assertSame(funes, entityManager.supplyActor(funesName));
    }

    /**
     * Testing the {@link RegistreEntityManager#supplyAuthor(String)} method.
     */
    @Test
    public void supplyAuthor() {
	logger.info("Lookup authors.");

	final String herbertName = "Frank Hebert";
	final Author herbert = entityManager.supplyAuthor(herbertName);
	assertNotNull(herbert);
	assertEquals(herbertName, herbert.getName());

	final String asimovName = "Isaac Asimov";
	final Author asimov = entityManager.supplyAuthor(asimovName);
	assertNotNull(asimov);
	assertNotNull(asimov.getId());
	assertEquals(asimovName, asimov.getName());

	final String tolkienName = "J.R.R. Tolkien";
	final Author tolkien = entityManager.supplyAuthor(tolkienName);
	assertNotNull(tolkien);
	assertEquals(tolkienName, tolkien.getName());

	assertSame(herbert, entityManager.supplyAuthor(herbertName));
	assertSame(asimov, entityManager.supplyAuthor(asimovName));
	assertSame(tolkien, entityManager.supplyAuthor(tolkienName));
    }

    /**
     * Testing the {@link RegistreEntityManager#supplyComposer(String)} method.
     */
    @Test
    public void supplyComposer() {
	logger.info("Lookup composers.");

	final String howardShoreName = "Howard Shore";
	final Composer howardShore =
		entityManager.supplyComposer(howardShoreName);
	assertNotNull(howardShore);
	assertNotNull(howardShore.getId());
	assertEquals(howardShoreName, howardShore.getName());

	final String hansZimmerName = "Hans Zimmer";
	final Composer hansZimmer =
		entityManager.supplyComposer(hansZimmerName);
	assertNotNull(hansZimmer);
	assertNotNull(hansZimmer.getId());
	assertEquals(hansZimmerName, hansZimmer.getName());

	final String johnWilliamsName = "John Williams";
	final Composer johnWilliams =
		entityManager.supplyComposer(johnWilliamsName);
	assertNotNull(johnWilliams);
	assertEquals(johnWilliamsName, johnWilliams.getName());

	assertSame(howardShore,
		entityManager.supplyComposer(howardShoreName));
	assertSame(hansZimmer, entityManager.supplyComposer(hansZimmerName));
	assertSame(johnWilliams,
		entityManager.supplyComposer(johnWilliamsName));
    }

    /**
     * Testing the {@link RegistreEntityManager#supplyCartoonist(String)}
     * method.
     */
    @Test
    public void supplyCartoonist() {
	logger.info("Lookup cartoonists.");

	final String philippeBuchetName = "Philippe Buchet";
	final Cartoonist philippeBuchet =
		entityManager.supplyCartoonist(philippeBuchetName);
	assertNotNull(philippeBuchet);
	assertEquals(philippeBuchetName, philippeBuchet.getName());

	final String jigounovName = "Jigounov";
	final Cartoonist jigounov =
		entityManager.supplyCartoonist(jigounovName);
	assertNotNull(jigounov);
	assertNotNull(jigounov.getId());
	assertEquals(jigounovName, jigounov.getName());

	assertSame(philippeBuchet,
		entityManager.supplyCartoonist(philippeBuchetName));
	assertSame(jigounov, entityManager.supplyCartoonist(jigounovName));
    }

    /**
     * Testing the {@link RegistreEntityManager#supplyLocation(String)} method.
     */
    @Test
    public void supplyLocation() {
	logger.info("Lookup locations.");

	final String verneuilName = "Verneuil";
	final Location verneuil =
		entityManager.supplyLocation(verneuilName);
	assertNotNull(verneuil);
	assertNotNull(verneuil.getId());
	assertEquals(verneuilName, verneuil.getName());

	final String marcignyName = "Marcigny";
	final Location marcigny =
		entityManager.supplyLocation(marcignyName);
	assertNotNull(marcigny);
	assertEquals(marcignyName, marcigny.getName());

	final String lyonName = "Lyon";
	final Location lyon = entityManager.supplyLocation(lyonName);
	assertNotNull(lyon);
	assertNotNull(lyon.getId());
	assertEquals(lyonName, lyon.getName());

	assertSame(verneuil, entityManager.supplyLocation(verneuilName));
	assertSame(marcigny, entityManager.supplyLocation(marcignyName));
	assertSame(lyon, entityManager.supplyLocation(lyonName));
    }

    /**
     * Testing the {@link RegistreEntityManager#supplyOwner(String)} method.
     */
    @Test
    public void supplyOwner() {
	logger.info("Lookup owners.");

	final String eloiName = "Eloi";
	final Owner eloi = entityManager.supplyOwner(eloiName);
	assertNotNull(eloi);
	assertEquals(eloiName, eloi.getName());

	final String gregoireName = "Grégoire";
	final Owner gregoire = entityManager.supplyOwner(gregoireName);
	assertNotNull(gregoire);
	assertNotNull(gregoire.getId());
	assertEquals(gregoireName, gregoire.getName());

	final String quentinName = "Quentin";
	final Owner quentin = entityManager.supplyOwner(quentinName);
	assertNotNull(quentin);
	assertEquals(quentinName, quentin.getName());

	assertSame(eloi, entityManager.supplyOwner(eloiName));
	assertSame(gregoire, entityManager.supplyOwner(gregoireName));
	assertSame(quentin, entityManager.supplyOwner(quentinName));
    }

    /**
     * Testing the {@link RegistreEntityManager#supplyDirector(String)} method.
     */
    @Test
    public void supplyDirector() {
	logger.info("Lookup directors.");

	final String spielbergName = "Steven Spielberg";
	final Director spielberg = entityManager.supplyDirector(spielbergName);
	assertNotNull(spielberg);
	assertNotNull(spielberg.getId());
	assertEquals(spielbergName, spielberg.getName());

	final String peterJacksonName = "Peter Jackson";
	final Director peterJackson =
		entityManager.supplyDirector(peterJacksonName);
	assertNotNull(peterJackson);
	assertEquals(peterJacksonName, peterJackson.getName());

	assertSame(spielberg, entityManager.supplyDirector(spielbergName));
	assertSame(peterJackson,
		entityManager.supplyDirector(peterJacksonName));
    }

    /**
     * Testing the {@link RegistreEntityManager#supplyScriptWriter(String)}
     * method.
     */
    @Test
    public void supplyScriptWriter() {
	logger.info("Lookup script writers.");

	final String morvanName = "Jean-David Morvan";
	final ScriptWriter morvan =
                entityManager.supplyScriptWriter(morvanName);
	assertNotNull(morvan);
	assertEquals(morvanName, morvan.getName());

	final String renardName = "Renard";
	final ScriptWriter renard =
                entityManager.supplyScriptWriter(renardName);
	assertNotNull(renard);
	assertNotNull(renard.getId());
	assertEquals(renardName, renard.getName());

	assertSame(morvan, entityManager.supplyScriptWriter(morvanName));
	assertSame(renard, entityManager.supplyScriptWriter(renardName));
    }

    /**
     * Testing the {@link RegistreEntityManager#supplySeries(String)} method.
     */
    @Test
    public void supplySeries() {
	logger.info("Lookup series.");

	final String merlinName = "Merlin";
	final Series merlin = entityManager.supplySeries(merlinName);
	assertNotNull(merlin);
	assertNotNull(merlin.getId());
	assertEquals(merlinName, merlin.getName());

	final String ncisName = "NCIS";
	final Series ncis = entityManager.supplySeries(ncisName);
	assertNotNull(ncis);
	assertEquals(ncisName, ncis.getName());

	assertSame(merlin, entityManager.supplySeries(merlinName));
	assertSame(ncis, entityManager.supplySeries(ncisName));
    }

}
