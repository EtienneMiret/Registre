package fr.elimerl.registre.modèle.recherche.grammaire;

import fr.elimerl.registre.modèle.recherche.signes.MotClé;

/**
 * Type d’expression du langage de recherche contenant seulement un mot-clé.
 */
public final class MotCléSimple extends Expression {

    /** Mot-clé contenu dans cette expression. */
    private final MotClé motClé;

    /**
     * Construit une expression {@code MotCléSimple} à partir d’un mot-clé.
     *
     * @param motClé
     *            mot-clé à mettre dans cette expression.
     */
    public MotCléSimple(final MotClé motClé) {
	this.motClé = motClé;
    }

    @Override
    public String toString() {
	return motClé.toString();
    }

}
