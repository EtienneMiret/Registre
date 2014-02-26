package fr.elimerl.registre.reprise;

/**
 * Interface du service qui va traiter les données par blocs.
 */
public interface Processeur {

    /**
     * Traiter {@code nombre} fiches à partir de la {@code première} fiche.
     *
     * @param première
     *            numéro de la première fiche à traiter. Il ne s’agit pas de son
     *            id, mais de son numéro quand les fiches sont classées par id
     *            croissantes.
     * @param nombre
     *            nombre de fiches à traiter.
     * @return le nombre de fiches effectivement traitées.
     */
    public int traiterFiches(int première, int nombre);

}
