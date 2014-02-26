package fr.elimerl.registre.reprise;

import javax.annotation.Resource;

/**
 * Classe principale du programme de reprise de données.
 */
public class RepriseDeDonnées {

    /**
     * Méthode statique chargée de lancer l’application.
     *
     * @param args
     *            inutilisé.
     */
    public static void main(final String[] args) {
	System.out.println("Pas encore implémenté.");
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
	while (nombre > 0) {
	    nombre = processeur.traiterFiches(i, tailleBloc);
	    i += nombre;
	}
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
