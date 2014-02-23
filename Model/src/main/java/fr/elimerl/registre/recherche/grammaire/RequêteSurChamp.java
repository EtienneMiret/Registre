package fr.elimerl.registre.recherche.grammaire;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import fr.elimerl.registre.entités.Fiche;
import fr.elimerl.registre.entités.Référence;
import fr.elimerl.registre.recherche.signes.Champ;
import fr.elimerl.registre.recherche.signes.MotClé;

/**
 * Type d’expression du langage de recherche qui représente une requête sur un
 * champ.
 *
 * @param <T>
 *            le type du champ sur lequel est fait cette requête.
 */
public final class RequêteSurChamp<T> extends Expression {

    /** Un nombre premier. Utiliser pour calculer le hash. */
    private static final int PREMIER = 31;

    /** Champ sur lequel cette requête est faite. */
    private final Champ<T> champ;

    /** Liste de mots-clés à trouver pour cette requête. */
    private final List<MotClé> motsClés;

    /**
     * Construit une requête sur un champ à partir du champ concerné et des
     * mots-clés à y trouver.
     *
     * @param champ
     *            champ sur lequel la requête est faite.
     * @param motsClés
     *            liste de mots-clés à trouver dans le champ.
     */
    public RequêteSurChamp(final Champ<T> champ, final MotClé... motsClés) {
	this.champ = champ;
	this.motsClés = Arrays.asList(motsClés);
    }

    @Override
    public Predicate créerPrédicat(final CriteriaBuilder constructeur,
	    final CriteriaQuery<Fiche> requête, final Root<Fiche> fiche) {
	final Predicate résultat;
	if (champ == Champ.TITRE) {
	    résultat = prédicatPourRéférence(Référence.Champ.TITRE,
		    constructeur, requête, fiche);
	} else if (champ == Champ.COMMENTAIRE) {
	    résultat = prédicatPourRéférence(Référence.Champ.COMMENTAIRE,
		    constructeur, requête, fiche);
	} else if (champ == Champ.SÉRIE) {
	    résultat = prédicatPourFiche(constructeur, requête, fiche);
	} else if (champ == Champ.PROPRIÉTAIRE) {
	    résultat = prédicatPourFiche(constructeur, requête, fiche);
	} else if (champ == Champ.EMPLACEMENT) {
	    résultat = prédicatPourFiche(constructeur, requête, fiche);
	} else {
	    throw new RuntimeException("Champ inconnu : " + champ);
	}
	return résultat;
    }

    /**
     * Crée un prédicat pour un champ référencé.
     *
     * @param champ
     *            le champ référencé correspondant au {@link #champ}.
     * @param constructeur
     *            constructeur de requêtes.
     * @param requête
     *            la requête principale dont on construit la clause where.
     * @param fiche
     *            la racine de la requête.
     * @return un prédicat, lié à la requête passée en paramètre, qui teste si
     *           une fiche contient les mot clés {@link #motsClés} dans son
     *           champ {@link #champ}.
     */
    private Predicate prédicatPourRéférence(final Référence.Champ champ,
	    final CriteriaBuilder constructeur,
	    final CriteriaQuery<Fiche> requête, final Root<Fiche> fiche) {
	final Predicate[] prédicats = new Predicate[motsClés.size()];
	final Subquery<Fiche> sousRequête = requête.subquery(Fiche.class);
	final Root<Référence> référence = sousRequête.from(Référence.class);
	final Path<String> mot = référence.get("mot").get("valeur");
	sousRequête.select(référence.<Fiche>get("fiche"));
	for (int i = 0; i < motsClés.size(); i++) {
	    prédicats[i] = constructeur.equal(mot, motsClés.get(i).getValeur());
	}
	sousRequête.where(constructeur.and(prédicats));
	return constructeur.in(fiche).value(sousRequête);
    }

    /**
     * Crée un prédicat pour un champ de {@link Fiche}.
     *
     * @param constructeur
     *            constructeur de requêtes.
     * @param requête
     *            la requête principale dont on construit la clause where.
     * @param fiche
     *            la racine de la requête.
     * @return un prédicat, lié à la requête passée en paramètre, qui teste si
     *         une fiche contient les mot clés {@link #motsClés} dans son champ
     *         {@link #champ}.
     */
    private Predicate prédicatPourFiche(final CriteriaBuilder constructeur,
	    final CriteriaQuery<Fiche> requête, final Root<Fiche> fiche) {
	final Predicate[] prédicats = new Predicate[motsClés.size()];
	for (int i = 0; i < motsClés.size(); i++) {
	    final String mot = motsClés.get(i).getValeur();
	    final Subquery<T> sousRequête =
		    requête.subquery(champ.getClasse());
	    final Root<T> type = sousRequête.from(champ.getClasse());
	    final Predicate comme = constructeur.like(
		    constructeur.lower(type.<String>get("nom")), mot);
	    sousRequête.select(type);
	    sousRequête.where(comme);
	    prédicats[i] = constructeur.in(fiche.get(champ.getNom())).value(
		    sousRequête);
	}
	return constructeur.and(prédicats);
    }

    @Override
    public boolean equals(final Object objet) {
	final boolean résultat;
	if (objet == this) {
	    résultat = true;
	} else if (objet instanceof RequêteSurChamp) {
	    final RequêteSurChamp<?> requête = (RequêteSurChamp<?>) objet;
	    if (champ == null && motsClés == null) {
		résultat = (requête.champ == null && requête.motsClés == null);
	    } else if (champ == null) {
		résultat = (requête.champ == null
			&& motsClés.equals(requête.motsClés));
	    } else if (motsClés == null) {
		résultat = (champ.equals(requête.champ)
			&& requête.motsClés == null);
	    } else {
		résultat = (champ.equals(requête.champ)
			&& motsClés.equals(requête.motsClés));
	    }
	} else {
	    résultat = false;
	}
	return résultat;
    }

    @Override
    public int hashCode() {
	return (motsClés == null ? 0 : motsClés.hashCode())
		+ PREMIER * (champ == null ? 0 : champ.hashCode());
    }

    @Override
    public String toString() {
	final Iterator<MotClé> itérateur = motsClés.iterator();
	final StringBuilder buffer = new StringBuilder();
	buffer.append(champ);
	buffer.append('(');
	while (itérateur.hasNext()) {
	    buffer.append(itérateur.next());
	    if (itérateur.hasNext()) {
		buffer.append(' ');
	    }
	}
	buffer.append(')');
	return buffer.toString();
    }

}
