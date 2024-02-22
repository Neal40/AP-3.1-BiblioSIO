package rudy_yoann.backend.Exemplaire;

import org.springframework.stereotype.Service;
import rudy_yoann.backend.Exceptions.ResourceAlreadyExistsException;
import rudy_yoann.backend.Exceptions.ResourceNotFoundException;

import java.util.List;

@Service
public interface ExemplaireService {

    List<Exemplaire> getAll();

    Exemplaire getById(Long id);

    Exemplaire create(Exemplaire exemplaire) throws ResourceAlreadyExistsException;

    Exemplaire update(Long id, Exemplaire updatedExemplaire) throws ResourceNotFoundException;

    Boolean delete(Long id) throws ResourceNotFoundException;
/*
    //Visualisation des Exemplaires - Begin

    List<Exemplaire> findAllByIdOrdreCroissant(List<Exemplaire> exemplaires);

    List<Exemplaire> findAllByIdOrdreDecroissant(List<Exemplaire> exemplaires);

    List<Exemplaire> findAllByAnneeAndMoisOrdreCroissant(List<Exemplaire> exemplaires);

    List<Exemplaire> findAllByAnneeAndMoisOrdreDecroissant(List<Exemplaire> exemplaires);

    List<Exemplaire> findAllByOnlyFromCertainsYearsAsc(List<Exemplaire> exemplaires);

    List<Exemplaire> findAllByOnlyFromCertainsYearsDesc(List<Exemplaire> exemplaires);
*/
    // END
}
