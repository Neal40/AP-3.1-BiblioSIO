package rudy_yoann.backend.Exemplaire;

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
import rudy_yoann.backend.Revue.Revue;
import rudy_yoann.backend.Revue.RevueRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ExemplaireRepository.class)
public class ExemplaireServiceTest {

    @MockBean
    ExemplaireService exemplaireService;
    private List<Exemplaire> exemplaires;
    private Revue revue1, revue2, revue3;


    @BeforeEach
    void setUp() {
        revue1 = Mockito.mock(Revue.class);
        revue2 = Mockito.mock(Revue.class);
        revue3 = Mockito.mock(Revue.class);
        exemplaires = new ArrayList<>() {{
            add(new Exemplaire(1L, revue1, "titre1", "Janvier", "2001", Statut.DISPONIBLE));
            add(new Exemplaire(2L, revue2, "titre2", "Février", "2002", Statut.INDISPONIBLE));
            add(new Exemplaire(3L, revue3, "titre3", "Mars", "2003", Statut.DISPONIBLE));
        }};
    }

    @Test
    void whenGettingAll_shouldHave3Todos() {
        //Mockito avec la méthode getAll
        Mockito.when(exemplaireService.getAll()).thenReturn(exemplaires);
        assertEquals(3, exemplaireService.getAll().size());
    }

    @Test
    void whenGettingById_shouldReturnIfExists_andBeTheSame() {
        //Mockito avec la méthode getById et également la gestion d'une exception
        Mockito.when(exemplaireService.getById(1L)).thenReturn(exemplaires.get(0));
        Mockito.when(exemplaireService.getById(15L)).thenThrow(ResourceNotFoundException.class);
        assertAll(
                () -> assertEquals(exemplaires.get(0), exemplaireService.getById(1L)),
                () -> assertThrows(ResourceNotFoundException.class, () -> exemplaireService.getById(15L))
        );
    }

    @Test
    void whenCreating_ShouldReturnSame() {
        //Mockito avec la méthode create
        Revue revue5 = Mockito.mock(Revue.class);
        Exemplaire exemplaire = new Exemplaire(5L, revue5, "titre5", "Mai", "2005", Statut.DISPONIBLE);
        Mockito.when(exemplaireService.create(exemplaire)).thenReturn(exemplaire);
        assertEquals(exemplaire, exemplaireService.create(exemplaire));
    }

    @Test
    void whenExisting_ShouldNotBeCreated() {
        //Mockito avec la méthode create et la gestion d'une exception
        Revue revue5 = Mockito.mock(Revue.class);
        Exemplaire exemplaire = new Exemplaire(5L, revue5, "titre5", "Mai", "2005", Statut.DISPONIBLE);
        Mockito.when(exemplaireService.create(exemplaire)).thenReturn(exemplaire);
        Mockito.when(exemplaireService.create(exemplaire)).thenThrow(ResourceAlreadyExistsException.class);
        assertThrows(ResourceAlreadyExistsException.class, () -> exemplaireService.create(exemplaire));
    }

    @Test
    void whenUpdate_ShouldModify() {
        //Mockito la méthode getById et update, on attend que la méthode update return updateTodo
        Exemplaire exemplaire = exemplaires.get(0);
        Exemplaire new_exemplaire = new Exemplaire(exemplaire.getId(), exemplaire.getRevue(), "Updaté", exemplaire.getMoisParution(), exemplaire.getAnneeParution(), exemplaire.getStatut());
        when(exemplaireService.getById(exemplaire.getId())).thenReturn(new_exemplaire);
        Exemplaire updateExemplaire = exemplaireService.getById(exemplaire.getId());
        when(exemplaireService.update(eq(exemplaire.getId()), any(Exemplaire.class))).thenReturn(updateExemplaire);
        exemplaireService.update(new_exemplaire.getId(), new_exemplaire);
        assertEquals(new_exemplaire, updateExemplaire);
    }

    @Test
    void whenUpdatingNonExisting_shouldThrowException() {
        //Mockito pour la gestion d'une exception de la méthode update, doThrow pour simuler l'exception lorsque update est appeléé
        Exemplaire exemplaire = exemplaires.get(0);
        Mockito.when(exemplaireService.getById(15L)).thenThrow(ResourceNotFoundException.class);
        doThrow(ResourceNotFoundException.class).when(exemplaireService).update(eq(15L), any(Exemplaire.class));
        assertThrows(ResourceNotFoundException.class, () -> exemplaireService.update(15L, exemplaire));
    }

    @Test
    void whenDeleting_ShouldDelete() {
        //Mockito pour la méthode delete
        Exemplaire exemplaire = exemplaires.get(0);
        Long id = exemplaire.getId();
        when(exemplaireService.delete(id)).thenReturn(true);
        exemplaireService.delete(id);
        assertFalse(exemplaireService.getAll().contains(exemplaire));
    }

    @Test
    void whenDeletingNonExisting_shouldThrowException() {
        //Mockito pour la gestion d'une exception de la méthode delete
        Long id = 15L;
        when(exemplaireService.delete(id)).thenThrow(ResourceNotFoundException.class);
        assertThrows(ResourceNotFoundException.class, () -> exemplaireService.delete(id));
    }


}
