package rudy_yoann.backend.Article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import rudy_yoann.backend.Emprunt.Emprunt;
import rudy_yoann.backend.Emprunt.EmpruntRepository;
import rudy_yoann.backend.Exceptions.ResourceAlreadyExistsException;
import rudy_yoann.backend.Exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@Qualifier("jpa")
public class ArticleJPAService implements ArticleService{

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public List<Article> getAll() {
        return articleRepository.findAll();
    }

    @Override
    public Article getById(Long id) {
        Optional<Article> article = articleRepository.findById(id);
        if (article.isPresent()) {
            return article.get();
        } else {
            throw new ResourceNotFoundException("Article", id);
        }
    }

    @Override
    public Article create(Article article) {
        if (articleRepository.findByTitre(article.getTitre())!=null) {
            throw new ResourceAlreadyExistsException("Article", article.getId());
        } else {
            return articleRepository.save(article);
        }
    }

    @Override
    public Article update(Long id, Article updatedArticle) throws ResourceNotFoundException {
        Optional<Article> article = articleRepository.findById(id);
        if (article.isPresent()) {
            articleRepository.save(updatedArticle);
            return updatedArticle;
        } else {
            throw new ResourceNotFoundException("Article", id);
        }
    }


    @Override
    public boolean delete(Long id) throws ResourceNotFoundException {
        Optional<Article> article = articleRepository.findById(id);
        if (article.isPresent()) {
            articleRepository.deleteById(id);
            return true;
        } else {
            throw new ResourceNotFoundException("Article", id);
        }
    }

}
