package BackERP.seguridad;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class ConfiguracionSeguridad

{

  /*  @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

                // Autorización de endpoints
        HttpSecurity httpSecurity = http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**").permitAll() // rutas públicas
                        .anyRequest().authenticated() // todo lo demás requiere login
                )
                // Configuración de OAuth2 Resource Server con JWT
                .oauth2ResourceServer(oauth2 -> oauth2.jwt());
        return http.build();
    }*/
}
