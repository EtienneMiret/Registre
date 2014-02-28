package fr.elimerl.registre.reprise;

import java.io.FileNotFoundException;
import java.sql.SQLException;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Classe principale du programme de reprise de données.
 */
@Service("main")
public class RepriseDeDonnées {

    /** Journal SLF4J de cette classe. */
    private static final Logger journal =
	    LoggerFactory.getLogger(RepriseDeDonnées.class);

    /**
     * Méthode statique chargée de lancer l’application.
     *
     * @param args
     *            inutilisé.
     */
    public static void main(final String[] args) {
	ClassPathXmlApplicationContext ctx = null;
	try {
	    ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
	    final RepriseDeDonnées main =
		    ctx.getBean("main", RepriseDeDonnées.class);
	    main.traiterToutesLesFiches();
	} catch (final BeanInitializationException e) {
	    journal.error("Impossible d’instancier le contexte.", e);
	    if (e.getCause() instanceof FileNotFoundException) {
		System.err.println("Un fichier config.properties doit se"
			+ " trouver dans le chemin d’accès aux classes.");
	    }
	} finally {
	    if (ctx != null) {
		ctx.close();
	    }
	}
    }

    /** Nombre de fiches à traiter en une transaction. */
    private int tailleBloc;

    /**
     * Service chargé de traiter les fiches par blocs.
     */
    @Resource(name = "processeur")
    private Processeur processeur;

    /**
     * Méthode principale de l’application : demande le traitement de toutes
     * les fiches.
     */
    public void traiterToutesLesFiches() {
	int nombre = tailleBloc;
	int i = 0;
	journal.info("Début du traitement.");
	while (nombre > 0) {
	    try {
		nombre = processeur.traiterFiches(i, tailleBloc);
		i += nombre;
		journal.info("{} fiches traitées.", new Integer(i));
	    } catch (final SQLException e) {
		nombre = 0;
		journal.error("Une erreur SQL nous empèche de continuer.", e);
	    }
	}
	journal.info("Fin du traitement.");
    }

    /**
     * Définit la taille des blocs à traiter.
     *
     * @param tailleBloc
     *            nombre de fiches à traiter en une transaction.
     */
    public void setTailleBloc(final int tailleBloc) {
	this.tailleBloc = tailleBloc;
    }

}
