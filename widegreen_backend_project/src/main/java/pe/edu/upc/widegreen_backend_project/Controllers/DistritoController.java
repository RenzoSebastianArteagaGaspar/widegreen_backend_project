package pe.edu.upc.widegreen_backend_project.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.widegreen_backend_project.Dtos.DistritoDTO;
import pe.edu.upc.widegreen_backend_project.Entities.Distrito;
import pe.edu.upc.widegreen_backend_project.Repository.DistritoRepository;
import pe.edu.upc.widegreen_backend_project.Repository.EventoRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class DistritoController {

    @Autowired
    private DistritoRepository distritoRepository;

    @Autowired
    private EventoRepository eventoRepository;

    /**
     * GET /api/v1/distritos
     * Listar todos los distritos con estadísticas de eventos activos (CITIZEN/LEADER/ADMIN)
     */
    @GetMapping("/distritos")
    @PreAuthorize("hasAuthority('ROLE_CITIZEN') or hasAuthority('ROLE_LEADER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<DistritoDTO>> listar() {
        List<Distrito> distritos = distritoRepository.findAll();
        List<DistritoDTO> dtos = new ArrayList<>();

        for (Distrito d : distritos) {
            long cantidad = eventoRepository.findAll().stream()
                    .filter(e -> e.getDistrito() != null &&
                            e.getDistrito().getId_distrito().equals(d.getId_distrito()) &&
                            "Activo".equalsIgnoreCase(e.getEvento_estado()))
                    .count();

            dtos.add(new DistritoDTO(
                    d.getId_distrito(),
                    d.getDistrito_nombre(),
                    d.getDistrito_zonas(),
                    (int) cantidad
            ));
        }
        return ResponseEntity.ok(dtos);
    }

    /**
     * GET /api/v1/distritos/{id}
     * Obtener un distrito específico por ID (CITIZEN/LEADER/ADMIN)
     */
    @GetMapping("/distritos/{id}")
    @PreAuthorize("hasAuthority('ROLE_CITIZEN') or hasAuthority('ROLE_LEADER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<DistritoDTO> obtenerPorId(@PathVariable Long id) {
        Distrito d = distritoRepository.findById(id).orElse(null);
        if (d == null) {
            return ResponseEntity.notFound().build();
        }

        long cantidad = eventoRepository.findAll().stream()
                .filter(e -> e.getDistrito() != null &&
                        e.getDistrito().getId_distrito().equals(d.getId_distrito()) &&
                        "Activo".equalsIgnoreCase(e.getEvento_estado()))
                .count();

        DistritoDTO dto = new DistritoDTO(
                d.getId_distrito(),
                d.getDistrito_nombre(),
                d.getDistrito_zonas(),
                (int) cantidad
        );
        return ResponseEntity.ok(dto);
    }

    /**
     * POST /api/v1/distritos
     * Crear un nuevo distrito (ADMIN only)
     */
    @PostMapping("/distritos")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> crearDistrito(@RequestBody DistritoDTO distritoDTO) {
        try {
            Distrito distrito = new Distrito();
            distrito.setDistrito_nombre(distritoDTO.getNombre());
            distrito.setDistrito_zonas(distritoDTO.getZonas());
            
            Distrito distritoGuardado = distritoRepository.save(distrito);
            
            DistritoDTO response = new DistritoDTO(
                    distritoGuardado.getId_distrito(),
                    distritoGuardado.getDistrito_nombre(),
                    distritoGuardado.getDistrito_zonas(),
                    0
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
