package BackERP.repository;


import BackERP.models.segUsuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RepositoryUsuarios extends JpaRepository<segUsuarios, Long> , JpaSpecificationExecutor<segUsuarios> {


}
