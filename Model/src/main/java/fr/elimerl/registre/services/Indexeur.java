package fr.elimerl.registre.services;

import static fr.elimerl.registre.entities.Référence.Champ.AUTRE;
import static fr.elimerl.registre.entities.Référence.Champ.COMMENTAIRE;
import static fr.elimerl.registre.entities.Référence.Champ.TITRE;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import fr.elimerl.registre.entities.Record;
import fr.elimerl.registre.entities.Référence;
import fr.elimerl.registre.recherche.signes.MotClé;

/**
 * Service charger d’indexer les fiches.
 */
public class Indexeur {

    /**
     * Le gestionnaire d’entités natif, fournit par le conteneur.
     */
    @PersistenceContext(name = "Registre")
    private EntityManager em;

    /**
     * Le gestionnaire d’entités Registre, fournit par le conteneur.
     */
    @Resource(name = "gestionnaireEntités")
    private GestionnaireEntités ge;

    /**
     * Refait l’index de la fiche donnée. L’index existant est effacé, puis un
     * nouvel index est créé.
     *
     * @param fiche
     *            la fiche à indexer.
     */
    public void réindexer(final Record fiche) {
	final Query désindexer = em.createNamedQuery("désindexerFiche");
	désindexer.setParameter("fiche", fiche);
	désindexer.executeUpdate();
	if (fiche.getTitle() != null) {
	    for (final String mot : découperEnMots(fiche.getTitle())) {
		em.persist(new Référence(ge.fournirMot(mot), TITRE, fiche));
	    }
	}
	if (fiche.getComment() != null) {
	    for (final String mot : découperEnMots(fiche.getComment())) {
		em.persist(new Référence(ge.fournirMot(mot),
			COMMENTAIRE, fiche));
	    }
	}
	final Set<String> mots = new HashSet<String>();
	for (final String chaîne : fiche.getOtherFields()) {
	    mots.addAll(découperEnMots(chaîne));
	}
	for (final String mot : mots) {
	    em.persist(new Référence(ge.fournirMot(mot), AUTRE, fiche));
	}
    }

    /**
     * Découpe la chaîne de caractère donnée en une suite de mots.
     *
     * @param chaîne
     *            la chaîne à découper en mots.
     * @return les mots contenus dans la chaîne, en minuscules et sans
     *         ponctuation.
     */
    public Set<String> découperEnMots(final String chaîne) {
	final Matcher comparateur = MotClé.MOTIF.matcher(chaîne);
	final Set<String> résultat = new HashSet<String>();
	int i = 0;
	while (i < chaîne.length()) {
	    if (comparateur.find(i)) {
		résultat.add(comparateur.group(1).toLowerCase());
		i = comparateur.end();
	    } else {
		i++;
	    }
	}
	return résultat;
    }

}
