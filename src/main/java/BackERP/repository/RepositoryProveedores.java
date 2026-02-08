package BackERP.repository;

import BackERP.models.erpProveedores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RepositoryProveedores  extends JpaRepository<erpProveedores, Long>, JpaSpecificationExecutor<erpProveedores> {


}
