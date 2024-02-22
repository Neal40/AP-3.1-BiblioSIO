package rudy_yoann.backend.Emprunt;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import rudy_yoann.backend.Emprunteur.Emprunteur;
import rudy_yoann.backend.Emprunteur.EmprunteurEmbeddedJSONSerializer;
import rudy_yoann.backend.Exemplaire.Exemplaire;
import rudy_yoann.backend.Exemplaire.ExemplaireEmbeddedJSONSerializer;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Entity
public class Emprunt{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.DATE)
    private Date dateEmprunt;
    @Temporal(TemporalType.DATE)
    private Date dateRetour;
    private Statut statut;
    @Temporal(TemporalType.DATE)
    private Date dateEcheance;
    private int duree;

    @ManyToOne
    @JoinColumn(name = "exemplaire_id")
    @JsonSerialize(using = ExemplaireEmbeddedJSONSerializer.class)
    private Exemplaire exemplaire;
    @ManyToOne
    @JoinColumn(name = "emprunteur_id")
    @JsonSerialize(using = EmprunteurEmbeddedJSONSerializer.class)
    private Emprunteur emprunteur;





    public Emprunt() { }

    public Emprunt(Long id, Date dateEmprunt, Date dateRetour, Statut statut, Emprunteur emprunteur, Exemplaire exemplaire, int duree) {
        this.id = id;
        this.dateEmprunt = dateEmprunt;
        this.dateRetour= dateRetour;
        this.statut = statut;
        this.emprunteur = emprunteur;
        this.exemplaire = exemplaire;
        this.duree = duree;
    }


    public void augmenterDuree() {
        this.duree++;
    }

    @PrePersist
    public void updateDateEcheance() {
        if (dateEmprunt != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateEmprunt);
            calendar.add(Calendar.DAY_OF_MONTH, 21);
            dateEcheance = calendar.getTime();
        }
    }




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateEmprunt() {
        return dateEmprunt;
    }

    public void setDateEmprunt(Date dateEmprunt) {
        this.dateEmprunt = dateEmprunt;
    }

    public Date getDateRetour() {
        return dateRetour;
    }

    public void setDateRetour(Date dateRetour) {
        this.dateRetour = dateRetour;
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

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public Date getDateEcheance() {
        return dateEcheance;
    }

    public void setDateEcheance(Date dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Emprunt emprunt)) return false;
        return getDuree() == emprunt.getDuree() && Objects.equals(getId(), emprunt.getId()) && Objects.equals(getDateEmprunt(), emprunt.getDateEmprunt()) && Objects.equals(getDateRetour(), emprunt.getDateRetour()) && getStatut() == emprunt.getStatut() && Objects.equals(getDateEcheance(), emprunt.getDateEcheance()) && Objects.equals(getExemplaire(), emprunt.getExemplaire()) && Objects.equals(getEmprunteur(), emprunt.getEmprunteur());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDateEmprunt(), getDateRetour(), getStatut(), getDateEcheance(), getDuree(), getExemplaire(), getEmprunteur());
    }



}

