package pe.edu.upc.widegreen_backend_project.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicacionDTO {
    private Long id;
    private String titulo;
    private String contenido;
    private LocalDateTime fecha;
    private String categoria;
    private Long id_cliente;
    private String nombre_autor;
}
