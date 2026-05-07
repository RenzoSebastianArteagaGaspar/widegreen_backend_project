package pe.edu.upc.widegreen_backend_project.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DTOToken {

    private String jwtToken;
    private Long id;
    private String authorities;

}
