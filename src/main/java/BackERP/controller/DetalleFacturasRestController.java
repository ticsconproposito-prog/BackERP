package BackERP.controller;

import BackERP.helper.erpDetalleFacturaSpecs;
import BackERP.models.felResumenDiarioDTO;
import BackERP.models.erpDetalleFacturas;
import BackERP.models.erpInventario;
import BackERP.repository.RepositoryDetalleFacturas;
import BackERP.repository.RepositoryInventario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
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
    @RequestParam(required = false) LocalDate fechaFin) {

    Specification<erpDetalleFacturas> spec = Specification
      .where(erpDetalleFacturaSpecs.idEncabezadoFacturaContains(idEncabezadoFactura))
      .and(erpDetalleFacturaSpecs.fechaBetween(fechaInicio, fechaFin));

    return repdetfac.findAll(spec);
  }

  @GetMapping("resumenDiario")
  public felResumenDiarioDTO getResumenDiario(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
    Object[] row = repdetfac.getResumenDiario(fecha);
    double totalVentas  = row[0] != null ? ((Number) row[0]).doubleValue() : 0.0;
    int totalProductos  = row[1] != null ? ((Number) row[1]).intValue()    : 0;
    return new felResumenDiarioDTO(fecha, totalVentas, totalProductos);
  }

  @PostMapping("grabarDetalleFactura")
  public String grabarDetalleFacturas(@RequestBody erpDetalleFacturas detalleFacturas){

    // 🔥 VALIDACIÓN: Evita división por cero y valores nulos

    System.out.println("descuento "+detalleFacturas.getCantidadDeDescuento() + "ImpBruto "+detalleFacturas.getImpBruto());
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

    // 🔹 Descontar inventario
    List<erpInventario> inventarios = repinv.findByIdProducto_IdProducto((long) detalleFacturas.getIdProducto());
    erpInventario inventario = inventarios.isEmpty() ? null : inventarios.get(0);

    if (inventario != null) {
      int nuevaCantidad = inventario.getCantidadExistencias() - (int) detalleFacturas.getCantidad();
      inventario.setCantidadExistencias(nuevaCantidad);
      inventario.setFechaModificacion(LocalDate.now());
      inventario.setHoraModificacion(LocalTime.now());
      repinv.save(inventario);
    }

    return "Grabado y actualizado inventario";
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
