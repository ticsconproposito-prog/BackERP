package BackERP.models;

import com.sun.istack.NotNull;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO principal que representa el payload del DTE para tu microservicio.
 * Alineado con el Anexo 13: Receptor, InfoDoc, Totales y Detalles/Productos.
 */

 public class felDteRequestDto {

    @NotNull
    private Integer tipoDoc;           // 1..10 (FACT, FCAM, FPEQ, FCAP, FESP, NABN, RDON, RECI, NDEB, NCRE)
    @NotBlank
    private String tipoVenta;          // B | S
    @NotNull
    private Integer destinoVenta;      // 1 = Guatemala
    @NotBlank
    private String fecha;              // DD/MM/AAAA
    @NotNull
    private Integer moneda;            // 1 = GTQ, 2 = USD
    @NotNull
    private BigDecimal tasa;           // 1 si Moneda=1
    @NotBlank
    private String referencia;         // único por emisor

    private Long numeroAcceso;         // opcional (contingencia)
    private String serieAdmin;         // opcional
    private Long numeroAdmin;          // opcional

    @Valid
    @NotNull
    private List<felItemDto> items;

    @Valid
    @NotNull
    private felReceptorDto receptor;

    @Valid
    @NotNull
    private felTotalesDto totales;

    @Valid
    private felDatosAdicionalesDto datosAdicionales;



    // Solo para Notas de Crédito/Débito
    private String daSerie;            // Serie documento asociado
    private Long daPreimpreso;         // Número documento asociado

    // ===== Getters y Setters =====
    public Integer getTipoDoc() { return tipoDoc; }
    public void setTipoDoc(Integer tipoDoc) { this.tipoDoc = tipoDoc; }

    public String getTipoVenta() { return tipoVenta; }
    public void setTipoVenta(String tipoVenta) { this.tipoVenta = tipoVenta; }

    public Integer getDestinoVenta() { return destinoVenta; }
    public void setDestinoVenta(Integer destinoVenta) { this.destinoVenta = destinoVenta; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public Integer getMoneda() { return moneda; }
    public void setMoneda(Integer moneda) { this.moneda = moneda; }

    public BigDecimal getTasa() { return tasa; }
    public void setTasa(BigDecimal tasa) { this.tasa = tasa; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }

    public Long getNumeroAcceso() { return numeroAcceso; }
    public void setNumeroAcceso(Long numeroAcceso) { this.numeroAcceso = numeroAcceso; }

    public String getSerieAdmin() { return serieAdmin; }
    public void setSerieAdmin(String serieAdmin) { this.serieAdmin = serieAdmin; }

    public Long getNumeroAdmin() { return numeroAdmin; }
    public void setNumeroAdmin(Long numeroAdmin) { this.numeroAdmin = numeroAdmin; }

    public List<felItemDto> getItems() { return items; }
    public void setItems(List<felItemDto> items) { this.items = items; }

    public felReceptorDto getReceptor() { return receptor; }
    public void setReceptor(felReceptorDto receptor) { this.receptor = receptor; }

    public felTotalesDto getTotales() { return totales; }
    public void setTotales(felTotalesDto totales) { this.totales = totales; }

    public felDatosAdicionalesDto getDatosAdicionales() { return datosAdicionales; }
    public void setDatosAdicionales(felDatosAdicionalesDto datosAdicionales) { this.datosAdicionales = datosAdicionales; }

    public String getDaSerie() { return daSerie; }
    public void setDaSerie(String daSerie) { this.daSerie = daSerie; }

    public Long getDaPreimpreso() { return daPreimpreso; }
    public void setDaPreimpreso(Long daPreimpreso) { this.daPreimpreso = daPreimpreso; }


}
