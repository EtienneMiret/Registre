package fr.elimerl.registre.modèle.recherche.grammaire;

/**
 * Type d’expression du langage de recherche qui représente une requête entre
 * parenthèse.
 */
public final class RequêteEntreParenthèse extends Expression {

    /** La requête contenue dans cette expression. */
    private Requête requête;

    /**
     * Construit une requête entre parenthèse à partir d’une requête.
     *
     * @param requête
     *            la requête à mettre entre parenthèse.
     */
    public RequêteEntreParenthèse(final Requête requête) {
	this.requête = requête;
    }

    @Override
    public String toString() {
	return "(" + requête + ")";
    }

}
