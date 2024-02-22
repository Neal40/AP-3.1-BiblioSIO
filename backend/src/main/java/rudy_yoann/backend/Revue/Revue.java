package rudy_yoann.backend.Revue;

import jakarta.persistence.*;
import rudy_yoann.backend.Exemplaire.Exemplaire;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Revue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titre;
    @OneToMany(mappedBy = "revue", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Exemplaire> exemplaires;

    public Revue() {

    }
    public Revue(Long id, String titre) {
        this.id = id;
        this.titre = titre;
        this.exemplaires = new ArrayList<>();
    }

    public Revue(Long id, String titre, List<Exemplaire> exemplaires) {
        this.id = id;
        this.titre = titre;
        this.exemplaires = exemplaires;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public List<Exemplaire> getExemplaires() {
        return exemplaires;
    }

    public void setExemplaires(List<Exemplaire> exemplaires) {
        this.exemplaires = exemplaires;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Revue revue = (Revue) o;
        return Objects.equals(id, revue.id) && Objects.equals(titre, revue.titre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titre);
    }
}
