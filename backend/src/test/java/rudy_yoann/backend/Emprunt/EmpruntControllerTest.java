package rudy_yoann.backend.Emprunt;

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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import rudy_yoann.backend.Emprunteur.Emprunteur;
import rudy_yoann.backend.Exceptions.ExceptionHandlingAdvice;
import rudy_yoann.backend.Exceptions.ResourceAlreadyExistsException;
import rudy_yoann.backend.Exceptions.ResourceNotFoundException;
import rudy_yoann.backend.Exemplaire.Exemplaire;
import rudy_yoann.backend.Revue.Revue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EmpruntController.class)
@ContextConfiguration(classes = EmpruntController.class)
@Import(ExceptionHandlingAdvice.class)
public class EmpruntControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    EmpruntService empruntService;
    private List<Emprunt> emprunts;
    private Emprunteur emprunteur = Mockito.mock(Emprunteur.class);
    private Exemplaire exemplaire = Mockito.mock(Exemplaire.class);
    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        emprunts = new ArrayList<>() {{
            add(new Emprunt(1L, java.sql.Date.valueOf("2023-01-01"), null, Statut.EN_COURS, emprunteur, exemplaire, 0));
            add(new Emprunt(2L, java.sql.Date.valueOf("2023-02-01"), null, Statut.EN_COURS, emprunteur, exemplaire, 0));
            add(new Emprunt(3L, java.sql.Date.valueOf("2023-03-01"), java.sql.Date.valueOf("2023-03-15"), Statut.TERMINE, emprunteur, exemplaire,0));
        }};
    }
    @Test
    void whenGettingAll_shouldGet3() throws Exception {
        Mockito.when(empruntService.getAll()).thenReturn(emprunts);
        mockMvc.perform(get("/emprunts")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()
        ).andExpect(jsonPath("$", hasSize(3))
        ).andDo(print());
    }

    @Test
    void whenGetWithCode1_shouldReturnEmprunt1() throws Exception {
        Mockito.when(empruntService.getById(3L)).thenReturn(emprunts.get(2));
        mockMvc.perform(get("/emprunts/3")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(content().contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()
        ).andExpect(jsonPath("$", hasEntry("id", 3))
        ).andDo(print());
    }

    @Test
    void whenGettingUnexistingId_should404() throws Exception {
        Mockito.when(empruntService.getById(11L)).thenThrow(ResourceNotFoundException.class);
        mockMvc.perform(get("/emprunts/11")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound()
        ).andDo(print());
    }

    @Test
    void whenCreatingIssue_shouldGetLinkToResource() throws Exception {

        Emprunt emprunt = new Emprunt(10L, java.sql.Date.valueOf("2023-10-10"), null, Statut.TERMINE, emprunteur, exemplaire,0);
        Mockito.when(empruntService.create(any(Emprunt.class))).thenReturn(emprunt);

        String toSend = mapper.writeValueAsString(emprunt);

        mockMvc.perform(post("/emprunts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toSend)
        ).andExpect(status().isCreated()
        ).andExpect(header().string("Location", "/emprunts/" + emprunt.getId())
        ).andDo(print()).andReturn();
    }

    @Test
    void whenCreatingWithExistingId_should404() throws Exception {
        Mockito.when(empruntService.create(any())).thenThrow(ResourceAlreadyExistsException.class);
        mockMvc.perform(post("/emprunts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(this.emprunts.get(2)))
        ).andExpect(status().isConflict()
        ).andDo(print());
    }

    @Test
    void whenUpdating_shouldReceiveTodoToUpdate_andReturnNoContent() throws Exception {
        Emprunt initial_emprunt = new Emprunt(4L, java.sql.Date.valueOf("2023-04-04"), null, Statut.EN_COURS, emprunteur, exemplaire,0);
        Emprunt updated_emprunt = new Emprunt(initial_emprunt.getId(), java.sql.Date.valueOf("2023-04-04"), java.sql.Date.valueOf("2023-05-05"), Statut.TERMINE, initial_emprunt.getEmprunteur(), initial_emprunt.getExemplaire(), initial_emprunt.getDuree());
        ArgumentCaptor<Emprunt> emprunt_received = ArgumentCaptor.forClass(Emprunt.class);

        Mockito.when(empruntService.update(anyLong(), any(Emprunt.class))).thenReturn(updated_emprunt);
        mockMvc.perform(put("/emprunts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updated_emprunt))
        ).andExpect(status().isNoContent());

        verify(empruntService).update(anyLong(), emprunt_received.capture());
        assertEquals(updated_emprunt.getId(), emprunt_received.getValue().getId());
    }

    @Test
    void whenDeletingExisting_shouldCallServiceWithCorrectId_andSendNoContent() throws Exception {
        Long id = 28L;
        when(empruntService.delete(eq(id))).thenReturn(true);
        mockMvc.perform(delete("/emprunts/" + id)
        ).andExpect(status().isNoContent()
        ).andDo(print());

        ArgumentCaptor<Long> id_received = ArgumentCaptor.forClass(Long.class);
        verify(empruntService).delete(id_received.capture());
        assertEquals(id, id_received.getValue());
    }

    @Test
    void whenDeleteNonExistingResource_shouldGet404() throws Exception {

        Mockito.doThrow(ResourceNotFoundException.class).when(empruntService).delete(anyLong());

        mockMvc.perform(delete("/emprunts/972")
        ).andExpect(status().isNotFound()
        ).andDo(print()).andReturn();
    }

}
