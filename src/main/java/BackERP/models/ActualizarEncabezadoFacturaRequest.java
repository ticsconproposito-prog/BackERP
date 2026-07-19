// ActualizarEncabezadoFacturaRequest.java
package BackERP.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

public class ActualizarEncabezadoFacturaRequest {

  @JsonProperty("totalBruto")
  private double totalBruto;

  @JsonProperty("porcentajeDeDescuento")
  private double porcentajeDeDescuento;

  @JsonProperty("cantidadDeDescuento")
  private double cantidadDeDescuento;

  @JsonProperty("totalNeto")
  private double totalNeto;

  @JsonProperty("iva")
  private double iva;

  @JsonProperty("total")
  private double total;

  @JsonProperty("idUsuarioModificacion")
  private int idUsuarioModificacion;

  // Getters y Setters
  public double getTotalBruto() {
    return totalBruto;
  }

  public void setTotalBruto(double totalBruto) {
    this.totalBruto = totalBruto;
  }

  public double getPorcentajeDeDescuento() {
    return porcentajeDeDescuento;
  }

  public void setPorcentajeDeDescuento(double porcentajeDeDescuento) {
    this.porcentajeDeDescuento = porcentajeDeDescuento;
  }

  public double getCantidadDeDescuento() {
    return cantidadDeDescuento;
  }

  public void setCantidadDeDescuento(double cantidadDeDescuento) {
    this.cantidadDeDescuento = cantidadDeDescuento;
  }

  public double getTotalNeto() {
    return totalNeto;
  }

  public void setTotalNeto(double totalNeto) {
    this.totalNeto = totalNeto;
  }

  public double getIva() {
    return iva;
  }

  public void setIva(double iva) {
    this.iva = iva;
  }

  public double getTotal() {
    return total;
  }

  public void setTotal(double total) {
    this.total = total;
  }

  public int getIdUsuarioModificacion() {
    return idUsuarioModificacion;
  }

  public void setIdUsuarioModificacion(int idUsuarioModificacion) {
    this.idUsuarioModificacion = idUsuarioModificacion;
  }
}
