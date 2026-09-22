package BackERP.models;

import java.time.LocalDate;
import java.util.List;

public class GananciaResumenDTO {

  private LocalDate fechaInicio;
  private LocalDate fechaFin;
  private String agrupacion;           // dia, semana, mes, anio
  private Double totalPrecioVenta;
  private Double totalPrecioCompra;
  private Double totalGanancia;
  private Double margenGanancia;       // porcentaje
  private Long totalProductos;
  private Long totalFacturas;
  private List<GananciaReporteDTO> detalle;

  public GananciaResumenDTO() {}

  // Getters y Setters
  public LocalDate getFechaInicio() { return fechaInicio; }
  public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

  public LocalDate getFechaFin() { return fechaFin; }
  public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

  public String getAgrupacion() { return agrupacion; }
  public void setAgrupacion(String agrupacion) { this.agrupacion = agrupacion; }

  public Double getTotalPrecioVenta() { return totalPrecioVenta; }
  public void setTotalPrecioVenta(Double totalPrecioVenta) { this.totalPrecioVenta = totalPrecioVenta; }

  public Double getTotalPrecioCompra() { return totalPrecioCompra; }
  public void setTotalPrecioCompra(Double totalPrecioCompra) { this.totalPrecioCompra = totalPrecioCompra; }

  public Double getTotalGanancia() { return totalGanancia; }
  public void setTotalGanancia(Double totalGanancia) { this.totalGanancia = totalGanancia; }

  public Double getMargenGanancia() { return margenGanancia; }
  public void setMargenGanancia(Double margenGanancia) { this.margenGanancia = margenGanancia; }

  public Long getTotalProductos() { return totalProductos; }
  public void setTotalProductos(Long totalProductos) { this.totalProductos = totalProductos; }

  public Long getTotalFacturas() { return totalFacturas; }
  public void setTotalFacturas(Long totalFacturas) { this.totalFacturas = totalFacturas; }

  public List<GananciaReporteDTO> getDetalle() { return detalle; }
  public void setDetalle(List<GananciaReporteDTO> detalle) { this.detalle = detalle; }
}
