package BackERP.repository;

import BackERP.models.erpEncabezadoFacturas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface RepositoryEncabezadoFacturas extends JpaRepository<erpEncabezadoFacturas, Long>, JpaSpecificationExecutor<erpEncabezadoFacturas> {

    // Buscar factura por el número de autorización FEL (UUID)
    Optional<erpEncabezadoFacturas> findByNumeroAutorizacionResAPI(String numeroAutorizacionResAPI);
}
