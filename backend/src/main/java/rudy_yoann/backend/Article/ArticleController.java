package rudy_yoann.backend.Article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rudy_yoann.backend.Emprunt.Emprunt;
import rudy_yoann.backend.Emprunt.EmpruntService;
import rudy_yoann.backend.Revue.Revue;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/articles")
@CrossOrigin(origins = "*")
public class ArticleController {
    private ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("")
    public List<Article> getAll() {
        return articleService.getAll();
    }

    @GetMapping("/{id}")
    public Article getById(@PathVariable Long id) {
        return articleService.getById(id);
    }

    @PostMapping()
    public ResponseEntity createArticle(@RequestBody Article article) {
        Article created_article = articleService.create(article);
        return ResponseEntity.created(URI.create("/articles/" + created_article.getId().toString())).build();
    }

    @PutMapping
    public ResponseEntity<Article> updateArticle(@RequestBody Article article) {
        Long id = article.getId();
        articleService.update(id, article);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteArticle(@PathVariable Long id) {
        articleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
