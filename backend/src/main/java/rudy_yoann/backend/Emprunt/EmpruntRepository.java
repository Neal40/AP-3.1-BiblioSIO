package rudy_yoann.backend.Emprunt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rudy_yoann.backend.Emprunteur.Classe;
import rudy_yoann.backend.Emprunteur.Emprunteur;
import rudy_yoann.backend.Revue.Revue;

import java.util.List;

@Repository
public interface EmpruntRepository extends JpaRepository<Emprunt, Long> {

    @Query(nativeQuery = true, value = "select statut from emprunt where statut = :statut")
    String findStatutByStatut(Statut statut);

    @Query("SELECT e FROM Emprunt e WHERE e.statut = :statut")
    List<Emprunt> findByStatut(@Param("statut") Statut statut);

    @Query("SELECT COUNT(e) FROM Emprunt e WHERE e.emprunteur = :emprunteur")
    int countByEmprunteur(@Param("emprunteur") Emprunteur emprunteur);

    @Query("SELECT e FROM Emprunt e WHERE e.exemplaire.revue.id IN :revues")
    List<Emprunt> findEmpruntByRevue(@Param("revuesIds") Long[] revuesIds);

    @Query("SELECT e FROM Emprunt e WHERE e.emprunteur.id = :emprunteur")
    List<Emprunt> findEmpruntByEmprunteur(@Param("emprunteurId") Long emprunteurId);

    @Query("SELECT e FROM Emprunt e WHERE e.emprunteur.classe IN :classes")
    List<Emprunt> findEmpruntByClasse(@Param("classes") Long[] classes);

    @Query("SELECT e FROM Emprunt e WHERE e.emprunteur.classe = :classe AND e.statut = :statut")
    List<Emprunt> findEmpruntsEnRetardByClasse(@Param("classe") Classe classe, @Param("statut") Statut statut);
}
