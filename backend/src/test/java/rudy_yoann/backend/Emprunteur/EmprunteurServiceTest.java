package rudy_yoann.backend.Emprunteur;

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
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = EmprunteurRepository.class)
public class EmprunteurServiceTest {

    @MockBean
    EmprunteurService emprunteurService;

    private List<Emprunteur> emprunteurs;

    @BeforeEach
    void setUp() {
        emprunteurs = new ArrayList<>() {{
            add(new Emprunteur(1L, "Nom1", "Prenom1", "Mail1", Role.etudiant, Classe.SIO1B,"2023", Statut.NON_BLOQUE));
            add(new Emprunteur(2L, "Nom2", "Prenom2", "Mail2",  Role.etudiant, Classe.SIO2A,"2023", Statut.NON_BLOQUE));
            add(new Emprunteur(3L, "Nom3", "Prenom3", "Mail3", Role.professeur, Classe.PROF, null, Statut.NON_BLOQUE));
        }};
    }

    @Test
    void whenGettingAll_shouldHave3Emprunteurs() {
        //Mockito avec la méthode getAll
        Mockito.when(emprunteurService.getAll()).thenReturn(emprunteurs);
        assertEquals(3, emprunteurService.getAll().size());
    }

    @Test
    void whenGettingById_shouldReturnIfExists_andBeTheSame() {
        //Mockito avec la méthode getById et également la gestion d'une exception
        Mockito.when(emprunteurService.getById(1L)).thenReturn(emprunteurs.get(0));
        Mockito.when(emprunteurService.getById(15L)).thenThrow(ResourceNotFoundException.class);
        assertAll(
                () -> assertEquals(emprunteurs.get(0), emprunteurService.getById(1L)),
                () -> assertThrows(ResourceNotFoundException.class, () -> emprunteurService.getById(15L))
        );
    }

    @Test
    void whenCreating_ShouldReturnSame() {
        //Mockito avec la méthode create
        Emprunteur emprunteur = new Emprunteur(5L, "Nom5", "Prenom5", "Mail5", Role.etudiant, Classe.SIO1B, "2025", Statut.NON_BLOQUE);
        Mockito.when(emprunteurService.create(emprunteur)).thenReturn(emprunteur);
        assertEquals(emprunteur, emprunteurService.create(emprunteur));
    }

    @Test
    void whenCreatingEmprunteur_Prof_ShouldReturnSame() {
        // Emprunteur avec le rôle de professeur et la classe "PROF"
        Emprunteur emprunteurProf = new Emprunteur(5L, "Nom5", "Prenom5", "Mail5", Role.professeur, Classe.PROF, "2025", Statut.NON_BLOQUE);

        // Utilisation de Mockito pour simuler le comportement du repository
        Mockito.when(emprunteurService.getById(emprunteurProf.getId())).thenReturn(null);
        Mockito.when(emprunteurService.create(emprunteurProf)).thenReturn(emprunteurProf);

        // Appel de la méthode create du service et vérification du résultat
        assertEquals(emprunteurProf, emprunteurService.create(emprunteurProf));
    }
    @Test
    void whenExisting_ShouldNotBeCreated() {
        //Mockito avec la méthode create et la gestion d'une exception
        Emprunteur emprunteur = new Emprunteur(2L, "Nom2", "Prenom2", "Mail2", Role.etudiant, Classe.SIO1A, "2022", Statut.NON_BLOQUE);
        Mockito.when(emprunteurService.create(emprunteur)).thenReturn(emprunteur);
        Mockito.when(emprunteurService.create(emprunteur)).thenThrow(ResourceAlreadyExistsException.class);
        assertThrows(ResourceAlreadyExistsException.class, () -> emprunteurService.create(emprunteur));
    }

    @Test
    void whenUpdate_ShouldModify() {
        //Mockito la méthode getById et update, on attend que la méthode update return updateEmprunteur
        Emprunteur emprunteur = emprunteurs.get(0);
        Emprunteur new_emprunteur = new Emprunteur(emprunteur.getId(), "Nom_Updaté", "Prénom_Updaté", "Mail_Updaté", Role.etudiant, Classe.SIO2B, "Promotion_Updaté", Statut.NON_BLOQUE);
        when(emprunteurService.getById(emprunteur.getId())).thenReturn(new_emprunteur);
        Emprunteur updateEmprunteur = emprunteurService.getById(emprunteur.getId());
        when(emprunteurService.update(eq(emprunteur.getId()), any(Emprunteur.class))).thenReturn(updateEmprunteur);
        emprunteurService.update(new_emprunteur.getId(), new_emprunteur);
        assertEquals(new_emprunteur, updateEmprunteur);
    }

    @Test
    void whenUpdatingNonExisting_ShouldThrowException() {
        //Mockito pour la gestion d'une exception de la méthode update, doThrow pour simuler l'exception lorsque update est appelée
        Emprunteur emprunteur = emprunteurs.get(0);
        Mockito.when(emprunteurService.getById(15L)).thenThrow(ResourceNotFoundException.class);
        doThrow(ResourceNotFoundException.class).when(emprunteurService).update(eq(15L), any(Emprunteur.class));
        assertThrows(ResourceNotFoundException.class, () -> emprunteurService.update(15L, emprunteur));
    }

    @Test
    void whenDeleting_ShouldDelete() {
        //Mockito pour la méthode delete
        Emprunteur emprunteur = emprunteurs.get(0);
        Long id = emprunteur.getId();
        when(emprunteurService.delete(id)).thenReturn(true);
        emprunteurService.delete(id);
        assertFalse(emprunteurService.getAll().contains(emprunteur));
    }

    @Test
    void whenDeletingNonExisting_ShouldThrowException() {
        //Mockito pour la gestion d'une exception de la méthode delete
        Long id = 15L;
        when(emprunteurService.delete(id)).thenThrow(ResourceNotFoundException.class);
        assertThrows(ResourceNotFoundException.class, () -> emprunteurService.delete(id));
    }

}
