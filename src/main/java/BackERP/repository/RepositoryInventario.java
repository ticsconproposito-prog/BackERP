package BackERP.repository;

import BackERP.models.erpinventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RepositoryInventario extends JpaRepository<erpinventario, Long>, JpaSpecificationExecutor<erpinventario> {
}
