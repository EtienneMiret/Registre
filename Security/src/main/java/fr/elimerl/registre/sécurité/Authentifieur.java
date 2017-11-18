package fr.elimerl.registre.sécurité;

import java.util.Iterator;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails
	.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.openid.OpenIDAttribute;
import org.springframework.security.openid.OpenIDAuthenticationToken;

import fr.elimerl.registre.entities.Utilisateur;

/**
 * Service chargé d’authentifier les utilisateurs. Vérifie que leur adresse
 * email est bien en base.
 */
public class Authentifieur implements UserDetailsService,
	AuthenticationUserDetailsService<OpenIDAuthenticationToken> {

    /** Url du fournisseur d’identité OpenID de Google. */
    private static final String GOOGLE_OPENID_ENDPOINT =
	    "https://www.google.com/accounts/o8/id";

    /** Url identifiant un attribut email dans le protocole OpenID. */
    private static final String TYPE_EMAIL_OPENID =
	    "http://axschema.org/contact/email";

    /** Journal SLF4J de cette classe. */
    private static final Logger journal =
	    LoggerFactory.getLogger(Authentifieur.class);

    /**
     * Gestionnaire d’entité natif, utilisé pour accéder à la base de données.
     */
    @PersistenceContext(name = "Registre")
    private EntityManager em;

    @Override
    public UserDetails loadUserDetails(final OpenIDAuthenticationToken token) {
	if (!token.getIdentityUrl().startsWith(GOOGLE_OPENID_ENDPOINT)) {
	    journal.info("Connexion refusée pour : {}.",
		    token.getIdentityUrl());
	    throw new UsernameNotFoundException(
		    "Google est le seul fournisseur d’identité autorisé.");
	}
	final Iterator<OpenIDAttribute> attributs =
		token.getAttributes().iterator();
	Utilisateur utilisateur = null;
	while (utilisateur == null && attributs.hasNext()) {
	    final OpenIDAttribute attribut = attributs.next();
	    if (attribut.getType().equals(TYPE_EMAIL_OPENID)) {
		final Iterator<String> emails = attribut.getValues().iterator();
		while (utilisateur == null && emails.hasNext()) {
		    final String email = emails.next();
		    try {
			utilisateur = chargerUtilisateur(email);
		    } catch (final NoResultException e) {
			journal.info("{} est inconnu en base.", email);
		    }
		}
	    }
	}
	if (utilisateur == null) {
	    throw new UsernameNotFoundException("Utilisateur inconnu.");
	}
	return new UtilisateurSpring(utilisateur);
    }

    @Override
    public UserDetails loadUserByUsername(final String username) {
	final UserDetails résultat;
	try {
	    résultat = new UtilisateurSpring(chargerUtilisateur(username));
	} catch (final NoResultException e) {
	    journal.info("{} est inconnu en base.", username);
	    throw new UsernameNotFoundException(username, e);
	}
	return résultat;
    }

    /**
     * Charge de la base l’utilisateur qui a l’adresse email donnée en
     * paramètre.
     *
     * @param email
     *            addresse email de l’utilisateur à charger.
     * @return l’utilisateur qui a l’adresse donnée.
     * @throws NoResultException
     *             si aucun utilisateur en base n’a cette adresse.
     */
    private Utilisateur chargerUtilisateur(final String email) {
	final CriteriaBuilder constructeur = em.getCriteriaBuilder();
	final CriteriaQuery<Utilisateur> requête =
		constructeur.createQuery(Utilisateur.class);
	final Root<Utilisateur> racine =
		requête.from(Utilisateur.class);
	requête.where(constructeur.equal(racine.get("email"),
		email));
	return em.createQuery(requête).getSingleResult();
    }

}
