package BackERP.repository;


import BackERP.models.segusuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RepositoryUsuarios extends JpaRepository<segusuarios, Long> , JpaSpecificationExecutor<segusuarios> {


}
