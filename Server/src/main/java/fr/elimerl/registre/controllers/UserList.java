package fr.elimerl.registre.controllers;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import fr.elimerl.registre.entities.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for the page that list all known users.
 */
@Controller
public class UserList {

    /**
     * Entity manager provided by the container. Provide access to the database.
     */
    @PersistenceContext(unitName = "Registre")
    private EntityManager em;

    /**
     * List all registered users.
     *
     * @param model
     *          the Spring model.
     * @return the name of the view to display.
     */
    @RequestMapping("/Utilisateurs")
    @Transactional(readOnly = true)
    public String listUsers(final Model model) {
	final User user = (User) SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal();
	model.addAttribute("user", user);
	final CriteriaBuilder builder = em.getCriteriaBuilder();
	final CriteriaQuery<User> query =
		builder.createQuery(User.class);
	query.from(User.class);
	final List<User> users =
		em.createQuery(query).getResultList();
	model.addAttribute("users", users);
	return "userList";
    }

}
