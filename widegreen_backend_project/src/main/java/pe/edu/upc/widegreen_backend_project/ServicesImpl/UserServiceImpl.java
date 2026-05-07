package pe.edu.upc.widegreen_backend_project.ServicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pe.edu.upc.widegreen_backend_project.Dtos.DTOUser;
import pe.edu.upc.widegreen_backend_project.Entities.Authority;
import pe.edu.upc.widegreen_backend_project.Entities.Cliente;
import pe.edu.upc.widegreen_backend_project.Entities.Nivel;
import pe.edu.upc.widegreen_backend_project.Entities.User;
import pe.edu.upc.widegreen_backend_project.Repository.ClienteRepository;
import pe.edu.upc.widegreen_backend_project.Repository.NivelRepository;
import pe.edu.upc.widegreen_backend_project.Repository.UserRepository;
import pe.edu.upc.widegreen_backend_project.Services.AuthorityService;
import pe.edu.upc.widegreen_backend_project.Services.ILiderService;
import pe.edu.upc.widegreen_backend_project.Services.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthorityService authorityService;

    @Autowired
    ILiderService liderService;

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    NivelRepository nivelRepository;

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    private List<Authority> authoritiesFromString(String authorities){

        List<Authority> authorityList = new ArrayList<>();
        List<String> authorityStringList = Arrays.stream(authorities.split(";")).toList();
        for(String authorityString: authorityStringList) {
            Authority authority = authorityService.findByName(authorityString);
            if (authority!=null) {
                authorityList.add(authority);
            }
        }
        return authorityList;
    }

    @Override
    public DTOUser add(DTOUser userDTO) {
        throw new RuntimeException("Use addWithRole() en lugar de add() para registrar usuarios con rol específico");
    }

    /**
     * Crear usuario con un rol específico
     * @param userDTO DTO con datos del usuario
     * @param roleName Nombre del rol (ROLE_CITIZEN, ROLE_LEADER, ROLE_ADMIN)
     * @return DTOUser con los datos del usuario creado
     */
    @Override
    public DTOUser addWithRole(DTOUser userDTO, String roleName) {
        // Obtener la autoridad por nombre
        Authority authority = authorityService.findByName(roleName);
        
        if (authority == null) {
            throw new RuntimeException("El rol '" + roleName + "' no existe. Use ROLE_CITIZEN, ROLE_LEADER o ROLE_ADMIN");
        }

        List<Authority> authorityList = new ArrayList<>();
        authorityList.add(authority);

        // Encriptar la contraseña
        String encryptedPassword = new BCryptPasswordEncoder().encode(userDTO.getPassword());

        User newUser = new User(null, userDTO.getUsername(),
                encryptedPassword,
                true, authorityList);

        newUser = userRepository.save(newUser);
        
        // Si es ROLE_CITIZEN, crear su perfil de Cliente
        if ("ROLE_CITIZEN".equals(roleName)) {
            Cliente cliente = new Cliente();
            cliente.setName(userDTO.getUsername()); // Usa el username como nombre inicial
            cliente.setApellido(""); // Apellido vacío inicial
            cliente.setCant_eventos_asistidos(0);
            
            // Obtener o crear el nivel básico (nivel 1)
            Nivel nivelBasico = nivelRepository.findById(1L).orElseGet(() -> {
                Nivel nuevoNivel = new Nivel();
                nuevoNivel.setNivel_alumno(1);
                nuevoNivel.setNivel_rango("Básico");
                return nivelRepository.save(nuevoNivel);
            });
            cliente.setNivel(nivelBasico);
            cliente.setUser(newUser);
            
            clienteRepository.save(cliente);
        }
        
        // Si es ROLE_LEADER, crear su perfil de Líder
        if ("ROLE_LEADER".equals(roleName)) {
            liderService.create(newUser, userDTO.getCareer(), userDTO.getInitiative());
        }
        
        // Devolver el DTO SIN la contraseña por seguridad
        userDTO.setId(newUser.getId());
        userDTO.setPassword(null); // No devolver contraseña por seguridad
        userDTO.setRoleName(roleName);
        return userDTO;
    }

    @Override
    public User addUser(User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        return userRepository.save(user);
    }
}