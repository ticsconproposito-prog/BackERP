package BackERP.repository;

import BackERP.models.erpProductos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RepositoryProductos extends JpaRepository<erpProductos, Long>, JpaSpecificationExecutor<erpProductos> {
}
