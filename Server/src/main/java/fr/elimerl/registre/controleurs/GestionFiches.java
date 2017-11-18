package fr.elimerl.registre.controleurs;

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_NOT_IMPLEMENTED;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.elimerl.registre.entities.Comic;
import fr.elimerl.registre.entities.Fiche;
import fr.elimerl.registre.entities.Film;
import fr.elimerl.registre.entities.Livre;

/**
 * Contrôleur chargé de la gestion des fiches : affichage, édition et création.
 */
@Controller
@RequestMapping("/Fiche")
public class GestionFiches {

    /**
     * Gestionnaire d’entité JPA, injecté par Spring.
     */
    @PersistenceContext(name = "Registre")
    private EntityManager em;

    /**
     * Affiche un fiche à l’écran pour lecture.
     *
     * @param id
     *            identifiant de la fiche à afficher.
     * @param modèle
     *            le modèle Spring.
     * @param réponse
     *            la réponse HTTP à envoyer à l’agent utilisateur.
     * @return le nom de la vue à afficher.
     */
    @RequestMapping(value = "/{id}", method = GET)
    @Transactional(readOnly = true)
    public String afficher(@PathVariable final Long id, final Model modèle,
	    final HttpServletResponse réponse) {
	final Fiche fiche = em.find(Fiche.class, id);
	modèle.addAttribute("fiche", fiche);
	final String vue;
	if (fiche == null) {
	    vue = "ficheInexistante";
	    réponse.setStatus(SC_NOT_FOUND);
	} else if (fiche instanceof Film) {
	    vue = "film";
	} else if (fiche instanceof Livre) {
	    vue = "livre";
	} else if (fiche instanceof Comic) {
	    vue = "bd";
	} else {
	    vue = "typeFicheInconnu";
	    réponse.setStatus(SC_NOT_IMPLEMENTED);
	}
	return vue;
    }

}
