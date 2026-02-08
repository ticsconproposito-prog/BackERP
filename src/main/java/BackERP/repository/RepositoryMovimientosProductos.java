package BackERP.repository;

import BackERP.models.erpMovimientosProductos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RepositoryMovimientosProductos extends JpaRepository<erpMovimientosProductos, Long> , JpaSpecificationExecutor<erpMovimientosProductos> {
}
