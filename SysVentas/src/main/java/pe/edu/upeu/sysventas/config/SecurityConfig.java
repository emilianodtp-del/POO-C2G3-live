package pe.edu.upeu.sysventas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuración de seguridad general del sistema.
 * Esta clase declara el bean PasswordEncoder que permite
 * encriptar las contraseñas de los usuarios antes de guardarlas.
 */
@Configuration
public class SecurityConfig {

    /**
     * Bean de tipo PasswordEncoder (usa BCrypt).
     * Spring lo inyectará automáticamente donde se requiera.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
