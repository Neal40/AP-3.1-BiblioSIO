package rudy_yoann.backend.Emprunteur;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import rudy_yoann.backend.Exceptions.ResourceAlreadyExistsException;
import rudy_yoann.backend.Exceptions.ResourceNotFoundException;
import rudy_yoann.backend.Revue.Revue;

import java.util.List;
import java.util.Optional;

@Service
@Qualifier("jpa")
public class EmprunteurJPAService implements EmprunteurService {

    @Autowired
    private EmprunteurRepository emprunteurRepository;

    @Override
    public List<Emprunteur> getAll() {
        return emprunteurRepository.findAll();
    }

    @Override
    public Emprunteur getById(Long id) {
        Optional<Emprunteur> emprunteur = emprunteurRepository.findById(id);
        if (emprunteur.isPresent()) {
            return emprunteur.get();
        } else {
            throw new ResourceNotFoundException("Emprunteur", id);
        }
    }

    @Override
    public Emprunteur create(Emprunteur emprunteur) {
        if (emprunteurRepository.findByNom(emprunteur.getNom())!=null) {
            throw new ResourceAlreadyExistsException("Emprunteur", emprunteur.getId());
        } else {
            if (emprunteur.getRole() == Role.professeur) {
                emprunteur.setClasse(Classe.PROF);
            }
            return emprunteurRepository.save(emprunteur);
        }
    }

    @Override
    public Emprunteur update(Long id, Emprunteur updatedEmprunteur) throws ResourceNotFoundException {
        Optional<Emprunteur> emprunteur = emprunteurRepository.findById(id);
        if (emprunteur.isPresent()) {
            emprunteurRepository.save(updatedEmprunteur);
            return updatedEmprunteur;
        } else {
            throw new ResourceNotFoundException("Emprunteur", id);
        }
    }

    @Override
    public boolean delete(Long id) throws ResourceNotFoundException {
        Optional<Emprunteur> emprunteur = emprunteurRepository.findById(id);
        if (emprunteur.isPresent()) {
            emprunteurRepository.deleteById(id);
            return true;
        }
        else {
            throw new ResourceNotFoundException("Emprunteur", id);
        }
    }
}