package rudy_yoann.backend.Article;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import rudy_yoann.backend.Exceptions.ExceptionHandlingAdvice;
import rudy_yoann.backend.Exceptions.ResourceAlreadyExistsException;
import rudy_yoann.backend.Exceptions.ResourceNotFoundException;
import rudy_yoann.backend.Exemplaire.Exemplaire;
import rudy_yoann.backend.Revue.Revue;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@ContextConfiguration(classes = ArticleController.class)
@Import(ExceptionHandlingAdvice.class)
public class ArticleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ArticleService articleService;
    private List<Article> articles;
    private Exemplaire exemplaire = Mockito.mock(Exemplaire.class);
    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        articles = new ArrayList<>() {{
            add(new Article(1L, "titre1", "desc1", exemplaire));
            add(new Article(2L, "titre2", "desc2", exemplaire));
            add(new Article(3L, "titre3", "desc3", exemplaire));
        }};
    }

    @Test
    void whenGettingAll_shouldGet3() throws Exception {
        when(articleService.getAll()).thenReturn(articles);
        mockMvc.perform(get("/articles")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()
        ).andExpect(jsonPath("$", hasSize(3))
        ).andDo(print());
    }

    @Test
    void whenGetWithCode1_shouldReturnTodo1() throws Exception {
        when(articleService.getById(3L)).thenReturn(articles.get(2));
        mockMvc.perform(get("/articles/3")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(content().contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()
        ).andExpect(jsonPath("$", hasEntry("id", 3))
        ).andDo(print());
    }

    @Test
    void whenGettingUnexistingId_should404() throws Exception {
        when(articleService.getById(11L)).thenThrow(ResourceNotFoundException.class);
        mockMvc.perform(get("/articles/11")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound()
        ).andDo(print());
    }

    @Test
    void whenCreatingIssue_shouldGetLinkToResource() throws Exception {
        Article article = new Article(5L, "titre5", "desc5", exemplaire);
        Mockito.when(articleService.create(Mockito.any(Article.class))).thenReturn(article);

        String toSend = mapper.writeValueAsString(article);

        mockMvc.perform(post("/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toSend)
        ).andExpect(status().isCreated()
        ).andExpect(header().string("Location", "/articles/" + article.getId())
        ).andDo(print()).andReturn();
    }

    @Test
    void whenCreatingWithExistingId_should404() throws Exception {
        //On vérifie que si on create un emprunteur avec un id existant, ça renvoie une exception
        when(articleService.create(any())).thenThrow(ResourceAlreadyExistsException.class);
        mockMvc.perform(post("/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(this.articles.get(2)))
        ).andExpect(status().isConflict()
        ).andDo(print());
    }


    @Test
    void whenUpdating_shouldReceiveTodoToUpdate_andReturnNoContent() throws Exception {
        Article initial_article = new Article(6L, "titre6", "desc6", exemplaire);
        Article updated_article = new Article(initial_article.getId(), "Updaté", initial_article.getDescription(), initial_article.getExemplaire());
        ArgumentCaptor<Article> article_received = ArgumentCaptor.forClass(Article.class);

        when(articleService.update(anyLong(), any(Article.class))).thenReturn(updated_article);
        mockMvc.perform(put("/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updated_article))
        ).andExpect(status().isNoContent());

        verify(articleService).update(anyLong(), article_received.capture());
        assertEquals(updated_article.getId(), article_received.getValue().getId());
    }


    @Test
    void whenDeletingExisting_shouldCallServiceWithCorrectId_andSendNoContent() throws Exception {
        Long id = 28L;

        //Lorsque la méthode delete est appelé, on renvoie true (lorsque le delete est effectué, notre méthode renvoie true)
        when(articleService.delete(eq(id))).thenReturn(true);
        mockMvc.perform(delete("/articles/" + id)
        ).andExpect(status().isNoContent()
        ).andDo(print());

        ArgumentCaptor<Long> id_received = ArgumentCaptor.forClass(Long.class);
        verify(articleService).delete(id_received.capture());
        assertEquals(id, id_received.getValue());
    }

    @Test
    void whenDeleteNonExistingResource_shouldGet404() throws Exception {
        //Gestion d'une exception pour la méthode delete avec mockito
        //Lorsque la méthode delete est appelée, on demande à moquiter de simuler l'exception
        Mockito.doThrow(ResourceNotFoundException.class).when(articleService).delete(anyLong());

        mockMvc.perform(delete("/articles/972")
        ).andExpect(status().isNotFound()
        ).andDo(print()).andReturn();
    }
}
