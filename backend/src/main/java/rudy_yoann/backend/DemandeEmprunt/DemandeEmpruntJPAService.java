package rudy_yoann.backend.DemandeEmprunt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import rudy_yoann.backend.Emprunt.Emprunt;
import rudy_yoann.backend.Emprunteur.Emprunteur;
import rudy_yoann.backend.Emprunteur.Statut;
import rudy_yoann.backend.Exceptions.ResourceAlreadyExistsException;
import rudy_yoann.backend.Exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@Qualifier("jpa")
public class DemandeEmpruntJPAService implements DemandeEmpruntService {

    @Autowired
    private DemandeEmpruntRepository demandeEmpruntRepository;

    @Override
    public List<DemandeEmprunt> getAll() {
        return demandeEmpruntRepository.findAll();
    }

    @Override
    public DemandeEmprunt getById(Long id) throws ResourceNotFoundException {
        Optional<DemandeEmprunt> demandeEmprunt = demandeEmpruntRepository.findById(id);
        if (demandeEmprunt.isPresent()) {
            return demandeEmprunt.get();
        } else {
            throw new ResourceNotFoundException("DemandeEmprunt", id);
        }
    }

    @Override
    public DemandeEmprunt create(DemandeEmprunt newDemande_Emprunt) throws ResourceAlreadyExistsException {
        if (newDemande_Emprunt.getEmprunteur().getStatut() == Statut.BLOQUE) {
            throw new RuntimeException("L'emprunteur est bloqué et ne peut pas faire de demande d'emprunt.");
        }

        int count = demandeEmpruntRepository.countByEmprunteur(newDemande_Emprunt.getEmprunteur());
        if (count >= 2) {
            throw new RuntimeException("L'emprunteur a déjà deux demandes d'emprunt");
        }

        if (demandeEmpruntRepository.findByStatut(newDemande_Emprunt.getStatut())!=null) {
            throw new ResourceAlreadyExistsException("DemandeEmprunt", newDemande_Emprunt.getId());
        } else {
            return demandeEmpruntRepository.save(newDemande_Emprunt);
        }
    }

    @Override
    public DemandeEmprunt update(Long id, DemandeEmprunt updatedDemande_Emprunt) throws ResourceNotFoundException {
        Optional<DemandeEmprunt> demandeEmprunt = demandeEmpruntRepository.findById(id);
        if (demandeEmprunt.isPresent()) {
            demandeEmpruntRepository.save(updatedDemande_Emprunt);
            return updatedDemande_Emprunt;
        } else {
            throw new ResourceNotFoundException("DemandeEmprunt", id);
        }
    }

    @Override
    public boolean delete(Long id) throws ResourceNotFoundException {
        Optional<DemandeEmprunt> demandeEmprunt = demandeEmpruntRepository.findById(id);
        if (demandeEmprunt.isPresent()) {
            demandeEmpruntRepository.deleteById(id);
            return true;
        } else {
            throw new ResourceNotFoundException("DemandeEmprunt", demandeEmprunt);
        }
    }

    @Override
    public List<DemandeEmprunt> getByEmprunteur(Emprunteur emprunteur) throws ResourceNotFoundException{
        List<DemandeEmprunt> demandeEmprunts = demandeEmpruntRepository.findDemandeEmpruntByEmprunteur(emprunteur);
        if (!demandeEmprunts.isEmpty()) {
            return demandeEmprunts;
        } else {
            throw new ResourceNotFoundException("DemandesEmprunts", emprunteur);
        }
    }
}
