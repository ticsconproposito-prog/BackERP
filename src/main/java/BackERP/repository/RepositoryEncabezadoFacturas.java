package BackERP.repository;

import BackERP.models.erpEncabezadoFacturas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RepositoryEncabezadoFacturas extends JpaRepository<erpEncabezadoFacturas, Long>, JpaSpecificationExecutor<erpEncabezadoFacturas> {

  Optional<erpEncabezadoFacturas> findByNumeroAutorizacionResAPI(String numeroAutorizacionResAPI);
  Optional<erpEncabezadoFacturas> findBySerieResAPIAndPreimpresoResAPI(String serieResAPI, long preimpresoResAPI);

  // Buscar encabezado activo
  Optional<erpEncabezadoFacturas> findByIdEncabezadoFacturaAndEstado(Long idEncabezadoFactura, int estado);
}
