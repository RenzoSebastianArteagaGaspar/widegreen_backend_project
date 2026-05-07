package pe.edu.upc.widegreen_backend_project.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LiderDTO {
    private Long id_lider;
    private String career;
    private String initiative;
    private LocalDateTime createdAt;
}
