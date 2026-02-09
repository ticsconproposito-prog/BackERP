package BackERP.repository;

import BackERP.models.erpMovimientosProductos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface RepositoryMovimientosProductos extends JpaRepository<erpMovimientosProductos, Long> , JpaSpecificationExecutor<erpMovimientosProductos> {
    Optional<erpMovimientosProductos> findByIdOrdenProducto(Long idOrdenProducto);

}
