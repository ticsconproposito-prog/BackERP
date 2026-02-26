package BackERP.repository;

import BackERP.models.segUsuariosPerfiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RepositoryUsuariosPerfiles extends JpaRepository<segUsuariosPerfiles, Integer>, JpaSpecificationExecutor<segUsuariosPerfiles> {
}
