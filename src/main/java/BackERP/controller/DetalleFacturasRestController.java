package BackERP.controller;

import BackERP.helper.erpDetalleFacturaSpecs;
import BackERP.models.felResumenDiarioDTO;
import BackERP.models.erpDetalleFacturas;
import BackERP.models.erpInventario;
import BackERP.repository.RepositoryDetalleFacturas;
import BackERP.repository.RepositoryInventario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

//@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class DetalleFacturasRestController {

  @Autowired
  private RepositoryDetalleFacturas repdetfac;
  @Autowired
  private RepositoryInventario repinv;

  @GetMapping("detalleFactura")
  public List<erpDetalleFacturas> getDetalleFacturas(
    @RequestParam(required = false) Integer idEncabezadoFactura,
    @RequestParam(required = false) LocalDate fechaInicio,
    @RequestParam(required = false) LocalDate fechaFin,
    @RequestParam(defaultValue = "ordenDetalleFactura,asc") String sort) {

    Specification<erpDetalleFacturas> spec = Specification
      .where(erpDetalleFacturaSpecs.idEncabezadoFacturaContains(idEncabezadoFactura))
      .and(erpDetalleFacturaSpecs.fechaBetween(fechaInicio, fechaFin));

    // Procesar el parámetro sort y aplicarlo
    return repdetfac.findAll(spec, parseSort(sort));
  }

  // Método auxiliar para parsear el parámetro sort
  private Sort parseSort(String sort) {
    String[] sortParams = sort.split(",");
    String sortField = sortParams[0];
    Sort.Direction sortDirection = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc")
      ? Sort.Direction.DESC
      : Sort.Direction.ASC;
    return Sort.by(sortDirection, sortField);
  }

  @GetMapping("resumenDiario")
  public felResumenDiarioDTO getResumenDiario(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
    Object[] row = repdetfac.getResumenDiario(fecha);
    double totalVentas  = row[0] != null ? ((Number) row[0]).doubleValue() : 0.0;
    int totalProductos  = row[1] != null ? ((Number) row[1]).intValue()    : 0;
    return new felResumenDiarioDTO(fecha, totalVentas, totalProductos);
  }

  @PostMapping("grabarDetalleFactura")
  @Transactional
  public String grabarDetalleFacturas(
    @RequestBody erpDetalleFacturas detalleFacturas,
    @RequestParam(required = false, defaultValue = "S") String rebajarInventario) {

    // 🔥 VALIDACIÓN: Evita división por cero y valores nulos
    double cantidadDeDescuento = detalleFacturas.getCantidadDeDescuento() != 0
      ? detalleFacturas.getCantidadDeDescuento()
      : 0.0;

    double impBruto = detalleFacturas.getImpBruto() != 0
      ? detalleFacturas.getImpBruto()
      : 0.0;

    // Calcula porcentaje de descuento solo si hay descuento y el impuesto bruto no es cero
    if (cantidadDeDescuento != 0 && impBruto != 0) {
      double PorcDescuento = (cantidadDeDescuento / impBruto) * 100;

      // 🔥 VALIDACIÓN: Evita valores infinitos o NaN
      if (Double.isInfinite(PorcDescuento) || Double.isNaN(PorcDescuento)) {
        PorcDescuento = 0.0;
      }

      detalleFacturas.setPorcentajeDeDescuento(PorcDescuento);
    } else if (detalleFacturas.getPorcentajeDeDescuento() == 0 && impBruto == 0) {
      // Si no hay impuesto bruto, el porcentaje es 0
      detalleFacturas.setPorcentajeDeDescuento(0.0);
    }

    // 🔥 VALIDACIÓN: Asegura que todos los valores numéricos tengan un valor válido
    detalleFacturas.setCantidad(detalleFacturas.getCantidad());
    detalleFacturas.setPrecioVenta(safeDouble(detalleFacturas.getPrecioVenta()));
    detalleFacturas.setCantidadDeDescuento(safeDouble(detalleFacturas.getCantidadDeDescuento()));
    detalleFacturas.setImpBruto(safeDouble(detalleFacturas.getImpBruto()));
    detalleFacturas.setImpNeto(safeDouble(detalleFacturas.getImpNeto()));
    detalleFacturas.setIva(safeDouble(detalleFacturas.getIva()));
    detalleFacturas.setImpTotal(safeDouble(detalleFacturas.getImpTotal()));

    // Asegura que porcentaje de descuento sea válido
    detalleFacturas.setPorcentajeDeDescuento(safeDouble(detalleFacturas.getPorcentajeDeDescuento()));

    detalleFacturas.setFechaModificacion(LocalDate.now());
    detalleFacturas.setHoraModificacion(LocalTime.now());
    detalleFacturas.setEstado(1);

    repdetfac.save(detalleFacturas);

    // 🔹 Descontar inventario SOLO si la bandera lo permite
    // Si rebajarInventario es "N" (mayúscula o minúscula), NO se rebaja
    // En cualquier otro caso (S, vacío, null, etc.) se rebaja
    if (!"N".equalsIgnoreCase(rebajarInventario)) {
      List<erpInventario> inventarios = repinv.findByIdProducto_IdProducto((long) detalleFacturas.getIdProducto());
      erpInventario inventario = inventarios.isEmpty() ? null : inventarios.get(0);

      if (inventario != null) {
        int nuevaCantidad = inventario.getCantidadExistencias() - (int) detalleFacturas.getCantidad();
        inventario.setCantidadExistencias(nuevaCantidad);
        inventario.setFechaModificacion(LocalDate.now());
        inventario.setHoraModificacion(LocalTime.now());
        repinv.save(inventario);
      }
    }

    return "Grabado" + ("N".equalsIgnoreCase(rebajarInventario) ? " sin rebajar inventario" : " y actualizado inventario");
  }

  // 🔥 MÉTODO AUXILIAR: Convierte valores inválidos a 0.0
  private double safeDouble(Double value) {
    if (value == null || Double.isInfinite(value) || Double.isNaN(value)) {
      return 0.0;
    }
    return value;
  }

  private double safeDouble(double value) {
    if (Double.isInfinite(value) || Double.isNaN(value)) {
      return 0.0;
    }
    return value;
  }

  @PatchMapping("actualizarConsignacionFacturada/{idDetalleFactura}")
  @Transactional
  public String actualizarConsignacionFacturada(
    @PathVariable Long idDetalleFactura,
    @RequestBody erpDetalleFacturas detalleFacturasActualizado) {

    // Buscar el detalle existente
    erpDetalleFacturas detalleExistente = repdetfac.findById(idDetalleFactura)
      .orElseThrow(() -> new RuntimeException("Detalle de factura no encontrado con ID: " + idDetalleFactura));

    // Verificar que el detalle está activo
    if (detalleExistente.getEstado() == 0) {
      throw new RuntimeException("No se puede actualizar un detalle de factura anulado");
    }

    // ACTUALIZAR SOLO EL CAMPO consignacionFacturada
    detalleExistente.setConsignacionFacturada(detalleFacturasActualizado.getConsignacionFacturada());

    // Actualizar fechas y usuario de modificación
    detalleExistente.setFechaModificacion(LocalDate.now());
    detalleExistente.setHoraModificacion(LocalTime.now());

    // Si el usuario de modificación viene en el body, actualizarlo
    if (detalleFacturasActualizado.getIdUsuarioModificacion() != 0) {
      detalleExistente.setIdUsuarioModificacion(detalleFacturasActualizado.getIdUsuarioModificacion());
    }

    // Guardar cambios
    repdetfac.save(detalleExistente);

    return "Campo consignacionFacturada actualizado exitosamente a: " +
      detalleExistente.getConsignacionFacturada();
  }

  @DeleteMapping("eliminarDetalleFactura/{idDetalleFactura}")
  public String eliminarDetalleFactura(@PathVariable long idDetalleFactura, @RequestBody erpDetalleFacturas  DetalleFacturas){

    erpDetalleFacturas updateerpDetalleFacturas = repdetfac.findById(idDetalleFactura).get();
    updateerpDetalleFacturas.setFechaModificacion(LocalDate.now());
    updateerpDetalleFacturas.setHoraModificacion(LocalTime.now());
    updateerpDetalleFacturas.setIdUsuarioModificacion(DetalleFacturas.getIdUsuarioModificacion());
    updateerpDetalleFacturas.setEstado(0);
    repdetfac.save(updateerpDetalleFacturas);
    return "Eliminado";
  }
}
