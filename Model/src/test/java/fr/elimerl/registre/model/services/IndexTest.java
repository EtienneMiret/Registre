package fr.elimerl.registre.model.services;

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
 * Test class for the {@link Index} class.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext.xml")
@Transactional("gestionnaireTransactions")
public class IndexTest {

    /** Zero, as a {@code Long}. */
    private static final Long ZERO = Long.valueOf(0L);

    /** The number one, as a {@code Long}. */
    private static final Long ONE = Long.valueOf(1L);

    /** The number two, as a {@code Long}. */
    private static final Long TWO = Long.valueOf(2L);

    /**
     * The entity manager. Provided by Spring.
     */
    @Resource(name = "gestionnaireEntités")
    private RegistreEntityManager entityManager;

    /**
     * The indexation service under test. Provided by Spring.
     */
    @Resource(name = "indexeur")
    private Index index;

    /**
     * Test the {@link Index#reindex(Record)} method on the record 0 (a
     * “Boule et Bill” comic).
     */
    @Test
    public void reindexBouleEtBill() {
	final EntityManager em = entityManager.getJpaEntityManager();
	final Record bouleEtBill = em.find(Record.class, ZERO);

	final Set<Reference> expected = new HashSet<Reference>();
	expected.add(createReference("bd", OTHER, bouleEtBill));
	expected.add(createReference("bande", OTHER, bouleEtBill));
	expected.add(createReference("déssinée", OTHER, bouleEtBill));
	expected.add(createReference("globe", TITLE, bouleEtBill));
	expected.add(createReference("trotters", TITLE, bouleEtBill));
	expected.add(createReference("boule", OTHER, bouleEtBill));
	expected.add(createReference("et", OTHER, bouleEtBill));
	expected.add(createReference("bill", OTHER, bouleEtBill));
	expected.add(createReference("claire", OTHER, bouleEtBill));
	expected.add(createReference("verneuil", OTHER, bouleEtBill));
	expected.add(createReference("etienne", OTHER, bouleEtBill));
	expected.add(createReference("grégoire", OTHER, bouleEtBill));
	expected.add(createReference("jigounov", OTHER, bouleEtBill));
	expected.add(createReference("renard", OTHER, bouleEtBill));
	expected.add(createReference("12", OTHER, bouleEtBill));

	index.reindex(bouleEtBill);
	assertEquals(expected, loadReferences(bouleEtBill));
    }

    /**
     * Test the {@link Index#reindex(Record)} method on the record 1 (season
     * 1 of Merlin series).
     */
    @Test
    public void reindexMerlin() {
	final EntityManager em = entityManager.getJpaEntityManager();
	final Record merlin = em.find(Record.class, ONE);

	final Set<Reference> expected = new HashSet<Reference>();
	expected.add(createReference("film", OTHER, merlin));
	expected.add(createReference("merlin", TITLE, merlin));
	expected.add(createReference("saison", TITLE, merlin));
	expected.add(createReference("1", TITLE, merlin));
	expected.add(createReference("merlin", OTHER, merlin));
	expected.add(createReference("une", COMMENT, merlin));
	expected.add(createReference("super", COMMENT, merlin));
	expected.add(createReference("série", COMMENT, merlin));
	expected.add(createReference("etienne", OTHER, merlin));
	expected.add(createReference("verneuil", OTHER, merlin));
	expected.add(createReference("claire", OTHER, merlin));
	expected.add(createReference("blu", OTHER, merlin));
	expected.add(createReference("ray", OTHER, merlin));
	expected.add(createReference("howard", OTHER, merlin));
	expected.add(createReference("shore", OTHER, merlin));
	expected.add(createReference("will", OTHER, merlin));
	expected.add(createReference("smith", OTHER, merlin));
	expected.add(createReference("emma", OTHER, merlin));
	expected.add(createReference("watson", OTHER, merlin));

	index.reindex(merlin);
	assertEquals(expected, loadReferences(merlin));
    }

    /**
     * Test the {@link Index#reindex(Record)} method on the record 2 (a Tom
     * Clancy book).
     */
    @Test
    public void reindexRainbowSix() {
	final EntityManager em = entityManager.getJpaEntityManager();
	final Record rainbowSix = em.find(Record.class, TWO);

	final Set<Reference> expected = new HashSet<Reference>();
	expected.add(createReference("livre", OTHER, rainbowSix));
	expected.add(createReference("rainbow", TITLE, rainbowSix));
	expected.add(createReference("six", TITLE, rainbowSix));
	expected.add(createReference("etienne", OTHER, rainbowSix));
	expected.add(createReference("verneuil", OTHER, rainbowSix));
	expected.add(createReference("tom", OTHER, rainbowSix));
	expected.add(createReference("clancy", OTHER, rainbowSix));

	index.reindex(rainbowSix);
	assertEquals(expected, loadReferences(rainbowSix));
    }

    /**
     * Load all references linked to the given record.
     *
     * @param record
     *          record whose references are wanted.
     * @return the set of references of the given record.
     */
    private Set<Reference> loadReferences(final Record record) {
	final EntityManager em = entityManager.getJpaEntityManager();
	final CriteriaBuilder builder = em.getCriteriaBuilder();
	final CriteriaQuery<Reference> query =
		builder.createQuery(Reference.class);
	final Root<Reference> root = query.from(Reference.class);
	query.where(builder.equal(root.get("record"), record));
	final List<Reference> list = em.createQuery(query).getResultList();
	return new HashSet<Reference>(list);
    }

    /**
     * Create a new reference with the given parameters. The given word (of type
     * {@link String} is converted into a {@link Word}.
     *
     * @param word
     *          the word to reference.
     * @param field
     *          the field where the specified word is.
     * @param record
     *          the record where the specified word is.
     * @return a new reference that states that the given word is in the given
     *          field of the given record.
     */
    private Reference createReference(final String word, final Field field,
	    final Record record) {
	return new Reference(entityManager.supplyWord(word), field, record);
    }

}
