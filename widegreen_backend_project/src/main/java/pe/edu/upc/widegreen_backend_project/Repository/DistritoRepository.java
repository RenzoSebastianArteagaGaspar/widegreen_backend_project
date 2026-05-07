package pe.edu.upc.widegreen_backend_project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.widegreen_backend_project.Entities.Distrito;

@Repository
public interface DistritoRepository extends JpaRepository<Distrito, Long> {
}