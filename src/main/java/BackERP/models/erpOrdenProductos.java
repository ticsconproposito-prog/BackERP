package BackERP.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "erpOrdenProductos", schema = "erpConfig")
public class erpOrdenProductos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idOrdenProducto")
    private Long idOrdenProducto;

    @Column(name = "idSucursal")
    private int idSucursal;

    @Column(name = "fechaOrden")
    private LocalDate fechaOrden;

    @Column(name = "idProveedor")
    private int idProveedor;

    @Column(name = "numeroDeDocumento", length = 150)
    private String numeroDeDocumento;

    @Column(name = "tipoDeMovimiento")
    private int tipoDeMovimiento;

    @Column(name = "tipoDeOrden")
    private int tipoDeOrden;

    @Column(name = "estadoFactura")
    private int estadoFactura;

    @Column(name = "precioTotalOrden", precision = 15, scale = 2)
    private BigDecimal precioTotalOrden;

    // Este campo no está en la tabla SQL, decide si lo agregas en la BD o lo eliminas
    @Column(name = "valorCancelado", precision = 15, scale = 2)
    private BigDecimal valorCancelado;

    @Column(name = "comentario", length = 3000)
    private String comentario;

    @Column(name = "estado")
    private int estado;

    @Column(name = "fechaModificacion")
    private LocalDate fechaModificacion;

    @Column(name = "horaModificacion")
    private LocalTime horaModificacion;

    @Column(name = "idUsuarioModificacion")
    private int idUsuarioModificacion;

    public erpOrdenProductos() {}

    // Getters y Setters
    public Long getIdOrdenProducto() { return idOrdenProducto; }
    public void setIdOrdenProducto(Long idOrdenProducto) { this.idOrdenProducto = idOrdenProducto; }

    public int getIdSucursal() { return idSucursal; }
    public void setIdSucursal(int idSucursal) { this.idSucursal = idSucursal; }

    public LocalDate getFechaOrden() { return fechaOrden; }
    public void setFechaOrden(LocalDate fechaOrden) { this.fechaOrden = fechaOrden; }

    public int getIdProveedor() { return idProveedor; }
    public void setIdProveedor(int idProveedor) { this.idProveedor = idProveedor; }

    public String getNumeroDeDocumento() { return numeroDeDocumento; }
    public void setNumeroDeDocumento(String numeroDeDocumento) { this.numeroDeDocumento = numeroDeDocumento; }

    public int getTipoDeMovimiento() { return tipoDeMovimiento; }
    public void setTipoDeMovimiento(int tipoDeMovimiento) { this.tipoDeMovimiento = tipoDeMovimiento; }

    public int getTipoDeOrden() { return tipoDeOrden; }
    public void setTipoDeOrden(int tipoDeOrden) { this.tipoDeOrden = tipoDeOrden; }

    public int getEstadoFactura() { return estadoFactura; }
    public void setEstadoFactura(int estadoFactura) { this.estadoFactura = estadoFactura; }

    public BigDecimal getPrecioTotalOrden() { return precioTotalOrden; }
    public void setPrecioTotalOrden(BigDecimal precioTotalOrden) { this.precioTotalOrden = precioTotalOrden; }

    public BigDecimal getValorCancelado() { return valorCancelado; }
    public void setValorCancelado(BigDecimal valorCancelado) { this.valorCancelado = valorCancelado; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }

    public LocalDate getFechaModificacion() { return fechaModificacion; }
    public void setFechaModificacion(LocalDate fechaModificacion) { this.fechaModificacion = fechaModificacion; }

    public LocalTime getHoraModificacion() { return horaModificacion; }
    public void setHoraModificacion(LocalTime horaModificacion) { this.horaModificacion = horaModificacion; }

    public int getIdUsuarioModificacion() { return idUsuarioModificacion; }
    public void setIdUsuarioModificacion(int idUsuarioModificacion) { this.idUsuarioModificacion = idUsuarioModificacion; }
}
