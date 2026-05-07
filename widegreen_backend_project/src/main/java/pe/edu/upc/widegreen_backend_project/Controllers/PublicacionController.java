package pe.edu.upc.widegreen_backend_project.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.widegreen_backend_project.Dtos.ComentarioDTO;
import pe.edu.upc.widegreen_backend_project.Dtos.PublicacionDTO;
import pe.edu.upc.widegreen_backend_project.Services.PublicacionService;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class PublicacionController {
    @Autowired
    private PublicacionService publicacionService;

    /**
     * POST /api/v1/ciudadanos/{clienteId}/publicaciones
     * Crear una nueva publicación para un ciudadano (CITIZEN/LEADER/ADMIN)
     */
    @PostMapping("/ciudadanos/{clienteId}/publicaciones")
    @PreAuthorize("hasAuthority('ROLE_CITIZEN') or hasAuthority('ROLE_LEADER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> crear(@PathVariable Long clienteId, @RequestBody PublicacionDTO dto) {
        try {
            
            dto.setId_cliente(clienteId);
            PublicacionDTO creada = publicacionService.crearPublicacion(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(creada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * GET /api/v1/publicaciones
     * Listar todas las publicaciones (CITIZEN/LEADER/ADMIN)
     */
    @GetMapping("/publicaciones")
    @PreAuthorize("hasAuthority('ROLE_CITIZEN') or hasAuthority('ROLE_LEADER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<PublicacionDTO>> listar() {
        return ResponseEntity.ok(publicacionService.listarTodas());
    }

    /**
     * GET /api/v1/publicaciones/{id}
     * Obtener una publicación por ID (CITIZEN/LEADER/ADMIN)
     */
    @GetMapping("/publicaciones/{id}")
    @PreAuthorize("hasAuthority('ROLE_CITIZEN') or hasAuthority('ROLE_LEADER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<PublicacionDTO> obtenerPorId(@PathVariable Long id) {
        PublicacionDTO pub = publicacionService.listarTodas().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
        return pub != null ? ResponseEntity.ok(pub) : ResponseEntity.notFound().build();
    }

    /**
     * PUT /api/v1/publicaciones/{id}
     * Editar una publicación (LEADER/ADMIN)
     */
    @PutMapping("/publicaciones/{id}")
    @PreAuthorize("hasAuthority('ROLE_LEADER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<PublicacionDTO> editar(@PathVariable Long id, @RequestBody PublicacionDTO dto) {
        PublicacionDTO editado = publicacionService.editarPublicacion(id, dto);
        return editado != null ? ResponseEntity.ok(editado) : ResponseEntity.notFound().build();
    }

    /**
     * DELETE /api/v1/publicaciones/{id}
     * Eliminar una publicación (LEADER/ADMIN)
     */
    @DeleteMapping("/publicaciones/{id}")
    @PreAuthorize("hasAuthority('ROLE_LEADER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        publicacionService.eliminarPublicacion(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/v1/publicaciones/por-categoria/{categoria}
     * Filtrar publicaciones por categoría (CITIZEN/LEADER/ADMIN)
     */
    @GetMapping("/publicaciones/por-categoria/{categoria}")
    @PreAuthorize("hasAuthority('ROLE_CITIZEN') or hasAuthority('ROLE_LEADER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<PublicacionDTO>> filtrarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(publicacionService.buscarPorCategoria(categoria));
    }

    /**
     * GET /api/v1/publicaciones/buscar?palabra={clave}
     * Buscar publicaciones por palabra clave (CITIZEN/LEADER/ADMIN)
     */
    @GetMapping("/publicaciones/buscar")
    @PreAuthorize("hasAuthority('ROLE_CITIZEN') or hasAuthority('ROLE_LEADER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<PublicacionDTO>> buscar(@RequestParam String palabra) {
        return ResponseEntity.ok(publicacionService.buscarPorPalabra(palabra));
    }

    /**
     * GET /api/v1/ciudadanos/{idCliente}/publicaciones
     * Listar publicaciones de un ciudadano específico (CITIZEN/LEADER/ADMIN)
     */
    @GetMapping("/ciudadanos/{idCliente}/publicaciones")
    @PreAuthorize("hasAuthority('ROLE_CITIZEN') or hasAuthority('ROLE_LEADER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<PublicacionDTO>> listarPorCliente(@PathVariable Long idCliente) {
        return ResponseEntity.ok(publicacionService.listarPorCliente(idCliente));
    }

    /**
     * POST /api/v1/publicaciones/{publicacionId}/comentarios
     * Agregar comentario a una publicación (CITIZEN/LEADER/ADMIN)
     */
    @PostMapping("/publicaciones/{publicacionId}/comentarios")
    @PreAuthorize("hasAuthority('ROLE_CITIZEN') or hasAuthority('ROLE_LEADER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> comentar(@PathVariable Long publicacionId, @RequestBody ComentarioDTO dto) {
        try {
            dto.setId_publicacion(publicacionId);
            ComentarioDTO creado = publicacionService.comentar(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * GET /api/v1/publicaciones/{idPub}/comentarios
     * Ver comentarios de una publicación (CITIZEN/LEADER/ADMIN)
     */
    @GetMapping("/publicaciones/{idPub}/comentarios")
    @PreAuthorize("hasAuthority('ROLE_CITIZEN') or hasAuthority('ROLE_LEADER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<ComentarioDTO>> verComentarios(@PathVariable Long idPub) {
        return ResponseEntity.ok(publicacionService.listarComentarios(idPub));
    }

    /**
     * DELETE /api/v1/publicaciones/comentarios/{id}
     * Eliminar un comentario (LEADER/ADMIN)
     */
    @DeleteMapping("/publicaciones/comentarios/{id}")
    @PreAuthorize("hasAuthority('ROLE_LEADER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> borrarComentario(@PathVariable Long id) {
        publicacionService.eliminarComentario(id);
        return ResponseEntity.noContent().build();
    }
}
