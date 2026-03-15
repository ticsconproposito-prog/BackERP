package BackERP.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "erpProductos", schema = "erpConfig")
public class erpProductos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProducto")
    private Long idProducto;

    @Column(name = "codigoProducto", length = 120)
    private String codigoProducto;

    @Column(name = "codigoProductoProveedor", length = 500)
    private String codigoProductoProveedor;

    @Column(name = "descripcionProducto", length = 500)
    private String descripcionProducto;

    @Column(name = "unidadDeMedida")
    private int unidadDeMedida;

    @Column(name = "precioCompra")
    private double precioCompra;

    @Column(name = "precioVenta")
    private double precioVenta;

    @Column(name = "estado")
    private int estado;

    @Column(name = "fechaModificacion")
    private LocalDate fechaModificacion;

    @Column(name = "horaModificacion")
    private LocalTime horaModificacion;

    @Column(name = "idUsuarioModificacion")
    private int idUsuarioModificacion;

    public erpProductos() {}

    // Getters y Setters
    public Long getIdProducto() { return idProducto; }
    public void setIdProducto(Long idProducto) { this.idProducto = idProducto; }

    public String getCodigoProducto() { return codigoProducto; }
    public void setCodigoProducto(String codigoProducto) { this.codigoProducto = codigoProducto; }

    public String getCodigoProductoProveedor() { return codigoProductoProveedor; }
    public void setCodigoProductoProveedor(String codigoProductoProveedor) { this.codigoProductoProveedor = codigoProductoProveedor; }

    public String getDescripcionProducto() { return descripcionProducto; }
    public void setDescripcionProducto(String descripcionProducto) { this.descripcionProducto = descripcionProducto; }

    public int getUnidadDeMedida() { return unidadDeMedida; }
    public void setUnidadDeMedida(int unidadDeMedida) { this.unidadDeMedida = unidadDeMedida; }

    public double getPrecioCompra() { return precioCompra; }
    public void setPrecioCompra(double precioCompra) { this.precioCompra = precioCompra; }

    public double getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(double precioVenta) { this.precioVenta = precioVenta; }

    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }

    public LocalDate getFechaModificacion() { return fechaModificacion; }
    public void setFechaModificacion(LocalDate fechaModificacion) { this.fechaModificacion = fechaModificacion; }

    public LocalTime getHoraModificacion() { return horaModificacion; }
    public void setHoraModificacion(LocalTime horaModificacion) { this.horaModificacion = horaModificacion; }

    public int getIdUsuarioModificacion() { return idUsuarioModificacion; }
    public void setIdUsuarioModificacion(int idUsuarioModificacion) { this.idUsuarioModificacion = idUsuarioModificacion; }
}
