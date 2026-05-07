package pe.edu.upc.widegreen_backend_project.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Table(name = "Lider")
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Lider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_lider;

    @OneToOne
    @JoinColumn(name = "id_user", unique = true)
    private User user;

    private String career;
    private String initiative;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
