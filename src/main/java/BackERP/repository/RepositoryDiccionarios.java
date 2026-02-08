package BackERP.repository;

import BackERP.models.erpdiccionarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RepositoryDiccionarios extends JpaRepository<erpdiccionarios, Long>, JpaSpecificationExecutor<erpdiccionarios> {
}
