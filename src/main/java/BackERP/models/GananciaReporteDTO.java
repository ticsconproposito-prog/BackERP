package BackERP.models;

import java.time.LocalDate;

public class GananciaReporteDTO {

  private String periodo;          // Ej: "2024-01", "2024-W05", "2024-01-15", "2024"
  private LocalDate fechaInicio;
  private LocalDate fechaFin;
  private Double totalPrecioVenta;
  private Double totalPrecioCompra;
  private Double totalGanancia;
  private Long totalProductos;
  private Long totalFacturas;

  public GananciaReporteDTO() {}

  public GananciaReporteDTO(String periodo, LocalDate fechaInicio, LocalDate fechaFin,
                            Double totalPrecioVenta, Double totalPrecioCompra,
                            Double totalGanancia, Long totalProductos, Long totalFacturas) {
    this.periodo = periodo;
    this.fechaInicio = fechaInicio;
    this.fechaFin = fechaFin;
    this.totalPrecioVenta = totalPrecioVenta != null ? totalPrecioVenta : 0.0;
    this.totalPrecioCompra = totalPrecioCompra != null ? totalPrecioCompra : 0.0;
    this.totalGanancia = totalGanancia != null ? totalGanancia : 0.0;
    this.totalProductos = totalProductos != null ? totalProductos : 0L;
    this.totalFacturas = totalFacturas != null ? totalFacturas : 0L;
  }

  // Getters y Setters
  public String getPeriodo() { return periodo; }
  public void setPeriodo(String periodo) { this.periodo = periodo; }

  public LocalDate getFechaInicio() { return fechaInicio; }
  public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

  public LocalDate getFechaFin() { return fechaFin; }
  public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

  public Double getTotalPrecioVenta() { return totalPrecioVenta; }
  public void setTotalPrecioVenta(Double totalPrecioVenta) { this.totalPrecioVenta = totalPrecioVenta; }

  public Double getTotalPrecioCompra() { return totalPrecioCompra; }
  public void setTotalPrecioCompra(Double totalPrecioCompra) { this.totalPrecioCompra = totalPrecioCompra; }

  public Double getTotalGanancia() { return totalGanancia; }
  public void setTotalGanancia(Double totalGanancia) { this.totalGanancia = totalGanancia; }

  public Long getTotalProductos() { return totalProductos; }
  public void setTotalProductos(Long totalProductos) { this.totalProductos = totalProductos; }

  public Long getTotalFacturas() { return totalFacturas; }
  public void setTotalFacturas(Long totalFacturas) { this.totalFacturas = totalFacturas; }
}
