package fr.elimerl.registre.reprise;

import java.sql.Connection;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import fr.elimerl.registre.entités.Nommé;
import fr.elimerl.registre.entités.Utilisateur;
import fr.elimerl.registre.services.GestionnaireEntités;

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

    @Override
    @Transactional
    public int traiterFiches(int première, int nombre) {
	// TODO Auto-generated method stub
	return 0;
    }

}
