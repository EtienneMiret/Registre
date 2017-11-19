package fr.elimerl.registre.services;

import static fr.elimerl.registre.entities.Reference.Field.OTHER;
import static fr.elimerl.registre.entities.Reference.Field.COMMENT;
import static fr.elimerl.registre.entities.Reference.Field.TITLE;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import fr.elimerl.registre.entities.Record;
import fr.elimerl.registre.entities.Reference;
import fr.elimerl.registre.search.tokens.Keyword;

/**
 * Service responsible for indexing records.
 */
public class Index {

    /**
     * The JPA entity manager, supplied by the container.
     */
    @PersistenceContext(name = "Registre")
    private EntityManager em;

    /**
     * The Registre entity manager, supplied by the container.
     */
    @Resource(name = "gestionnaireEntités")
    private RegistreEntityManager rem;

    /**
     * Reindex the given record. The existing index is deleted and a new one
     * is created.
     *
     * @param record
     *          the record to index.
     */
    public void reindex(final Record record) {
	final Query unindex = em.createNamedQuery("unindexRecord");
	unindex.setParameter("record", record);
	unindex.executeUpdate();
	if (record.getTitle() != null) {
	    for (final String mot : cutInWords(record.getTitle())) {
		em.persist(new Reference(rem.supplyWord(mot), TITLE, record));
	    }
	}
	if (record.getComment() != null) {
	    for (final String mot : cutInWords(record.getComment())) {
		em.persist(new Reference(rem.supplyWord(mot),
				COMMENT, record));
	    }
	}
	final Set<String> words = new HashSet<String>();
	for (final String string : record.getOtherFields()) {
	    words.addAll(cutInWords(string));
	}
	for (final String mot : words) {
	    em.persist(new Reference(rem.supplyWord(mot), OTHER, record));
	}
    }

    /**
     * Cut the given string in a set of words.
     *
     * @param string
     *          the string to cut in words.
     * @return the words in the specified string, lowercase and without
     *          punctuation.
     */
    public Set<String> cutInWords(final String string) {
	final Matcher matcher = Keyword.PATTERN.matcher(string);
	final Set<String> result = new HashSet<String>();
	int i = 0;
	while (i < string.length()) {
	    if (matcher.find(i)) {
		result.add(matcher.group(1).toLowerCase());
		i = matcher.end();
	    } else {
		i++;
	    }
	}
	return result;
    }

}
