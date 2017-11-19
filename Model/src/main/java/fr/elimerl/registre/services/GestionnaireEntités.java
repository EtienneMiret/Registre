package fr.elimerl.registre.services;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fr.elimerl.registre.entities.*;
import fr.elimerl.registre.entities.Actor;

/**
 * Cette classe encapsule un {@link EntityManager} pour fournir des
 * fonctionalités propres à Registre.
 */
public class GestionnaireEntités {

    /**
     * Le véritable gestionnaire d’entités encapsulé par cette classe. Fournit
     * par le conteneur.
     */
    @PersistenceContext(unitName = "Registre")
    private EntityManager em;

    /**
     * Cherche un objet dans la base de données en fonction d’un clé.
     *
     * @param <T>
     *            le type de l’objet cherché.
     * @param type
     *            la classe de l’objet cherché.
     * @param clef
     *            le nom de l’attribut qui sert de clé.
     * @param valeur
     *            la valeur que doit avoir la clé.
     * @return l’objet cherché s’il existe, {@code null} sinon.
     * @throws NonUniqueResultException
     *             s’il y a plusieurs objets qui correspondent aux critères
     *             donnés.
     */
    private <T> T chercher(final Class<T> type, final String clef,
	    final Object valeur) {
	final CriteriaBuilder builder = em.getCriteriaBuilder();
	final CriteriaQuery<T> requête = builder.createQuery(type);
	final Root<T> racine = requête.from(type);
	requête.where(builder.equal(racine.get(clef), valeur));
	T résultat;
	try {
	    résultat = em.createQuery(requête).getSingleResult();
	} catch (final NoResultException e) {
	    résultat = null;
	}
	return résultat;
    }

    /**
     * Fournit un mot ayant la valeur donnée en argument. Le récupère de la base
     * de donnée s’il existe déjà, le crée sinon.
     *
     * @param valeur
     *            la {@link Word#value valeur} du mot.
     * @return le mot demandé.
     */
    public Word fournirMot(final String valeur) {
	final Word enBase = chercher(Word.class, "value", valeur);
	return (enBase == null ? em.merge(new Word(valeur)) : enBase);
    }

    /**
     * Cherche un nommé dans la base de données.
     *
     * @param type
     *            la classe du nommé que l’on cherche.
     * @param nom
     *            le nom du nommé que l’on cherche.
     * @param <T>
     *            le type du nommé que l’on cherche.
     * @return le nommé cherché s’il existe, {@code null} sinon.
     */
    private <T extends Named> T chercherNommé(final Class<T> type,
	    final String nom) {
	return chercher(type, "name", nom);
    }

    /**
     * Fournit un acteur ayant le nom donné en argument. Le récupère de la base
     * de données s’il existe déjà, le crée sinon.
     *
     * @param nom
     *            nom de l’acteur voulu.
     * @return l’acteur ayant le nom donné en argument.
     */
    public Actor fournirActeur(final String nom) {
	final Actor enBase = chercherNommé(Actor.class, nom);
	return (enBase == null ? em.merge(new Actor(nom)) : enBase);
    }

    /**
     * Fournit un auteur ayant le nom donné en argument. Le récupère de la base
     * de données s’il existe déjà, le crée sinon.
     *
     * @param nom
     *            nom de l’auteur voulu.
     * @return l’auteur ayant le nom donné en argument.
     */
    public Author fournirAuteur(final String nom) {
	final Author enBase = chercherNommé(Author.class, nom);
	return (enBase == null ? em.merge(new Author(nom)) : enBase);
    }

    /**
     * Fournit un compositeur ayant le nom donné en argument. Le récupère de la
     * base de données s’il existe déjà, le crée sinon.
     *
     * @param nom
     *            nom du compositeur voulu.
     * @return le compositeur ayant le nom donné en argument.
     */
    public Composer fournirCompositeur(final String nom) {
	final Composer enBase = chercherNommé(Composer.class, nom);
	return (enBase == null ? em.merge(new Composer(nom)) : enBase);
    }

    /**
     * Fournit un dessinateur ayant le nom donné en argument. Le récupère de la
     * base de données s’il existe déjà, le crée sinon.
     *
     * @param nom
     *            nom du dessinateur voulu.
     * @return le dessinateur ayant le nom donné en argument.
     */
    public Cartoonist fournirDessinateur(final String nom) {
	final Cartoonist enBase = chercherNommé(Cartoonist.class, nom);
	return (enBase == null ? em.merge(new Cartoonist(nom)) : enBase);
    }

    /**
     * Fournit un emplacement ayant le nom donné en argument. Le récupère de la
     * base de données s’il existe déjà, le crée sinon.
     *
     * @param nom
     *            nom de l’emplacement voulu.
     * @return l’emplacement ayant le nom donné en argument.
     */
    public Location fournirEmplacement(final String nom) {
	final Location enBase = chercherNommé(Location.class, nom);
	return (enBase == null ? em.merge(new Location(nom)) : enBase);
    }

    /**
     * Fournit un propriétaire ayant le nom donné en argument. Le récupère de la
     * base de données s’il existe déjà, le crée sinon.
     *
     * @param nom
     *            nom du propriétaire voulu.
     * @return le propriétaire ayant le nom donné en argument.
     */
    public Owner fournirPropriétaire(final String nom) {
	final Owner enBase = chercherNommé(Owner.class, nom);
	return (enBase == null ? em.merge(new Owner(nom)) : enBase);
    }

    /**
     * Fournit un réalisateur ayant le nom donné en argument. Le récupère de la
     * base de données s’il existe déjà, le crée sinon.
     *
     * @param nom
     *            nom du réalisateur voulu.
     * @return le réalisateur ayant le nom donné en argument.
     */
    public Réalisateur fournirRéalisateur(final String nom) {
	final Réalisateur enBase = chercherNommé(Réalisateur.class, nom);
	return (enBase == null ? em.merge(new Réalisateur(nom)) : enBase);
    }

    /**
     * Fournit un scénariste ayant le nom donné en argument. Le récupère de la
     * base de données s’il existe déjà, le crée sinon.
     *
     * @param nom
     *            nom du scénariste voulu.
     * @return le scénariste ayant le nom donné en argument.
     */
    public Scénariste fournirScénariste(final String nom) {
	final Scénariste enBase = chercherNommé(Scénariste.class, nom);
	return (enBase == null ? em.merge(new Scénariste(nom)) : enBase);
    }

    /**
     * Fournit une série ayant le nom donné en argument. La récupère de la base
     * de données si elle existe déjà, la crée sinon.
     *
     * @param nom
     *            nom de la série voulue.
     * @return la série ayant le nom donné en argument.
     */
    public Série fournirSérie(final String nom) {
	final Série enBase = chercherNommé(Série.class, nom);
	return (enBase == null ? em.merge(new Série(nom)) : enBase);
    }

    /**
     * Renvoie le gestionnaire d’entité natif encapsulé par ce gestionnaire
     * d’entités Registre.
     *
     * @return le gestionnaire d’entité natif encapsulé par ce gestionnaire
     *         d’entités Registre.
     */
    public EntityManager getGestionnaireNatif() {
	return em;
    }

}
