package fr.elimerl.registre.reprise;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import fr.elimerl.registre.entités.Fiche;
import fr.elimerl.registre.entités.Livre;
import fr.elimerl.registre.entités.Nommé;
import fr.elimerl.registre.entités.Utilisateur;
import fr.elimerl.registre.services.GestionnaireEntités;

/**
 * Implémentation du processeur. Cet objet est chargé de traiter les fiches
 * par blocs.
 */
@Resource(name = "processeur")
public class ImplProcesseur implements Processeur {

    /**
     * Connexion à l’ancienne base de donnée. Nécessite seulement un accès en
     * lecture.
     */
    @Resource(name = "connexionAncienneBase")
    private Connection ancienneBase;

    /**
     * Gestionnaire d’entités registre. Sera utilisé pour créer tous les
     * {@link Nommé}s.
     */
    @Resource(name = "gestionnaireEntités")
    private GestionnaireEntités gestionnaire;

    /**
     * Gestionnaire d’entités JPA. Sera utilisé pour créer les
     * {@link Utilisateur}s.
     */
    @PersistenceContext(unitName = "Registre")
    private EntityManager em;

    /**
     * Requête préparée sur l’ancienne base.
     */
    private PreparedStatement requête;

    @Override
    @Transactional(rollbackOn = SQLException.class)
    public int traiterFiches(final int première, final int nombre)
	    throws SQLException {
	int traitées = 0;
	requête.setInt(1, première);
	requête.setInt(2, nombre);
	final ResultSet résultat = requête.executeQuery();
	while (résultat.next()) {
	    final String type = résultat.getString("type");
	    final Fiche fiche;
	    if (type.equals("livre")) {
		fiche = créerLivre(résultat);
	    } else if (type.equals("BD")) {
		fiche = créerBd(résultat);
	    } else { // Film. Type Blu-ray, DVD ou cassette.
		fiche = créerFilm(résultat);
	    }
	    remplirChampsCommuns(fiche, résultat);
	    em.persist(fiche);
	    traitées++;
	}
	return traitées;
    }

    /**
     * Crée un livre à partir du résultat de la requête SQL {@link #requête}.
     *
     * @param résultat
     *            résultat de {@link #requête}.
     * @return une fiche nouvellement crée (sans id).
     * @throws SQLException
     *             en cas d’erreur SQL.
     */
    private Fiche créerLivre(final ResultSet résultat) throws SQLException {
	final Utilisateur créateur =
		fournirUtilisateur(résultat.getString("createur"));
	final String titre = résultat.getString("titre");
	final String auteur = résultat.getString("auteur");
	final String genres = résultat.getString("genres");
	final Livre livre = new Livre(titre, créateur);
	if (auteur != null) {
	    livre.setAuteur(gestionnaire.fournirAuteur(auteur));
	}
	if (genres != null && !genres.isEmpty()) {
	    livre.setGenreFantastique(Boolean.valueOf(genres
		    .contains("fantastique")));
	    livre.setGenreHistoireVraie(Boolean.valueOf(genres
		    .contains("histoire vraie")));
	    livre.setGenreHistorique(Boolean.valueOf(genres
		    .concat("historique")));
	    livre.setGenreHumour(Boolean.valueOf(genres
		    .contains("humour")));
	    livre.setGenrePolicier(Boolean.valueOf(genres
		    .contains("policier")));
	    livre.setGenreRomantique(Boolean.valueOf(genres
		    .contains("romantique")));
	    livre.setGenreSf(Boolean.valueOf(genres
		    .contains("science-fiction")));
	}
	return livre;
    }

    private Fiche créerBd(ResultSet résultat) {
	// TODO Auto-generated method stub
	return null;
    }

    private Fiche créerFilm(ResultSet résultat) {
	// TODO Auto-generated method stub
	return null;
    }

    private void remplirChampsCommuns(Fiche fiche, ResultSet résultat) {
        // TODO Auto-generated method stub
        
    }

    /**
     * Va chercher en base l’utilisateur dont le nom est donné. Le crée et
     * l’enregistre en base s’il n’existait pas.
     *
     * @param nom
     *            nom de l’utilisateur à créer/récupérer.
     * @return l’utilisateur appelé {@code nomm}.
     */
    private Utilisateur fournirUtilisateur(final String nom) {
	final String nouveauNom = (nom == null ? "Système" : nom);
	final CriteriaBuilder constructeur = em.getCriteriaBuilder();
	final CriteriaQuery<Utilisateur> requête =
		constructeur.createQuery(Utilisateur.class);
	final Root<Utilisateur> racine = requête.from(Utilisateur.class);
	requête.where(racine.get("nom").in(nouveauNom));
	Utilisateur résultat;
	try {
	    résultat = em.createQuery(requête).getSingleResult();
	} catch (final NoResultException e) {
	    résultat = em.merge(new Utilisateur(nouveauNom, nouveauNom));
	}
	return résultat;
    }

}
