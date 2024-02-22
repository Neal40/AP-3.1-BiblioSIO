package rudy_yoann.backend.Exemplaire;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rudy_yoann.backend.Emprunt.Emprunt;
import rudy_yoann.backend.Emprunteur.Classe;
import rudy_yoann.backend.Emprunteur.Emprunteur;
import rudy_yoann.backend.Revue.Revue;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/exemplaires")
@CrossOrigin(origins = "*")
public class ExemplaireController {
    private ExemplaireService exemplaireService;

    @Autowired
    public ExemplaireController(ExemplaireService exemplaireService) {
        this.exemplaireService = exemplaireService;
    }

    @GetMapping
    public List<Exemplaire> getAll() {
        return exemplaireService.getAll();
    }

    @GetMapping("/{id}")
    public Exemplaire getById(@PathVariable Long id) {
        return exemplaireService.getById(id);
    }

    @PostMapping
    public ResponseEntity createExemplaire(@RequestBody Exemplaire exemplaire) {
        Exemplaire created_exemplaire = exemplaireService.create(exemplaire);
        return ResponseEntity.created(URI.create("/exemplaires/" + created_exemplaire.getId().toString())).build();
    }

    @PutMapping
    public ResponseEntity<Exemplaire> updateExemplaire(@RequestBody Exemplaire exemplaire) {
        Long id = exemplaire.getId();
        exemplaireService.update(id, exemplaire);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteExemplaire(@PathVariable Long id) {
        exemplaireService.delete(id);
        return ResponseEntity.noContent().build();
    }
    /*
    //Visualisation des Exemplaires - Begin
    @GetMapping("/{idOrdreCroissant]")
    public List<Exemplaire> findAllByIdOrdreCroissant(@RequestParam List<Exemplaire> exemplaires) {
        return exemplaireService.findAllByIdOrdreCroissant(exemplaires);
    }
    @GetMapping("/{idOrdreDecroissant]")
    public List<Exemplaire> findAllByIdOrdreDecroissant(@RequestParam List<Exemplaire> exemplaires) {
        return exemplaireService.findAllByIdOrdreDecroissant(exemplaires);
    }
    @GetMapping("/anneeAndMoisOrdreCroissant")
    public List<Exemplaire> findAllByAnneeAndMoisOrdreCroissant(@RequestParam List<Exemplaire> exemplaires) {
        return exemplaireService.findAllByAnneeAndMoisOrdreCroissant(exemplaires);
    }

    @GetMapping("/anneeAndMoisOrdreDecroissant")
    public List<Exemplaire> findAllByAnneeAndMoisOrdreDecroissant(@RequestParam List<Exemplaire> exemplaires) {
        return exemplaireService.findAllByAnneeAndMoisOrdreDecroissant(exemplaires);

    }

    @GetMapping("/anneeOrdreCroissant")
    public List<Exemplaire> findAllByOnlyFromCertainsYearsAsc(@RequestParam List<Exemplaire> exemplaires) {
        return exemplaireService.findAllByOnlyFromCertainsYearsAsc(exemplaires);
    }

    @GetMapping("/anneeOrdreDecroissant")
    public List<Exemplaire> findAllByOnlyFromCertainsYearsDesc(@RequestParam List<Exemplaire> exemplaires) {
        return exemplaireService.findAllByOnlyFromCertainsYearsDesc(exemplaires);
    }
*/


    // END

}
