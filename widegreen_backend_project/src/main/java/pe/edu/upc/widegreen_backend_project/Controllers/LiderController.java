package pe.edu.upc.widegreen_backend_project.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.widegreen_backend_project.Dtos.LiderDTO;
import pe.edu.upc.widegreen_backend_project.Entities.Lider;
import pe.edu.upc.widegreen_backend_project.Services.ILiderService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*", maxAge = 3600)
public class LiderController {

    @Autowired
    private ILiderService liderService;

    /**
     * GET /api/v1/lideres
     * Listar todos los líderes (LEADER/ADMIN)
     */
    @GetMapping("/lideres")
    @PreAuthorize("hasAuthority('ROLE_LEADER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<LiderDTO>> obtenerTodos() {
        List<Lider> lideres = liderService.getAll();
        List<LiderDTO> dtos = lideres.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * GET /api/v1/lideres/{liderId}
     * Obtener líder por ID (LEADER/ADMIN)
     */
    @GetMapping("/lideres/{liderId}")
    @PreAuthorize("hasAuthority('ROLE_LEADER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<LiderDTO> obtenerPorId(@PathVariable Long liderId) {
        var liderOpt = liderService.getById(liderId);
        if (liderOpt.isPresent()) {
            return ResponseEntity.ok(convertToDTO(liderOpt.get()));
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * GET /api/v1/usuarios/{userId}/lideres
     * Obtener perfil de líder por ID de usuario (LEADER/ADMIN)
     */
    @GetMapping("/usuarios/{userId}/lideres")
    @PreAuthorize("hasAuthority('ROLE_LEADER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<LiderDTO> obtenerPorUserId(@PathVariable Long userId) {
        var liderOpt = liderService.getByUserId(userId);
        if (liderOpt.isPresent()) {
            return ResponseEntity.ok(convertToDTO(liderOpt.get()));
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * PUT /api/v1/lideres/{liderId}
     * Actualizar perfil de líder (LEADER/ADMIN)
     */
    @PutMapping("/lideres/{liderId}")
    @PreAuthorize("hasAuthority('ROLE_LEADER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<LiderDTO> actualizar(
            @PathVariable Long liderId,
            @RequestBody LiderDTO liderDTO) {
        
        Lider updated = liderService.update(liderId, liderDTO.getCareer(), liderDTO.getInitiative());
        
        if (updated != null) {
            return ResponseEntity.ok(convertToDTO(updated));
        }
        
        return ResponseEntity.notFound().build();
    }

    /**
     * DELETE /api/v1/lideres/{liderId}
     * Eliminar perfil de líder (ADMIN)
     */
    @DeleteMapping("/lideres/{liderId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long liderId) {
        liderService.delete(liderId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Convertir Lider a LiderDTO
     */
    private LiderDTO convertToDTO(Lider lider) {
        LiderDTO dto = new LiderDTO();
        dto.setId_lider(lider.getId_lider());
        dto.setCareer(lider.getCareer());
        dto.setInitiative(lider.getInitiative());
        dto.setCreatedAt(lider.getCreatedAt());
        return dto;
    }
}
