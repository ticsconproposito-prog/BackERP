package BackERP.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "erpEmpleados", schema = "erpConfig")
public class erpEmpleados {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idEmpleado")
    private long idEmpleado;

    @Column(name = "nombre", length = 60)
    private String nombre;

    @Column(name = "apellido", length = 60)
    private String apellido;

    @Column(name = "email", length = 80)
    private String email;

    @Column(name = "telefono")
    private Integer telefono;

    @Column(name = "idCargo")
    private Integer idCargo;

    @Column(name = "idUbicacion")
    private Integer idUbicacion;

    @Column(name = "direccionResidencia", length = 250)
    private String direccionResidencia;

    @Column(name = "fechaNacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "estado")
    private Integer estado;

    @Column(name = "fechaIngresoLaboral")
    private LocalDate fechaIngresoLaboral;

    @Column(name = "fechaModificacion")
    private LocalDate fechaModificacion;

    @Column(name = "horaModificacion")
    private LocalTime horaModificacion;

    @Column(name = "idUsuarioModificacion")
    private Integer idUsuarioModificacion;

    // constructor vacío
    public erpEmpleados() {}

    // getters y setters...

    public long getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(long idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public int getIdCargo() {
        return idCargo;
    }

    public void setIdCargo(int idCargo) {
        this.idCargo = idCargo;
    }

    public int getIdUbicacion() {
        return idUbicacion;
    }

    public void setIdUbicacion(int idUbicacion) {
        this.idUbicacion = idUbicacion;
    }

    public String getDireccionResidencia() {
        return direccionResidencia;
    }

    public void setDireccionResidencia(String direccionResidencia) {
        this.direccionResidencia = direccionResidencia;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public LocalDate getFechaIngresoLaboral() {
        return fechaIngresoLaboral;
    }

    public void setFechaIngresoLaboral(LocalDate fechaIngresoLaboral) {
        this.fechaIngresoLaboral = fechaIngresoLaboral;
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
