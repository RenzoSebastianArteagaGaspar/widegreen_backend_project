package pe.edu.upc.widegreen_backend_project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.widegreen_backend_project.Entities.Comentario;
import java.util.List;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    List<Comentario> findByPublicacionId(Long idPublicacion);
}