package BackERP.repository;

import BackERP.models.erpOrdenProductos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RepositoryOrdenProductos extends JpaRepository<erpOrdenProductos, Long> , JpaSpecificationExecutor<erpOrdenProductos> {
}
