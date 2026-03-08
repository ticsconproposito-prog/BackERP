package BackERP.repository;

import BackERP.models.erpDiccionarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RepositoryDiccionarios extends JpaRepository<erpDiccionarios, Long>, JpaSpecificationExecutor<erpDiccionarios> {
}
