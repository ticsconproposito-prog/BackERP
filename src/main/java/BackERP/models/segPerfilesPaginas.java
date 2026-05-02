package BackERP.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "segPerfilesPaginas", schema = "erpConfig")
public class segPerfilesPaginas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPerfilPagina")
    private int idPerfilPagina;

    @Column(name = "idPerfil")
    private Long idPerfil;

    @Column(name = "idPagina")
    private Long idPagina;

    @Column(name = "permiso")
    private int permiso;

    @Column(name = "estado")
    private int estado;

    @Column(name = "fechaModificacion")
    private LocalDate fechaModificacion;

    @Column(name = "horaModificacion")
    private LocalTime horaModificacion;

    @Column(name = "idUsuarioModificacion")
    private int idUsuarioModificacion;

    public segPerfilesPaginas() {}

    // Getters y Setters
    public int getIdPerfilPagina() { return idPerfilPagina; }
    public void setIdPerfilPagina(int idPerfilPagina) { this.idPerfilPagina = idPerfilPagina; }

    public Long getIdPerfil() { return idPerfil; }
    public void setIdPerfil(Long idPerfil) { this.idPerfil = idPerfil; }

    public Long getIdPagina() { return idPagina; }
    public void setIdPagina(Long idPagina) { this.idPagina = idPagina; }

    public int getPermiso() { return permiso; }
    public void setPermiso(int permiso) { this.permiso = permiso; }

    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }

    public LocalDate getFechaModificacion() { return fechaModificacion; }
    public void setFechaModificacion(LocalDate fechaModificacion) { this.fechaModificacion = fechaModificacion; }

    public LocalTime getHoraModificacion() { return horaModificacion; }
    public void setHoraModificacion(LocalTime horaModificacion) { this.horaModificacion = horaModificacion; }

    public int getIdUsuarioModificacion() { return idUsuarioModificacion; }
    public void setIdUsuarioModificacion(int idUsuarioModificacion) { this.idUsuarioModificacion = idUsuarioModificacion; }
}
