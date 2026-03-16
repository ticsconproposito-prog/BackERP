package BackERP.repository;


import BackERP.models.segPerfiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RepositoryPerfiles extends JpaRepository<segPerfiles, Long> , JpaSpecificationExecutor<segPerfiles> {


}
