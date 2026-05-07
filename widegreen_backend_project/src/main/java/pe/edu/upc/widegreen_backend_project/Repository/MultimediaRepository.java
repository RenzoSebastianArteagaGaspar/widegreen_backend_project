package pe.edu.upc.widegreen_backend_project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upc.widegreen_backend_project.Entities.Multimedia;
import java.util.List;

@Repository
public interface MultimediaRepository extends JpaRepository<Multimedia, Long> {
    @Query("SELECT m FROM Multimedia m WHERE m.tipoEntidad = :tipoEntidad AND m.idEntidad = :idEntidad")
    List<Multimedia> findByTipoEntidadAndIdEntidad(@Param("tipoEntidad") String tipoEntidad, @Param("idEntidad") Long idEntidad);
}
