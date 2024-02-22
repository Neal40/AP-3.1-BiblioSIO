package rudy_yoann.backend.DemandeEmprunt;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import rudy_yoann.backend.DemandeEmprunt.Statut;
import rudy_yoann.backend.Emprunteur.Emprunteur;
import rudy_yoann.backend.Emprunteur.EmprunteurEmbeddedJSONSerializer;
import rudy_yoann.backend.Exemplaire.Exemplaire;
import rudy_yoann.backend.Exemplaire.ExemplaireEmbeddedJSONSerializer;

import java.util.Date;
import java.util.Objects;

@EntityListeners(DemandeEmpruntEntityListener.class)
@Entity
public class DemandeEmprunt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.DATE)
    private Date dateCreation;
    @Temporal(TemporalType.DATE)
    private Date dateTraitement;
    private Statut statut;

    @ManyToOne
    @JoinColumn(name = "exemplaire_id")
    @JsonSerialize(using = ExemplaireEmbeddedJSONSerializer.class)
    private Exemplaire exemplaire;
    @ManyToOne
    @JoinColumn(name = "emprunteur_id")
    @JsonSerialize(using = EmprunteurEmbeddedJSONSerializer.class)
    private Emprunteur emprunteur;

    public DemandeEmprunt(Long id, Date dateCreation, Date dateTraitement, Statut statut, Exemplaire exemplaire, Emprunteur emprunteur) {
        this.id = id;
        this.dateCreation = dateCreation;
        this.dateTraitement = dateTraitement;
        this.statut = statut;
        this.exemplaire = exemplaire;
        this.emprunteur = emprunteur;
    }

    public DemandeEmprunt() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Date getDateTraitement() {
        return dateTraitement;
    }

    public void setDateTraitement(Date dateTraitement) {
        this.dateTraitement = dateTraitement;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public Exemplaire getExemplaire() {
        return exemplaire;
    }

    public void setExemplaire(Exemplaire exemplaire) {
        this.exemplaire = exemplaire;
    }

    public Emprunteur getEmprunteur() {
        return emprunteur;
    }

    public void setEmprunteur(Emprunteur emprunteur) {
        this.emprunteur = emprunteur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DemandeEmprunt that)) return false;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getDateCreation(), that.getDateCreation()) && Objects.equals(getDateTraitement(), that.getDateTraitement()) && getStatut() == that.getStatut() && Objects.equals(getExemplaire(), that.getExemplaire()) && Objects.equals(getEmprunteur(), that.getEmprunteur());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDateCreation(), getDateTraitement(), getStatut(), getExemplaire(), getEmprunteur());
    }



}