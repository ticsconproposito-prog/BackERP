package BackERP.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

  @Bean(name = "felTaskExecutor")
  public Executor felTaskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

    // Configuración crítica para evitar fuga de hilos
    executor.setCorePoolSize(5);           // Hilos mínimos siempre disponibles
    executor.setMaxPoolSize(20);            // Máximo de hilos simultáneos
    executor.setQueueCapacity(100);         // Cola de espera
    executor.setKeepAliveSeconds(60);       // Hilos extras mueren después de 60s inactivos

    // POLÍTICA CRÍTICA: Rechazar nuevas tareas cuando no hay capacidad
    // Esto evita que se acumulen hilos infinitos
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());

    executor.setThreadNamePrefix("FEL-ASYNC-");
    executor.initialize();
    return executor;
  }
}
