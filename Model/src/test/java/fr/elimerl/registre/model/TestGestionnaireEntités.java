package fr.elimerl.registre.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

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
     * Teste la méthode {@link GestionnaireEntités#chercherFiche(Integer)
     * chercherFiche(Integer)}.
     */
    @Test
    public void chercherFiche() {
	logger.info("Recherche de fiches.");

	final Long idGlobeTrotters = Long.valueOf(0);
	final Fiche globTrotters = gestionnaire.chercherFiche(idGlobeTrotters);
	assertNotNull(globTrotters);
	assertEquals(idGlobeTrotters, globTrotters.getId());
	assertTrue(globTrotters instanceof BandeDessinée);

	final Long idMerlin = Long.valueOf(1);
	final Fiche merlin = gestionnaire.chercherFiche(idMerlin);
	assertNotNull(merlin);
	assertEquals(idMerlin, merlin.getId());
	assertTrue(merlin instanceof Film);

	final Fiche vide = gestionnaire.chercherFiche(Long.valueOf(2));
	assertNull(vide);
    }

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
	assertNull(bonjour.getId());
	assertEquals(chaîneBonjour, bonjour.getValeur());
    }

    /**
     * Teste la méthode {@link GestionnaireEntités#fournirActeur(String)
     * fournirActeur(String)}.
     */
    @Test
    public void fournirActeur() {
	logger.info("Recherche d’acteurs.");

	final String nomEvaMendes = "Eva Mendes";
	final Acteur evaMendes = gestionnaire.fournirActeur(nomEvaMendes);
	assertNotNull(evaMendes);
	assertNotNull(evaMendes.getId());
	assertEquals(nomEvaMendes, evaMendes.getNom());

	final String nomWillSmith = "Will Smith";
	final Acteur willSmith = gestionnaire.fournirActeur(nomWillSmith);
	assertNotNull(willSmith);
	assertNotNull(willSmith.getId());
	assertEquals(nomWillSmith, willSmith.getNom());

	final String nomFunès = "Louis de Funès";
	final Acteur funès = gestionnaire.fournirActeur(nomFunès);
	assertNotNull(funès);
	assertNull(funès.getId());
	assertEquals(nomFunès, funès.getNom());
    }

    /**
     * Teste la méthode {@link GestionnaireEntités#fournirAuteur(String)
     * fournirAuteur(String)}.
     */
    @Test
    public void fournirAuteur() {
	logger.info("Recherche d’auteurs.");

	final String nomHerbert = "Frank Hebert";
	final Auteur herbert = gestionnaire.fournirAuteur(nomHerbert);
	assertNotNull(herbert);
	assertNull(herbert.getId());
	assertEquals(nomHerbert, herbert.getNom());

	final String nomAsimov = "Isaac Asimov";
	final Auteur asimov = gestionnaire.fournirAuteur(nomAsimov);
	assertNotNull(asimov);
	assertNotNull(asimov.getId());
	assertEquals(nomAsimov, asimov.getNom());

	final String nomTolkien = "J.R.R. Tolkien";
	final Auteur tolkien = gestionnaire.fournirAuteur(nomTolkien);
	assertNotNull(tolkien);
	assertNull(tolkien.getId());
	assertEquals(nomTolkien, tolkien.getNom());
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
	assertNull(johnWilliams.getId());
	assertEquals(nomJohnWilliams, johnWilliams.getNom());
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
	assertNull(philippeBuchet.getId());
	assertEquals(nomPhilippeBuchet, philippeBuchet.getNom());

	final String nomJigounov = "Jigounov";
	final Dessinateur jigounov =
		gestionnaire.fournirDessinateur(nomJigounov);
	assertNotNull(jigounov);
	assertNotNull(jigounov.getId());
	assertEquals(nomJigounov, jigounov.getNom());
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
	assertNull(marcigny.getId());
	assertEquals(nomMarcigny, marcigny.getNom());

	final String nomLyon = "Lyon";
	final Emplacement lyon = gestionnaire.fournirEmplacement(nomLyon);
	assertNotNull(lyon);
	assertNotNull(lyon.getId());
	assertEquals(nomLyon, lyon.getNom());
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
	assertNull(eloi.getId());
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
	assertNull(quentin.getId());
	assertEquals(nomQuentin, quentin.getNom());
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
	assertNull(peterJackson.getId());
	assertEquals(nomPeterJackson, peterJackson.getNom());
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
	assertNull(morvan.getId());
	assertEquals(nomMorvan, morvan.getNom());

	final String nomRenard = "Renard";
	final Scénariste renard = gestionnaire.fournirScénariste(nomRenard);
	assertNotNull(renard);
	assertNotNull(renard.getId());
	assertEquals(nomRenard, renard.getNom());
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
	assertNull(ncis.getId());
	assertEquals(nomNcis, ncis.getNom());
    }

}
