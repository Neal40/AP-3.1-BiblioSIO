package rudy_yoann.backend.Revue;

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
@ContextConfiguration(classes = RevueController.class)
@Import(ExceptionHandlingAdvice.class)
public class RevueControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    RevueService revueService;

    private List<Revue> revues;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        revues = new ArrayList<>() {{
            add(new Revue(1L, "titre1"));
            add(new Revue(2L, "titre2"));
            add(new Revue(3L, "titre3"));
            add(new Revue(4L, "titre4"));
            add(new Revue(5L, "titre5"));
        }};
    }


    @Test
    void whenGettingAll_shouldGet5() throws Exception {
        //On vérifie que lorsqu'on get tous les todos cela les renvoie bien tous
        when(revueService.getAll()).thenReturn(revues);
        mockMvc.perform(get("/revues")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()
        ).andExpect(jsonPath("$", hasSize(5))
        ).andDo(print());
    }

    @Test
    void whenGetWithCode1_shouldReturnTodo1() throws Exception {
        //On vérifie que si on get un todo avec une id spécifique dans son url, ça renvoie bien ce todo
        when(revueService.getById(5L)).thenReturn(revues.get(4));
        mockMvc.perform(get("/revues/5")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(content().contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()
        ).andExpect(jsonPath("$", hasEntry("id", 5))
        ).andDo(print());
    }

    @Test
    void whenGettingUnexistingId_should404() throws Exception {
        //On vérifie que si on get un todo avec une id inexistante dans son url, ça renvoie bien une exception
        when(revueService.getById(11L)).thenThrow(ResourceNotFoundException.class);
        mockMvc.perform(get("/revues/11")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound()
        ).andDo(print());
    }

    @Test
    void whenCreatingIssue_shouldGetLinkToResource() throws Exception {
        //On vérifie qu'à la création d'un todo, l'url existe bien
        Revue revue = new Revue(5L, "titre5");
        Mockito.when(revueService.create(Mockito.any(Revue.class))).thenReturn(revue);

        String toSend = mapper.writeValueAsString(revue);

        mockMvc.perform(post("/revues")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toSend)
        ).andExpect(status().isCreated()
        ).andExpect(header().string("Location", "/revues/" + revue.getId())
        ).andDo(print()).andReturn();
    }

    @Test
    void whenCreatingWithExistingId_should404() throws Exception {
        //On vérifie que si on create un emprunteur avec un id existant, ça renvoie une exception
        when(revueService.create(any())).thenThrow(ResourceAlreadyExistsException.class);
        mockMvc.perform(post("/revues")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(this.revues.get(2)))
        ).andExpect(status().isConflict()
        ).andDo(print());
    }


    @Test
    void whenUpdating_shouldReceiveTodoToUpdate_andReturnNoContent() throws Exception {
        Revue initial_revue = new Revue(6L, "titre6");
        Revue updated_revue = new Revue(initial_revue.getId(), "Updaté");
        ArgumentCaptor<Revue> revue_received = ArgumentCaptor.forClass(Revue.class);

        //Lorsque la méthode update est appelé sur n'importe quel id et n'importe quel todo, on renvoie updated_todo
        when(revueService.update(anyLong(), any(Revue.class))).thenReturn(updated_revue);
        mockMvc.perform(put("/revues")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updated_revue))
        ).andExpect(status().isNoContent());

        verify(revueService).update(anyLong(), revue_received.capture());
        assertEquals(updated_revue, revue_received.getValue());
    }


    @Test
    void whenDeletingExisting_shouldCallServiceWithCorrectId_andSendNoContent() throws Exception {
        Long id = 28L;

        //Lorsque la méthode delete est appelé, on renvoie true (lorsque le delete est effectué, notre méthode renvoie true)
        when(revueService.delete(eq(id))).thenReturn(true);
        mockMvc.perform(delete("/revues/" + id)
        ).andExpect(status().isNoContent()
        ).andDo(print());

        ArgumentCaptor<Long> id_received = ArgumentCaptor.forClass(Long.class);
        verify(revueService).delete(id_received.capture());
        assertEquals(id, id_received.getValue());
    }

    @Test
    void whenDeleteNonExistingResource_shouldGet404() throws Exception {
        //Gestion d'une exception pour la méthode delete avec mockito
        //Lorsque la méthode delete est appelée, on demande à moquiter de simuler l'exception
        Mockito.doThrow(ResourceNotFoundException.class).when(revueService).delete(anyLong());

        mockMvc.perform(delete("/revues/972")
        ).andExpect(status().isNotFound()
        ).andDo(print()).andReturn();
    }
}
