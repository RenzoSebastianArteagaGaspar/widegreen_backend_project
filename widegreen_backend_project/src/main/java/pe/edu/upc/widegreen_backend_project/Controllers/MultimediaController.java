package pe.edu.upc.widegreen_backend_project.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.upc.widegreen_backend_project.Dtos.MultimediaDTO;
import pe.edu.upc.widegreen_backend_project.Services.MultimediaService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/multimedia")
public class MultimediaController {
    
    @Autowired
    private MultimediaService multimediaService;

    /**
     * POST /api/v1/multimedia/publicaciones/{publicacionId}/fotos
     * Subir una foto para una publicación (CITIZEN/LEADER/ADMIN)
     */
    @PostMapping("/publicaciones/{publicacionId}/fotos")
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_CITIZEN') or hasAuthority('ROLE_LEADER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> subirFotoPublicacion(
            @PathVariable Long publicacionId,
            @RequestParam("file") MultipartFile file) {
        try {
            System.out.println("=== MULTIMEDIA CONTROLLER - INICIO ===");
            System.out.println("PublicacionId: " + publicacionId);
            System.out.println("Archivo: " + (file != null ? file.getOriginalFilename() : "null"));
            System.out.println("Tamaño: " + (file != null ? file.getSize() : 0));
            
            if (file == null || file.isEmpty()) {
                System.out.println("ERROR: Archivo vacío o null");
                return ResponseEntity.badRequest().body("El archivo está vacío");
            }

            byte[] contenido = file.getBytes();
            System.out.println("Bytes leídos: " + contenido.length);
            
            MultimediaDTO foto = multimediaService.subirFoto(
                    "PUBLICACION",
                    publicacionId,
                    file.getOriginalFilename(),
                    file.getContentType(),
                    contenido
            );
            
            System.out.println("Foto guardada exitosamente ID: " + foto.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(foto);
        } catch (IOException e) {
            System.out.println("ERROR IOException: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error al leer el archivo: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("ERROR Exception: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * POST /api/v1/multimedia/eventos/{eventoId}/fotos
     * Subir una foto para un evento (CITIZEN/LEADER/ADMIN)
     */
    @PostMapping("/eventos/{eventoId}/fotos")
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_CITIZEN') or hasAuthority('ROLE_LEADER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> subirFotoEvento(
            @PathVariable Long eventoId,
            @RequestParam("file") MultipartFile file) {
        try {
            System.out.println("=== MULTIMEDIA CONTROLLER - SUBIR FOTO EVENTO ===");
            System.out.println("EventoId: " + eventoId);
            System.out.println("Archivo: " + (file != null ? file.getOriginalFilename() : "null"));
            System.out.println("Tamaño: " + (file != null ? file.getSize() : 0));
            
            if (file == null || file.isEmpty()) {
                System.out.println("ERROR: Archivo vacío o null");
                return ResponseEntity.badRequest().body("El archivo está vacío");
            }

            byte[] contenido = file.getBytes();
            System.out.println("Bytes leídos: " + contenido.length);
            
            MultimediaDTO foto = multimediaService.subirFoto(
                    "EVENTO",
                    eventoId,
                    file.getOriginalFilename(),
                    file.getContentType(),
                    contenido
            );
            
            System.out.println("Foto guardada exitosamente ID: " + foto.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(foto);
        } catch (IOException e) {
            System.out.println("ERROR IOException: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error al leer el archivo: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("ERROR Exception: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * GET /api/v1/multimedia/publicaciones/{publicacionId}/fotos
     * Obtener todas las fotos de una publicación (CITIZEN/LEADER/ADMIN)
     */
    @GetMapping("/publicaciones/{publicacionId}/fotos")
    @PreAuthorize("hasAuthority('ROLE_CITIZEN') or hasAuthority('ROLE_LEADER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<MultimediaDTO>> obtenerFotosPublicacion(@PathVariable Long publicacionId) {
        List<MultimediaDTO> fotos = multimediaService.obtenerFotosPorEntidad("PUBLICACION", publicacionId);
        return ResponseEntity.ok(fotos);
    }

    /**
     * GET /api/v1/multimedia/eventos/{eventoId}/fotos
     * Obtener todas las fotos de un evento (CITIZEN/LEADER/ADMIN)
     */
    @GetMapping("/eventos/{eventoId}/fotos")
    @PreAuthorize("hasAuthority('ROLE_CITIZEN') or hasAuthority('ROLE_LEADER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<MultimediaDTO>> obtenerFotosEvento(@PathVariable Long eventoId) {
        List<MultimediaDTO> fotos = multimediaService.obtenerFotosPorEntidad("EVENTO", eventoId);
        return ResponseEntity.ok(fotos);
    }

    /**
     * GET /api/v1/multimedia/{id}
     * Obtener una foto específica (CITIZEN/LEADER/ADMIN)
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_CITIZEN') or hasAuthority('ROLE_LEADER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> obtenerFoto(@PathVariable Long id) {
        MultimediaDTO foto = multimediaService.obtenerFoto(id);
        if (foto != null) {
            return ResponseEntity.ok(foto);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * DELETE /api/v1/multimedia/{id}
     * Eliminar una foto (LEADER/ADMIN)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_LEADER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> eliminarFoto(@PathVariable Long id) {
        multimediaService.eliminarFoto(id);
        return ResponseEntity.noContent().build();
    }
}
