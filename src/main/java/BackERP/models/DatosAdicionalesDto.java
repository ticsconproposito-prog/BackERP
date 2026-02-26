package BackERP.models;

import jakarta.validation.constraints.NotBlank;

public class DatosAdicionalesDto {

    @NotBlank
    private String tipoReceptor;

    private String email;
    private String enviar;
    private String adicional01;
    private String adicional02;
    private String adicional03;
    private String adicional04;

    // Getters y Setters
    public String getTipoReceptor() { return tipoReceptor; }
    public void setTipoReceptor(String tipoReceptor) { this.tipoReceptor = tipoReceptor; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getEnviar() { return enviar; }
    public void setEnviar(String enviar) { this.enviar = enviar; }

    public String getAdicional01() { return adicional01; }
    public void setAdicional01(String adicional01) { this.adicional01 = adicional01; }

    public String getAdicional02() { return adicional02; }
    public void setAdicional02(String adicional02) { this.adicional02 = adicional02; }

    public String getAdicional03() { return adicional03; }
    public void setAdicional03(String adicional03) { this.adicional03 = adicional03; }

    public String getAdicional04() { return adicional04; }
    public void setAdicional04(String adicional04) { this.adicional04 = adicional04; }
}
