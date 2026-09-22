package BackERP.controller;

import BackERP.models.GananciaReporteDTO;
import BackERP.models.GananciaResumenDTO;
import BackERP.repository.RepositoryDetalleFacturas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("reporteGanancias")
public class ReporteGananciasRestController {

  @Autowired
  private RepositoryDetalleFacturas repdetfac;

  // ==================== ENDPOINTS ====================

  /**
   * Reporte de ganancias agrupado por día, semana, mes o año.
   *
   * @param fechaInicio Fecha inicial del período (formato ISO: yyyy-MM-dd)
   * @param fechaFin    Fecha final del período (formato ISO: yyyy-MM-dd)
   * @param agrupacion  Tipo de agrupación: "dia", "semana", "mes", "anio" (default: "dia")
   * @return GananciaResumenDTO con totales y detalle por período
   */
  @GetMapping
  public GananciaResumenDTO getReporteGanancias(
    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
    @RequestParam(defaultValue = "dia") String agrupacion) {

    // Validar fechas
    if (fechaInicio.isAfter(fechaFin)) {
      throw new RuntimeException("La fecha de inicio no puede ser posterior a la fecha fin");
    }

    String agr = agrupacion.trim().toLowerCase();
    List<Object[]> rows;

    switch (agr) {
      case "semana":
      case "semanal":
        rows = repdetfac.getGananciasPorSemana(fechaInicio, fechaFin);
        agr = "semana";
        break;
      case "mes":
      case "mensual":
        rows = repdetfac.getGananciasPorMes(fechaInicio, fechaFin);
        agr = "mes";
        break;
      case "anio":
      case "año":
      case "anual":
        rows = repdetfac.getGananciasPorAnio(fechaInicio, fechaFin);
        agr = "anio";
        break;
      case "dia":
      case "diario":
      default:
        rows = repdetfac.getGananciasPorDia(fechaInicio, fechaFin);
        agr = "dia";
        break;
    }

    // Construir el detalle
    List<GananciaReporteDTO> detalle = new ArrayList<>();
    for (Object[] row : rows) {
      String periodo       = row[0] != null ? row[0].toString() : "";
      Double totalVenta    = row[1] != null ? ((Number) row[1]).doubleValue() : 0.0;
      Double totalCompra   = row[2] != null ? ((Number) row[2]).doubleValue() : 0.0;
      Double totalGanancia = row[3] != null ? ((Number) row[3]).doubleValue() : 0.0;
      Long totalProductos  = row[4] != null ? ((Number) row[4]).longValue()   : 0L;
      Long totalFacturas   = row[5] != null ? ((Number) row[5]).longValue()   : 0L;

      detalle.add(new GananciaReporteDTO(
        periodo,
        fechaInicio,
        fechaFin,
        totalVenta,
        totalCompra,
        totalGanancia,
        totalProductos,
        totalFacturas
      ));
    }

    // Obtener totales generales
    List<Object[]> totalesList = repdetfac.getTotalesGanancias(fechaInicio, fechaFin);

    Double granTotalVenta    = 0.0;
    Double granTotalCompra   = 0.0;
    Double granTotalGanancia = 0.0;
    Long granTotalProductos  = 0L;
    Long granTotalFacturas   = 0L;

    if (!totalesList.isEmpty()) {
      Object[] totales = totalesList.get(0);
      granTotalVenta    = totales[0] != null ? ((Number) totales[0]).doubleValue() : 0.0;
      granTotalCompra   = totales[1] != null ? ((Number) totales[1]).doubleValue() : 0.0;
      granTotalGanancia = totales[2] != null ? ((Number) totales[2]).doubleValue() : 0.0;
      granTotalProductos = totales[3] != null ? ((Number) totales[3]).longValue()   : 0L;
      granTotalFacturas  = totales[4] != null ? ((Number) totales[4]).longValue()   : 0L;
    }

    // Calcular margen de ganancia (%)
    Double margen = granTotalVenta > 0
      ? (granTotalGanancia / granTotalVenta) * 100.0
      : 0.0;

    // Construir respuesta
    GananciaResumenDTO resumen = new GananciaResumenDTO();
    resumen.setFechaInicio(fechaInicio);
    resumen.setFechaFin(fechaFin);
    resumen.setAgrupacion(agr);
    resumen.setTotalPrecioVenta(granTotalVenta);
    resumen.setTotalPrecioCompra(granTotalCompra);
    resumen.setTotalGanancia(granTotalGanancia);
    resumen.setMargenGanancia(margen);
    resumen.setTotalProductos(granTotalProductos);
    resumen.setTotalFacturas(granTotalFacturas);
    resumen.setDetalle(detalle);

    return resumen;
  }

