package fr.elimerl.registre.modèle.services;

import static fr.elimerl.registre.modèle.entités.Référence.Champ.AUTRE;
import static fr.elimerl.registre.modèle.entités.Référence.Champ.COMMENTAIRE;
import static fr.elimerl.registre.modèle.entités.Référence.Champ.TITRE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import fr.elimerl.registre.modèle.entités.Acteur;
import fr.elimerl.registre.modèle.entités.Auteur;
import fr.elimerl.registre.modèle.entités.Compositeur;
import fr.elimerl.registre.modèle.entités.Dessinateur;
import fr.elimerl.registre.modèle.entités.Emplacement;
import fr.elimerl.registre.modèle.entités.Fiche;
import fr.elimerl.registre.modèle.entités.Mot;
import fr.elimerl.registre.modèle.entités.Propriétaire;
import fr.elimerl.registre.modèle.entités.Réalisateur;
import fr.elimerl.registre.modèle.entités.Référence;
import fr.elimerl.registre.modèle.entités.Référence.Champ;
import fr.elimerl.registre.modèle.entités.Scénariste;
import fr.elimerl.registre.modèle.entités.Série;

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

    /** Zéro, en tant que {@code Long}. */
    private static final Long ZÉRO = Long.valueOf(0L);

    /** Le nombre un, en tant que {@code Long}. */
    private static final Long UN = Long.valueOf(1L);

    /** Le nombre deux, en tant que {@code Long}. */
    private static final Long DEUX = Long.valueOf(2L);

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

    /**
     * Teste la méthode {@link GestionnaireEntités#réindexer(Fiche)
     * réindexer(Fiche)} sur la fiche 0 (une bande-dessinée de Boule et Bill).
     */
    @Test
    @Transactional
    public void réindexerBouleEtBill() {
	final EntityManager em = gestionnaire.getGestionnaireNatif();
	final Fiche bouleEtBill = em.find(Fiche.class, ZÉRO);

	final Set<Référence> référencesAttendues = new HashSet<Référence>();
	référencesAttendues.add(créerRéférence("bd", AUTRE, bouleEtBill));
	référencesAttendues.add(créerRéférence("bande", AUTRE, bouleEtBill));
	référencesAttendues.add(créerRéférence("déssinée", AUTRE, bouleEtBill));
	référencesAttendues.add(créerRéférence("globe", TITRE, bouleEtBill));
	référencesAttendues.add(créerRéférence("trotters", TITRE, bouleEtBill));
	référencesAttendues.add(créerRéférence("boule", AUTRE, bouleEtBill));
	référencesAttendues.add(créerRéférence("et", AUTRE, bouleEtBill));
	référencesAttendues.add(créerRéférence("bill", AUTRE, bouleEtBill));
	référencesAttendues.add(créerRéférence("claire", AUTRE, bouleEtBill));
	référencesAttendues.add(créerRéférence("verneuil", AUTRE, bouleEtBill));
	référencesAttendues.add(créerRéférence("etienne", AUTRE, bouleEtBill));
	référencesAttendues.add(créerRéférence("grégoire", AUTRE, bouleEtBill));
	référencesAttendues.add(créerRéférence("jigounov", AUTRE, bouleEtBill));
	référencesAttendues.add(créerRéférence("jean", AUTRE, bouleEtBill));
	référencesAttendues.add(créerRéférence("van", AUTRE, bouleEtBill));
	référencesAttendues.add(créerRéférence("hamme", AUTRE, bouleEtBill));
	référencesAttendues.add(créerRéférence("12", AUTRE, bouleEtBill));

	gestionnaire.réindexer(bouleEtBill);
	assertEquals(référencesAttendues, chargerRéférences(bouleEtBill));
    }

    /**
     * Teste la méthode {@link GestionnaireEntités#réindexer(Fiche)
     * réindexer(Fiche)} sur la fiche 1 (la saison 1 de la série Merlin).
     */
    @Test
    public void réindexerMerlin() {
	final EntityManager em = gestionnaire.getGestionnaireNatif();
	final Fiche merlin = em.find(Fiche.class, UN);

	final Set<Référence> référencesAttendues = new HashSet<Référence>();
	référencesAttendues.add(créerRéférence("film", AUTRE, merlin));
	référencesAttendues.add(créerRéférence("merlin", TITRE, merlin));
	référencesAttendues.add(créerRéférence("saison", TITRE, merlin));
	référencesAttendues.add(créerRéférence("1", TITRE, merlin));
	référencesAttendues.add(créerRéférence("merlin", AUTRE, merlin));
	référencesAttendues.add(créerRéférence("une", COMMENTAIRE, merlin));
	référencesAttendues.add(créerRéférence("super", COMMENTAIRE, merlin));
	référencesAttendues.add(créerRéférence("série", TITRE, merlin));
	référencesAttendues.add(créerRéférence("etienne", AUTRE, merlin));
	référencesAttendues.add(créerRéférence("verneuil", AUTRE, merlin));
	référencesAttendues.add(créerRéférence("claire", AUTRE, merlin));
	référencesAttendues.add(créerRéférence("blu-ray", AUTRE, merlin));
	référencesAttendues.add(créerRéférence("howard", AUTRE, merlin));
	référencesAttendues.add(créerRéférence("shore", AUTRE, merlin));
	référencesAttendues.add(créerRéférence("will", AUTRE, merlin));
	référencesAttendues.add(créerRéférence("smith", AUTRE, merlin));
	référencesAttendues.add(créerRéférence("emma", AUTRE, merlin));
	référencesAttendues.add(créerRéférence("watson", AUTRE, merlin));

	gestionnaire.réindexer(merlin);
	assertEquals(référencesAttendues, chargerRéférences(merlin));
    }

    /**
     * Teste la méthode {@link GestionnaireEntités#réindexer(Fiche)
     * réindexer(Fiche)} sur la fiche 2 (un livre de Tom Clancy).
     */
    @Test
    public void réindexerRainbowSix() {
	final EntityManager em = gestionnaire.getGestionnaireNatif();
	final Fiche rainbowSix = em.find(Fiche.class, DEUX);

	final Set<Référence> référencesAttendues = new HashSet<Référence>();
	référencesAttendues.add(créerRéférence("livre", AUTRE, rainbowSix));
	référencesAttendues.add(créerRéférence("rainbow", TITRE, rainbowSix));
	référencesAttendues.add(créerRéférence("six", TITRE, rainbowSix));
	référencesAttendues.add(créerRéférence("etienne", AUTRE, rainbowSix));
	référencesAttendues.add(créerRéférence("verneuil", AUTRE, rainbowSix));
	référencesAttendues.add(créerRéférence("tom", AUTRE, rainbowSix));
	référencesAttendues.add(créerRéférence("clancy", AUTRE, rainbowSix));

	gestionnaire.réindexer(rainbowSix);
	assertEquals(référencesAttendues, chargerRéférences(rainbowSix));
    }

    /**
     * Charge toutes les références associées à la fiche données en paramètre.
     *
     * @param fiche
     *            la fiche dont on veut les références.
     * @return l’ensemble des référence de la fiche donnée.
     */
    private Set<Référence> chargerRéférences(final Fiche fiche) {
	final EntityManager em = gestionnaire.getGestionnaireNatif();
	final CriteriaBuilder constructeur = em.getCriteriaBuilder();
	final CriteriaQuery<Référence> requête =
		constructeur.createQuery(Référence.class);
	final Root<Référence> racine = requête.from(Référence.class);
	requête.where(constructeur.equal(racine.get("fiche"), fiche));
	final List<Référence> liste = em.createQuery(requête).getResultList();
	return new HashSet<Référence>(liste);
    }

    /**
     * Crée une nouvelle référence avec les paramètres donnés. Le mot donné (de
     * type {@link String}) est converti en {@link Mot}.
     *
     * @param mot
     *            le mot à référencer.
     * @param champ
     *            le champ dans lequel se trouve le mot à référencer.
     * @param fiche
     *            la fiche dans laquelle se trouve le mot à référencer.
     * @return une nouvelle référence indiquant la présence du mot donné dans le
     *         champ donné de la fiche donnée.
     */
    private Référence créerRéférence(final String mot, final Champ champ,
	    final Fiche fiche) {
	return new Référence(gestionnaire.fournirMot(mot), champ, fiche);
    }

}
