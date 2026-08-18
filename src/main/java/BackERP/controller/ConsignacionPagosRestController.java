package BackERP.controller;

import BackERP.helper.erpConsignacionPagosSpecs;
import BackERP.models.erpConsignacionPagos;
import BackERP.repository.RepositoryConsignacionPagos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/consignacionPagos")
public class ConsignacionPagosRestController {

  @Autowired
  private RepositoryConsignacionPagos repositoryConsignacionPagos;

  // GET - Obtener todos los pagos con filtros
  @GetMapping
  public List<erpConsignacionPagos> getConsignacionPagos(
          @RequestParam(required = false) Integer idEncabezadoFactura,
          @RequestParam(required = false) Integer estado,  // Cambiado de String a Integer
          @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
          @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

      Specification<erpConsignacionPagos> spec = Specification
              .where(erpConsignacionPagosSpecs.idEncabezadoFacturaEquals(idEncabezadoFactura))
              .and(erpConsignacionPagosSpecs.estadoEquals(estado))
              .and(erpConsignacionPagosSpecs.fechaPagoBetween(fechaInicio, fechaFin));

      return repositoryConsignacionPagos.findAll(spec);
  }

    // ConsignacionPagosRestController.java
    @GetMapping("/resumenConsignaciones")
    public ResponseEntity<Map<String, Object>> getResumenConsignacionesPagos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(required = false) String nombreCliente,
            @RequestParam(required = false) String facturaProcesada) {

        // Validar fechas
        if (fechaInicio == null || fechaFin == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Las fechas son requeridas");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        if (fechaInicio.isAfter(fechaFin)) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "La fecha de inicio no puede ser posterior a la fecha de fin");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        // Limpiar nombreCliente si viene vacío
        if (nombreCliente != null && nombreCliente.trim().isEmpty()) {
            nombreCliente = null;
        }

        // Limpiar facturaProcesada si viene vacío
        if (facturaProcesada != null && facturaProcesada.trim().isEmpty()) {
            facturaProcesada = null;
        }

        // Validar que facturaProcesada tenga un valor válido si se proporciona
        if (facturaProcesada != null && !facturaProcesada.matches("[SN]")) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "El valor de facturaProcesada debe ser 'S' (procesada) o 'N' (no procesada)");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        try {
            List<Object[]> resultado = repositoryConsignacionPagos.getResumenConsignaciones(
                    fechaInicio, fechaFin, nombreCliente, facturaProcesada);

            long totalConsignaciones = 0L;
            double montoTotalConsignaciones = 0.0;
            double totalPagado = 0.0;

            if (resultado != null && !resultado.isEmpty()) {
                Object[] fila = resultado.get(0);
                if (fila != null && fila.length >= 3) {
                    totalConsignaciones = fila[0] != null ? ((Number) fila[0]).longValue() : 0L;
                    montoTotalConsignaciones = fila[1] != null ? ((Number) fila[1]).doubleValue() : 0.0;
                    totalPagado = fila[2] != null ? ((Number) fila[2]).doubleValue() : 0.0;
                }
            }

            double saldoPendiente = montoTotalConsignaciones - totalPagado;

            double porcentajePagado = 0.0;
            double porcentajePendiente = 0.0;

            if (montoTotalConsignaciones > 0) {
                porcentajePagado = (totalPagado / montoTotalConsignaciones) * 100;
                porcentajePendiente = (saldoPendiente / montoTotalConsignaciones) * 100;
            }

            Map<String, Object> response = new HashMap<>();
            response.put("totalConsignaciones", totalConsignaciones);
            response.put("montoTotal", Math.round(montoTotalConsignaciones * 100.0) / 100.0);
            response.put("totalPagado", Math.round(totalPagado * 100.0) / 100.0);
            response.put("saldoPendiente", Math.round(saldoPendiente * 100.0) / 100.0);
            response.put("moneda", "GTQ");
            response.put("porcentajePagado", Math.round(porcentajePagado * 100.0) / 100.0);
            response.put("porcentajePendiente", Math.round(porcentajePendiente * 100.0) / 100.0);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error al obtener el resumen de consignaciones: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

  // GET - Obtener pagos por ID de factura
  @GetMapping("/factura/{idEncabezadoFactura}")
  public List<erpConsignacionPagos> getPagosByFactura(@PathVariable int idEncabezadoFactura) {
    return repositoryConsignacionPagos.findByIdEncabezadoFactura(idEncabezadoFactura);
  }

  // GET - Obtener pago por ID
  @GetMapping("/{idConsignacionPago}")
  public Optional<erpConsignacionPagos> getConsignacionPagoById(@PathVariable Long idConsignacionPago) {
    return repositoryConsignacionPagos.findById(idConsignacionPago);
  }

  // GET - Obtener total de pagos por factura
  @GetMapping("/totalFactura/{idEncabezadoFactura}")
  public Double getTotalPagosByFactura(@PathVariable int idEncabezadoFactura) {
    Double total = repositoryConsignacionPagos.sumMontoPagosByFactura(idEncabezadoFactura);
    return total != null ? total : 0.0;
  }

  // POST - Crear nuevo pago
  @PostMapping
  public int  grabarConsignacionPago(@RequestBody erpConsignacionPagos consignacionPago) {
    consignacionPago.setFechaModificacion(LocalDate.now());
    consignacionPago.setHoraModificacion(LocalTime.now());

    consignacionPago.setEstado(1);
    erpConsignacionPagos saved = repositoryConsignacionPagos.save(consignacionPago);
    return  saved.getIdConsignacionPago();
  }

  // PUT - Actualizar pago existente
  @PutMapping("/{idConsignacionPago}")
  public String editarConsignacionPago(@PathVariable Long idConsignacionPago,
                                       @RequestBody erpConsignacionPagos consignacionPago) {
    Optional<erpConsignacionPagos> optionalPago = repositoryConsignacionPagos.findById(idConsignacionPago);

    if (optionalPago.isPresent()) {
      erpConsignacionPagos updatePago = optionalPago.get();
      updatePago.setIdEncabezadoFactura(consignacionPago.getIdEncabezadoFactura());
      updatePago.setMontoPago(consignacionPago.getMontoPago());
      updatePago.setFechaPago(consignacionPago.getFechaPago());
      updatePago.setEstado(consignacionPago.getEstado());
      updatePago.setObservaciones(consignacionPago.getObservaciones());
      updatePago.setFechaModificacion(LocalDate.now());
      updatePago.setHoraModificacion(LocalTime.now());
      updatePago.setIdUsuarioModificacion(consignacionPago.getIdUsuarioModificacion());

      repositoryConsignacionPagos.save(updatePago);
      return "Pago actualizado correctamente";
    } else {
      return "Pago no encontrado";
    }
  }

  // PATCH - Actualizar estado del pago (específico)
  @PatchMapping("/{idConsignacionPago}/estado")
  public String actualizarEstadoPago(@PathVariable Long idConsignacionPago,
                                     @RequestParam int estado,
                                     @RequestParam int idUsuarioModificacion) {
    Optional<erpConsignacionPagos> optionalPago = repositoryConsignacionPagos.findById(idConsignacionPago);

    if (optionalPago.isPresent()) {
      erpConsignacionPagos updatePago = optionalPago.get();
      updatePago.setEstado(estado);
      updatePago.setFechaModificacion(LocalDate.now());
      updatePago.setHoraModificacion(LocalTime.now());
      updatePago.setIdUsuarioModificacion(idUsuarioModificacion);

      repositoryConsignacionPagos.save(updatePago);
      return "Estado actualizado a: " + estado;
    } else {
      return "Pago no encontrado";
    }
  }

  // DELETE - Eliminar pago (borrado lógico)
  @DeleteMapping("/{idConsignacionPago}")
  public String eliminarConsignacionPago(@PathVariable Long idConsignacionPago,
                                         @RequestParam int idUsuarioModificacion) {
    Optional<erpConsignacionPagos> optionalPago = repositoryConsignacionPagos.findById(idConsignacionPago);

    if (optionalPago.isPresent()) {
      erpConsignacionPagos updatePago = optionalPago.get();
      updatePago.setEstado(3);
      updatePago.setFechaModificacion(LocalDate.now());
      updatePago.setHoraModificacion(LocalTime.now());
      updatePago.setIdUsuarioModificacion(idUsuarioModificacion);

      repositoryConsignacionPagos.save(updatePago);
      return "Pago anulado correctamente";
    } else {
      return "Pago no encontrado";
    }
  }

  // DELETE - Eliminar todos los pagos de una factura
  @DeleteMapping("/factura/{idEncabezadoFactura}")
  public String eliminarPagosByFactura(@PathVariable int idEncabezadoFactura,
                                       @RequestParam int idUsuarioModificacion) {
    List<erpConsignacionPagos> pagos = repositoryConsignacionPagos.findByIdEncabezadoFactura(idEncabezadoFactura);

    if (!pagos.isEmpty()) {
      pagos.forEach(pago -> {
        pago.setEstado(0);
        pago.setFechaModificacion(LocalDate.now());
        pago.setHoraModificacion(LocalTime.now());
        pago.setIdUsuarioModificacion(idUsuarioModificacion);
        repositoryConsignacionPagos.save(pago);
      });
      return "Todos los pagos de la factura han sido anulados";
    } else {
      return "No se encontraron pagos para esta factura";
    }
  }

  // DELETE - Eliminar físicamente (peligroso, usar con cuidado)
  @DeleteMapping("/hardDelete/{idConsignacionPago}")
  public String eliminarConsignacionPagoFisico(@PathVariable Long idConsignacionPago) {
    if (repositoryConsignacionPagos.existsById(idConsignacionPago)) {
      repositoryConsignacionPagos.deleteById(idConsignacionPago);
      return "Pago eliminado físicamente de la base de datos";
    } else {
      return "Pago no encontrado";
    }
  }
}
