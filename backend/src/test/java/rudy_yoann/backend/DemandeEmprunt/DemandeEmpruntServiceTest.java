package rudy_yoann.backend.DemandeEmprunt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import rudy_yoann.backend.Emprunteur.Emprunteur;
import rudy_yoann.backend.Exceptions.ResourceAlreadyExistsException;
import rudy_yoann.backend.Exceptions.ResourceNotFoundException;
import rudy_yoann.backend.Exemplaire.Exemplaire;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DemandeEmpruntRepository.class)
public class DemandeEmpruntServiceTest {

    @MockBean
    private DemandeEmpruntService demandeEmpruntService;
    private List<DemandeEmprunt> demandeEmprunts;
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

        demandeEmprunts = new ArrayList<>() {{
            add(new DemandeEmprunt(1L, java.sql.Date.valueOf("2023-01-01"), java.sql.Date.valueOf("2023-01-10"), Statut.EN_COURS, exemplaire1 , emprunteur1));
            add(new DemandeEmprunt(2L, java.sql.Date.valueOf("2023-02-02"), java.sql.Date.valueOf("2023-02-20"), Statut.VALIDEE, exemplaire2 , emprunteur2));
            add(new DemandeEmprunt(3L, java.sql.Date.valueOf("2023-03-03"), java.sql.Date.valueOf("2023-03-30"), Statut.REJETEE, exemplaire3 , emprunteur3));
        }};
    }


    @Test
    void whenGettingAll_shouldHave3DemandeEmprunts() {
        when(demandeEmpruntService.getAll()).thenReturn(demandeEmprunts);
        assertEquals(3, demandeEmpruntService.getAll().size());
    }

    @Test
    void whenGettingById_shouldReturnIfExists_andBeTheSame() {
        Mockito.when(demandeEmpruntService.getById(1L)).thenReturn(demandeEmprunts.get(0));
        Mockito.when(demandeEmpruntService.getById(15L)).thenThrow(ResourceNotFoundException.class);
        assertAll(
                () -> assertEquals(demandeEmprunts.get(0), demandeEmpruntService.getById(1L)),
                () -> assertThrows(ResourceNotFoundException.class, () -> demandeEmpruntService.getById(15L))
        );
    }
    @Test

    void whenCreating_ShouldReturnSame() {
        DemandeEmprunt demandeEmprunt = new DemandeEmprunt(5L, java.sql.Date.valueOf("2023-05-05"), java.sql.Date.valueOf("2023-05-25"), Statut.EN_COURS, exemplaire1 , emprunteur1);
        Mockito.when(demandeEmpruntService.create(demandeEmprunt)).thenReturn(demandeEmprunt);
        assertEquals(demandeEmprunt, demandeEmpruntService.create(demandeEmprunt));
    }

    @Test
    void whenExisting_ShouldNotBeCreated() {
        DemandeEmprunt demandeEmprunt = demandeEmprunts.get(0);
        Mockito.when(demandeEmpruntService.create(demandeEmprunt)).thenReturn(demandeEmprunt);
        Mockito.when(demandeEmpruntService.create(demandeEmprunt)).thenThrow(ResourceAlreadyExistsException.class);
        assertThrows(ResourceAlreadyExistsException.class, () -> demandeEmpruntService.create(demandeEmprunt));
    }

    @Test
    void whenEmprunteurStatusIsBLOQUE_ShouldNotBeCreated() {
        DemandeEmprunt demandeEmprunt = demandeEmprunts.get(0);
        Mockito.when(emprunteur1.getStatut()).thenReturn(rudy_yoann.backend.Emprunteur.Statut.BLOQUE);
        Mockito.when(demandeEmpruntService.create(demandeEmprunt)).thenThrow(RuntimeException.class);
        assertThrows(RuntimeException.class, () -> demandeEmpruntService.create(demandeEmprunt));
    }

    @Test
    void whenEmprunteurHaveMoreThanTwoDemandeEmprunt_ShouldNotBeCreated(){
        DemandeEmprunt demandeEmprunt = demandeEmprunts.get(0);
        Mockito.when(emprunteur1.getDemandeEmprunts()).thenReturn(Arrays.asList(mock(DemandeEmprunt.class), mock(DemandeEmprunt.class), mock(DemandeEmprunt.class)));
        when(demandeEmpruntService.create(demandeEmprunt)).thenThrow(RuntimeException.class);
        assertThrows(RuntimeException.class, () -> demandeEmpruntService.create(demandeEmprunt));
    }

    @Test
    void whenUpdate_ShouldModify() {
        DemandeEmprunt demandeEmprunt = demandeEmprunts.get(0);
        DemandeEmprunt new_demandeEmprunt = new DemandeEmprunt(demandeEmprunt.getId(), java.sql.Date.valueOf("2023-01-01"), java.sql.Date.valueOf("2023-01-21"), Statut.VALIDEE, exemplaire1, emprunteur1);
        when(demandeEmpruntService.getById(demandeEmprunt.getId())).thenReturn(new_demandeEmprunt);
        DemandeEmprunt updateDemandeEmprunt = demandeEmpruntService.getById(demandeEmprunt.getId());
        when(demandeEmpruntService.update(eq(demandeEmprunt.getId()), any(DemandeEmprunt.class))).thenReturn(updateDemandeEmprunt);
        demandeEmpruntService.update(new_demandeEmprunt.getId(), new_demandeEmprunt);
        assertEquals(new_demandeEmprunt, updateDemandeEmprunt);

    }

    @Test
    void whenUpdatingNonExisting_ShouldThrowException() {
        DemandeEmprunt demandeEmprunt = demandeEmprunts.get(0);
        Mockito.when(demandeEmpruntService.getById(15L)).thenThrow(ResourceNotFoundException.class);
        doThrow(ResourceNotFoundException.class).when(demandeEmpruntService).update(eq(15L), any(DemandeEmprunt.class));
        assertThrows(ResourceNotFoundException.class, () -> demandeEmpruntService.update(15L, demandeEmprunt));
    }

    @Test
    void whenDeleting_ShouldDelete() {
        DemandeEmprunt demandeEmprunt = demandeEmprunts.get(0);
        Long id = demandeEmprunt.getId();
        when(demandeEmpruntService.delete(id)).thenReturn(true);
        demandeEmpruntService.delete(id);
        assertFalse(demandeEmpruntService.getAll().contains(demandeEmprunt));
    }

    @Test
    void whenDeletingNonExisting_ShouldThrowException() {
        Long id = 15L;
        when(demandeEmpruntService.delete(id)).thenThrow(ResourceNotFoundException.class);
        assertThrows(ResourceNotFoundException.class, () -> demandeEmpruntService.delete(id));
    }

    @Test
    void whenGettingByEmprunteur_shouldReturnSizeIfExists() {
        DemandeEmprunt demandeEmprunt = new DemandeEmprunt(5L, java.sql.Date.valueOf("2023-05-05"), java.sql.Date.valueOf("2023-05-25"), Statut.EN_COURS, exemplaire1 , emprunteur1);
        Emprunteur emprunteur5 = mock(Emprunteur.class);

        List<DemandeEmprunt> listDemandeEmprunt = new ArrayList<>()    {{
            add (demandeEmprunts.get(0));
            add (demandeEmprunt);
        }};

        Mockito.when(demandeEmpruntService.getByEmprunteur(emprunteur1)).thenReturn(listDemandeEmprunt);
        Mockito.when(demandeEmpruntService.getByEmprunteur(emprunteur5)).thenThrow(ResourceNotFoundException.class);
        assertAll(
                () -> assertEquals(2, demandeEmpruntService.getByEmprunteur(emprunteur1).size()),
                () -> assertThrows(ResourceNotFoundException.class, () -> demandeEmpruntService.getByEmprunteur(emprunteur5))
        );
    }

}
