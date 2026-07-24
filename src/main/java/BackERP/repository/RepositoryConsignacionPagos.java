package BackERP.repository;

import BackERP.models.erpConsignacionPagos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RepositoryConsignacionPagos extends JpaRepository<erpConsignacionPagos, Long>, JpaSpecificationExecutor<erpConsignacionPagos> {

    // Buscar pagos por factura
    List<erpConsignacionPagos> findByIdEncabezadoFactura(int idEncabezadoFactura);

    // Buscar pagos por estado - CORREGIDO: ahora usa int en lugar de String
    List<erpConsignacionPagos> findByEstado(int estado);

    // Buscar pagos por factura y estado
    List<erpConsignacionPagos> findByIdEncabezadoFacturaAndEstado(int idEncabezadoFactura, int estado);

    // Obtener suma de pagos por factura
    @Query("SELECT SUM(c.montoPago) FROM erpConsignacionPagos c WHERE c.idEncabezadoFactura = :idFactura AND c.estado = 1 ")
    Double sumMontoPagosByFactura(@Param("idFactura") int idFactura);

    // Nuevo método para múltiples facturas (más eficiente)
    @Query("SELECT c.idEncabezadoFactura, SUM(c.montoPago) FROM erpConsignacionPagos c " +
            "WHERE c.idEncabezadoFactura IN :idsFactura AND c.estado = 1 " +
            "GROUP BY c.idEncabezadoFactura")
    List<Object[]> sumMontoPagosByFacturas(@Param("idsFactura") List<Integer> idsFactura);



  // RepositoryConsignacionPagos.java - Método actualizado

  @Query("SELECT " +
    "COUNT(c) as totalConsignaciones, " +
    "SUM(c.montoPago) as montoTotalConsignaciones, " +
    "SUM(c.montoPago) as totalPagado, " +
    "(SELECT COALESCE(SUM(e.total), 0) - COALESCE(SUM(c2.montoPago), 0) " +
    "FROM erpEncabezadoFacturas e " +
    "LEFT JOIN erpConsignacionPagos c2 ON e.idEncabezadoFactura = c2.idEncabezadoFactura AND c2.estado = 1 " +
    "WHERE e.estado = 1 " +
    "AND e.tipoDocumento = 4 " +
    "AND e.FechaFactura BETWEEN :fechaInicio AND :fechaFin " +
    "AND (:nombreCliente IS NULL OR LOWER(e.idCliente.nombreCliente) LIKE LOWER(CONCAT('%', :nombreCliente, '%')))) as saldoPendiente " +
    "FROM erpConsignacionPagos c " +
    "WHERE c.estado = 1 " +
    "AND c.idEncabezadoFactura IN ( " +
    "  SELECT e.idEncabezadoFactura FROM erpEncabezadoFacturas e " +
    "  WHERE e.estado = 1 " +
    "  AND e.tipoDocumento = 4 " +
    "  AND e.FechaFactura BETWEEN :fechaInicio AND :fechaFin " +
    "  AND (:nombreCliente IS NULL OR LOWER(e.idCliente.nombreCliente) LIKE LOWER(CONCAT('%', :nombreCliente, '%'))))")
  Object[] getResumenConsignacionesPagos(
    @Param("fechaInicio") LocalDate fechaInicio,
    @Param("fechaFin") LocalDate fechaFin,
    @Param("nombreCliente") String nombreCliente
  );

}
