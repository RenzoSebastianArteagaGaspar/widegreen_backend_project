package pe.edu.upc.widegreen_backend_project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upc.widegreen_backend_project.Entities.Publicacion;

import java.util.List;

@Repository
public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {

    @Query("SELECT p FROM Publicacion p WHERE p.id_Cliente.id_cliente = :idCliente")
    List<Publicacion> listarPublicacionesPorCliente(@Param("idCliente") Long idCliente);

    @Query("SELECT p FROM Publicacion p WHERE lower(p.titulo) LIKE lower(concat('%', :keyword, '%')) OR lower(p.contenido) LIKE lower(concat('%', :keyword, '%'))")
    List<Publicacion> buscarPorPalabraClave(@Param("keyword") String keyword);

    List<Publicacion> findByCategoria(String categoria);

    List<Publicacion> findAllByOrderByFechaDesc();
}
