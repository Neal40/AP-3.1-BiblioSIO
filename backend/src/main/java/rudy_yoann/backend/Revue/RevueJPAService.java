package rudy_yoann.backend.Revue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import rudy_yoann.backend.Exceptions.ResourceAlreadyExistsException;
import rudy_yoann.backend.Exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@Qualifier("jpa")
public class RevueJPAService implements RevueService{

    @Autowired
    private RevueRepository revueRepository;

    @Override
    public List<Revue> getAll() {
        return revueRepository.findAll();
    }


    @Override
    public Revue getById(Long id) {
        Optional<Revue> revue = revueRepository.findById(id);
        if (revue.isPresent()) {
            return revue.get();
        } else {
            throw new ResourceNotFoundException("Revue", id);
        }
    }


    @Override
    public Revue create(Revue revue) {
        if (revueRepository.findByTitre(revue.getTitre())!=null) {
            throw new ResourceAlreadyExistsException("Revue", revue.getId());
        } else {
            return revueRepository.save(revue);
        }
    }


    @Override
    public Revue update(Long id, Revue updatedRevue) {
        Optional<Revue> revue = revueRepository.findById(id);
        if (revue.isPresent()) {
            revueRepository.save(updatedRevue);
            return updatedRevue;
        } else {
            throw new ResourceNotFoundException("Todo", id);
        }
    }


    @Override
    public Boolean delete(Long id) throws ResourceNotFoundException {
        Optional<Revue> revue = revueRepository.findById(id);
        if (revue.isPresent()) {
            revueRepository.deleteById(id);
            return true;
        } else {
            throw new ResourceNotFoundException("Revue", id);
        }
    }
}
