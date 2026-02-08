package BackERP.repository;

import BackERP.models.erpEncabezadoFacturas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RepositoryEncabezadoFacturas extends JpaRepository<erpEncabezadoFacturas, Long>, JpaSpecificationExecutor<erpEncabezadoFacturas> {
}
