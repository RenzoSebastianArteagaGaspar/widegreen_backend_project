package pe.edu.upc.widegreen_backend_project.ServicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pe.edu.upc.widegreen_backend_project.Entities.User;
import pe.edu.upc.widegreen_backend_project.Services.UserService;
import pe.edu.upc.widegreen_backend_project.Security.UserSecurity;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        
        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }
        
        System.out.println("=== UserDetailsServiceImpl.loadUserByUsername ===");
        System.out.println("Username: " + username);
        System.out.println("User ID: " + user.getId());
        System.out.println("User Enabled: " + user.isEnabled());
        System.out.println("Authorities count: " + (user.getAuthorities() != null ? user.getAuthorities().size() : 0));
        if (user.getAuthorities() != null) {
            System.out.println("Authorities: " + user.getAuthorities().stream()
                    .map(a -> a.getName())
                    .toList());
        }
        System.out.println("=== FIN ===");
        
        return new UserSecurity(user);
    }
}

