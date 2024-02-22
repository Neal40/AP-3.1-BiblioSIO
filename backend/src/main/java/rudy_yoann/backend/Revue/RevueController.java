package rudy_yoann.backend.Revue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/revues")
@CrossOrigin(origins = "*")
public class RevueController {
    private RevueService revueService;

    @Autowired
    public RevueController(RevueService revueService) {
        this.revueService = revueService;
    }


    @GetMapping
    public List<Revue> getAll() {
        return revueService.getAll();
    }


    @GetMapping("/{id}")
    public Revue getById(@PathVariable Long id) {
        return revueService.getById(id);
    }

    @PostMapping
    public ResponseEntity<Revue> createRevue(@RequestBody Revue revue) {
        Revue created_revue = revueService.create(revue);
        return ResponseEntity.created(URI.create("/revues/" + created_revue.getId().toString())).build();
    }

    @PutMapping
    public ResponseEntity<Revue> updateRevue(@RequestBody Revue revue) {
        Long id = revue.getId();
        revueService.update(id, revue);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Revue> deleteRevue(@PathVariable Long id) {
        revueService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
