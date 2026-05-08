package BackERP;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;  // ← AGREGAR ESTA IMPORTACIÓN
import org.springframework.transaction.annotation.EnableTransactionManagement;  // ← TAMBIÉN AGREGAR (para @Transactional)

@SpringBootApplication
@EnableAsync                    // ← AGREGAR: Habilita métodos asíncronos
@EnableTransactionManagement    // ← AGREGAR: Habilita @Transactional (importante)
public class Application extends SpringBootServletInitializer {

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    return builder.sources(Application.class);
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
