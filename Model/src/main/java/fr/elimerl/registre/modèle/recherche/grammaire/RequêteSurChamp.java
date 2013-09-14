package fr.elimerl.registre.modèle.recherche.grammaire;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import fr.elimerl.registre.modèle.recherche.signes.Champ;
import fr.elimerl.registre.modèle.recherche.signes.MotClé;

/**
 * Type d’expression du langage de recherche qui représente une requête sur un
 * champ.
 */
public final class RequêteSurChamp extends Expression {

    /** Un nombre premier. Utiliser pour calculer le hash. */
    private static final int PREMIER = 31;

    /** Champ sur lequel cette requête est faite. */
    private final Champ champ;

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
    public RequêteSurChamp(final Champ champ, final MotClé... motsClés) {
	this.champ = champ;
	this.motsClés = Arrays.asList(motsClés);
    }

    @Override
    public boolean equals(final Object objet) {
	final boolean résultat;
	if (objet == this) {
	    résultat = true;
	} else if (objet instanceof RequêteSurChamp) {
	    final RequêteSurChamp requête = (RequêteSurChamp) objet;
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
