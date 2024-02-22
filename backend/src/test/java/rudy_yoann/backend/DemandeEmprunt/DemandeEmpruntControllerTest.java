package rudy_yoann.backend.DemandeEmprunt;

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
import rudy_yoann.backend.Emprunt.Emprunt;
import rudy_yoann.backend.Emprunt.EmpruntController;
import rudy_yoann.backend.Emprunt.EmpruntService;
import rudy_yoann.backend.Emprunteur.Emprunteur;
import rudy_yoann.backend.Exceptions.ExceptionHandlingAdvice;
import rudy_yoann.backend.Exceptions.ResourceAlreadyExistsException;
import rudy_yoann.backend.Exceptions.ResourceNotFoundException;
import rudy_yoann.backend.Exemplaire.Exemplaire;

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
@ContextConfiguration(classes = DemandeEmpruntController.class)
@Import(ExceptionHandlingAdvice.class)
public class DemandeEmpruntControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    DemandeEmpruntService demandeEmpruntService;
    private List<DemandeEmprunt> demandeEmprunts;
    private Emprunteur emprunteur = Mockito.mock(Emprunteur.class);
    private Exemplaire exemplaire = Mockito.mock(Exemplaire.class);
    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        demandeEmprunts = new ArrayList<>() {{
            add(new DemandeEmprunt(1L, java.sql.Date.valueOf("2023-01-01"), java.sql.Date.valueOf("2023-01-10"), rudy_yoann.backend.DemandeEmprunt.Statut.EN_COURS, exemplaire , emprunteur));
            add(new DemandeEmprunt(2L, java.sql.Date.valueOf("2023-02-02"), java.sql.Date.valueOf("2023-02-20"), rudy_yoann.backend.DemandeEmprunt.Statut.VALIDEE, exemplaire , emprunteur));
            add(new DemandeEmprunt(3L, java.sql.Date.valueOf("2023-03-03"), java.sql.Date.valueOf("2023-03-30"), rudy_yoann.backend.DemandeEmprunt.Statut.REJETEE, exemplaire , emprunteur));
        }};
    }
    @Test
    void whenGettingAll_shouldGet3() throws Exception {
        Mockito.when(demandeEmpruntService.getAll()).thenReturn(demandeEmprunts);
        mockMvc.perform(get("/demandeEmprunts")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()
        ).andExpect(jsonPath("$", hasSize(3))
        ).andDo(print());
    }

    @Test
    void whenGetWithCode1_shouldReturnEmprunt1() throws Exception {
        Mockito.when(demandeEmpruntService.getById(3L)).thenReturn(demandeEmprunts.get(2));
        mockMvc.perform(get("/demandeEmprunts/3")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(content().contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()
        ).andExpect(jsonPath("$", hasEntry("id", 3))
        ).andDo(print());
    }

    @Test
    void whenGettingUnexistingId_should404() throws Exception {
        Mockito.when(demandeEmpruntService.getById(11L)).thenThrow(ResourceNotFoundException.class);
        mockMvc.perform(get("/demandeEmprunts/11")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound()
        ).andDo(print());
    }

    @Test
    void whenCreatingIssue_shouldGetLinkToResource() throws Exception {

        DemandeEmprunt demandeEmprunt = new DemandeEmprunt(5L, java.sql.Date.valueOf("2023-05-05"), java.sql.Date.valueOf("2023-05-25"), rudy_yoann.backend.DemandeEmprunt.Statut.EN_COURS, exemplaire , emprunteur);
        Mockito.when(demandeEmpruntService.create(any(DemandeEmprunt.class))).thenReturn(demandeEmprunt);

        String toSend = mapper.writeValueAsString(demandeEmprunt);

        mockMvc.perform(post("/demandeEmprunts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toSend)
        ).andExpect(status().isCreated()
        ).andExpect(header().string("Location", "/demandeEmprunts/" + demandeEmprunt.getId())
        ).andDo(print()).andReturn();
    }

    @Test
    void whenCreatingWithExistingId_should404() throws Exception {
        Mockito.when(demandeEmpruntService.create(any())).thenThrow(ResourceAlreadyExistsException.class);
        mockMvc.perform(post("/demandeEmprunts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(this.demandeEmprunts.get(2)))
        ).andExpect(status().isConflict()
        ).andDo(print());
    }

    @Test
    void whenUpdating_shouldReceiveTodoToUpdate_andReturnNoContent() throws Exception {
        DemandeEmprunt initial_demandeEmprunt = demandeEmprunts.get(0);
        DemandeEmprunt updated_demandeEmprunt = new DemandeEmprunt(initial_demandeEmprunt.getId(), java.sql.Date.valueOf("2023-04-04"), java.sql.Date.valueOf("2023-05-05"), Statut.VALIDEE, initial_demandeEmprunt.getExemplaire(), initial_demandeEmprunt.getEmprunteur());
        ArgumentCaptor<DemandeEmprunt> demandeEmprunt_received = ArgumentCaptor.forClass(DemandeEmprunt.class);

        Mockito.when(demandeEmpruntService.update(anyLong(), any(DemandeEmprunt.class))).thenReturn(updated_demandeEmprunt);
        mockMvc.perform(put("/demandeEmprunts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updated_demandeEmprunt))
        ).andExpect(status().isNoContent());

        verify(demandeEmpruntService).update(anyLong(), demandeEmprunt_received.capture());
        assertEquals(updated_demandeEmprunt.getId(), demandeEmprunt_received.getValue().getId());
    }

    @Test
    void whenDeletingExisting_shouldCallServiceWithCorrectId_andSendNoContent() throws Exception {
        Long id = 28L;
        when(demandeEmpruntService.delete(eq(id))).thenReturn(true);
        mockMvc.perform(delete("/demandeEmprunts/" + id)
        ).andExpect(status().isNoContent()
        ).andDo(print());

        ArgumentCaptor<Long> id_received = ArgumentCaptor.forClass(Long.class);
        verify(demandeEmpruntService).delete(id_received.capture());
        assertEquals(id, id_received.getValue());
    }

    @Test
    void whenDeleteNonExistingResource_shouldGet404() throws Exception {
        Mockito.doThrow(ResourceNotFoundException.class).when(demandeEmpruntService).delete(anyLong());

        mockMvc.perform(delete("/demandeEmprunts/972")
        ).andExpect(status().isNotFound()
        ).andDo(print()).andReturn();
    }

}
