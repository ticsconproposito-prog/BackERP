package BackERP.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "segUsuariosPerfiles", schema = "erpConfig")
public class segUsuariosPerfiles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsuarioPerfil")
    private int idUsuarioPerfil;

    @Column(name = "idUsuario")
    private int idUsuario;

    @Column(name = "idPerfil")
    private int idPerfil;

    @Column(name = "permiso", length = 20)
    private String permiso;

    @Column(name = "comentario", length = 250)
    private String comentario;

    @Column(name = "estado")
    private int estado;

    @Column(name = "fechaModificacion")
    private LocalDate fechaModificacion;

    @Column(name = "horaModificacion")
    private LocalTime horaModificacion;

    @Column(name = "idUsuarioModificacion")
    private int idUsuarioModificacion;

    public segUsuariosPerfiles() {}

    // Getters y Setters
    public int getIdUsuarioPerfil() { return idUsuarioPerfil; }
    public void setIdUsuarioPerfil(int idUsuarioPerfil) { this.idUsuarioPerfil = idUsuarioPerfil; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public int getIdPerfil() { return idPerfil; }
    public void setIdPerfil(int idPerfil) { this.idPerfil = idPerfil; }

    public String getPermiso() { return permiso; }
    public void setPermiso(String permiso) { this.permiso = permiso; }

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
