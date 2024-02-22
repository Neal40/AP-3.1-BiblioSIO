package rudy_yoann.backend.Exemplaire;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import rudy_yoann.backend.Article.Article;
import rudy_yoann.backend.DemandeEmprunt.DemandeEmprunt;
import rudy_yoann.backend.Emprunt.Emprunt;
import rudy_yoann.backend.Revue.Revue;
import rudy_yoann.backend.Revue.RevueEmbeddedJSONSerializer;

import java.util.List;
import java.util.Objects;

@Entity
public class Exemplaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "revue_id")
    @JsonSerialize(using = RevueEmbeddedJSONSerializer.class)
    private Revue revue;
    private String titre;
    private String moisParution;
    private String anneeParution;

    @OneToMany(mappedBy = "exemplaire", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Article> articles;

    @OneToMany(mappedBy = "exemplaire", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Emprunt> emprunts;

    @OneToMany(mappedBy = "exemplaire", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DemandeEmprunt> demandeEmprunts;
    private Statut statut;


    public Exemplaire() {
    }

    public Exemplaire(Long id, Revue revue, String titre, String moisParution, String anneeParution, Statut statut) {
        this.id = id;
        this.revue = revue;
        this.titre = titre;
        this.moisParution = moisParution;
        this.anneeParution = anneeParution;
        this.statut = statut;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Revue getRevue() {
        return revue;
    }

    public void setRevue(Revue revue) {
        this.revue = revue;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getMoisParution() {
        return moisParution;
    }

    public void setMoisParution(String moisParution) {
        this.moisParution = moisParution;
    }

    public String getAnneeParution() {
        return anneeParution;
    }

    public void setAnneeParution(String anneeParution) {
        this.anneeParution = anneeParution;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public List<Emprunt> getEmprunts() {
        return emprunts;
    }

    public void setEmprunts(List<Emprunt> emprunts) {
        this.emprunts = emprunts;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
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
        if (!(o instanceof Exemplaire that)) return false;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getRevue(), that.getRevue()) && Objects.equals(getTitre(), that.getTitre()) && Objects.equals(getMoisParution(), that.getMoisParution()) && Objects.equals(getAnneeParution(), that.getAnneeParution()) && Objects.equals(getArticles(), that.getArticles()) && Objects.equals(getEmprunts(), that.getEmprunts()) && Objects.equals(getDemandeEmprunts(), that.getDemandeEmprunts()) && getStatut() == that.getStatut();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getRevue(), getTitre(), getMoisParution(), getAnneeParution(), getArticles(), getEmprunts(), getDemandeEmprunts(), getStatut());
    }
}
