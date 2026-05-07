package pe.edu.upc.widegreen_backend_project.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Distrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_distrito;

    private String distrito_nombre;
    private String distrito_zonas;
    private Double latitud;
    private Double longitud;

    @ManyToOne
    @JoinColumn(name="id_ciudad")
    private Ciudad id_Ciudad;

}
