package fr.elimerl.registre.modèle.services;

import static fr.elimerl.registre.modèle.entités.Référence.Champ.AUTRE;
import static fr.elimerl.registre.modèle.entités.Référence.Champ.COMMENTAIRE;
import static fr.elimerl.registre.modèle.entités.Référence.Champ.TITRE;
import static org.junit.Assert.assertEquals;

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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import fr.elimerl.registre.modèle.entités.Fiche;
import fr.elimerl.registre.modèle.entités.Mot;
import fr.elimerl.registre.modèle.entités.Référence;
import fr.elimerl.registre.modèle.entités.Référence.Champ;

/**
 * Classe de test pour l’{@link Indexeur}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext.xml")
@Transactional("gestionnaireTransactions")
public class TestIndexeur {

    /** Zéro, en tant que {@code Long}. */
    private static final Long ZÉRO = Long.valueOf(0L);

    /** Le nombre un, en tant que {@code Long}. */
    private static final Long UN = Long.valueOf(1L);

    /** Le nombre deux, en tant que {@code Long}. */
    private static final Long DEUX = Long.valueOf(2L);

    /**
     * Gestionnaire d’entités fournit par Spring.
     */
    @Resource(name = "gestionnaireEntités")
    private GestionnaireEntités gestionnaire;

    /**
     * Le service d’indexation testé, fournit par Spring.
     */
    @Resource(name = "indexeur")
    private Indexeur indexeur;

    /**
     * Teste la méthode {@link GestionnaireEntités#réindexer(Fiche)
     * réindexer(Fiche)} sur la fiche 0 (une bande-dessinée de Boule et Bill).
     */
    @Test
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
	référencesAttendues.add(créerRéférence("renard", AUTRE, bouleEtBill));
	référencesAttendues.add(créerRéférence("12", AUTRE, bouleEtBill));

	indexeur.réindexer(bouleEtBill);
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
	référencesAttendues.add(créerRéférence("série", COMMENTAIRE, merlin));
	référencesAttendues.add(créerRéférence("etienne", AUTRE, merlin));
	référencesAttendues.add(créerRéférence("verneuil", AUTRE, merlin));
	référencesAttendues.add(créerRéférence("claire", AUTRE, merlin));
	référencesAttendues.add(créerRéférence("blu", AUTRE, merlin));
	référencesAttendues.add(créerRéférence("ray", AUTRE, merlin));
	référencesAttendues.add(créerRéférence("howard", AUTRE, merlin));
	référencesAttendues.add(créerRéférence("shore", AUTRE, merlin));
	référencesAttendues.add(créerRéférence("will", AUTRE, merlin));
	référencesAttendues.add(créerRéférence("smith", AUTRE, merlin));
	référencesAttendues.add(créerRéférence("emma", AUTRE, merlin));
	référencesAttendues.add(créerRéférence("watson", AUTRE, merlin));

	indexeur.réindexer(merlin);
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

	indexeur.réindexer(rainbowSix);
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
