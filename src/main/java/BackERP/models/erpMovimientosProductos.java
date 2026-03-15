package BackERP.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "erpMovimientosProductos", schema = "erpConfig")
public class erpMovimientosProductos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idMovimientoProducto")
    private Long idMovimientoProducto;

    @Column(name = "idOrdenProducto")
    private int idOrdenProducto;

    @ManyToOne
    @JoinColumn(name = "idProducto", referencedColumnName = "idProducto")
    private erpProductos idProducto;

    @Column(name = "cantidad")
    private int cantidad;

    @Column(name = "precioCompra", precision = 15, scale = 2)
    private BigDecimal precioCompra;

    @Column(name = "idUbicacion")
    private int idUbicacion;

    @Column(name = "estado")
    private int estado;

    @Column(name = "fechaModificacion")
    private LocalDate fechaModificacion;

    @Column(name = "horaModificacion")
    private LocalTime horaModificacion;

    @Column(name = "idUsuarioModificacion")
    private int idUsuarioModificacion;

    public erpMovimientosProductos() {}

    // Getters y Setters
    public Long getIdMovimientoProducto() { return idMovimientoProducto; }
    public void setIdMovimientoProducto(Long idMovimientoProducto) { this.idMovimientoProducto = idMovimientoProducto; }

    public int getIdOrdenProducto() { return idOrdenProducto; }
    public void setIdOrdenProducto(int idOrdenProducto) { this.idOrdenProducto = idOrdenProducto; }

    public erpProductos getIdProducto() { return idProducto; }
    public void setIdProducto(erpProductos idProducto) { this.idProducto = idProducto; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public BigDecimal getPrecioCompra() { return precioCompra; }
    public void setPrecioCompra(BigDecimal precioCompra) { this.precioCompra = precioCompra; }

    public int getIdUbicacion() { return idUbicacion; }
    public void setIdUbicacion(int idUbicacion) { this.idUbicacion = idUbicacion; }

    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }

    public LocalDate getFechaModificacion() { return fechaModificacion; }
    public void setFechaModificacion(LocalDate fechaModificacion) { this.fechaModificacion = fechaModificacion; }

    public LocalTime getHoraModificacion() { return horaModificacion; }
    public void setHoraModificacion(LocalTime horaModificacion) { this.horaModificacion = horaModificacion; }

    public int getIdUsuarioModificacion() { return idUsuarioModificacion; }
    public void setIdUsuarioModificacion(int idUsuarioModificacion) { this.idUsuarioModificacion = idUsuarioModificacion; }
}
