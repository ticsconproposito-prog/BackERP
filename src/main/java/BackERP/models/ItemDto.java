package BackERP.models;

import java.math.BigDecimal;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO de detalle (producto/servicio) del DTE.
 * Incluye montos calculados para validación/consistencia con la guía FEL.
 */
public class ItemDto {

    @NotBlank
    private String producto;

    @NotBlank
    private String descripcion;

    @NotNull
    private Integer medida;            // 1 = UNIDAD, según catálogo FEL

    @NotNull
    private BigDecimal cantidad;

    @NotNull
    private BigDecimal precio;         // IVA incluido

    @NotNull
    private BigDecimal porcDesc;

    @NotNull
    private BigDecimal impBruto;

    @NotNull
    private BigDecimal impDescuento;

    @NotNull
    private BigDecimal impExento;

    @NotNull
    private BigDecimal impOtros;

    @NotNull
    private BigDecimal impNeto;

    @NotNull
    private BigDecimal impIsr;

    @NotNull
    private BigDecimal impIva;

    @NotNull
    private BigDecimal impTotal;

    private String tipoVentaDet;       // B | S (opcional si ya envías a nivel documento)

    @Valid
    private DatosAdicionalesProdDto datosAdicionalesProd;


    // ===== Getters y Setters =====
    public String getProducto() { return producto; }
    public void setProducto(String producto) { this.producto = producto; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Integer getMedida() { return medida; }
    public void setMedida(Integer medida) { this.medida = medida; }

    public BigDecimal getCantidad() { return cantidad; }
    public void setCantidad(BigDecimal cantidad) { this.cantidad = cantidad; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public BigDecimal getPorcDesc() { return porcDesc; }
    public void setPorcDesc(BigDecimal porcDesc) { this.porcDesc = porcDesc; }

    public BigDecimal getImpBruto() { return impBruto; }
    public void setImpBruto(BigDecimal impBruto) { this.impBruto = impBruto; }

    public BigDecimal getImpDescuento() { return impDescuento; }
    public void setImpDescuento(BigDecimal impDescuento) { this.impDescuento = impDescuento; }

    public BigDecimal getImpExento() { return impExento; }
    public void setImpExento(BigDecimal impExento) { this.impExento = impExento; }

    public BigDecimal getImpOtros() { return impOtros; }
    public void setImpOtros(BigDecimal impOtros) { this.impOtros = impOtros; }

    public BigDecimal getImpNeto() { return impNeto; }
    public void setImpNeto(BigDecimal impNeto) { this.impNeto = impNeto; }

    public BigDecimal getImpIsr() { return impIsr; }
    public void setImpIsr(BigDecimal impIsr) { this.impIsr = impIsr; }

    public BigDecimal getImpIva() { return impIva; }
    public void setImpIva(BigDecimal impIva) { this.impIva = impIva; }

    public BigDecimal getImpTotal() { return impTotal; }
    public void setImpTotal(BigDecimal impTotal) { this.impTotal = impTotal; }

    public String getTipoVentaDet() { return tipoVentaDet; }
    public void setTipoVentaDet(String tipoVentaDet) { this.tipoVentaDet = tipoVentaDet; }

    public DatosAdicionalesProdDto getDatosAdicionalesProd() { return datosAdicionalesProd; }
    public void setDatosAdicionalesProd(DatosAdicionalesProdDto datosAdicionalesProd) { this.datosAdicionalesProd = datosAdicionalesProd; }

}
