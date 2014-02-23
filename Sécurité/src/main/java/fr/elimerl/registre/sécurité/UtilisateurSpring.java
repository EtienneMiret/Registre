package fr.elimerl.registre.sécurité;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import fr.elimerl.registre.entités.Utilisateur;

/**
 * Représente un utilisateur de Registre dans le framework Spring.
 */
public class UtilisateurSpring extends User {

    /** Numéro de version utilisé pour la sérialisation. */
    private static final long serialVersionUID = 8681631417262947043L;

    /**
     * Liste des rôles donnés à tous les utilisateurs. Dans cette version de
     * registre, il n’y a qu’un seul rôle qui est donné à tous les utilisateurs
     * enregistrés.
     */
    private static final Collection<GrantedAuthority> RÔLES =
	    AuthorityUtils.createAuthorityList("ROLE_UTILISATEUR");

    /**
     * L’utilisateur en base de donné auquel correspond cet utilisteur Spring.
     */
    private final Utilisateur utilisateur;

    /**
     * Création d’un utilisateur Spring à partir d’un utilisateur en base.
     *
     * @param utilisateur
     *            utilisateur en base pour lequel on veut un utilisateur Spring.
     */
    public UtilisateurSpring(final Utilisateur utilisateur) {
	super(utilisateur.getEmail(), "****", RÔLES);
	this.utilisateur = utilisateur;
    }

    /**
     * Renvoie l’utilisateur en base auquel correspond cet utilisateur Spring.
     *
     * @return l’utilisateur en base auquel correspond cet utilisateur Spring.
     */
    public Utilisateur getUtilisateur() {
	return utilisateur;
    }

}
