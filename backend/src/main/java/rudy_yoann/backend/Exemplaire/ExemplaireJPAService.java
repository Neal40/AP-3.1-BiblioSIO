package rudy_yoann.backend.Exemplaire;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import rudy_yoann.backend.Exceptions.ResourceAlreadyExistsException;
import rudy_yoann.backend.Exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@Qualifier("jpa")
public class ExemplaireJPAService implements ExemplaireService {

    @Autowired
    private ExemplaireRepository exemplaireRepository;

    @Override
    public List<Exemplaire> getAll() {
        return exemplaireRepository.findAll();
    }

    @Override
    public Exemplaire getById(Long id) {
        Optional<Exemplaire> exemplaire = exemplaireRepository.findById(id);
        if (exemplaire.isPresent()) {
            return exemplaire.get();
        } else {
            throw new ResourceNotFoundException("Exemplaire", id);
        }
    }

    @Override
    public Exemplaire create(Exemplaire exemplaire) {
        if (exemplaireRepository.findByTitre(exemplaire.getTitre())!=null) {
            throw new ResourceAlreadyExistsException("Exemplaire", exemplaire.getId());
        } else {
            return exemplaireRepository.save(exemplaire);
        }
    }

    @Override
    public Exemplaire update(Long id, Exemplaire updatedExemplaire) throws ResourceNotFoundException {
        Optional<Exemplaire> exemplaire = exemplaireRepository.findById(id);
        if (exemplaire.isPresent()) {
            exemplaireRepository.save(updatedExemplaire);
            return updatedExemplaire;
        } else {
            throw new ResourceNotFoundException("Exemplaire", id);
        }
    }

    @Override
    public Boolean delete(Long id) throws ResourceNotFoundException {
        Optional<Exemplaire> exemplaire = exemplaireRepository.findById(id);
        if (exemplaire.isPresent()) {
            exemplaireRepository.deleteById(id);
            return true;
        } else {
            throw new ResourceNotFoundException("Exemplaire", id);
        }
    }

    /*
    //Visualisation des Exemplaires - Begin
    @Override
    public List<Exemplaire> findAllByIdOrdreCroissant(List<Exemplaire> exemplaires) {
        List<Exemplaire> exemplaire = exemplaireRepository.findAllByNumeroOrdreCroissant(exemplaires);
        if (!exemplaire.isEmpty()) {
            return exemplaire;
        } else {
            throw new ResourceNotFoundException("Exemplaire", exemplaires);
        }
    }
    @Override
    public List<Exemplaire> findAllByIdOrdreDecroissant(List<Exemplaire> exemplaires) {
        List<Exemplaire> exemplaire = exemplaireRepository.findAllByNumeroOrdreDecroissant(exemplaires);
        if (!exemplaire.isEmpty()) {
            return exemplaire;
        } else {
            throw new ResourceNotFoundException("Exemplaire", exemplaires);
        }
    }

    @Override
    public List<Exemplaire> findAllByAnneeAndMoisOrdreCroissant(List<Exemplaire> exemplaires) {
        List<Exemplaire> exemplaire = exemplaireRepository.findAllByAnneeAndMoisOrdreCroissant(exemplaires);
        if (!exemplaire.isEmpty()) {
            return exemplaire;
        } else {
            throw new ResourceNotFoundException("Exemplaire", exemplaires);
        }
    }
    @Override
    public List<Exemplaire> findAllByAnneeAndMoisOrdreDecroissant(List<Exemplaire> exemplaires) {
        List<Exemplaire> exemplaire = exemplaireRepository.findAllByAnneeAndMoisOrdreDecroissant(exemplaires);
        if (!exemplaire.isEmpty()) {
            return exemplaire;
        } else {
            throw new ResourceNotFoundException("Exemplaire", exemplaires);
        }
    }

    @Override
    public List<Exemplaire> findAllByOnlyFromCertainsYearsAsc(List<Exemplaire> exemplaires) {
        List<Exemplaire> result = exemplaireRepository.findAllByOnlyFromCertainsYearsAsc(exemplaires);

        if (!result.isEmpty()) {
            return result;
        } else {
            throw new ResourceNotFoundException("Exemplaire", exemplaires);
        }
    }
    @Override
    public List<Exemplaire> findAllByOnlyFromCertainsYearsDesc(List<Exemplaire> exemplaires) {
        List<Exemplaire> result = exemplaireRepository.findAllByOnlyFromCertainsYearsDesc(exemplaires);

        if (!result.isEmpty()) {
            return result;
        } else {
            throw new ResourceNotFoundException("Exemplaire", exemplaires);
        }
    }
*/
    //END

}
