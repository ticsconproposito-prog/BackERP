package BackERP.models;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "fel")
public class FelProperties {

    // Credenciales HTTP Basic
    private String basicUser;
    private String basicPass;

    // Credenciales WebService
    private String usuario;
    private String password;

    // Datos adicionales FEL
    private String nitEmisor;
    private int establecimiento;
    private String idMaquina;
    private String tipoRespuesta;
    private String endpoint;

    // ===== Getters y Setters =====
    public String getBasicUser() { return basicUser; }
    public void setBasicUser(String basicUser) { this.basicUser = basicUser; }

    public String getBasicPass() { return basicPass; }
    public void setBasicPass(String basicPass) { this.basicPass = basicPass; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNitEmisor() { return nitEmisor; }
    public void setNitEmisor(String nitEmisor) { this.nitEmisor = nitEmisor; }

    public int getEstablecimiento() { return establecimiento; }
    public void setEstablecimiento(int establecimiento) { this.establecimiento = establecimiento; }

    public String getIdMaquina() { return idMaquina; }
    public void setIdMaquina(String idMaquina) { this.idMaquina = idMaquina; }

    public String getTipoRespuesta() { return tipoRespuesta; }
    public void setTipoRespuesta(String tipoRespuesta) { this.tipoRespuesta = tipoRespuesta; }

    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
}
