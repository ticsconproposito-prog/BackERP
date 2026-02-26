package BackERP.repository;

import BackERP.models.segPerfilesPaginas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RepositoryPerfilesPaginas extends JpaRepository<segPerfilesPaginas, Integer>, JpaSpecificationExecutor<segPerfilesPaginas> {
}
