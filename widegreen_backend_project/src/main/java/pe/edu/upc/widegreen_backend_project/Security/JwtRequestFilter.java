package pe.edu.upc.widegreen_backend_project.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtilService jwtUtilService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String token = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            username = jwtUtilService.extractUsername(token);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserSecurity securityUser = (UserSecurity) this.userDetailsService.loadUserByUsername(username);

            if (jwtUtilService.validateToken(token, securityUser)) {
                // Extraer authorities del token JWT
                List<String> authoritiesFromToken = jwtUtilService.extractAuthorities(token);
                List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
                
                if (authoritiesFromToken != null && !authoritiesFromToken.isEmpty()) {
                    // Usar las authorities del token
                    grantedAuthorities = authoritiesFromToken.stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
                } else {
                    // Fallback: usar las authorities de la BD si el token no las trae
                    grantedAuthorities = new ArrayList<>(securityUser.getAuthorities());
                }
                
                System.out.println("=== JwtRequestFilter ===");
                System.out.println("Username: " + username);
                System.out.println("Authorities from token: " + authoritiesFromToken);
                System.out.println("Authorities count: " + grantedAuthorities.size());
                System.out.println("Authorities: " + grantedAuthorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()));
                System.out.println("=== FIN JwtRequestFilter ===");
                
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        securityUser, null, grantedAuthorities);
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        chain.doFilter(request, response);
    }

}