  /**
   * Reporte de ganancias por producto en un rango de fechas.
   */
  @GetMapping("porProducto")
  public List<Map<String, Object>> getReporteGananciasPorProducto(
    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

    if (fechaInicio.isAfter(fechaFin)) {
      throw new RuntimeException("La fecha de inicio no puede ser posterior a la fecha fin");
    }

    List<Object[]> rows = repdetfac.getGananciasPorProducto(fechaInicio, fechaFin);
    List<Map<String, Object>> resultado = new ArrayList<>();

    for (Object[] row : rows) {
      Map<String, Object> item = new LinkedHashMap<>();
      item.put("idProducto",        row[0] != null ? ((Number) row[0]).longValue()   : null);
      item.put("nombreProducto",    row[1] != null ? row[1].toString()               : "N/A");
      item.put("totalProductos",    row[2] != null ? ((Number) row[2]).longValue()   : 0L);
      item.put("totalPrecioVenta",  row[3] != null ? ((Number) row[3]).doubleValue() : 0.0);
      item.put("totalPrecioCompra", row[4] != null ? ((Number) row[4]).doubleValue() : 0.0);
      item.put("totalGanancia",     row[5] != null ? ((Number) row[5]).doubleValue() : 0.0);
      resultado.add(item);
    }
    return resultado;
  }

  /**
   * Reporte rápido de ganancias de un día específico.
   */
  @GetMapping("dia")
  public GananciaResumenDTO getReporteGananciasDia(
    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
    return getReporteGanancias(fecha, fecha, "dia");
  }

  /**
   * Reporte rápido de ganancias de un mes específico.
   * Recibe el año y el mes (1-12).
   */
  @GetMapping("mes")
  public GananciaResumenDTO getReporteGananciasMes(
    @RequestParam int anio,
    @RequestParam int mes) {

    if (mes < 1 || mes > 12) {
      throw new RuntimeException("El mes debe estar entre 1 y 12");
    }

    LocalDate fechaInicio = LocalDate.of(anio, mes, 1);
    LocalDate fechaFin    = fechaInicio.withDayOfMonth(fechaInicio.lengthOfMonth());

    return getReporteGanancias(fechaInicio, fechaFin, "dia");
  }

  /**
   * Reporte rápido de ganancias de un año específico.
   */
  @GetMapping("anio")
  public GananciaResumenDTO getReporteGananciasAnio(@RequestParam int anio) {
    LocalDate fechaInicio = LocalDate.of(anio, 1, 1);
    LocalDate fechaFin    = LocalDate.of(anio, 12, 31);
    return getReporteGanancias(fechaInicio, fechaFin, "mes");
  }

  /**
   * Comparativa año contra año (mismo rango de fechas en años distintos).
   * Ej: fechaInicio=01-01, fechaFin=31-03, anioBase=2023, anioComparar=2024
   */
  @GetMapping("comparativaAnual")
  public Map<String, Object> getComparativaAnual(
    @RequestParam int anioBase,
    @RequestParam int anioComparar,
    @RequestParam(defaultValue = "01-01") String fechaInicio,
    @RequestParam(defaultValue = "12-31") String fechaFin) {

    String[] iniParts = fechaInicio.split("-");
    String[] finParts = fechaFin.split("-");

    LocalDate iniBase = LocalDate.of(anioBase, Integer.parseInt(iniParts[1]), Integer.parseInt(iniParts[0]));
    LocalDate finBase = LocalDate.of(anioBase, Integer.parseInt(finParts[1]), Integer.parseInt(finParts[0]));

    LocalDate iniComp = LocalDate.of(anioComparar, Integer.parseInt(iniParts[1]), Integer.parseInt(iniParts[0]));
    LocalDate finComp = LocalDate.of(anioComparar, Integer.parseInt(finParts[1]), Integer.parseInt(finParts[0]));

    GananciaResumenDTO resumenBase = getReporteGanancias(iniBase, finBase, "mes");
    GananciaResumenDTO resumenComp = getReporteGanancias(iniComp, finComp, "mes");

    Map<String, Object> resultado = new LinkedHashMap<>();
    resultado.put("anioBase", resumenBase);
    resultado.put("anioComparar", resumenComp);
    resultado.put("diferenciaGanancia",
      resumenComp.getTotalGanancia() - resumenBase.getTotalGanancia());
    resultado.put("variacionPorcentual",
      resumenBase.getTotalGanancia() > 0
        ? ((resumenComp.getTotalGanancia() - resumenBase.getTotalGanancia())
        / resumenBase.getTotalGanancia()) * 100.0
        : 0.0);

    return resultado;
  }
}
