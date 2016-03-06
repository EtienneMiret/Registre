package fr.elimerl.registre.controleurs;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.elimerl.registre.entités.Utilisateur;
import fr.elimerl.registre.sécurité.UtilisateurSpring;

/**
 * Contrôleur de la page qui liste les utilisateurs existants.
 */
@Controller
public class ListerUtilisateurs {

    /**
     * Gestionnaire d’entités fournit par le conteneur. Permet d’accéder à la
     * base de données.
     */
    @PersistenceContext(unitName = "Registre")
    private EntityManager em;

    /**
     * Liste les utilisateurs enregistrés en base.
     *
     * @param modèle
     *            le modèle Spring.
     * @return le nom de la vue à afficher.
     */
    @RequestMapping("/Utilisateurs")
    @Transactional(readOnly = true)
    public String listerUtilisateurs(final Model modèle) {
	final UtilisateurSpring utilisateurSpring = (UtilisateurSpring)
		SecurityContextHolder.getContext().getAuthentication()
		.getPrincipal();
	modèle.addAttribute("utilisateur", utilisateurSpring.getUtilisateur());
	final CriteriaBuilder builder = em.getCriteriaBuilder();
	final CriteriaQuery<Utilisateur> requête =
		builder.createQuery(Utilisateur.class);
	requête.from(Utilisateur.class);
	final List<Utilisateur> utilisateurs =
		em.createQuery(requête).getResultList();
	modèle.addAttribute("utilisateurs", utilisateurs);
	return "listeUtilisateurs";
    }

}
