package BackERP.repository;

import BackERP.models.erpConsignacionPagos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RepositoryConsignacionPagos extends JpaRepository<erpConsignacionPagos, Long>, JpaSpecificationExecutor<erpConsignacionPagos> {

  // Buscar pagos por factura
  List<erpConsignacionPagos> findByIdEncabezadoFactura(int idEncabezadoFactura);

  // Buscar pagos por estado
  List<erpConsignacionPagos> findByEstado(String estado);

  // Buscar pagos por factura y estado
  List<erpConsignacionPagos> findByIdEncabezadoFacturaAndEstado(int idEncabezadoFactura, String estado);

  // Obtener suma de pagos por factura
  @Query("SELECT SUM(c.montoPago) FROM erpConsignacionPagos c WHERE c.idEncabezadoFactura = :idFactura AND c.estado = 1")
  Double sumMontoPagosByFactura(@Param("idFactura") int idFactura);
}
