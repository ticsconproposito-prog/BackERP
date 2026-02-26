package BackERP.repository;


import BackERP.models.segperfiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RepositoryPerfiles extends JpaRepository<segperfiles, Long> , JpaSpecificationExecutor<segperfiles> {


}
