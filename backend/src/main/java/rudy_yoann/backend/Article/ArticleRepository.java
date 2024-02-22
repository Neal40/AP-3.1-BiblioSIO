package rudy_yoann.backend.Article;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rudy_yoann.backend.Emprunt.Statut;


public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query(nativeQuery = true, value = "select titre from article where titre = :titre")
    String findByTitre(String titre);

}
