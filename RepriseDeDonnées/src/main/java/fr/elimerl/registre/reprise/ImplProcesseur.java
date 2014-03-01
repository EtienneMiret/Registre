package fr.elimerl.registre.reprise;

import static fr.elimerl.registre.entités.Film.Support.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fr.elimerl.registre.entités.BandeDessinée;
import fr.elimerl.registre.entités.Fiche;
import fr.elimerl.registre.entités.Film;
import fr.elimerl.registre.entités.Film.Support;
import fr.elimerl.registre.entités.Livre;
import fr.elimerl.registre.entités.Nommé;
import fr.elimerl.registre.entités.Utilisateur;
import fr.elimerl.registre.services.GestionnaireEntités;

/**
 * Implémentation du processeur. Cet objet est chargé de traiter les fiches
 * par blocs.
 */
@Service("processeur")
public class ImplProcesseur implements Processeur {

    /** Journal SLF4J de cette fiche. */
    private static final Logger journal =
	    LoggerFactory.getLogger(ImplProcesseur.class);

    /**
     * L’ancienne base de données de Registre.
     */
    @Resource(name = "ancienneBase")
    private DataSource ancienneBase;

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

    /**
     * Connexion à l’ancienne base de donnée. Nécessite seulement un accès en
     * lecture.
     */
    private Connection connexionAncienneBase;

    /**
     * Ouvre une connexion vers l’ancienne base de données.
     *
     * @throws SQLException
     *             en cas d’impossibilité de se connecter à la base.
     */
    @PostConstruct
    public void seConnecter() throws SQLException {
	connexionAncienneBase = ancienneBase.getConnection();
	requête = connexionAncienneBase.prepareStatement("select * from tout"
		+ " left join films on tout.id = films.id"
		+ " left join bd on tout.id = bd.id"
		+ " left join livres on tout.id = livres.id"
		+ " order by tout.id"
		+ " limit ?, ?",
		ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    }

    /**
     * Ferme la connexion vers l’ancienne base de données.
     *
     * @throws SQLException
     *             en cas d’erreur SQL
     */
    @PreDestroy
    public void seDeconnecter() throws SQLException {
	requête.close();
	connexionAncienneBase.close();
    }

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
	    journal.debug("{} traitée.", fiche);
	    traitées++;
	}
	résultat.close();
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
	if (auteur != null && !auteur.isEmpty()) {
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

    /**
     * Crée une bande dessinée à partir du résultat de la requête SQL
     * {@link #requête}.
     *
     * @param résultat
     *            résultat de {@link #requête}.
     * @return une fiche nouvellement crée (sans id).
     * @throws SQLException
     *             en cas d’erreur SQL.
     */
    private Fiche créerBd(final ResultSet résultat) throws SQLException {
	final Utilisateur créateur =
		fournirUtilisateur(résultat.getString("createur"));
	final String titre = résultat.getString("titre");
	final String dessinateur = résultat.getString("dessinateur");
	final String scénariste = résultat.getString("scenariste");
	final Integer numéro = (Integer) résultat.getObject("numero");
	final BandeDessinée bd = new BandeDessinée(titre, créateur);
	if (dessinateur != null && !dessinateur.isEmpty()) {
	    bd.setDessinateur(gestionnaire.fournirDessinateur(dessinateur));
	}
	if (scénariste != null && !scénariste.isEmpty()) {
	    bd.setScénariste(gestionnaire.fournirScénariste(scénariste));
	}
	bd.setNuméro(numéro);
	return bd;
    }

    /**
     * Crée un film à partir du résultat de la requête SQL {@link #requête}.
     *
     * @param résultat
     *            résultat de {@link #requête}.
     * @return une fiche nouvellement crée (sans id).
     * @throws SQLException
     *             en cas d’erreur SQL.
     */
    private Fiche créerFilm(final ResultSet résultat) throws SQLException {
	final Utilisateur créateur =
		fournirUtilisateur(résultat.getString("createur"));
	final String titre = résultat.getString("titre");
	final String réalisateur = résultat.getString("realisateur");
	final String compositeur = résultat.getString("compositeur");
	final int id = résultat.getInt("films.id");
	final Support support;
	final String nomSupport = résultat.getString("type");
	if (nomSupport.equals("DVD")) {
	    support = DVD;
	} else if (nomSupport.equals("cassette")) {
	    support = K7;
	} else if (nomSupport.equals("disque Blu-ray")) {
	    support = BRD;
	} else {
	    journal.error("Support inconnu : « {} ».", nomSupport);
	    throw new RuntimeException("Support inconnu : " + nomSupport);
	}
	final Film film = new Film(titre, créateur, support);
	if (réalisateur != null && !réalisateur.isEmpty()) {
	    film.setRéalisateur(gestionnaire.fournirRéalisateur(réalisateur));
	}
	if (compositeur != null && !compositeur.isEmpty()) {
	    film.setCompositeur(gestionnaire.fournirCompositeur(compositeur));
	}
	return film;
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
