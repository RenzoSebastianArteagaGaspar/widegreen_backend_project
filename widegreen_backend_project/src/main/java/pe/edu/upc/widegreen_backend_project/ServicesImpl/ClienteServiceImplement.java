package pe.edu.upc.widegreen_backend_project.ServicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.widegreen_backend_project.Dtos.ClienteDTO;
import pe.edu.upc.widegreen_backend_project.Entities.Cliente;
import pe.edu.upc.widegreen_backend_project.Entities.Nivel;
import pe.edu.upc.widegreen_backend_project.Entities.User;
import pe.edu.upc.widegreen_backend_project.Repository.ClienteRepository;
import pe.edu.upc.widegreen_backend_project.Repository.NivelRepository;
import pe.edu.upc.widegreen_backend_project.Repository.UserRepository;
import pe.edu.upc.widegreen_backend_project.Services.ClienteService;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteServiceImplement implements ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private NivelRepository nivelRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Cliente registrarCliente(ClienteDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setName(dto.getName());
        cliente.setApellido(dto.getApellido());
        cliente.setCant_eventos_asistidos(0); // Inicializa en 0

        Nivel nivel = nivelRepository.findById(dto.getId_nivel())
                .orElseThrow(() -> new RuntimeException("Nivel no encontrado"));
        cliente.setNivel(nivel);

        User user = userRepository.findById(dto.getId_user())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        cliente.setUser(user);

        return clienteRepository.save(cliente);
    }

    @Override
    public List<Cliente> buscarClientesPorNivel(Integer nivel) {
        return clienteRepository.buscarClientesPorNivel(nivel);
    }

    @Override
    public List<Cliente> mostrarClientes() {
        return clienteRepository.findAll();
    }

    @Override
    public Optional<Cliente> obtenerPorId(Long clienteId) {
        return clienteRepository.findById(clienteId);
    }

    @Override
    public Optional<Cliente> obtenerPorUserId(Long userId) {
        return clienteRepository.findAll().stream()
                .filter(c -> c.getUser().getId().equals(userId))
                .findFirst();
    }

    @Override
    public Cliente actualizar(Long clienteId, String name, String apellido, Integer cantEventos, Long idNivel) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(clienteId);
        
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            
            if (name != null) {
                cliente.setName(name);
            }
            if (apellido != null) {
                cliente.setApellido(apellido);
            }
            if (cantEventos != null) {
                cliente.setCant_eventos_asistidos(cantEventos);
            }
            if (idNivel != null) {
                Nivel nivel = nivelRepository.findById(idNivel)
                        .orElseThrow(() -> new RuntimeException("Nivel no encontrado"));
                cliente.setNivel(nivel);
            }
            
            return clienteRepository.save(cliente);
        }
        
        return null;
    }

    @Override
    public void eliminar(Long clienteId) {
        clienteRepository.deleteById(clienteId);
    }
}
