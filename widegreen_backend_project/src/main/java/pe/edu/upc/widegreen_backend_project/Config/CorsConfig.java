package pe.edu.upc.widegreen_backend_project.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Permitir origen del frontend Angular
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:4200",      // Desarrollo local Angular
                "http://localhost:3000",      // Alternativa si usan otro puerto
                "http://127.0.0.1:4200",
                "http://127.0.0.1:3000"
        ));
        
        // Permitir métodos HTTP
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
        ));
        
        // Permitir headers
        configuration.setAllowedHeaders(Arrays.asList(
                "Content-Type",
                "Authorization",
                "X-Requested-With",
                "Accept",
                "Origin"
        ));
        
        // Permitir credenciales (cookies, tokens)
        configuration.setAllowCredentials(true);
        
        // Tiempo máximo de cache para preflight requests (en segundos)
        configuration.setMaxAge(3600L);
        
        // Headers a exponer al cliente
        configuration.setExposedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type"
        ));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        
        return source;
    }
}
