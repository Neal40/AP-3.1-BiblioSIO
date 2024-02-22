package rudy_yoann.backend.Emprunt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rudy_yoann.backend.Emprunteur.Classe;
import rudy_yoann.backend.Emprunteur.Emprunteur;
import rudy_yoann.backend.Emprunt.Statut;
import rudy_yoann.backend.Exceptions.ResourceAlreadyExistsException;
import rudy_yoann.backend.Exceptions.ResourceNotFoundException;
import rudy_yoann.backend.Revue.Revue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Qualifier("jpa")
public class EmpruntJPAService implements EmpruntService {

    @Autowired
    private EmpruntRepository empruntRepository;

    @Override
    public List<Emprunt> getAll() {
        return empruntRepository.findAll();
    }

    @Override
    public Emprunt getById(Long id) {
        Optional<Emprunt> emprunt = empruntRepository.findById(id);
        if (emprunt.isPresent()) {
            return emprunt.get();
        } else {
            throw new ResourceNotFoundException("Emprunt", id);
        }
    }

    @Override
    public Emprunt create(Emprunt emprunt) {
        if (emprunt.getEmprunteur().getStatut() == rudy_yoann.backend.Emprunteur.Statut.BLOQUE) {
            throw new RuntimeException("L'emprunteur est bloqué et ne peut pas faire d'emprunt");
        }

        int count = empruntRepository.countByEmprunteur(emprunt.getEmprunteur());
        if (count >= 2) {
            throw new RuntimeException("L'emprunteur a déjà deux emprunts");
        }

        if (empruntRepository.findStatutByStatut(emprunt.getStatut())!=null) {
            throw new ResourceAlreadyExistsException("Emprunt", emprunt.getId());
        } else {
            return empruntRepository.save(emprunt);
        }
    }

    @Override
    public Emprunt update(Long id, Emprunt updatedEmprunt) throws ResourceNotFoundException {
        Optional<Emprunt> emprunt = empruntRepository.findById(id);
        if (emprunt.isPresent()) {
            empruntRepository.save(updatedEmprunt);
            return updatedEmprunt;
        } else {
            throw new ResourceNotFoundException("Emprunt", emprunt);
        }
    }


    @Override
    public boolean delete(Long id) throws ResourceNotFoundException {
        Optional<Emprunt> emprunt = empruntRepository.findById(id);
        if (emprunt.isPresent()) {
            empruntRepository.deleteById(id);
            return true;
        } else {
            throw new ResourceNotFoundException("Emprunt", id);
        }
    }

    @Override
    public void verifierDureeEmprunts() {
        List<Emprunt> emprunts = getAll();
        for (Emprunt emprunt : emprunts) {
            emprunt.augmenterDuree();
            if (emprunt.getDuree() > 21) {
                Emprunteur emprunteur = emprunt.getEmprunteur();
                bloquerCompte(emprunteur);
            }
        }
    }

    @Override
    public void bloquerCompte(Emprunteur emprunteur) {
        emprunteur.setStatut(rudy_yoann.backend.Emprunteur.Statut.BLOQUE);
    }

    @Override
    public void mettreAJourStatutEmpruntsEnRetard() {
        List<Emprunt> empruntsEnCours = empruntRepository.findByStatut(Statut.EN_COURS);

        Date aujourdHui = new Date();

        for (Emprunt emprunt : empruntsEnCours) {
            if (aujourdHui.after(emprunt.getDateEcheance())) {
                emprunt.setStatut(Statut.EN_RETARD);
                empruntRepository.save(emprunt);
            }
        }
    }

    @Override
    public List<Emprunt> getByRevue(Long[] revuesIds) throws ResourceNotFoundException{

        List<Emprunt> emprunts = empruntRepository.findEmpruntByRevue(revuesIds);
        if (!emprunts.isEmpty()) {
            return emprunts;
        }
        throw new ResourceNotFoundException("Emprunts", revuesIds);
    }


    @Override
    public List<Emprunt> getByEmprunteur(Long empruntsIds) throws ResourceNotFoundException{
        List<Emprunt> emprunts = empruntRepository.findEmpruntByEmprunteur(empruntsIds);
        if (!emprunts.isEmpty()) {
            return emprunts;
        }
        throw new ResourceNotFoundException("Emprunts", empruntsIds);
    }

    @Override
    public List<Emprunt> getByEmprunteurClasse(Long[] classes) throws ResourceNotFoundException{
        List<Emprunt> emprunts = empruntRepository.findEmpruntByClasse(classes);
        if (!emprunts.isEmpty()) {
            return emprunts;
        }
        throw new ResourceNotFoundException("Emprunts", classes);
    }

    @Override
    public List<Emprunt> getByStatutEnRetard() throws ResourceNotFoundException{
        List<Emprunt> emprunts = empruntRepository.findByStatut(Statut.EN_RETARD);
        if (!emprunts.isEmpty()) {
            return emprunts;
        }
        throw new ResourceNotFoundException("Emprunts", Statut.EN_RETARD);
    }

    @Override
    public List<Emprunt> getByStatutEnRetardAndByClasse(Classe classe) throws ResourceNotFoundException{
        List<Emprunt> emprunts = empruntRepository.findEmpruntsEnRetardByClasse(classe, Statut.EN_RETARD);
        if (!emprunts.isEmpty()) {
            return emprunts;
        }
        throw new ResourceNotFoundException("Emprunts", classe);
    }
}