package fr.elimerl.registre.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Type de fiche qui représente un livre.
 */
@Entity
@Table(name = "livres")
public class Livre extends Record {

    /** L’auteur de ce livre. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auteur")
    private Author auteur;

    /**
     * Constructeur sans arguments, requis par Hibernate.
     */
    protected Livre() {
	super();
    }

    /**
     * Crée une nouvelle référence de livre.
     *
     * @param titre
     *            titre de ce livre.
     * @param créateur
     *            utilisateur qui a référencé ce livre.
     */
    public Livre(final String titre, final Utilisateur créateur) {
	super(titre, créateur);
    }

    @Override
    public List<String> getOtherFields() {
	final List<String> résultat = super.getOtherFields();
	résultat.add("livre");
	if (auteur != null) {
	    résultat.add(auteur.getNom());
	}
	return résultat;
    }

    /**
     * Renvoie l’auteur de ce livre.
     *
     * @return l’auteur de ce livre.
     */
    public Author getAuteur() {
	return auteur;
    }

    /**
     * Définit l’auteur de ce livre.
     *
     * @param auteur
     *            l’auteur de ce livre.
     */
    public void setAuteur(final Author auteur) {
	this.auteur = auteur;
    }

}
