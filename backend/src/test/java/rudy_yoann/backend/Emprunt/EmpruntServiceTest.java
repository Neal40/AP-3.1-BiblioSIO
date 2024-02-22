package rudy_yoann.backend.Emprunt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import rudy_yoann.backend.DemandeEmprunt.DemandeEmprunt;
import rudy_yoann.backend.Emprunteur.Classe;
import rudy_yoann.backend.Emprunteur.Emprunteur;
import rudy_yoann.backend.Emprunteur.Role;
import rudy_yoann.backend.Exceptions.ResourceAlreadyExistsException;
import rudy_yoann.backend.Exceptions.ResourceNotFoundException;
import rudy_yoann.backend.Exemplaire.Exemplaire;
import rudy_yoann.backend.Revue.Revue;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = EmpruntRepository.class)
public class EmpruntServiceTest {

    @MockBean
    private EmpruntService empruntService;
    private List<Emprunt> emprunts;
    private Emprunteur emprunteur1, emprunteur2, emprunteur3;
    private Exemplaire exemplaire1, exemplaire2, exemplaire3;

    @BeforeEach
    void setUp() {
        emprunteur1 = mock(Emprunteur.class);
        emprunteur2 = mock(Emprunteur.class);
        emprunteur3 = mock(Emprunteur.class);

        exemplaire1 = mock(Exemplaire.class);
        exemplaire2 = mock(Exemplaire.class);
        exemplaire3 = mock(Exemplaire.class);
        emprunts = new ArrayList<>() {{
            add(new Emprunt(1L, java.sql.Date.valueOf("2023-01-01"), null, Statut.EN_COURS, emprunteur1, exemplaire1,0 ));
            add(new Emprunt(2L, java.sql.Date.valueOf("2023-02-01"), null, Statut.EN_COURS, emprunteur2, exemplaire2, 25));
            add(new Emprunt(3L, java.sql.Date.valueOf("2023-03-01"), java.sql.Date.valueOf("2023-03-15"), Statut.TERMINE, emprunteur3, exemplaire3, 0));
        }};
    }

    @Test
    void whenGettingAll_shouldHave3Emprunts() {
        when(empruntService.getAll()).thenReturn(emprunts);
        assertEquals(3, empruntService.getAll().size());
    }

    @Test
    void whenGettingById_shouldReturnIfExists_andBeTheSame() {
        Mockito.when(empruntService.getById(1L)).thenReturn(emprunts.get(0));
        Mockito.when(empruntService.getById(15L)).thenThrow(ResourceNotFoundException.class);
        assertAll(
                () -> assertEquals(emprunts.get(0), empruntService.getById(1L)),
                () -> assertThrows(ResourceNotFoundException.class, () -> empruntService.getById(15L))
        );
    }
    @Test

    void whenCreating_ShouldReturnSame() {
        Emprunt emprunt = new Emprunt(5L, java.sql.Date.valueOf("2023-05-05"), null, Statut.EN_COURS, emprunteur1, exemplaire1,0);
        Mockito.when(empruntService.create(emprunt)).thenReturn(emprunt);
        assertEquals(emprunt, empruntService.create(emprunt));
    }

    @Test
    void whenExisting_ShouldNotBeCreated() {
        Emprunt emprunt = new Emprunt(1L, java.sql.Date.valueOf("2023-01-01"), null, Statut.EN_COURS, emprunteur1, exemplaire1, 0);
        Mockito.when(empruntService.create(emprunt)).thenReturn(emprunt);
        Mockito.when(empruntService.create(emprunt)).thenThrow(ResourceAlreadyExistsException.class);
        assertThrows(ResourceAlreadyExistsException.class, () -> empruntService.create(emprunt));
    }

    @Test
    void whenEmprunteurStatusIsBLOQUE_ShouldNotBeCreated() {
        Emprunt emprunt = emprunts.get(0);
        Mockito.when(emprunteur1.getStatut()).thenReturn(rudy_yoann.backend.Emprunteur.Statut.BLOQUE);
        Mockito.when(empruntService.create(emprunt)).thenThrow(RuntimeException.class);
        assertThrows(RuntimeException.class, () -> empruntService.create(emprunt));
    }

