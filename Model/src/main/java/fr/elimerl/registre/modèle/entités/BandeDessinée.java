package fr.elimerl.registre.modèle.entités;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Type de fiche qui représente une bande-dessinée.
 */
@Entity
@Table(name = "bandes_dessinees")
public class BandeDessinée extends Fiche {

    /**
     * Dessinateur de cette bande-dessinée.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dessinateur")
    private Dessinateur dessinateur;

    /**
     * Scénariste de cette bande-dessinée.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scenariste")
    private Scénariste scénariste;

    /**
     * Numéro de cette bande-dessinée dans sa série.
     */
    @Column(name = "numero")
    private Integer numéro;

    /**
     * Référence une nouvelle bande-dessinée.
     *
     * @param titre
     *            titre de cette bande-dessinée.
     * @param créateur
     *            utilisateur qui référence cette bande-dessinée.
     */
    public BandeDessinée(final String titre, final Utilisateur créateur) {
	super(titre, créateur);
    }

    /**
     * Constructeur sans arguments, requis par Hibernate.
     */
    protected BandeDessinée() {
	super();
    }

    @Override
    public List<String> getAutresChamps() {
	final List<String> résultat = super.getAutresChamps();
	résultat.add("BD");
	résultat.add("bande-déssinée");
	if (dessinateur != null) {
	    résultat.add(dessinateur.getNom());
	}
	if (scénariste != null) {
	    résultat.add(scénariste.getNom());
	}
	if (numéro != null) {
	    résultat.add(numéro.toString());
	}
	return résultat;
    }

    /**
     * Renvoie le dessinateur de cette bande-dessinée.
     *
     * @return le dessinateur de cette bande-dessinée, ou {@code null} s’il est
     *         inconnu.
     * @see #dessinateur
     */
    public Dessinateur getDessinateur() {
        return dessinateur;
    }

    /**
     * Définit le dessinateur de cette bande-dessinée.
     *
     * @param dessinateur
     *            dessinateur de cette bande-dessinée, ou {@code null} s’il est
     *            inconnu.
     * @see #dessinateur
     */
    public void setDessinateur(final Dessinateur dessinateur) {
        this.dessinateur = dessinateur;
    }

    /**
     * Renvoie le scénariste de cette bande-dessinée.
     *
     * @return le scénariste de cette bande-dessinée, ou {@code null} s’il est
     *         inconnu.
     * @see #scénariste
     */
    public Scénariste getScénariste() {
        return scénariste;
    }

    /**
     * Définit le scénariste de cette bande-dessinée.
     *
     * @param scénariste
     *            scénariste de cette bande-dessinée, ou {@code null} s’il est
     *            inconnu.
     * @see #scénariste
     */
    public void setScénariste(final Scénariste scénariste) {
        this.scénariste = scénariste;
    }

    /**
     * Renvoie le numéro de cette bande-dessinée dans sa série.
     *
     * @return le numéro de cette bande-dessinée dans sa série, ou {@code null}
     *         s’il est inconnu.
     * @see #numéro
     */
    public Integer getNuméro() {
        return numéro;
    }

    /**
     * Définit le numéro de cette bande-dessinée dans sa série.
     *
     * @param numéro
     *            numéro de cette bande-dessinée dans sa série, ou {@code null}
     *            s’il est inconnu.
     * @see #numéro
     */
    public void setNuméro(final Integer numéro) {
        this.numéro = numéro;
    }

}
