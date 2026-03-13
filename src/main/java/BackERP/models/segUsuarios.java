package BackERP.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "segUsuarios", schema = "erpConfig")
public class segUsuarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsuario")
    private long idUsuario;

    @Column(name = "idEmpleado")
    private int idEmpleado;

    @Column(name = "usuario", length = 50, nullable = false)
    private String usuario;

    @Column(name = "contrasena", length = 50, nullable = false)
    private String contrasena;

    @Column(name = "comentario", length = 500)
    private String comentario;

    @Column(name = "estado")
    private int estado;

    @Column(name = "fechaModificacion")
    private LocalDate fechaModificacion;

    @Column(name = "horaModificacion")
    private LocalTime horaModificacion;

    @Column(name = "idUsuarioModificacion")
    private int idUsuarioModificacion;

    public segUsuarios() {}

    // getters y setters...

    public long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
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
