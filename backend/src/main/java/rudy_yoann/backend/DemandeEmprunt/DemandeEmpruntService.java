package rudy_yoann.backend.DemandeEmprunt;

import org.springframework.stereotype.Service;
import rudy_yoann.backend.Emprunteur.Emprunteur;
import rudy_yoann.backend.Exceptions.ResourceAlreadyExistsException;
import rudy_yoann.backend.Exceptions.ResourceNotFoundException;

import java.util.List;

@Service
public interface DemandeEmpruntService {
    List<DemandeEmprunt> getAll();

    DemandeEmprunt getById(Long id) throws ResourceNotFoundException;

    DemandeEmprunt create(DemandeEmprunt newDemande_Emprunt) throws ResourceAlreadyExistsException;

    DemandeEmprunt update(Long id, DemandeEmprunt updatedDemande_Emprunt) throws ResourceNotFoundException;

    boolean delete(Long id) throws ResourceNotFoundException;

    List<DemandeEmprunt> getByEmprunteur(Emprunteur emprunteur) throws ResourceNotFoundException;
}
