package pe.edu.upc.widegreen_backend_project.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteDTO {
    private Long id_cliente;
    private String name;
    private String apellido;
    private Integer cant_eventos_asistidos;
    private Long id_nivel;
    private Long id_user;
}