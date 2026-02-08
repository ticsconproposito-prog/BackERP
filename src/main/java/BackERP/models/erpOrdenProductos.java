package BackERP.models;


import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity

public class erpOrdenProductos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long idOrdenProducto;       // nombre de la propiedad en Java (camelCase)
    @Column
    private int idSucursal;
    @Column
    private LocalDate fechaOrden;
    @Column
    private int idProveedor;
    @Column
    private String numeroDeDocumento;
    @Column
    private int tipoDeMovimiento;
    @Column
    private int tipoDeOrden;
    @Column
    private int estadoFactura;
    @Column
    private double precioTotalOrden;
    @Column
    private double  valorCancelado;
    @Column
    private String comentario;
    @Column
    private int  estado;
    @Column
    private LocalDate fechaModificacion;
    @Column
    private LocalTime horaModificacion;
    @Column
    private int idUsuarioModificacion;


    public erpOrdenProductos() {
    }

    public Long getIdOrdenProducto() {
        return idOrdenProducto;
    }

    public void setIdOrdenProducto(Long idOrdenProducto) {
        this.idOrdenProducto = idOrdenProducto;
    }


    public int getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(int idSucursal) {
        this.idSucursal = idSucursal;
    }

    public LocalDate getFechaOrden() {
        return fechaOrden;
    }

    public void setFechaOrden(LocalDate fechaIngreso) {
        this.fechaOrden = fechaIngreso;
    }


    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getNumeroDeDocumento() {
        return numeroDeDocumento;
    }

    public void setNumeroDeDocumento(String numeroDeDocumento) {
        this.numeroDeDocumento = numeroDeDocumento;
    }

    public int getTipoDeMovimiento() {
        return tipoDeMovimiento;
    }

    public void setTipoDeMovimiento(int tipoDeMovimiento) {
        this.tipoDeMovimiento = tipoDeMovimiento;
    }

    public int getTipoDeOrden() {
        return tipoDeOrden;
    }

    public void setTipoDeOrden(int tipoDeOrden) {
        this.tipoDeOrden = tipoDeOrden;
    }

    public int getEstadoFactura() {
        return estadoFactura;
    }

    public void setEstadoFactura(int estadoFactura) {
        this.estadoFactura = estadoFactura;
    }

    public double getPrecioTotalOrden() {
        return precioTotalOrden;
    }

    public void setPrecioTotalOrden(double precioTotalOrden) {
        this.precioTotalOrden = precioTotalOrden;
    }

    public double getValorCancelado() {
        return valorCancelado;
    }

    public void setValorCancelado(double valorCancelado) {
        this.valorCancelado = valorCancelado;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
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
