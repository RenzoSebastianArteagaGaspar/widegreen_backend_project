package pe.edu.upc.widegreen_backend_project.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime; // Importante para US #3 (Noticias del día)
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Publicacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String contenido;
    private LocalDateTime fecha;
    private String categoria;

    @ManyToOne
    @JoinColumn(name="id_cliente")
    private Cliente id_Cliente;

    @OneToMany(mappedBy = "idPublicacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Multimedia> multimedia;
}
