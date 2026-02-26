package BackERP.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "segUsuariosPerfiles", schema = "erpConfig")
public class segUsuariosPerfiles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int idUsuarioPerfil;

    @Column
    private int idUsuario;

    @Column
    private int idPerfil;

    @Column(length = 20)
    private String permiso;

    @Column(length = 250)
    private String comentario;

    @Column
    private int estado;

    @Column
    private LocalDate fechaModificacion;

    @Column
    private LocalTime horaModificacion;

    @Column
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
