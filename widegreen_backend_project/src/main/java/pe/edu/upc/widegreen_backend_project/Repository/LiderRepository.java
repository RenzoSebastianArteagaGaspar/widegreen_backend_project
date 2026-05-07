package pe.edu.upc.widegreen_backend_project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.widegreen_backend_project.Entities.Lider;

import java.util.Optional;

@Repository
public interface LiderRepository extends JpaRepository<Lider, Long> {
    Optional<Lider> findByUser_Id(Long userId);
}
