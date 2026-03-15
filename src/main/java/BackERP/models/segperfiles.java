package BackERP.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "segPerfiles", schema = "erpConfig")
public class segperfiles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPerfil")
    private int idPerfil;

    @Column(name = "nombrePerfil")
    private String nombrePerfil;

    @Column(name = "estado")
    private int estado;

    @Column(name = "fechaModificacion")
    private LocalDate fechaModificacion;

    @Column(name = "horaModificacion")
    private LocalTime horaModificacion;

    @Column(name = "idUsuarioModificacion")
    private int idUsuarioModificacion;

    public segperfiles() {}

    // Getters y setters
    public int getIdPerfil() { return idPerfil; }
    public void setIdPerfil(int idPerfil) { this.idPerfil = idPerfil; }

    public String getNombrePerfil() { return nombrePerfil; }
    public void setNombrePerfil(String nombrePerfil) { this.nombrePerfil = nombrePerfil; }

    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }

    public LocalDate getFechaModificacion() { return fechaModificacion; }
    public void setFechaModificacion(LocalDate fechaModificacion) { this.fechaModificacion = fechaModificacion; }

    public LocalTime getHoraModificacion() { return horaModificacion; }
    public void setHoraModificacion(LocalTime horaModificacion) { this.horaModificacion = horaModificacion; }

    public int getIdUsuarioModificacion() { return idUsuarioModificacion; }
    public void setIdUsuarioModificacion(int idUsuarioModificacion) { this.idUsuarioModificacion = idUsuarioModificacion; }
}
