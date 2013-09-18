package fr.elimerl.registre.modèle.recherche.grammaire;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import fr.elimerl.registre.modèle.entités.Fiche;

/**
 * Une expression du langage de requête, sert à construire une {@link Requête}
 * avec des opérateurs booléens.
 */
public abstract class Expression {

    /**
     * Crée un prédicat qui vérifie qu’une fiche correspond bien aux critères de
     * cette expression.
     *
     * @param constructeur
     *            constructeur de requête.
     * @param requête
     *            la requête principale dont on construit la clause where.
     * @param fiche
     *            la racine de la requête principale.
     * @return un prédicat lié à la requête passée en paramètre qui vérifie
     *         qu’une fiche correspond bien aux critères de cette expression.
     */
    public abstract Predicate créerPrédicat(CriteriaBuilder constructeur,
	    CriteriaQuery<Fiche> requête, Root<Fiche> fiche);

}
