package rudy_yoann.backend.Emprunteur;

import jakarta.persistence.*;
import rudy_yoann.backend.DemandeEmprunt.DemandeEmprunt;
import rudy_yoann.backend.Emprunt.Emprunt;
import rudy_yoann.backend.Emprunteur.Classe;
import rudy_yoann.backend.Emprunteur.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Emprunteur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String prenom;
    private String mail;
    private Role role;
    private Classe classe;
    private String promotion;
    private Statut statut;

    @OneToMany(mappedBy = "emprunteur", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Emprunt> emprunts;

    @OneToMany(mappedBy = "emprunteur", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DemandeEmprunt> demandeEmprunts;

    public Emprunteur() {
    }

    public Emprunteur(Long id, String nom, String prenom, String mail, Role role, Classe classe, String promotion, Statut statut) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.mail = mail;
        this.role = role;
        this.classe = classe;
        this.promotion = promotion;
        this.statut = statut;
        this.emprunts = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getMail() {
        return mail;
    }
    public void setMail(String mail) {
        this.mail = mail;
    }

    // Gestion fines des emprunteurs

    public String getPromotion() {
        return promotion;
    }
    public void setPromotion(String promotion) {
        this.promotion = promotion;
    }
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Classe getClasse() {
        return classe;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public List<Emprunt> getEmprunts() {
        return emprunts;
    }

    public void setEmprunts(List<Emprunt> emprunts) {
        this.emprunts = emprunts;
    }

    public List<DemandeEmprunt> getDemandeEmprunts() {
        return demandeEmprunts;
    }

    public void setDemandeEmprunts(List<DemandeEmprunt> demandeEmprunts) {
        this.demandeEmprunts = demandeEmprunts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Emprunteur that)) return false;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getNom(), that.getNom()) && Objects.equals(getPrenom(), that.getPrenom()) && Objects.equals(getMail(), that.getMail()) && getRole() == that.getRole() && getClasse() == that.getClasse() && Objects.equals(getPromotion(), that.getPromotion()) && getStatut() == that.getStatut() && Objects.equals(getEmprunts(), that.getEmprunts()) && Objects.equals(getDemandeEmprunts(), that.getDemandeEmprunts());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNom(), getPrenom(), getMail(), getRole(), getClasse(), getPromotion(), getStatut(), getEmprunts(), getDemandeEmprunts());
    }
}
