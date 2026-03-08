package BackERP.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class erpMovimientosProductos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long idMovimientoProducto;
    @Column
    private int idOrdenProducto;
    @ManyToOne
    @JoinColumn(name = "idProducto", referencedColumnName = "idProducto")
    private erpProductos idProducto;

    @Column
    private int cantidad;
    @Column
    private double precioCompra;
    @Column
    private int idUbicacion;
    @Column
    private int  estado;
    @Column
    private LocalDate fechaModificacion;
    @Column
    private LocalTime horaModificacion;
    @Column
    private int idUsuarioModificacion;


    public erpMovimientosProductos() {
    }


    public Long getIdMovimientoProducto() {
        return idMovimientoProducto;
    }

    public void setIdMovimientoProducto(Long idMovimientoProducto) {
        this.idMovimientoProducto = idMovimientoProducto;
    }

    public int getIdOrdenProducto() {
        return idOrdenProducto;
    }

    public void setIdOrdenProducto(int idOrdenProducto) {
        this.idOrdenProducto = idOrdenProducto;
    }



    public erpProductos getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(erpProductos idProducto) {
        this.idProducto = idProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(double precioCompra) {
        this.precioCompra = precioCompra;
    }

    public int getIdUbicacion() {
        return idUbicacion;
    }

    public void setIdUbicacion(int idUbicacion) {
        this.idUbicacion = idUbicacion;
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