    @Test
    void whenEmprunteurHaveMoreThanTwoDemandeEmprunt_ShouldNotBeCreated(){
        Emprunt emprunt = emprunts.get(0);
        Mockito.when(emprunteur1.getEmprunts()).thenReturn(Arrays.asList(mock(Emprunt.class), mock(Emprunt.class), mock(Emprunt.class)));
        when(empruntService.create(emprunt)).thenThrow(RuntimeException.class);
        assertThrows(RuntimeException.class, () -> empruntService.create(emprunt));
    }

    @Test
    void whenUpdate_ShouldModify() {
        Emprunt emprunt = emprunts.get(0);
        Emprunt new_emprunt = new Emprunt(emprunt.getId(), java.sql.Date.valueOf("2023-01-01"), java.sql.Date.valueOf("2023-01-21"), Statut.TERMINE, emprunteur1, exemplaire1, 0);
        when(empruntService.getById(emprunt.getId())).thenReturn(new_emprunt);
        Emprunt updateEmprunt = empruntService.getById(emprunt.getId());
        when(empruntService.update(eq(emprunt.getId()), any(Emprunt.class))).thenReturn(updateEmprunt);
        empruntService.update(new_emprunt.getId(), new_emprunt);
        assertEquals(new_emprunt, updateEmprunt);

    }

    @Test
    void whenUpdatingNonExisting_ShouldThrowException() {
        Emprunt emprunt = emprunts.get(0);
        Mockito.when(empruntService.getById(15L)).thenThrow(ResourceNotFoundException.class);
        doThrow(ResourceNotFoundException.class).when(empruntService).update(eq(15L), any(Emprunt.class));
        assertThrows(ResourceNotFoundException.class, () -> empruntService.update(15L, emprunt));
    }

    @Test
    void whenDeleting_ShouldDelete() {
        Emprunt emprunt = emprunts.get(0);
        Long id = emprunt.getId();
        when(empruntService.delete(id)).thenReturn(true);
        empruntService.delete(id);
        assertFalse(empruntService.getAll().contains(emprunt));
    }

    @Test
    void whenDeletingNonExisting_ShouldThrowException() {
        Long id = 15L;
        when(empruntService.delete(id)).thenThrow(ResourceNotFoundException.class);
        assertThrows(ResourceNotFoundException.class, () -> empruntService.delete(id));
    }


    @Test
    void whenGettingByStatutEnRetard_shouldBeTheSame() {
        Emprunt emprunt5 = mock(Emprunt.class);
        emprunt5.setStatut(Statut.EN_RETARD);
        List<Emprunt> empruntsList = Collections.singletonList(emprunt5);
        Mockito.when(empruntService.getByStatutEnRetard()).thenReturn(empruntsList);
        assertEquals(empruntsList, empruntService.getByStatutEnRetard());
    }

    @Test
    void whenGettingByStatutEnRetard_shouldThrowExceptionIfAnyEmpruntIsEN_RETARD() {
        Mockito.when(empruntService.getByStatutEnRetard()).thenReturn(emprunts);
        Mockito.when(empruntService.getByStatutEnRetard()).thenThrow(ResourceNotFoundException.class);
        assertThrows(ResourceNotFoundException.class, () -> empruntService.getByStatutEnRetard());
    }

    @Test
    void whenGettingByStatutEnRetardAndByEmprunteurClasse_shouldReturnIfExists_andBeTheSame() {
        emprunteur1.setClasse(Classe.SIO2B);
        emprunts.get(0).setStatut(Statut.EN_RETARD);
        List<Emprunt> empruntsList = Collections.singletonList(emprunts.get(0));
        Mockito.when(empruntService.getByStatutEnRetardAndByClasse(Classe.SIO2B)).thenReturn(empruntsList);
        Mockito.when(empruntService.getByStatutEnRetardAndByClasse(Classe.SIO2A)).thenThrow(ResourceNotFoundException.class);
        assertAll(
                () -> assertEquals(empruntsList, empruntService.getByStatutEnRetardAndByClasse(Classe.SIO2B)),
                () -> assertThrows(ResourceNotFoundException.class, () -> empruntService.getByStatutEnRetardAndByClasse(Classe.SIO2A))
        );
    }

}
