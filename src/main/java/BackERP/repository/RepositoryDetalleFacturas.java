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
}
