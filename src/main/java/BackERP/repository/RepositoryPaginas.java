package BackERP.repository;

import BackERP.models.segPaginas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RepositoryPaginas extends JpaRepository<segPaginas, Long> , JpaSpecificationExecutor<segPaginas> {
}
