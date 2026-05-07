package pe.edu.upc.widegreen_backend_project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upc.widegreen_backend_project.Entities.EventoPorCliente;

import java.util.List;

@Repository
public interface EventoPorClienteRepository extends JpaRepository<EventoPorCliente, Long> {

    @Query("SELECT count(e) > 0 FROM EventoPorCliente e WHERE e.id_Cliente.id_cliente = :idCliente AND e.id_evento.id_evento = :idEvento")
    boolean existeInscripcion(@Param("idCliente") Long idCliente, @Param("idEvento") Long idEvento);

    @Query("SELECT e FROM EventoPorCliente e WHERE e.id_Cliente.id_cliente = :idCliente AND e.id_evento.id_evento = :idEvento")
    EventoPorCliente buscarPorClienteYEvento(@Param("idCliente") Long idCliente, @Param("idEvento") Long idEvento);

    @Query("SELECT e FROM EventoPorCliente e WHERE e.id_Cliente.id_cliente = :idCliente")
    List<EventoPorCliente> listarPorCliente(@Param("idCliente") Long idCliente);

    @Query("SELECT count(e) FROM EventoPorCliente e WHERE e.id_evento.id_evento = :idEvento")
    Integer contarInscritosPorEvento(@Param("idEvento") Long idEvento);
}
