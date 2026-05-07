package pe.edu.upc.widegreen_backend_project.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistritoDTO {
    private Long id_distrito;
    private String nombre;
    private String zonas;
    private int cantidad_eventos_activos;
}