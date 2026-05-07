package pe.edu.upc.widegreen_backend_project.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "Cliente")
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_cliente;
    private String name;
    private String apellido;
    private Integer cant_eventos_asistidos;

    @ManyToOne
    @JoinColumn(name = "id_nivel")
    private Nivel Nivel;

    @ManyToOne
    @JoinColumn(name="id_user")
    private User User;

}
