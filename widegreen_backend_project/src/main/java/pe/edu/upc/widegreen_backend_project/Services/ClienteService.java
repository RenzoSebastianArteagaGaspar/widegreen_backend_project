package pe.edu.upc.widegreen_backend_project.Services;

import pe.edu.upc.widegreen_backend_project.Dtos.ClienteDTO;
import pe.edu.upc.widegreen_backend_project.Entities.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteService {
    Cliente registrarCliente(ClienteDTO clienteDTO);
    List<Cliente> buscarClientesPorNivel(Integer idNivel);
    List<Cliente> mostrarClientes();
    Optional<Cliente> obtenerPorId(Long clienteId);
    Optional<Cliente> obtenerPorUserId(Long userId);
    Cliente actualizar(Long clienteId, String name, String apellido, Integer cantEventos, Long idNivel);
    void eliminar(Long clienteId);
}
