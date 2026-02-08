package BackERP.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class segpaginas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long idPagina;
    @Column
    private String nombrePagina;
    @Column
    private String URL;
    @Column
    private int estado;
    @Column
    private LocalDate fechaModificacion;
    @Column
    private LocalTime horaModificacion;
    @Column
    private int idUsuarioModificacion;

    public segpaginas() {

    }

    public long getIdPagina() {
        return idPagina;
    }

    public void setIdPagina(long idPagina) {
        this.idPagina = idPagina;
    }

    public String getNombrePagina() {
        return nombrePagina;
    }

    public void setNombrePagina(String nombrePagina) {
        this.nombrePagina = nombrePagina;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
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
