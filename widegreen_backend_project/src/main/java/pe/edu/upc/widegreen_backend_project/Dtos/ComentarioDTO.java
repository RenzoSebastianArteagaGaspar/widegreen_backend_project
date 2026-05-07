package pe.edu.upc.widegreen_backend_project.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComentarioDTO {
    private Long id_comentario;
    private String contenido;
    private Long id_cliente;
    private String nombre_cliente;
    private Long id_publicacion;
}
