package rudy_yoann.backend.Emprunt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rudy_yoann.backend.Emprunteur.Classe;
import rudy_yoann.backend.Emprunteur.Emprunteur;
import rudy_yoann.backend.Exceptions.ResourceAlreadyExistsException;
import rudy_yoann.backend.Exceptions.ResourceNotFoundException;
import rudy_yoann.backend.Revue.Revue;

import java.util.List;

@Service
public interface EmpruntService {
    List<Emprunt> getAll();

    Emprunt getById(Long id) throws ResourceNotFoundException;

    Emprunt create(Emprunt newEmprunt) throws ResourceAlreadyExistsException;

    Emprunt update(Long id, Emprunt updatedEmprunt) throws ResourceNotFoundException;

    boolean delete(Long id) throws ResourceNotFoundException;

    @Scheduled(cron = "0 0 0 * * ?") // tous les jours à minuit
    void verifierDureeEmprunts();

    void bloquerCompte(Emprunteur emprunteur);

    @Scheduled(cron = "0 0 0 * * ?") // tous les jours à minuit
    void mettreAJourStatutEmpruntsEnRetard();

    List<Emprunt> getByRevue(Long[] revues) throws ResourceNotFoundException;

    List<Emprunt> getByEmprunteur(Long emprunteursIds) throws ResourceNotFoundException;

    List<Emprunt> getByEmprunteurClasse(Long[] classes) throws ResourceNotFoundException;

    List<Emprunt> getByStatutEnRetard() throws ResourceNotFoundException;

    List<Emprunt> getByStatutEnRetardAndByClasse(Classe classe) throws ResourceNotFoundException;

}
