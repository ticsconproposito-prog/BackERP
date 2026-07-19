package BackERP.repository;

import BackERP.models.erpEncabezadoFacturas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

// Agregar esto a RepositoryEncabezadoFacturas.java
public interface RepositoryEncabezadoFacturas extends JpaRepository<erpEncabezadoFacturas, Long>, JpaSpecificationExecutor<erpEncabezadoFacturas> {

  Optional<erpEncabezadoFacturas> findByNumeroAutorizacionResAPI(String numeroAutorizacionResAPI);
  Optional<erpEncabezadoFacturas> findBySerieResAPIAndPreimpresoResAPI(String serieResAPI, long preimpresoResAPI);

  Optional<erpEncabezadoFacturas> findByIdEncabezadoFacturaAndEstado(Long idEncabezadoFactura, int estado);

  // RepositoryEncabezadoFacturas.java
  @Query("SELECT COUNT(e), SUM(e.total) FROM erpEncabezadoFacturas e " +
    "WHERE e.estado = 1 " +
    "AND e.facturaProcesada = 'S' " +
    "AND e.FechaFactura BETWEEN :fechaInicio AND :fechaFin " +
    "AND (:nombreCliente IS NULL OR LOWER(e.idCliente.nombreCliente) LIKE LOWER(CONCAT('%', :nombreCliente, '%'))) " +
    "AND (:tipoDocumento IS NULL OR e.tipoDocumento = :tipoDocumento)")
  Object[] getResumenFacturas(
    @Param("fechaInicio") LocalDate fechaInicio,
    @Param("fechaFin") LocalDate fechaFin,
    @Param("nombreCliente") String nombreCliente,
    @Param("tipoDocumento") Integer tipoDocumento
  );
}
