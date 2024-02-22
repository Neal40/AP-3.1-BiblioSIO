package rudy_yoann.backend.Emprunteur;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rudy_yoann.backend.Exemplaire.Exemplaire;

import java.util.List;

@Repository
public interface EmprunteurRepository extends JpaRepository<Emprunteur, Long> {

    @Query(nativeQuery = true, value = "select nom from emprunteur where nom = :nom")
    String findByNom(String nom);

    //Visualisation des Emprunteurs - Begin
    @Query(nativeQuery = true, value = "SELECT * FROM emprunteur ORDER BY classe ASC")
    List<Emprunteur> findAllByClasseOrdreCroissant(List<Emprunteur> emprunteurs);

    @Query(nativeQuery = true, value = "SELECT * FROM emprunteur ORDER BY classe DESC")
    List<Emprunteur> findAllByClasseOrdreDecroissant(List<Emprunteur> emprunteurs);

    @Query(nativeQuery = true, value = "SELECT * FROM emprunteur ORDER BY nom ASC")
    List<Emprunteur> findAllByNomOrdreCroissant(List<Emprunteur> emprunteurs);

    @Query(nativeQuery = true, value = "SELECT * FROM emprunteur ORDER BY nom DESC")
    List<Emprunteur> findAllByNomOrdreDecroissant(List<Emprunteur> emprunteurs);

    @Query(nativeQuery = true, value = "SELECT * FROM emprunteur WHERE classe = :classe ORDER BY nom ASC")
    List<Emprunteur> findAllByNomOrdreCroissantAndClasse(@Param("classe") Classe classe);


    @Query(nativeQuery = true, value = "SELECT * FROM emprunteur WHERE classe = :classe ORDER BY nom DESC")
    List<Emprunteur> findAllByNomOrdreDecroissantAndClasse(@Param("classe") Classe classe);


    // END
}
