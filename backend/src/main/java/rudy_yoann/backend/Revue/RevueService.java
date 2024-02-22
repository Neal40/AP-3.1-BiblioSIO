package rudy_yoann.backend.Revue;

import org.springframework.stereotype.Service;
import rudy_yoann.backend.Exceptions.ResourceAlreadyExistsException;
import rudy_yoann.backend.Exceptions.ResourceNotFoundException;

import java.util.List;

@Service
public interface RevueService {

    List<Revue> getAll();

    Revue getById(Long id);

    Revue create(Revue revue) throws ResourceAlreadyExistsException;

    Revue update(Long id, Revue updatedRevue) throws ResourceNotFoundException;

    Boolean delete(Long id) throws ResourceNotFoundException;
}
