package pe.edu.upc.widegreen_backend_project.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_evento;
    private String evento_titulo;
    private String descripcion;
    private LocalDate evento_fechaInicio;
    private LocalDate evento_fechaFin;
    private LocalTime evento_hora;
    private String evento_estado;
    private Double latitud;
    private Double longitud;

    @ManyToOne
    @JoinColumn(name = "id_distrito")
    private Distrito distrito;

    @OneToMany(mappedBy = "idEvento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Multimedia> multimedia;

    @OneToMany(mappedBy = "id_evento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventoPorCliente> eventosPorCliente;
}
