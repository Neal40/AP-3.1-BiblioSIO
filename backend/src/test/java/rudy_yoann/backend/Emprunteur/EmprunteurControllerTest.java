package rudy_yoann.backend.Emprunteur;

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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@ContextConfiguration(classes = EmprunteurController.class)
@Import(ExceptionHandlingAdvice.class)
public class EmprunteurControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    EmprunteurService emprunteurService;

    private List<Emprunteur> emprunteurs;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        emprunteurs = new ArrayList<>() {{
            add(new Emprunteur(1L, "Nom1", "Prenom1", "Mail1", Role.etudiant, Classe.SIO1B,"2023", Statut.NON_BLOQUE));
            add(new Emprunteur(2L, "Nom2", "Prenom2", "Mail2",  Role.etudiant, Classe.SIO2A,"2023", Statut.NON_BLOQUE));
            add(new Emprunteur(3L, "Nom3", "Prenom3", "Mail3", Role.professeur, Classe.PROF, null, Statut.NON_BLOQUE));
        }};
    }

    @Test
    void whenGettingAll_shouldGet3() throws Exception {
        //On vérifie que lorsqu'on GET, tous les emprunteurs sont renvoyés
        when(emprunteurService.getAll()).thenReturn(emprunteurs);
        mockMvc.perform(get("/emprunteurs")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()
        ).andExpect(jsonPath("$", hasSize(3))
        ).andDo(print());
    }

    @Test
    void whenGetWithCode1_shouldReturnTodo1() throws Exception {
        //On vérifie que si on GET un emprunteur avec un id spécifique dans son url, ça renvoie bien cet emprunteur
        when(emprunteurService.getById(3L)).thenReturn(emprunteurs.get(2));
        mockMvc.perform(get("/emprunteurs/3")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(content().contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()
        ).andExpect(jsonPath("$", hasEntry("id", 3))
        ).andDo(print());
    }

    @Test
    void whenGettingUnexistingId_Should404() throws Exception {
        //On vérifie que si on GET un emprunteur avec un id inexistant dans son url, ça renvoie bien une exception
        when(emprunteurService.getById(11L)).thenThrow(ResourceNotFoundException.class);
        mockMvc.perform(get("/emprunteurs/11")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound()
        ).andDo(print());
    }

    @Test
    void whenCreatingIssue_shouldGetLinkToResource() throws Exception {
        //On vérifie qu'à la création d'un emprunteur, l'url existe bien
        Emprunteur emprunteur = new Emprunteur(10L, "Nom10", "Prenom10", "Mail10", Role.etudiant, Classe.SIO2A, "2023", Statut.NON_BLOQUE);
        Mockito.when(emprunteurService.create(Mockito.any(Emprunteur.class))).thenReturn(emprunteur);

        String toSend = mapper.writeValueAsString(emprunteur);

        mockMvc.perform(post("/emprunteurs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toSend)
        ).andExpect(status().isCreated()
        ).andExpect(header().string("Location", "/emprunteurs/" + emprunteur.getId())
        ).andDo(print()).andReturn();
    }

    @Test
    void whenCreatingWithExistingId_should404() throws Exception {
        //On vérifie que si on create un emprunteur avec un id existante, ça renvoie une exception
        when(emprunteurService.create(any())).thenThrow(ResourceAlreadyExistsException.class);
        mockMvc.perform(post("/emprunteurs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(this.emprunteurs.get(2)))
        ).andExpect(status().isConflict()
        ).andDo(print());
    }

    @Test
    void whenUpdating_shouldReceiveTodoToUpdate_andReturnNoContent() throws Exception {
        //Lorsque la méthode UPDATE est appelé sur n'importe quel id et n'importe quel emprunteur, on renvoie bien updated_emprunteur
        Emprunteur initial_emprunteur = new Emprunteur(4L, "Nom4", "Prenom4", "Mail4", Role.etudiant, Classe.SIO1B, "2023", Statut.NON_BLOQUE);
        Emprunteur updated_emprunteur = new Emprunteur(initial_emprunteur.getId(), "Nom_Updaté", "Prénom_Updaté", "Mail_Updaté", Role.professeur, Classe.PROF, "2024", Statut.NON_BLOQUE);
        ArgumentCaptor<Emprunteur> emprunteur_received = ArgumentCaptor.forClass(Emprunteur.class);

        when(emprunteurService.update(anyLong(), any(Emprunteur.class))).thenReturn(updated_emprunteur);
        mockMvc.perform(put("/emprunteurs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updated_emprunteur))
        ).andExpect(status().isNoContent());

        verify(emprunteurService).update(anyLong(), emprunteur_received.capture());
        assertEquals(updated_emprunteur, emprunteur_received.getValue());
    }

    @Test
    void whenDeletingExisting_ShouldCallServiceWithCorrectId_andSendNoContent() throws Exception {
        Long id = 28L;
        //Lorsque la méthode delete est appelé, on renvoie true (lorsque le delete est effectué, notre méthode renvoie true)
        when(emprunteurService.delete(eq(id))).thenReturn(true);
        mockMvc.perform(delete("/emprunteurs/" + id)
        ).andExpect(status().isNoContent()
        ).andDo(print());

        ArgumentCaptor<Long> id_received = ArgumentCaptor.forClass(Long.class);
        verify(emprunteurService).delete(id_received.capture());
        assertEquals(id, id_received.getValue());
    }

    @Test
    void whenDeleteNonExistingResource_shouldGet404() throws Exception {
        //Gestion d'une exception pour la méthode delete avec mockito
        //Lorsque la méthode delete est appelée, on demande à moquiter de simuler l'exception
        Mockito.doThrow(ResourceNotFoundException.class).when(emprunteurService).delete(anyLong());

        mockMvc.perform(delete("/emprunteurs/972")
        ).andExpect(status().isNotFound()
        ).andDo(print()).andReturn();
    }
}