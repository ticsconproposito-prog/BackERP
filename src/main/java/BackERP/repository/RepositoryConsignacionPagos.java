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


// RepositoryConsignacionPagos.java - Método actualizado con facturaProcesada

  @Query(value = "SELECT " +
    "COALESCE(COUNT(*), 0) as totalConsignaciones, " +
    "COALESCE(SUM(ef.totalBruto), 0) as montoTotalConsignaciones, " +
    "COALESCE( " +
    "    ( " +
    "        SELECT SUM(cp.montoPago) " +
    "        FROM erpConsignacionPagos cp " +
    "        WHERE cp.idEncabezadoFactura IN ( " +
    "            SELECT ef2.idEncabezadoFactura " +
    "            FROM erpEncabezadoFacturas ef2 " +
    "            INNER JOIN erpClientes c2 ON ef2.idCliente = c2.idCliente " +
    "            WHERE ef2.estado = 1 " +
    "              AND ef2.tipoDocumento = 4 " +
    "              AND (:facturaProcesada IS NULL OR ef2.facturaProcesada = :facturaProcesada) " +
    "              AND ef2.fechaFactura BETWEEN :fechaInicio AND :fechaFin " +
    "              AND ( " +
    "                  (:nombreCliente IS NULL OR LOWER(c2.nombreCliente) LIKE LOWER(CONCAT('%', :nombreCliente, '%'))) " +
    "                  OR (:nombreCliente IS NULL OR LOWER(ef2.nombreFactura) LIKE LOWER(CONCAT('%', :nombreCliente, '%'))) " +
    "              ) " +
    "        ) " +
    "        AND cp.estado = 1 " +
    "    ), 0 " +
    ") as totalPagado " +
    "FROM erpEncabezadoFacturas ef " +
    "INNER JOIN erpClientes c ON ef.idCliente = c.idCliente " +
    "WHERE ef.estado = 1 " +
    "  AND ef.tipoDocumento = 4 " +
    "  AND (:facturaProcesada IS NULL OR ef.facturaProcesada = :facturaProcesada) " +
    "  AND ef.fechaFactura BETWEEN :fechaInicio AND :fechaFin " +
    "  AND ( " +
    "      (:nombreCliente IS NULL OR LOWER(c.nombreCliente) LIKE LOWER(CONCAT('%', :nombreCliente, '%'))) " +
    "      OR (:nombreCliente IS NULL OR LOWER(ef.nombreFactura) LIKE LOWER(CONCAT('%', :nombreCliente, '%'))) " +
    "  )", nativeQuery = true)
  Object[] getResumenConsignaciones(
    @Param("fechaInicio") LocalDate fechaInicio,
    @Param("fechaFin") LocalDate fechaFin,
    @Param("nombreCliente") String nombreCliente,
    @Param("facturaProcesada") String facturaProcesada
  );

}
