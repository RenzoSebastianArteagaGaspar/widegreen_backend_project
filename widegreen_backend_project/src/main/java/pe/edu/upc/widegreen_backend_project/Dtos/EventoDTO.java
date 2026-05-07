package pe.edu.upc.widegreen_backend_project.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventoDTO {
    private Long id_evento;
    private String evento_titulo;
    private String descripcion;
    private LocalDate evento_fechaInicio;
    private LocalDate evento_fechaFin;
    private LocalTime evento_hora;
    private String evento_estado;
    private Long id_distrito;
    private String nombre_distrito;
    private Integer cantidad_inscritos;
    private Double latitud;
    private Double longitud;
}
