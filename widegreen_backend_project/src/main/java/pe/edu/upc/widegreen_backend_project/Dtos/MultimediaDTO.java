package pe.edu.upc.widegreen_backend_project.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MultimediaDTO {
    private Long id;
    private String nombreArchivo;
    private String tipoArchivo;
    private String contenidoImagenBase64;
    private Long tamañoArchivo;
    private LocalDateTime fechaSubida;
    private String tipoEntidad; // "PUBLICACION", "EVENTO"
    private Long idEntidad;
}
