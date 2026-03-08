
package BackERP.models;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO del receptor (cliente). Para C/F (Consumidor Final) la guía exige
 * nombre y dirección.
 */
public class felReceptorDto {

    @NotBlank
    private String nitReceptor; // "C/F" o NIT

    private String nombre;      // obligatorio si C/F
    private String direccion;   // obligatorio si C/F

    // ===== Getters y Setters =====
    public String getNitReceptor() { return nitReceptor; }
    public void setNitReceptor(String nitReceptor) { this.nitReceptor = nitReceptor; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
}

