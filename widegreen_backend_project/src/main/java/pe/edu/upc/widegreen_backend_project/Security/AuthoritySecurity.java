package pe.edu.upc.widegreen_backend_project.Security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import pe.edu.upc.widegreen_backend_project.Entities.Authority;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthoritySecurity implements GrantedAuthority {

    private Authority authority;

    @Override
    public String getAuthority() {
        return authority.getName();
    }


}
