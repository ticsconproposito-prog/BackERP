package BackERP.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "erpConsignacionPagos", schema = "erpConfig")
public class erpConsignacionPagos {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "idConsignacionPago")
  private Long idConsignacionPago;

  @Column(name = "idEncabezadoFactura", nullable = false)
  private int idEncabezadoFactura;

  @Column(name = "montoPago", nullable = false)
  private double montoPago;

  @Column(name = "fechaPago", nullable = false)
  private LocalDate fechaPago;

  @Column(name = "estado", columnDefinition = "varchar(20) default 'Pendiente'")
  private String estado;

  @Column(name = "observaciones", length = 500)
  private String observaciones;

  @Column(name = "fechaModificacion")
  private LocalDate fechaModificacion;

  @Column(name = "horaModificacion")
  private LocalTime horaModificacion;

  @Column(name = "idUsuarioModificacion")
  private int idUsuarioModificacion;

  // Constructor vacío
  public erpConsignacionPagos() {}

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

  public LocalDate getFechaModificacion() {
    return fechaModificacion;
  }

  public void setFechaModificacion(LocalDate fechaModificacion) {
    this.fechaModificacion = fechaModificacion;
  }

  public LocalTime getHoraModificacion() {
    return horaModificacion;
  }

  public void setHoraModificacion(LocalTime horaModificacion) {
    this.horaModificacion = horaModificacion;
  }

  public int getIdUsuarioModificacion() {
    return idUsuarioModificacion;
  }

  public void setIdUsuarioModificacion(int idUsuarioModificacion) {
    this.idUsuarioModificacion = idUsuarioModificacion;
  }
}
