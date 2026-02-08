package BackERP.models;


import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
// Usa los nombres exactos en tu BD
public class erpProductos
{




    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long idProducto;       // nombre de la propiedad en Java (camelCase)
    @Column
    private String codigoProducto;
    @Column
    private String codigoProductoProveedor;
    @Column
    private String descripcionProducto;
    @Column
    private int unidadDeMedida;
    @Column
    private int  estado;
    @Column
    private LocalDate fechaModificacion;
    @Column
    private LocalTime horaModificacion;
    @Column
    private int idUsuarioModificacion;



    public erpProductos() {

    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public String getCodigoProductoProveedor() {
        return codigoProductoProveedor;
    }

    public void setCodigoProductoProveedor(String codigoProductoProveedor) {
        this.codigoProductoProveedor = codigoProductoProveedor;
    }

    public String getDescripcionProducto() {
        return descripcionProducto;
    }

    public void setDescripcionProducto(String descripcionProducto) {
        this.descripcionProducto = descripcionProducto;
    }

    public int getUnidadDeMedida() {
        return unidadDeMedida;
    }

    public void setUnidadDeMedida(int unidadDeMedida) {
        this.unidadDeMedida = unidadDeMedida;
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
