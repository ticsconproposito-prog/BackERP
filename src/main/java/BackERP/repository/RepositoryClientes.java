package BackERP.repository;

import BackERP.models.erpclientes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RepositoryClientes  extends JpaRepository<erpclientes, Long>, JpaSpecificationExecutor<erpclientes> {
}
