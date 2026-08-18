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


    // RepositoryConsignacionPagos.java
    @Query(value = "SELECT " +
            "COUNT(*) as totalConsignaciones, " +
            "SUM(ef.totalBruto) as montoTotalConsignaciones, " +
            "COALESCE( " +
            "    ( " +
            "        SELECT SUM(montoPago) " +
            "        FROM erpConsignacionPagos " +
            "        WHERE IdEncabezadofactura IN ( " +
            "            SELECT ef2.IdEncabezadofactura " +
            "            FROM erpEncabezadoFacturas ef2 " +
            "            INNER JOIN erpClientes c2 ON ef2.idCliente = c2.idCliente " +
            "            WHERE ef2.tipoDocumento = 4 " +
            "            AND ef2.FechaFactura BETWEEN :fechaInicio AND :fechaFin " +
            "            AND ( " +
            "                (:nombreCliente IS NULL OR :nombreCliente = '') OR " +
            "                LOWER(c2.nombreCliente) LIKE LOWER(CONCAT('%', :nombreCliente, '%')) OR " +
            "                LOWER(ef2.nombreFactura) LIKE LOWER(CONCAT('%', :nombreCliente, '%')) " +
            "            ) " +
            "            AND ( " +
            "                (:facturaProcesada IS NULL OR :facturaProcesada = '') OR " +
            "                (:facturaProcesada = 'S' AND ef2.facturaProcesada = 'S') OR " +
            "                (:facturaProcesada != 'S' AND ef2.facturaProcesada != 'S') " +
            "            ) " +
            "        ) " +
            "    ), 0 " +
            ") as totalPagado " +
            "FROM erpEncabezadoFacturas ef " +
            "INNER JOIN erpClientes c ON ef.idCliente = c.idCliente " +
            "WHERE ef.tipoDocumento = 4 " +
            "AND ef.FechaFactura BETWEEN :fechaInicio AND :fechaFin " +
            "AND ( " +
            "    (:nombreCliente IS NULL OR :nombreCliente = '') OR " +
            "    LOWER(c.nombreCliente) LIKE LOWER(CONCAT('%', :nombreCliente, '%')) OR " +
            "    LOWER(ef.nombreFactura) LIKE LOWER(CONCAT('%', :nombreCliente, '%')) " +
            ") " +
            "AND ( " +
            "    (:facturaProcesada IS NULL OR :facturaProcesada = '') OR " +
            "    (:facturaProcesada = 'S' AND ef.facturaProcesada = 'S') OR " +
            "    (:facturaProcesada != 'S' AND ef.facturaProcesada != 'S') " +
            ")",
            nativeQuery = true)
    List<Object[]> getResumenConsignaciones(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin,
            @Param("nombreCliente") String nombreCliente,
            @Param("facturaProcesada") String facturaProcesada
    );
}
