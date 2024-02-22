package rudy_yoann.backend.Emprunteur;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import rudy_yoann.backend.Exceptions.ResourceAlreadyExistsException;
import rudy_yoann.backend.Exceptions.ResourceNotFoundException;
import rudy_yoann.backend.Exemplaire.Exemplaire;

import java.util.List;

@Service
public interface EmprunteurService {
    List<Emprunteur> getAll();

    Emprunteur getById(Long id) throws ResourceNotFoundException;

    Emprunteur create(Emprunteur newEmprunteur) throws ResourceAlreadyExistsException;

    Emprunteur update(Long id, Emprunteur updatedEmprunteur) throws ResourceNotFoundException;

    boolean delete(Long id) throws ResourceNotFoundException;

    //Visualisation des Emprunteurs - Begin



    // END

}
