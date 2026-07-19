package BackERP.dto;

import java.time.LocalDate;

public class ConsignacionPagoDTO {
  private Long idConsignacionPago;
  private int idEncabezadoFactura;
  private double montoPago;
  private LocalDate fechaPago;
  private String estado;
  private String observaciones;
  private int idUsuarioModificacion;

  // Getters y Setters
  public Long getIdConsignacionPago() {
    return idConsignacionPago;
  }

  public void setIdConsignacionPago(Long idConsignacionPago) {
    this.idConsignacionPago = idConsignacionPago;
  }

  public int getIdEncabezadoFactura() {
    return idEncabezadoFactura;
  }

  public void setIdEncabezadoFactura(int idEncabezadoFactura) {
    this.idEncabezadoFactura = idEncabezadoFactura;
  }

  public double getMontoPago() {
    return montoPago;
  }

  public void setMontoPago(double montoPago) {
    this.montoPago = montoPago;
  }

  public LocalDate getFechaPago() {
    return fechaPago;
  }

  public void setFechaPago(LocalDate fechaPago) {
    this.fechaPago = fechaPago;
  }

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public int getIdUsuarioModificacion() {
    return idUsuarioModificacion;
  }

  public void setIdUsuarioModificacion(int idUsuarioModificacion) {
    this.idUsuarioModificacion = idUsuarioModificacion;
  }
}
