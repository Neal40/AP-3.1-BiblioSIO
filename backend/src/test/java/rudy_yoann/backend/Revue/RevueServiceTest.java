package rudy_yoann.backend.Revue;

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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RevueRepository.class)
public class RevueServiceTest {

    @MockBean
    RevueService revueService;

    private List<Revue> revues;

    @BeforeEach
    void setUp() {
        revues = new ArrayList<>(){{
            add(new Revue(1L, "titre1"));
            add(new Revue(2L, "titre2"));
            add(new Revue(3L, "titre3"));
        }};
    }

    @Test
    void whenGettingAll_shouldHave3Todos() {
        //Mockito avec la méthode getAll
        Mockito.when(revueService.getAll()).thenReturn(revues);
        assertEquals(3, revueService.getAll().size());
    }

    @Test
    void whenGettingById_shouldReturnIfExists_andBeTheSame() {
        //Mockito avec la méthode getById et également la gestion d'une exception
        Mockito.when(revueService.getById(1L)).thenReturn(revues.get(0));
        Mockito.when(revueService.getById(15L)).thenThrow(ResourceNotFoundException.class);
        assertAll(
                () -> assertEquals(revues.get(0), revueService.getById(1L)),
                () -> assertThrows(ResourceNotFoundException.class, () -> revueService.getById(15L))
        );
    }

    @Test
    void whenCreating_ShouldReturnSame() {
        //Mockito avec la méthode create
        Revue revue = new Revue(5L, "titre5");
        Mockito.when(revueService.create(revue)).thenReturn(revue);
        assertEquals(revue, revueService.create(revue));
    }

    @Test
    void whenExisting_ShouldNotBeCreated() {
        //Mockito avec la méthode create et la gestion d'une exception
        Revue revue = new Revue(5L, "titre5");
        Mockito.when(revueService.create(revue)).thenReturn(revue);
        Mockito.when(revueService.create(revue)).thenThrow(ResourceAlreadyExistsException.class);
        assertThrows(ResourceAlreadyExistsException.class, () -> revueService.create(revue));
    }

    @Test
    void whenUpdate_ShouldModify() {
        //Mockito la méthode getById et update, on attend que la méthode update return updateTodo
        Revue revue = revues.get(0);
        Revue new_revue = new Revue(revue.getId(), "Updaté");
        when(revueService.getById(revue.getId())).thenReturn(new_revue);
        Revue updateRevue = revueService.getById(revue.getId());
        when(revueService.update(eq(revue.getId()), any(Revue.class))).thenReturn(updateRevue);
        revueService.update(new_revue.getId(), new_revue);
        assertEquals(new_revue, updateRevue);
    }

    @Test
    void whenUpdatingNonExisting_shouldThrowException() {
        //Mockito pour la gestion d'une exception de la méthode update, doThrow pour simuler l'exception lorsque update est appeléé
        Revue revue = revues.get(0);
        Mockito.when(revueService.getById(15L)).thenThrow(ResourceNotFoundException.class);
        doThrow(ResourceNotFoundException.class).when(revueService).update(eq(15L), any(Revue.class));
        assertThrows(ResourceNotFoundException.class, () -> revueService.update(15L, revue));
    }

    @Test
    void whenDeleting_ShouldDelete() {
        //Mockito pour la méthode delete
        Revue revue = revues.get(0);
        Long id = revue.getId();
        when(revueService.delete(id)).thenReturn(true);
        revueService.delete(id);
        assertFalse(revueService.getAll().contains(revue));
    }

    @Test
    void whenDeletingNonExisting_shouldThrowException() {
        //Mockito pour la gestion d'une exception de la méthode delete
        Long id = 15L;
        when(revueService.delete(id)).thenThrow(ResourceNotFoundException.class);
        assertThrows(ResourceNotFoundException.class, () -> revueService.delete(id));
    }
}
