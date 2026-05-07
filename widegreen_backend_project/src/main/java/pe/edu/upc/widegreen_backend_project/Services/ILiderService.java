package pe.edu.upc.widegreen_backend_project.Services;

import pe.edu.upc.widegreen_backend_project.Entities.Lider;
import pe.edu.upc.widegreen_backend_project.Entities.User;
import java.util.List;
import java.util.Optional;

public interface ILiderService {
    Lider create(User user, String career, String initiative);
    Optional<Lider> getByUserId(Long userId);
    Optional<Lider> getById(Long liderId);
    List<Lider> getAll();
    Lider update(Long liderId, String career, String initiative);
    void delete(Long liderId);
}
