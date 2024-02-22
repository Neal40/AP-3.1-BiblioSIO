package rudy_yoann.backend.Emprunt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rudy_yoann.backend.Emprunteur.Classe;
import rudy_yoann.backend.Emprunteur.Emprunteur;
import rudy_yoann.backend.Revue.Revue;
import rudy_yoann.backend.Revue.RevueService;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.stream;

@RestController
@RequestMapping("/emprunts")
@CrossOrigin(origins = "*")
public class EmpruntController {

    private EmpruntService empruntSerivce;

    public EmpruntController() {
    }


    @Autowired
    public EmpruntController(EmpruntService empruntService) {
        this.empruntSerivce = empruntService;
    }

    @GetMapping("")
    public List<Emprunt> getAll() {
        return empruntSerivce.getAll();
    }

    @GetMapping("/{id}")
    public Emprunt getById(@PathVariable Long id) {
        return empruntSerivce.getById(id);
    }

    @PostMapping()
    public ResponseEntity<Emprunt> createEmprunt(@RequestBody Emprunt emprunt) {
        Emprunt created_emprunt = empruntSerivce.create(emprunt);
        return ResponseEntity.created(URI.create("/emprunts/" + created_emprunt.getId().toString())).build();
    }

    @PutMapping
    public ResponseEntity<Emprunt> updateEmprunt(@RequestBody Emprunt emprunt) {
        Long id = emprunt.getId();
        empruntSerivce.update(id, emprunt);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Emprunt> deleteEmprunt(@PathVariable Long id) {
        empruntSerivce.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("ids")
    public List<Emprunt> getByRevues(@RequestParam("ids") Long[] revueIds){
        return empruntSerivce.getByRevue(revueIds);
    }

    @GetMapping("id")
    public List<Emprunt> getByEmprunteur(@RequestParam("id") Long emprunteurId) {
        return empruntSerivce.getByEmprunteur(emprunteurId);
    }

    @GetMapping("classes")
    public List<Emprunt> getByEmprunteurClasse(@RequestParam("classes") Long[] classes) {
        return empruntSerivce.getByEmprunteurClasse(classes);
    }

    @GetMapping("/en-retard")
    public List<Emprunt> getByStatutEnRetard() {
        return empruntSerivce.getByStatutEnRetard();
    }

    @GetMapping("/classe")
    public List<Emprunt> getByStatutEnRetardAndByClasse(@RequestParam("classe") Classe classe) {
        return empruntSerivce.getByStatutEnRetardAndByClasse(classe);
    }
}
