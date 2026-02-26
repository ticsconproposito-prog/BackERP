package BackERP.repository;

import BackERP.models.segpaginas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RepositoryPaginas extends JpaRepository<segpaginas, Long> , JpaSpecificationExecutor<segpaginas> {
}
