package rudy_yoann.backend.DemandeEmprunt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rudy_yoann.backend.Emprunt.Emprunt;
import rudy_yoann.backend.Emprunt.EmpruntService;
import rudy_yoann.backend.Emprunteur.Emprunteur;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/demandeEmprunts")
@CrossOrigin(origins = "*")
public class    DemandeEmpruntController {

    private DemandeEmpruntService demandeEmpruntService;

    @Autowired
    public DemandeEmpruntController(DemandeEmpruntService demandeEmpruntService) {
        this.demandeEmpruntService = demandeEmpruntService;
    }

    @GetMapping("")
    public List<DemandeEmprunt> getAll() {
        return demandeEmpruntService.getAll();
    }

    @GetMapping("/{id}")
    public DemandeEmprunt getById(@PathVariable Long id) {
        return demandeEmpruntService.getById(id);
    }

    @PostMapping()
    public ResponseEntity<DemandeEmprunt> createDemandeEmprunt(@RequestBody DemandeEmprunt demandeEmprunt) {
        DemandeEmprunt created_DemandeEmprunt = demandeEmpruntService.create(demandeEmprunt);
        return ResponseEntity.created(URI.create("/demandeEmprunts/" + created_DemandeEmprunt.getId().toString())).build();
    }

    @PutMapping
    public ResponseEntity<DemandeEmprunt> updateDemandeEmprunt(@RequestBody DemandeEmprunt demandeEmprunt) {
        Long id = demandeEmprunt.getId();
        demandeEmpruntService.update(id, demandeEmprunt);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DemandeEmprunt> deleteDemandeEmprunt(@PathVariable Long id) {
        demandeEmpruntService.delete(id);
        return ResponseEntity.noContent().build();
    }

    public List<DemandeEmprunt> getByEmprunteur(@RequestBody Emprunteur emprunteur) {
        return  demandeEmpruntService.getByEmprunteur(emprunteur);
    }
}
