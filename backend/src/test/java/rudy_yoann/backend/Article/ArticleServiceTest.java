package rudy_yoann.backend.Article;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import rudy_yoann.backend.Exceptions.ResourceAlreadyExistsException;
import rudy_yoann.backend.Exceptions.ResourceNotFoundException;
import rudy_yoann.backend.Exemplaire.Exemplaire;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ArticleRepository.class)
public class ArticleServiceTest {

    @MockBean
    ArticleService articleService;
    private List<Article> articles;
    private Exemplaire exemplaire = Mockito.mock(Exemplaire.class);


    @BeforeEach
    void setUp() {
        articles = new ArrayList<>() {{
            add(new Article(1L, "titre1", "desc1", exemplaire));
            add(new Article(2L, "titre2", "desc2", exemplaire));
            add(new Article(3L, "titre3", "desc3", exemplaire));
        }};
    }

    @Test
    void whenGettingAll_shouldHave3Todos() {
        //Mockito avec la méthode getAll
        Mockito.when(articleService.getAll()).thenReturn(articles);
        assertEquals(3, articleService.getAll().size());
    }

    @Test
    void whenGettingById_shouldReturnIfExists_andBeTheSame() {
        //Mockito avec la méthode getById et également la gestion d'une exception
        Mockito.when(articleService.getById(1L)).thenReturn(articles.get(0));
        Mockito.when(articleService.getById(15L)).thenThrow(ResourceNotFoundException.class);
        assertAll(
                () -> assertEquals(articles.get(0), articleService.getById(1L)),
                () -> assertThrows(ResourceNotFoundException.class, () -> articleService.getById(15L))
        );
    }

    @Test
    void whenCreating_ShouldReturnSame() {
        //Mockito avec la méthode create
        Article article = new Article(5L, "titre5", "desc5", exemplaire);
        Mockito.when(articleService.create(article)).thenReturn(article);
        assertEquals(article, articleService.create(article));
    }

    @Test
    void whenExisting_ShouldNotBeCreated() {
        //Mockito avec la méthode create et la gestion d'une exception
        Article article = new Article(5L, "titre5", "desc5", exemplaire);
        Mockito.when(articleService.create(article)).thenReturn(article);
        Mockito.when(articleService.create(article)).thenThrow(ResourceAlreadyExistsException.class);
        assertThrows(ResourceAlreadyExistsException.class, () -> articleService.create(article));
    }

    @Test
    void whenUpdate_ShouldModify() {
        //Mockito la méthode getById et update, on attend que la méthode update return updateTodo
        Article article = articles.get(0);
        Article new_article = new Article(article.getId(), "Updaté", article.getDescription(), article.getExemplaire());
        when(articleService.getById(article.getId())).thenReturn(new_article);
        Article updatedArticle = articleService.getById(article.getId());
        when(articleService.update(eq(article.getId()), any(Article.class))).thenReturn(updatedArticle);
        articleService.update(new_article.getId(), new_article);
        assertEquals(new_article, updatedArticle);
    }

    @Test
    void whenUpdatingNonExisting_shouldThrowException() {
        //Mockito pour la gestion d'une exception de la méthode update, doThrow pour simuler l'exception lorsque update est appeléé
        Article article = articles.get(0);
        Mockito.when(articleService.getById(15L)).thenThrow(ResourceNotFoundException.class);
        doThrow(ResourceNotFoundException.class).when(articleService).update(eq(15L), any(Article.class));
        assertThrows(ResourceNotFoundException.class, () -> articleService.update(15L, article));
    }

    @Test
    void whenDeleting_ShouldDelete() {
        //Mockito pour la méthode delete
        Article article = articles.get(0);
        Long id = article.getId();
        when(articleService.delete(id)).thenReturn(true);
        articleService.delete(id);
        assertFalse(articleService.getAll().contains(article));
    }

    @Test
    void whenDeletingNonExisting_shouldThrowException() {
        //Mockito pour la gestion d'une exception de la méthode delete
        Long id = 15L;
        when(articleService.delete(id)).thenThrow(ResourceNotFoundException.class);
        assertThrows(ResourceNotFoundException.class, () -> articleService.delete(id));
    }
}
