package BackERP.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "erpDetalleFacturas", schema = "erpConfig")
public class erpDetalleFacturas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idDetalleFactura")
    private Long idDetalleFactura;

    @Column(name = "idEncabezadoFactura")
    private int idEncabezadoFactura;

    @Column(name = "idProducto")
    private int idProducto;

    @Column(name = "idUnidadDeMedida")
    private int idUnidadDeMedida;

    @Column(name = "cantidad")
    private int cantidad;

    @Column(name = "precioVenta")
    private double precioVenta;

    @Column(name = "cantidadDeDescuento")
    private double cantidadDeDescuento;

    @Column(name = "porcentajeDeDescuento")
    private double porcentajeDeDescuento;

    @Column(name = "ImpBruto")
    private double ImpBruto;

    @Column(name = "ImpExento")
    private double ImpExento;

    @Column(name = "ImpOtros")
    private double ImpOtros;

    @Column(name = "ImpNeto")
    private double ImpNeto;

    @Column(name = "iva")
    private double iva;

    @Column(name = "isr")
    private double isr;

    @Column(name = "ImpTotal")
    private double ImpTotal;

    @Column(name = "estado")
    private int estado;

    @Column(name = "fechaModificacion")
    private LocalDate fechaModificacion;

    @Column(name = "horaModificacion")
    private LocalTime horaModificacion;

    @Column(name = "idUsuarioModificacion")
    private int idUsuarioModificacion;

    public erpDetalleFacturas() {}
    // getters y setters...


    public Long getIdDetalleFactura() {
        return idDetalleFactura;
    }

    public void setIdDetalleFactura(Long idDetalleFactura) {
        this.idDetalleFactura = idDetalleFactura;
    }

    public int getIdEncabezadoFactura() {
        return idEncabezadoFactura;
    }

    public void setIdEncabezadoFactura(int idEncabezadoFactura) {
        this.idEncabezadoFactura = idEncabezadoFactura;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getIdUnidadDeMedida() {
        return idUnidadDeMedida;
    }

    public void setIdUnidadDeMedida(int idUnidadDeMedida) {
        this.idUnidadDeMedida = idUnidadDeMedida;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public double getCantidadDeDescuento() {
        return cantidadDeDescuento;
    }

    public void setCantidadDeDescuento(double cantidadDeDescuento) {
        this.cantidadDeDescuento = cantidadDeDescuento;
    }

    public double getPorcentajeDeDescuento() {
        return porcentajeDeDescuento;
    }

    public void setPorcentajeDeDescuento(double porcentajeDeDescuento) {
        this.porcentajeDeDescuento = porcentajeDeDescuento;
    }

    public double getImpNeto() {
        return ImpNeto;
    }

    public void setImpNeto(double impNeto) {
        ImpNeto = impNeto;
    }

    public double getImpOtros() {
        return ImpOtros;
    }

    public void setImpOtros(double impOtros) {
        ImpOtros = impOtros;
    }

    public double getImpExento() {
        return ImpExento;
    }

    public void setImpExento(double impExento) {
        ImpExento = impExento;
    }

    public double getImpBruto() {
        return ImpBruto;
    }

    public void setImpBruto(double impBruto) {
        ImpBruto = impBruto;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public double getIsr() {
        return isr;
    }

    public void setIsr(double isr) {
        this.isr = isr;
    }

    public double getImpTotal() {
        return ImpTotal;
    }

    public void setImpTotal(double impTotal) {
        ImpTotal = impTotal;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
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
