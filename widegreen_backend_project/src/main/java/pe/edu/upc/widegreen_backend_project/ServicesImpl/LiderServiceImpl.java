package pe.edu.upc.widegreen_backend_project.ServicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.widegreen_backend_project.Entities.Lider;
import pe.edu.upc.widegreen_backend_project.Entities.User;
import pe.edu.upc.widegreen_backend_project.Repository.LiderRepository;
import pe.edu.upc.widegreen_backend_project.Services.ILiderService;

import java.util.List;
import java.util.Optional;

@Service
public class LiderServiceImpl implements ILiderService {

    @Autowired
    private LiderRepository liderRepository;

    @Override
    public Lider create(User user, String career, String initiative) {
        Lider lider = new Lider();
        lider.setUser(user);
        lider.setCareer(career);
        lider.setInitiative(initiative);
        return liderRepository.save(lider);
    }

    @Override
    public Optional<Lider> getByUserId(Long userId) {
        return liderRepository.findByUser_Id(userId);
    }

    @Override
    public Optional<Lider> getById(Long liderId) {
        return liderRepository.findById(liderId);
    }

    @Override
    public List<Lider> getAll() {
        return liderRepository.findAll();
    }

    @Override
    public Lider update(Long liderId, String career, String initiative) {
        Optional<Lider> liderOpt = liderRepository.findById(liderId);
        if (liderOpt.isPresent()) {
            Lider lider = liderOpt.get();
            if (career != null) {
                lider.setCareer(career);
            }
            if (initiative != null) {
                lider.setInitiative(initiative);
            }
            return liderRepository.save(lider);
        }
        return null;
    }

    @Override
    public void delete(Long liderId) {
        liderRepository.deleteById(liderId);
    }
}
