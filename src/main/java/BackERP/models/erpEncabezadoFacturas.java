package BackERP.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class erpEncabezadoFacturas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long idEncabezadoFactura;
    @Column
    private int tipoDocumento;
    @ManyToOne
    @JoinColumn(name = "idCliente", referencedColumnName = "idCliente")
    private erpclientes idCliente ;
    @Column
    private String tipoVenta ;
    @Column
    private int destinoVenta ;
    @Column
    private LocalDate FechaFactura ;
    @Column
    private int moneda ;
    @Column
    private double tasaDeCambio;
    @Column
    private String referencia ;
    @Column
    private int numeroAcceso ;
    @Column
    private String serieAdmin ;
    @Column
    private int numeroAdmin ;
    @Column
    private String reversion ;
    @Column
    private double totalBruto ;
    @Column
    private double porcentajeDeDescuento;
    @Column
    private double cantidadDeDescuento ;
    @Column
    private double exento ;
    @Column
    private double otro;
    @Column
    private double totalNeto;
    @Column
    private double isr;
    @Column
    private double iva;
    @Column
    private double total;
    @Column
    private String facturaProcesada;
    @Column
    private String direccionEntrega;
    @Column
    private int tipoReceptor;
    @Column
    private String enviarCorreo;
    @Column
    private String serieResAPI;
    @Column
    private int preimpresoResAPI;
    @Column
    private String nombreResAPI;
    @Column
    private String direccionResAPI;
    @Column
    private String telefonoResAPI;
    @Column
    private String numeroAutorizacionResAPI;
    @Column
    private String referenciaResAPI;
    @Column
    private String respuestaXML;
    @Column
    private int  estado;
    @Column
    private LocalDate fechaModificacion;
    @Column
    private LocalTime horaModificacion;
    @Column
    private int idUsuarioModificacion;

    public erpEncabezadoFacturas() {
    }

    public Long getIdEncabezadoFactura() {
        return idEncabezadoFactura;
    }

    public void setIdEncabezadoFactura(Long idEncabezadoFactura) {
        this.idEncabezadoFactura = idEncabezadoFactura;
    }

    public int getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(int tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public erpclientes getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(erpclientes idCliente) {
        this.idCliente = idCliente;
    }

    public String getTipoVenta() {
        return tipoVenta;
    }

    public void setTipoVenta(String tipoVenta) {
        this.tipoVenta = tipoVenta;
    }

    public int getDestinoVenta() {
        return destinoVenta;
    }

    public void setDestinoVenta(int destinoVenta) {
        this.destinoVenta = destinoVenta;
    }

    public LocalDate getFechaFactura() {
        return FechaFactura;
    }

    public void setFechaFactura(LocalDate fechaFactura) {
        FechaFactura = fechaFactura;
    }

    public int getMoneda() {
        return moneda;
    }

    public void setMoneda(int moneda) {
        this.moneda = moneda;
    }

    public double getTasaDeCambio() {
        return tasaDeCambio;
    }

    public void setTasaDeCambio(double tasaDeCambio) {
        this.tasaDeCambio = tasaDeCambio;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public int getNumeroAcceso() {
        return numeroAcceso;
    }

    public void setNumeroAcceso(int numeroAcceso) {
        this.numeroAcceso = numeroAcceso;
    }

    public String getSerieAdmin() {
        return serieAdmin;
    }

    public void setSerieAdmin(String serieAdmin) {
        this.serieAdmin = serieAdmin;
    }

    public int getNumeroAdmin() {
        return numeroAdmin;
    }

    public void setNumeroAdmin(int numeroAdmin) {
        this.numeroAdmin = numeroAdmin;
    }

    public String getReversion() {
        return reversion;
    }

    public void setReversion(String reversion) {
        this.reversion = reversion;
    }

    public double getTotalBruto() {
        return totalBruto;
    }

    public void setTotalBruto(double totalBruto) {
        this.totalBruto = totalBruto;
    }

    public double getPorcentajeDeDescuento() {
        return porcentajeDeDescuento;
    }

    public void setPorcentajeDeDescuento(double porcentajeDeDescuento) {
        this.porcentajeDeDescuento = porcentajeDeDescuento;
    }

    public double getCantidadDeDescuento() {
        return cantidadDeDescuento;
    }

    public void setCantidadDeDescuento(double cantidadDeDescuento) {
        this.cantidadDeDescuento = cantidadDeDescuento;
    }

    public double getExento() {
        return exento;
    }

    public void setExento(double exento) {
        this.exento = exento;
    }

    public double getOtro() {
        return otro;
    }

    public void setOtro(double otro) {
        this.otro = otro;
    }

    public double getTotalNeto() {
        return totalNeto;
    }

    public void setTotalNeto(double totalNeto) {
        this.totalNeto = totalNeto;
    }

    public double getIsr() {
        return isr;
    }

    public void setIsr(double isr) {
        this.isr = isr;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getFacturaProcesada() {
        return facturaProcesada;
    }

    public void setFacturaProcesada(String facturaProcesada) {
        this.facturaProcesada = facturaProcesada;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public int getTipoReceptor() {
        return tipoReceptor;
    }

    public void setTipoReceptor(int tipoReceptor) {
        this.tipoReceptor = tipoReceptor;
    }

    public String getEnviarCorreo() {
        return enviarCorreo;
    }

    public void setEnviarCorreo(String enviarCorreo) {
        this.enviarCorreo = enviarCorreo;
    }

    public String getSerieResAPI() {
        return serieResAPI;
    }

    public void setSerieResAPI(String serieResAPI) {
        this.serieResAPI = serieResAPI;
    }

    public int getPreimpresoResAPI() {
        return preimpresoResAPI;
    }

    public void setPreimpresoResAPI(int preimpresoResAPI) {
        this.preimpresoResAPI = preimpresoResAPI;
    }

    public String getNombreResAPI() {
        return nombreResAPI;
    }

    public void setNombreResAPI(String nombreResAPI) {
        this.nombreResAPI = nombreResAPI;
    }

    public String getDireccionResAPI() {
        return direccionResAPI;
    }

    public void setDireccionResAPI(String direccionResAPI) {
        this.direccionResAPI = direccionResAPI;
    }

    public String getTelefonoResAPI() {
        return telefonoResAPI;
    }

    public void setTelefonoResAPI(String telefonoResAPI) {
        this.telefonoResAPI = telefonoResAPI;
    }

    public String getNumeroAutorizacionResAPI() {
        return numeroAutorizacionResAPI;
    }

    public void setNumeroAutorizacionResAPI(String numeroAutorizacionResAPI) {
        this.numeroAutorizacionResAPI = numeroAutorizacionResAPI;
    }

    public String getReferenciaResAPI() {
        return referenciaResAPI;
    }

    public void setReferenciaResAPI(String referenciaResAPI) {
        this.referenciaResAPI = referenciaResAPI;
    }

    public String getRespuestaXML() {
        return respuestaXML;
    }

    public void setRespuestaXML(String respuestaXML) {
        this.respuestaXML = respuestaXML;
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
