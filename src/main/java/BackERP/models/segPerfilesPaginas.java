package BackERP.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "segPerfilesPaginas", schema = "erpConfig")
public class segPerfilesPaginas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int idPerfilPagina;

    @Column
    private int idPerfil;

    @Column
    private int idPagina;

    @Column
    private int permiso;

    @Column
    private int estado;

    @Column
    private LocalDate fechaModificacion;

    @Column
    private LocalTime horaModificacion;

    @Column
    private int idUsuarioModificacion;

    public segPerfilesPaginas() {}

    // Getters y Setters
    public int getIdPerfilPagina() { return idPerfilPagina; }
    public void setIdPerfilPagina(int idPerfilPagina) { this.idPerfilPagina = idPerfilPagina; }

    public int getIdPerfil() { return idPerfil; }
    public void setIdPerfil(int idPerfil) { this.idPerfil = idPerfil; }

    public int getIdPagina() { return idPagina; }
    public void setIdPagina(int idPagina) { this.idPagina = idPagina; }

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
