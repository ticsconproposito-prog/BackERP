package BackERP.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;


@Entity
public class erpDiccionarios {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long idDiccionario;
    @Column
    private String diccionario;
    @Column
    private int indice;
    @Column
    private String valor;
    @Column
    private String descripcion;
    @Column
    private int  estado;
    @Column
    private LocalDate fechaModificacion;
    @Column
    private LocalTime horaModificacion;
    @Column
    private int idUsuarioModificacion;

    public erpDiccionarios() {
    }

    public Long getIdDiccionario() {
        return idDiccionario;
    }

    public void setIdDiccionario(Long idDiccionario) {
        this.idDiccionario = idDiccionario;
    }

    public String getDiccionario() {
        return diccionario;
    }

    public void setDiccionario(String diccionario) {
        this.diccionario = diccionario;
    }

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
