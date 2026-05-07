package pe.edu.upc.widegreen_backend_project.Dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DTOUser {

    private Long id;
    private String username;
    private String password;
    private String roleName; // Nombre del rol (ROLE_CITIZEN, ROLE_LEADER, ROLE_ADMIN)
    private String career; // Solo para ROLE_LEADER
    private String initiative; // Solo para ROLE_LEADER

}
