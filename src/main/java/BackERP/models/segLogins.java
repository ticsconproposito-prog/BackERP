package BackERP.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "segLogins", schema = "erpConfig")
public class segLogins {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int idLogin;

    @Column
    private int idUsuario;

    @Column
    private String estadoConexion;

    @Column
    private int estado;

    @Column
    private LocalDate fechaModificacion;

    @Column
    private LocalTime horaModificacion;

    @Column
    private int idUsuarioModificacion;

    public segLogins() {}

    // Getters y Setters
    public int getIdLogin() { return idLogin; }
    public void setIdLogin(int idLogin) { this.idLogin = idLogin; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getEstadoConexion() { return estadoConexion; }
    public void setEstadoConexion(String estadoConexion) { this.estadoConexion = estadoConexion; }

    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }

    public LocalDate getFechaModificacion() { return fechaModificacion; }
    public void setFechaModificacion(LocalDate fechaModificacion) { this.fechaModificacion = fechaModificacion; }

    public LocalTime getHoraModificacion() { return horaModificacion; }
    public void setHoraModificacion(LocalTime horaModificacion) { this.horaModificacion = horaModificacion; }

    public int getIdUsuarioModificacion() { return idUsuarioModificacion; }
    public void setIdUsuarioModificacion(int idUsuarioModificacion) { this.idUsuarioModificacion = idUsuarioModificacion; }
}
