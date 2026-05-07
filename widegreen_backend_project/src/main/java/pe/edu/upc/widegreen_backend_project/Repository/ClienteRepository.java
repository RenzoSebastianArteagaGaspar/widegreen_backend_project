package pe.edu.upc.widegreen_backend_project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.upc.widegreen_backend_project.Entities.Cliente;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    //Query Nativo
    @Query(value = "SELECT * FROM cliente WHERE cliente_nivel = :nivel", nativeQuery = true)
    List<Cliente> buscarClientesPorNivel(@Param("nivel") Integer nivel);
}
