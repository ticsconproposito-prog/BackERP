package BackERP.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class erpinventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long  idProductoInventario;
    @ManyToOne
    @JoinColumn(name = "idProducto", referencedColumnName = "idProducto")
    private erpProductos idProducto;
    @Column
    private int cantidadExistencias;
    @Column
    private int cantidadDanados;
    @Column
    private int idUbicacion;
    @Column
    private double precioCompra;
    @Column
    private double precioVenta;
    @Column
    private int  estado;
    @Column
    private LocalDate fechaModificacion;
    @Column
    private LocalTime horaModificacion;
    @Column
    private int idUsuarioModificacion;

    public erpinventario() {
    }

    public Long getIdProductoInventario() {
        return idProductoInventario;
    }

    public void setIdProductoInventario(Long idProductoInventario) {
        this.idProductoInventario = idProductoInventario;
    }

    public erpProductos getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(erpProductos idProducto) {
        this.idProducto = idProducto;
    }

    public int getCantidadExistencias() {
        return cantidadExistencias;
    }

    public void setCantidadExistencias(int cantidadExistencias) {
        this.cantidadExistencias = cantidadExistencias;
    }

    public int getCantidadDanados() {
        return cantidadDanados;
    }

    public void setCantidadDanados(int cantidadDanados) {
        this.cantidadDanados = cantidadDanados;
    }

    public int getIdUbicacion() {
        return idUbicacion;
    }

    public void setIdUbicacion(int idUbicacion) {
        this.idUbicacion = idUbicacion;
    }

    public double getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(double precioCompra) {
        this.precioCompra = precioCompra;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
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
