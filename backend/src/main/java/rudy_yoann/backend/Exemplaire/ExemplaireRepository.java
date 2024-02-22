package rudy_yoann.backend.Exemplaire;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExemplaireRepository extends JpaRepository<Exemplaire, Long> {

    @Query(nativeQuery = true, value = "SELECT titre FROM exemplaire WHERE titre = :titre")
    String findByTitre(String titre);
/*
    //Visualisation des Exemplaires - Begin
    @Query(nativeQuery = true, value = "SELECT * FROM exemplaire ORDER BY id ASC")
    List<Exemplaire> findAllByNumeroOrdreCroissant(List<Exemplaire> exemplaires);

    @Query(nativeQuery = true, value = "SELECT * FROM exemplaire ORDER BY id DESC")
    List<Exemplaire> findAllByNumeroOrdreDecroissant(List<Exemplaire> exemplaires);

    @Query(nativeQuery = true, value = "SELECT * FROM exemplaire ORDER BY anneeParution ASC, moisParution ASC")
    List<Exemplaire> findAllByAnneeAndMoisOrdreCroissant(List<Exemplaire> exemplaires);

    @Query(nativeQuery = true, value = "SELECT * FROM exemplaire ORDER BY anneeParution DESC, moisParution DESC")
    List<Exemplaire> findAllByAnneeAndMoisOrdreDecroissant(List<Exemplaire> exemplaires);

    @Query(nativeQuery = true, value = "SELECT * FROM exemplaire WHERE anneeParution IN :annees AND anneeParution ASC")
    List<Exemplaire> findAllByOnlyFromCertainsYearsAsc(@Param("annees") List<Exemplaire> exemplaires);

    @Query(nativeQuery = true, value = "SELECT * FROM exemplaire WHERE anneeParution IN :annees AND anneeParution DESC")
    List<Exemplaire> findAllByOnlyFromCertainsYearsDesc(@Param("annees") List<Exemplaire> exemplaires);
*/
    //Visualisation des emprunteurs - End
}
