package rudy_yoann.backend.Article;

import org.springframework.stereotype.Service;
import rudy_yoann.backend.Emprunt.Emprunt;
import rudy_yoann.backend.Exceptions.ResourceAlreadyExistsException;
import rudy_yoann.backend.Exceptions.ResourceNotFoundException;

import java.util.List;

@Service
public interface ArticleService {
    List<Article> getAll();

    Article getById(Long id) throws ResourceNotFoundException;

    Article create(Article newArticle) throws ResourceAlreadyExistsException;

    Article update(Long id, Article updatedArticle) throws ResourceNotFoundException;

    boolean delete(Long id) throws ResourceNotFoundException;
}
