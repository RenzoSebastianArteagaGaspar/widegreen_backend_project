package pe.edu.upc.widegreen_backend_project.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Multimedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombreArchivo;
    private String tipoArchivo;
    @Column(columnDefinition="TEXT")
    private String contenidoImagenBase64;
    private Long tamañoArchivo;
    private LocalDateTime fechaSubida;
    private String tipoEntidad; // "PUBLICACION", "EVENTO"
    private Long idEntidad; // ID de la publicación o evento
    
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="id_publicacion")
    private Publicacion idPublicacion;
    
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="id_evento")
    private Evento idEvento;
}
