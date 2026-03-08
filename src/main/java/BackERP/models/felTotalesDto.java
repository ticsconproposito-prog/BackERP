
package BackERP.models;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

/**
 * DTO de totales del documento (suma de líneas).
 * Deben cuadrar con las sumatorias y tolerancia permitida.
 */
public class felTotalesDto {

    @NotNull
    private BigDecimal bruto;

    @NotNull
    private BigDecimal descuento;

    @NotNull
    private BigDecimal exento;

    @NotNull
    private BigDecimal otros;

    @NotNull
    private BigDecimal neto;

    @NotNull
    private BigDecimal isr;

    @NotNull
    private BigDecimal iva;

    @NotNull
    private BigDecimal total;

    // ===== Getters y Setters =====
    public BigDecimal getBruto() { return bruto; }
    public void setBruto(BigDecimal bruto) { this.bruto = bruto; }

    public BigDecimal getDescuento() { return descuento; }
    public void setDescuento(BigDecimal descuento) { this.descuento = descuento; }

    public BigDecimal getExento() { return exento; }
    public void setExento(BigDecimal exento) { this.exento = exento; }

    public BigDecimal getOtros() { return otros; }
    public void setOtros(BigDecimal otros) { this.otros = otros; }

    public BigDecimal getNeto() { return neto; }
    public void setNeto(BigDecimal neto) { this.neto = neto; }

    public BigDecimal getIsr() { return isr; }
    public void setIsr(BigDecimal isr) { this.isr = isr; }

    public BigDecimal getIva() { return iva; }
    public void setIva(BigDecimal iva) { this.iva = iva; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
}
