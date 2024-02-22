package rudy_yoann.backend.Exemplaire;

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
import rudy_yoann.backend.Revue.Revue;

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
@ContextConfiguration(classes = ExemplaireController.class)
@Import(ExceptionHandlingAdvice.class)
public class ExemplaireControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ExemplaireService exemplaireService;
    private List<Exemplaire> exemplaires;
    private ObjectMapper mapper = new ObjectMapper();
    private Revue revue = Mockito.mock(Revue.class);

    @BeforeEach
    void setUp() {
        exemplaires = new ArrayList<>() {{
            add(new Exemplaire(1L, revue, "titre1", "Janvier", "2001", Statut.DISPONIBLE));
            add(new Exemplaire(2L, revue, "titre2", "Février", "2002", Statut.INDISPONIBLE));
            add(new Exemplaire(3L, revue, "titre3", "Mars", "2003", Statut.DISPONIBLE));
            add(new Exemplaire(4L, revue, "titre4", "Avril", "2004", Statut.INDISPONIBLE));
            add(new Exemplaire(5L, revue, "titre5", "Mai", "2005", Statut.DISPONIBLE));
        }};
    }

    @Test
    void whenGettingAll_shouldGet5() throws Exception {
        //On vérifie que lorsqu'on get tous les todos cela les renvoie bien tous
        when(exemplaireService.getAll()).thenReturn(exemplaires);
        mockMvc.perform(get("/exemplaires")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()
        ).andExpect(jsonPath("$", hasSize(5))
        ).andDo(print());
    }

    @Test
    void whenGetWithCode1_shouldReturnTodo1() throws Exception {
        //On vérifie que si on get un todo avec une id spécifique dans son url, ça renvoie bien ce todo
        when(exemplaireService.getById(5L)).thenReturn(exemplaires.get(4));
        mockMvc.perform(get("/exemplaires/5")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(content().contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()
        ).andExpect(jsonPath("$", hasEntry("id", 5))
        ).andDo(print());
    }

    @Test
    void whenCreatingIssue_shouldGetLinkToResource() throws Exception {
        //On vérifie qu'à la création d'un todo, l'url existe bien
        Exemplaire exemplaire = new Exemplaire(5L, revue, "titre5", "Mai", "2005", Statut.DISPONIBLE);
        Mockito.when(exemplaireService.create(Mockito.any(Exemplaire.class))).thenReturn(exemplaire);

        String toSend = mapper.writeValueAsString(exemplaire);

        mockMvc.perform(post("/exemplaires")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toSend)
        ).andExpect(status().isCreated()
        ).andExpect(header().string("Location", "/exemplaires/" + exemplaire.getId())
        ).andDo(print()).andReturn();
    }

    @Test
    void whenCreatingWithExistingId_should404() throws Exception {
        //On vérifie que si on create un todo avec une id existante, ça renvoie une exception
        when(exemplaireService.create(any())).thenThrow(ResourceAlreadyExistsException.class);
        mockMvc.perform(post("/exemplaires")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(this.exemplaires.get(2)))
        ).andExpect(status().isConflict()
        ).andDo(print());
    }


    @Test
    void whenUpdating_shouldReceiveTodoToUpdate_andReturnNoContent() throws Exception {
        Exemplaire initial_exemplaire = new Exemplaire(6L, revue, "titre6", "Juin", "2006", Statut.INDISPONIBLE);
        Exemplaire updated_exemplaire = new Exemplaire(initial_exemplaire.getId(), initial_exemplaire.getRevue(), "Updaté", initial_exemplaire.getMoisParution(), initial_exemplaire.getAnneeParution(), initial_exemplaire.getStatut());
        ArgumentCaptor<Exemplaire> exemplaire_received = ArgumentCaptor.forClass(Exemplaire.class);

        //Lorsque la méthode update est appelé sur n'importe quel id et n'importe quel todo, on renvoie updated_todo
        when(exemplaireService.update(anyLong(), any(Exemplaire.class))).thenReturn(updated_exemplaire);
        mockMvc.perform(put("/exemplaires")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updated_exemplaire))
        ).andExpect(status().isNoContent());

        verify(exemplaireService).update(anyLong(), exemplaire_received.capture());
        assertEquals(updated_exemplaire.getId(), exemplaire_received.getValue().getId());
    }



    @Test
    void whenDeletingExisting_shouldCallServiceWithCorrectId_andSendNoContent() throws Exception {
        Long id = 28L;
        //Lorsque la méthode delete est appelé, on renvoie true (lorsque le delete est effectué, notre méthode renvoie true)
        when(exemplaireService.delete(eq(id))).thenReturn(true);
        mockMvc.perform(delete("/exemplaires/" + id)
        ).andExpect(status().isNoContent()
        ).andDo(print());

        ArgumentCaptor<Long> id_received = ArgumentCaptor.forClass(Long.class);
        verify(exemplaireService).delete(id_received.capture());
        assertEquals(id, id_received.getValue());
    }

    @Test
    void whenDeleteNonExistingResource_shouldGet404() throws Exception {
        //Gestion d'une exception pour la méthode delete avec mockito
        //Lorsque la méthode delete est appelée, on demande à moquiter de simuler l'exception
        Mockito.doThrow(ResourceNotFoundException.class).when(exemplaireService).delete(anyLong());

        mockMvc.perform(delete("/exemplaires/972")
        ).andExpect(status().isNotFound()
        ).andDo(print()).andReturn();
    }
}
