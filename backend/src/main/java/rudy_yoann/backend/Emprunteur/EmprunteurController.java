package rudy_yoann.backend.Emprunteur;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rudy_yoann.backend.Revue.Revue;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/emprunteurs")
@CrossOrigin(origins = "*")
public class EmprunteurController {

    private EmprunteurService emprunteurService;

    @Autowired
    public EmprunteurController(EmprunteurService emprunteurService) {
        this.emprunteurService=emprunteurService;
    }

    @GetMapping("")
    public List<Emprunteur> getAll() {
        return emprunteurService.getAll();
    }

    @GetMapping("/{id}")
    public Emprunteur getById(@PathVariable Long id) {
        return emprunteurService.getById(id);
    }

    @PostMapping()
    public ResponseEntity createEmprunteur(@RequestBody Emprunteur emprunteur) {
        Emprunteur created_emprunteur = emprunteurService.create(emprunteur);
        return ResponseEntity.created(URI.create("/emprunteurs/" + created_emprunteur.getId().toString())).build();
    }

    @PutMapping
    public ResponseEntity<Emprunteur> updateEmprunteur(@RequestBody Emprunteur emprunteur) {
        Long id = emprunteur.getId();
        emprunteurService.update(id, emprunteur);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteEmprunteur(@PathVariable Long id) {
        emprunteurService.delete(id);
        return ResponseEntity.noContent().build();
    }

}