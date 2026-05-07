package pe.edu.upc.widegreen_backend_project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upc.widegreen_backend_project.Entities.Evento;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

    // JPQL para buscar por estado
    @Query("SELECT e FROM Evento e WHERE e.evento_estado = :estado")
    List<Evento> findByEstadoJPQL(@Param("estado") String estado);

    // JPQL para buscar por título
    @Query("SELECT e FROM Evento e WHERE LOWER(e.evento_titulo) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Evento> buscarPorTituloJPQL(@Param("keyword") String keyword);

    //  JPQL para rango de fechas
    @Query("SELECT e FROM Evento e WHERE e.evento_fechaInicio >= :inicio AND e.evento_fechaFin <= :fin ORDER BY e.evento_fechaInicio ASC")
    List<Evento> listarEventosPorRangoFechas(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);

    // Native query para contar
    @Query(value = "SELECT COUNT(*) FROM evento WHERE evento_estado = :estado", nativeQuery = true)
    int contarEventosPorEstadoNative(@Param("estado") String estado);
}