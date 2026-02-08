package BackERP.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity

public class erpProveedores {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idProveedor;
    @Column
    private String nombre;
    @Column
    private String direccionFisica;
    @Column
    private String correoElectronico;
    @Column
    private String nombreDeContacto1;
    @Column
    private String nombreDeContacto2;
    @Column
    private String telefono1;
    @Column
    private String telefono2;
    @Column
    private Double creditoAutorizado;
    @Column
    private Double deudaActual;
    @Column
    private Integer estado;
    @Column
    private LocalDate fechaModificacion;
    @Column
    private LocalTime horaModificacion;
    @Column
    private Integer idUsuarioModificacion;

    public erpProveedores() {
    }

    public Integer getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Integer idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccionFisica() {
        return direccionFisica;
    }

    public void setDireccionFisica(String direccionFisica) {
        this.direccionFisica = direccionFisica;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getNombreDeContacto1() {
        return nombreDeContacto1;
    }

    public void setNombreDeContacto1(String nombreDeContacto1) {
        this.nombreDeContacto1 = nombreDeContacto1;
    }

    public String getNombreDeContacto2() {
        return nombreDeContacto2;
    }

    public void setNombreDeContacto2(String nombreDeContacto2) {
        this.nombreDeContacto2 = nombreDeContacto2;
    }

    public String getTelefono1() {
        return telefono1;
    }

    public void setTelefono1(String telefono1) {
        this.telefono1 = telefono1;
    }

    public String getTelefono2() {
        return telefono2;
    }

    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2;
    }

    public Double getCreditoAutorizado() {
        return creditoAutorizado;
    }

    public void setCreditoAutorizado(Double creditoAutorizado) {
        this.creditoAutorizado = creditoAutorizado;
    }

    public Double getDeudaActual() {
        return deudaActual;
    }

    public void setDeudaActual(Double deudaActual) {
        this.deudaActual = deudaActual;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
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

    public Integer getIdUsuarioModificacion() {
        return idUsuarioModificacion;
    }

    public void setIdUsuarioModificacion(Integer idUsuarioModificacion) {
        this.idUsuarioModificacion = idUsuarioModificacion;
    }
}
