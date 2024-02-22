package rudy_yoann.backend.DemandeEmprunt;

import jakarta.persistence.PostUpdate;
import rudy_yoann.backend.Emprunt.Emprunt;
import rudy_yoann.backend.Emprunt.EmpruntRepository;

public class DemandeEmpruntEntityListener {

    private EmpruntRepository empruntRepository;

    public DemandeEmpruntEntityListener() {
    }

    public DemandeEmpruntEntityListener(EmpruntRepository empruntRepository) {
        this.empruntRepository = empruntRepository;
    }

    @SuppressWarnings("unused")
    @PostUpdate
    public void createEmprunt(DemandeEmprunt demandeEmprunt) {
        if (demandeEmprunt.getStatut() == Statut.VALIDEE) {
            Emprunt emprunt = new Emprunt();
            emprunt.setEmprunteur(demandeEmprunt.getEmprunteur());
            emprunt.setExemplaire(demandeEmprunt.getExemplaire());
            empruntRepository.save(emprunt);
        }
    }
}
