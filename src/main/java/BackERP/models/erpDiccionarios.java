package BackERP.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "ErpDiccionarios", schema = "erpConfig")
public class erpDiccionarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idDiccionario")
    private Long idDiccionario;

    @Column(name = "diccionario", length = 100)
    private String diccionario;

    @Column(name = "indice")
    private int indice;

    @Column(name = "valor", length = 100)
    private String valor;

    @Column(name = "descripcion", length = 300)
    private String descripcion;

    @Column(name = "estado")
    private int estado;

    @Column(name = "fechaModificacion")
    private LocalDate fechaModificacion;

    @Column(name = "horaModificacion")
    private LocalTime horaModificacion;

    @Column(name = "idUsuarioModificacion")
    private int idUsuarioModificacion;

    public erpDiccionarios() {}

    // Getters y Setters
    public Long getIdDiccionario() { return idDiccionario; }
    public void setIdDiccionario(Long idDiccionario) { this.idDiccionario = idDiccionario; }

    public String getDiccionario() { return diccionario; }
    public void setDiccionario(String diccionario) { this.diccionario = diccionario; }

    public int getIndice() { return indice; }
    public void setIndice(int indice) { this.indice = indice; }

    public String getValor() { return valor; }
    public void setValor(String valor) { this.valor = valor; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }

    public LocalDate getFechaModificacion() { return fechaModificacion; }
    public void setFechaModificacion(LocalDate fechaModificacion) { this.fechaModificacion = fechaModificacion; }

    public LocalTime getHoraModificacion() { return horaModificacion; }
    public void setHoraModificacion(LocalTime horaModificacion) { this.horaModificacion = horaModificacion; }

    public int getIdUsuarioModificacion() { return idUsuarioModificacion; }
    public void setIdUsuarioModificacion(int idUsuarioModificacion) { this.idUsuarioModificacion = idUsuarioModificacion; }
}
