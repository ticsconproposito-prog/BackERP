package BackERP.config;

public class FelResult {
    private boolean ok;
    private String numeroAutorizacion;
    private String serie;
    private String Preimpreso;
    private String error;
    private String rawResponse;

    // Campos adicionales
    private String nombre;
    private String direccion;
    private String telefono;
    private String referencia;

    // Getters y Setters
    public boolean isOk() { return ok; }
    public void setOk(boolean ok) { this.ok = ok; }

    public String getNumeroAutorizacion() { return numeroAutorizacion; }
    public void setNumeroAutorizacion(String numeroAutorizacion) { this.numeroAutorizacion = numeroAutorizacion; }

    public String getSerie() { return serie; }
    public void setSerie(String serie) { this.serie = serie; }

    public String getPreimpreso() { return Preimpreso; }
    public void setPreimpreso(String preimpreso) { this.Preimpreso = preimpreso; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public String getRawResponse() { return rawResponse; }
    public void setRawResponse(String rawResponse) { this.rawResponse = rawResponse; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }
}
