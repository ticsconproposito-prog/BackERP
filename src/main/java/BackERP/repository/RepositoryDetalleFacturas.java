package BackERP.repository;

import BackERP.models.erpDetalleFacturas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface RepositoryDetalleFacturas extends JpaRepository<erpDetalleFacturas, Long>, JpaSpecificationExecutor<erpDetalleFacturas> {

  // Buscar detalles activos por ID de encabezado
  List<erpDetalleFacturas> findByIdEncabezadoFacturaAndEstado(int idEncabezadoFactura, int estado);

  // Resumen diario: total de ventas y cantidad de productos en una fecha
  @Query("SELECT SUM(d.ImpTotal), SUM(d.cantidad) FROM erpDetalleFacturas d WHERE d.fechaModificacion = :fecha")
  Object[] getResumenDiario(@Param("fecha") LocalDate fecha);

  // Actualización lógica masiva de detalles
  @Modifying
  @Transactional
  @Query("UPDATE erpDetalleFacturas d SET d.estado = 0, d.fechaModificacion = CURRENT_DATE, " +
    "d.horaModificacion = CURRENT_TIME, d.idUsuarioModificacion = :idUsuario WHERE d.idEncabezadoFactura = :idEncabezado")
  void anularDetallesPorEncabezado(@Param("idEncabezado") int idEncabezadoFactura,
                                   @Param("idUsuario") int idUsuarioModificacion);

  // ==================== REPORTE DE GANANCIAS ====================

  // Reporte agrupado por DÍA
  @Query(value = "SELECT CAST(d.fechaModificacion AS DATE) AS periodo, " +
    "SUM(d.ImpTotal) AS totalVenta, " +
    "SUM(d.precioCompra * d.cantidad) AS totalCompra, " +
    "SUM(d.ImpTotal - (d.precioCompra * d.cantidad)) AS totalGanancia, " +
    "SUM(d.cantidad) AS totalProductos, " +
    "COUNT(DISTINCT d.idEncabezadoFactura) AS totalFacturas " +
    "FROM erpDetalleFacturas d " +
    "WHERE d.estado = 1 AND d.fechaModificacion BETWEEN :fechaInicio AND :fechaFin " +
    "GROUP BY CAST(d.fechaModificacion AS DATE) " +
    "ORDER BY CAST(d.fechaModificacion AS DATE) ASC", nativeQuery = true)
  List<Object[]> getGananciasPorDia(@Param("fechaInicio") LocalDate fechaInicio,
                                    @Param("fechaFin") LocalDate fechaFin);

  // Reporte agrupado por SEMANA (ISO week)
  @Query(value = "SELECT CONCAT(YEAR(d.fechaModificacion), '-W', LPAD(WEEK(d.fechaModificacion, 3), 2, '0')) AS periodo, " +
    "SUM(d.ImpTotal) AS totalVenta, " +
    "SUM(d.precioCompra * d.cantidad) AS totalCompra, " +
    "SUM(d.ImpTotal - (d.precioCompra * d.cantidad)) AS totalGanancia, " +
    "SUM(d.cantidad) AS totalProductos, " +
    "COUNT(DISTINCT d.idEncabezadoFactura) AS totalFacturas " +
    "FROM erpDetalleFacturas d " +
    "WHERE d.estado = 1 AND d.fechaModificacion BETWEEN :fechaInicio AND :fechaFin " +
    "GROUP BY YEAR(d.fechaModificacion), WEEK(d.fechaModificacion, 3) " +
    "ORDER BY YEAR(d.fechaModificacion) ASC, WEEK(d.fechaModificacion, 3) ASC", nativeQuery = true)
  List<Object[]> getGananciasPorSemana(@Param("fechaInicio") LocalDate fechaInicio,
                                       @Param("fechaFin") LocalDate fechaFin);

  // Reporte agrupado por MES
  @Query(value = "SELECT DATE_FORMAT(d.fechaModificacion, '%Y-%m') AS periodo, " +
    "SUM(d.ImpTotal) AS totalVenta, " +
    "SUM(d.precioCompra * d.cantidad) AS totalCompra, " +
    "SUM(d.ImpTotal - (d.precioCompra * d.cantidad)) AS totalGanancia, " +
    "SUM(d.cantidad) AS totalProductos, " +
    "COUNT(DISTINCT d.idEncabezadoFactura) AS totalFacturas " +
    "FROM erpDetalleFacturas d " +
    "WHERE d.estado = 1 AND d.fechaModificacion BETWEEN :fechaInicio AND :fechaFin " +
    "GROUP BY DATE_FORMAT(d.fechaModificacion, '%Y-%m') " +
    "ORDER BY DATE_FORMAT(d.fechaModificacion, '%Y-%m') ASC", nativeQuery = true)
  List<Object[]> getGananciasPorMes(@Param("fechaInicio") LocalDate fechaInicio,
                                    @Param("fechaFin") LocalDate fechaFin);

  // Reporte agrupado por AÑO
  @Query(value = "SELECT CAST(YEAR(d.fechaModificacion) AS CHAR) AS periodo, " +
    "SUM(d.ImpTotal) AS totalVenta, " +
    "SUM(d.precioCompra * d.cantidad) AS totalCompra, " +
    "SUM(d.ImpTotal - (d.precioCompra * d.cantidad)) AS totalGanancia, " +
    "SUM(d.cantidad) AS totalProductos, " +
    "COUNT(DISTINCT d.idEncabezadoFactura) AS totalFacturas " +
    "FROM erpDetalleFacturas d " +
    "WHERE d.estado = 1 AND d.fechaModificacion BETWEEN :fechaInicio AND :fechaFin " +
    "GROUP BY YEAR(d.fechaModificacion) " +
    "ORDER BY YEAR(d.fechaModificacion) ASC", nativeQuery = true)
  List<Object[]> getGananciasPorAnio(@Param("fechaInicio") LocalDate fechaInicio,
                                     @Param("fechaFin") LocalDate fechaFin);

  // Totales generales del período (sin agrupación)
  @Query(value = "SELECT " +
    "COALESCE(SUM(d.ImpTotal), 0) AS totalVenta, " +
    "COALESCE(SUM(d.precioCompra * d.cantidad), 0) AS totalCompra, " +
    "COALESCE(SUM(d.ImpTotal - (d.precioCompra * d.cantidad)), 0) AS totalGanancia, " +
    "COALESCE(SUM(d.cantidad), 0) AS totalProductos, " +
    "COUNT(DISTINCT d.idEncabezadoFactura) AS totalFacturas " +
    "FROM erpDetalleFacturas d " +
    "WHERE d.estado = 1 AND d.fechaModificacion BETWEEN :fechaInicio AND :fechaFin", nativeQuery = true)
  List<Object[]> getTotalesGanancias(@Param("fechaInicio") LocalDate fechaInicio,
                                     @Param("fechaFin") LocalDate fechaFin);

  // Reporte de ganancias por PRODUCTO
  @Query(value = "SELECT d.idProducto, p.nombreProducto, " +
    "SUM(d.cantidad) AS totalProductos, " +
    "SUM(d.ImpTotal) AS totalVenta, " +
    "SUM(d.precioCompra * d.cantidad) AS totalCompra, " +
    "SUM(d.ImpTotal - (d.precioCompra * d.cantidad)) AS totalGanancia " +
    "FROM erpDetalleFacturas d " +
    "LEFT JOIN erpProductos p ON d.idProducto = p.idProducto " +
    "WHERE d.estado = 1 AND d.fechaModificacion BETWEEN :fechaInicio AND :fechaFin " +
    "GROUP BY d.idProducto, p.nombreProducto " +
    "ORDER BY totalGanancia DESC", nativeQuery = true)
  List<Object[]> getGananciasPorProducto(@Param("fechaInicio") LocalDate fechaInicio,
                                         @Param("fechaFin") LocalDate fechaFin);
}
