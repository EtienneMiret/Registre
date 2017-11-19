package fr.elimerl.registre.modèle.services;

import static fr.elimerl.registre.entities.Reference.Field.OTHER;
import static fr.elimerl.registre.entities.Reference.Field.COMMENT;
import static fr.elimerl.registre.entities.Reference.Field.TITLE;
import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fr.elimerl.registre.entities.Record;
import fr.elimerl.registre.entities.Word;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import fr.elimerl.registre.entities.Reference;
import fr.elimerl.registre.entities.Reference.Field;
import fr.elimerl.registre.services.RegistreEntityManager;
import fr.elimerl.registre.services.Index;

/**
 * Classe de test pour l’{@link Index}.
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
    private RegistreEntityManager gestionnaire;

    /**
     * Le service d’indexation testé, fournit par Spring.
     */
    @Resource(name = "indexeur")
    private Index indexeur;

    /**
     * Teste la méthode {@link RegistreEntityManager#réindexer(Record)
     * reindex(Record)} sur la fiche 0 (une bande-dessinée de Boule et Bill).
     */
    @Test
    public void réindexerBouleEtBill() {
	final EntityManager em = gestionnaire.getJpaEntityManager();
	final Record bouleEtBill = em.find(Record.class, ZÉRO);

	final Set<Reference> référencesAttendues = new HashSet<Reference>();
	référencesAttendues.add(créerRéférence("bd", OTHER, bouleEtBill));
	référencesAttendues.add(créerRéférence("bande", OTHER, bouleEtBill));
	référencesAttendues.add(créerRéférence("déssinée", OTHER, bouleEtBill));
	référencesAttendues.add(créerRéférence("globe", TITLE, bouleEtBill));
	référencesAttendues.add(créerRéférence("trotters", TITLE, bouleEtBill));
	référencesAttendues.add(créerRéférence("boule", OTHER, bouleEtBill));
	référencesAttendues.add(créerRéférence("et", OTHER, bouleEtBill));
	référencesAttendues.add(créerRéférence("bill", OTHER, bouleEtBill));
	référencesAttendues.add(créerRéférence("claire", OTHER, bouleEtBill));
	référencesAttendues.add(créerRéférence("verneuil", OTHER, bouleEtBill));
	référencesAttendues.add(créerRéférence("etienne", OTHER, bouleEtBill));
	référencesAttendues.add(créerRéférence("grégoire", OTHER, bouleEtBill));
	référencesAttendues.add(créerRéférence("jigounov", OTHER, bouleEtBill));
	référencesAttendues.add(créerRéférence("renard", OTHER, bouleEtBill));
	référencesAttendues.add(créerRéférence("12", OTHER, bouleEtBill));

	indexeur.reindex(bouleEtBill);
	assertEquals(référencesAttendues, chargerRéférences(bouleEtBill));
    }

    /**
     * Teste la méthode {@link RegistreEntityManager#réindexer(Record)
     * reindex(Record)} sur la fiche 1 (la saison 1 de la série Merlin).
     */
    @Test
    public void réindexerMerlin() {
	final EntityManager em = gestionnaire.getJpaEntityManager();
	final Record merlin = em.find(Record.class, UN);

	final Set<Reference> référencesAttendues = new HashSet<Reference>();
	référencesAttendues.add(créerRéférence("film", OTHER, merlin));
	référencesAttendues.add(créerRéférence("merlin", TITLE, merlin));
	référencesAttendues.add(créerRéférence("saison", TITLE, merlin));
	référencesAttendues.add(créerRéférence("1", TITLE, merlin));
	référencesAttendues.add(créerRéférence("merlin", OTHER, merlin));
	référencesAttendues.add(créerRéférence("une", COMMENT, merlin));
	référencesAttendues.add(créerRéférence("super", COMMENT, merlin));
	référencesAttendues.add(créerRéférence("série", COMMENT, merlin));
	référencesAttendues.add(créerRéférence("etienne", OTHER, merlin));
	référencesAttendues.add(créerRéférence("verneuil", OTHER, merlin));
	référencesAttendues.add(créerRéférence("claire", OTHER, merlin));
	référencesAttendues.add(créerRéférence("blu", OTHER, merlin));
	référencesAttendues.add(créerRéférence("ray", OTHER, merlin));
	référencesAttendues.add(créerRéférence("howard", OTHER, merlin));
	référencesAttendues.add(créerRéférence("shore", OTHER, merlin));
	référencesAttendues.add(créerRéférence("will", OTHER, merlin));
	référencesAttendues.add(créerRéférence("smith", OTHER, merlin));
	référencesAttendues.add(créerRéférence("emma", OTHER, merlin));
	référencesAttendues.add(créerRéférence("watson", OTHER, merlin));

	indexeur.reindex(merlin);
	assertEquals(référencesAttendues, chargerRéférences(merlin));
    }

    /**
     * Teste la méthode {@link RegistreEntityManager#réindexer(Record)
     * reindex(Record)} sur la fiche 2 (un livre de Tom Clancy).
     */
    @Test
    public void réindexerRainbowSix() {
	final EntityManager em = gestionnaire.getJpaEntityManager();
	final Record rainbowSix = em.find(Record.class, DEUX);

	final Set<Reference> référencesAttendues = new HashSet<Reference>();
	référencesAttendues.add(créerRéférence("livre", OTHER, rainbowSix));
	référencesAttendues.add(créerRéférence("rainbow", TITLE, rainbowSix));
	référencesAttendues.add(créerRéférence("six", TITLE, rainbowSix));
	référencesAttendues.add(créerRéférence("etienne", OTHER, rainbowSix));
	référencesAttendues.add(créerRéférence("verneuil", OTHER, rainbowSix));
	référencesAttendues.add(créerRéférence("tom", OTHER, rainbowSix));
	référencesAttendues.add(créerRéférence("clancy", OTHER, rainbowSix));

	indexeur.reindex(rainbowSix);
	assertEquals(référencesAttendues, chargerRéférences(rainbowSix));
    }

    /**
     * Charge toutes les références associées à la fiche données en paramètre.
     *
     * @param fiche
     *            la fiche dont on veut les références.
     * @return l’ensemble des référence de la fiche donnée.
     */
    private Set<Reference> chargerRéférences(final Record fiche) {
	final EntityManager em = gestionnaire.getJpaEntityManager();
	final CriteriaBuilder constructeur = em.getCriteriaBuilder();
	final CriteriaQuery<Reference> requête =
		constructeur.createQuery(Reference.class);
	final Root<Reference> racine = requête.from(Reference.class);
	requête.where(constructeur.equal(racine.get("record"), fiche));
	final List<Reference> liste = em.createQuery(requête).getResultList();
	return new HashSet<Reference>(liste);
    }

    /**
     * Crée une nouvelle référence avec les paramètres donnés. Le mot donné (de
     * type {@link String}) est converti en {@link Word}.
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
    private Reference créerRéférence(final String mot, final Field champ,
	    final Record fiche) {
	return new Reference(gestionnaire.supplyWord(mot), champ, fiche);
    }

}
