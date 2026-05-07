package pe.edu.upc.widegreen_backend_project.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.widegreen_backend_project.Dtos.DTOToken;
import pe.edu.upc.widegreen_backend_project.Dtos.DTOUser;
import pe.edu.upc.widegreen_backend_project.Entities.User;
import pe.edu.upc.widegreen_backend_project.Repository.UserRepository;
import pe.edu.upc.widegreen_backend_project.Services.UserService;
import pe.edu.upc.widegreen_backend_project.Security.JwtUtilService;
import pe.edu.upc.widegreen_backend_project.Security.UserSecurity;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/autenticacion")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtilService jwtUtilService;

    @Autowired
    UserRepository userRepository;

    /**
     * POST /api/v1/autenticacion/registro
     * Registrar un nuevo usuario con rol específico (Public)
     * Body: {
     *   "username": "usuario1",
     *   "password": "password123",
     *   "roleName": "ROLE_CITIZEN"  // Opcional: ROLE_CITIZEN, ROLE_LEADER, ROLE_ADMIN (default: ROLE_CITIZEN)
     * }
     */
    @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody DTOUser user){
        try {
            // Si no se especifica rol, asignar ROLE_CITIZEN por defecto
            String roleName = user.getRoleName() != null && !user.getRoleName().isEmpty() 
                ? user.getRoleName() 
                : "ROLE_CITIZEN";
            
            DTOUser createdUser = userService.addWithRole(user, roleName);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * POST /api/v1/autenticacion/login
     * Autenticarse e iniciar sesión (Public)
     */
    @PostMapping("/login")
    public ResponseEntity<DTOToken> login(@RequestBody User user){
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );
            UserSecurity userSecurity = (UserSecurity) userDetailsService.loadUserByUsername(user.getUsername());
            String jwt = jwtUtilService.generateToken(userSecurity);
            Long id = userSecurity.getUser().getId();
            String authorities = userSecurity.getUser().getAuthorities().stream()
                    .map(authority -> authority.getName())
                    .collect(Collectors.joining(";", "", ""));
            return ResponseEntity.ok(new DTOToken(jwt, id, authorities));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * GET /api/v1/autenticacion/me
     * Obtener info del usuario autenticado actual (DEBUG)
     */
    @GetMapping("/autenticacion/me")
    @PreAuthorize("hasAuthority('ROLE_CITIZEN') or hasAuthority('ROLE_LEADER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Map<String, Object>> getCurrentUser() {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserSecurity) {
                UserSecurity userSecurity = (UserSecurity) principal;
                Map<String, Object> response = new HashMap<>();
                response.put("username", userSecurity.getUsername());
                response.put("userId", userSecurity.getUser().getId());
                response.put("authorities", userSecurity.getAuthorities().stream()
                        .map(a -> a.getAuthority())
                        .toList());
                response.put("enabled", userSecurity.isEnabled());
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    /**
     * GET /api/v1/usuarios/{id}
     * Obtener información de un usuario (CITIZEN/LEADER/ADMIN)
     */
    @GetMapping("/usuarios/{id}")
    @PreAuthorize("hasAuthority('ROLE_CITIZEN') or hasAuthority('ROLE_LEADER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<User> obtenerPorId(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            User user = userRepository.findById(id).orElse(null);
            if (user != null) {
                user.setPassword(null);
                return ResponseEntity.ok(user);
            }
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * GET /api/v1/usuarios/nombre/{username}
     * Obtener información por nombre de usuario (CITIZEN/LEADER/ADMIN)
     */
    @GetMapping("/usuarios/nombre/{username}")
    @PreAuthorize("hasAuthority('ROLE_CITIZEN') or hasAuthority('ROLE_LEADER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<User> obtenerPorNombreUsuario(@PathVariable String username) {
        User u = userService.findByUsername(username);
        if (u != null) {
            u.setPassword(null);
            return ResponseEntity.ok(u);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * DELETE /api/v1/usuarios/{id}
     * Eliminar un usuario (ADMIN)
     */
    @DeleteMapping("/usuarios/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Map<String, String>> eliminar(@PathVariable Long id) {
        Map<String, String> respuesta = new HashMap<>();
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            respuesta.put("mensaje", "Usuario eliminado correctamente.");
            return ResponseEntity.ok(respuesta);
        } else {
            respuesta.put("error", "Usuario no encontrado.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
        }
    }
}
