package BackERP.repository;

import BackERP.models.erpClientes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RepositoryClientes  extends JpaRepository<erpClientes, Long>, JpaSpecificationExecutor<erpClientes> {
}
