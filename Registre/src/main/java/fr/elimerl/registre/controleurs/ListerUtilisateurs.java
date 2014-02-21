package fr.elimerl.registre.controleurs;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.elimerl.registre.modèle.entités.Utilisateur;

/**
 * Contrôleur de la page qui liste les utilisateurs existants.
 */
@Controller
public class ListerUtilisateurs {

    /**
     * Liste les utilisateurs dans le modèle.
     *
     * @param modèle
     *            le modèle Spring.
     * @return le nom de la vue à afficher.
     */
    @RequestMapping("/Utilisateurs")
    public String listerUtilisateurs(final Model modèle) {
	final List<Utilisateur> utilisateurs = new ArrayList<Utilisateur>(3);
	utilisateurs.add(new Utilisateur("Etienne", "etienne@email"));
	utilisateurs.add(new Utilisateur("Grégoire", "gregoire@email"));
	utilisateurs.add(new Utilisateur("Claire", "claire@email"));
	modèle.addAttribute("utilisateurs", utilisateurs);
	return "listeUtilisateurs";
    }

}
