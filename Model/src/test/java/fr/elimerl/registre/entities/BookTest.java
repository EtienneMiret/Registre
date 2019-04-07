package fr.elimerl.registre.entities;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

/**
 * Test class for the {@link Book} class.
 */
public class BookTest {

    /** This class’ logger. */
    private static final Logger logger =
	    LoggerFactory.getLogger(BookTest.class);

    /** The {@link Book} under test. */
    private Book book;

    /**
     * Setup test environment.
     */
    @Before
    public void setUp() {
	book = new Book("L’Assassin royal", null);
    }

    /**
     * Test the {@link Book#getAuthor()} and {@link Book#setAuthor(Author)}
     * methods.
     */
    @Test
    public void author() {
	logger.info("Author test.");

	final Author author1 = new Author("Robin Hobb");
	final Author author2 = new Author("Tom Clancy");

	book.setAuthor(author1);
	assertEquals(author1, book.getAuthor());

	book.setAuthor(author2);
	assertEquals(author2, book.getAuthor());
    }

}
