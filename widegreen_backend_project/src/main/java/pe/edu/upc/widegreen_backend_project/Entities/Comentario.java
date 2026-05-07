package pe.edu.upc.widegreen_backend_project.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Comentario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_comentario;
    private String contenido;

    @ManyToOne
    @JoinColumn(name="id_cliente")
    private Cliente id_Cliente;

    @ManyToOne
    @JoinColumn(name="id_publicacion")
    private Publicacion publicacion;
}
