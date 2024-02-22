package rudy_yoann.backend.DemandeEmprunt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rudy_yoann.backend.Emprunt.Emprunt;
import rudy_yoann.backend.Emprunteur.Emprunteur;

import java.util.List;

@Repository
public interface DemandeEmpruntRepository extends JpaRepository<DemandeEmprunt, Long> {
    @Query(nativeQuery = true, value = "select statut from demandeEmprunt where statut = :statut")
    String findByStatut(Statut statut);
    @Query("SELECT COUNT(d) FROM DemandeEmprunt d WHERE d.emprunteur = :emprunteur")
    int countByEmprunteur(@Param("emprunteur")Emprunteur emprunteur);

    @Query("SELECT e FROM DemandeEmprunt e WHERE e.emprunteur = :emprunteur")
    List<DemandeEmprunt> findDemandeEmpruntByEmprunteur(@Param("emprunteur") Emprunteur emprunteur);

}
