package rudy_yoann.backend.Revue;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RevueRepository extends JpaRepository<Revue, Long> {

    @Query(nativeQuery = true, value = "select titre from revue where titre = :titre")
    String findByTitre(String titre);
}
