package pe.edu.upc.widegreen_backend_project.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class EventoPorCliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_eventoPorCliente;
    private LocalDate fechaInscripcion;
    private String calificacion;
    private String asistencia;

    @ManyToOne
    @JoinColumn(name="id_cliente")
    private Cliente id_Cliente;

    @ManyToOne
    @JoinColumn(name="id_evento")
    private Evento id_evento;
}
