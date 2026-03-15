package BackERP.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "erpEncabezadoFacturas", schema = "erpConfig")
public class erpEncabezadoFacturas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idEncabezadoFactura")
    private Long idEncabezadoFactura;

    @Column(name = "tipoDocumento")
    private int tipoDocumento;

    @ManyToOne
    @JoinColumn(name = "idCliente", referencedColumnName = "idCliente")
    private erpClientes idCliente;

    @Column(name = "tipoVenta", length = 10)
    private String tipoVenta;

    @Column(name = "destinoVenta")
    private int destinoVenta;

    @Column(name = "FechaFactura")
    private LocalDate FechaFactura;

    @Column(name = "moneda")
    private int moneda;

    @Column(name = "tasaDeCambio")
    private double tasaDeCambio;

    @Column(name = "referencia", length = 40)
    private String referencia;

    @Column(name = "numeroAcceso")
    private int numeroAcceso;

    @Column(name = "serieAdmin", length = 20)
    private String serieAdmin;

    @Column(name = "numeroAdmin")
    private int numeroAdmin;

    @Column(name = "reversion", length = 1)
    private String reversion;

    @Column(name = "totalBruto")
    private double totalBruto;

    @Column(name = "porcentajeDeDescuento")
    private double porcentajeDeDescuento;

    @Column(name = "cantidadDeDescuento")
    private double cantidadDeDescuento;

    @Column(name = "exento")
    private double exento;

    @Column(name = "otro")
    private double otro;

    @Column(name = "totalNeto")
    private double totalNeto;

    @Column(name = "isr")
    private double isr;

    @Column(name = "iva")
    private double iva;

    @Column(name = "total")
    private double total;

    @Column(name = "facturaProcesada", length = 2)
    private String facturaProcesada;

    @Column(name = "direccionEntrega", length = 2000)
    private String direccionEntrega;

    @Column(name = "tipoReceptor")
    private int tipoReceptor;

    @Column(name = "enviarCorreo", length = 1)
    private String enviarCorreo;

    @Column(name = "serieResAPI", length = 50)
    private String serieResAPI;

    @Column(name = "preimpresoResAPI")
    private long preimpresoResAPI;

    @Column(name = "nombreResAPI", length = 200)
    private String nombreResAPI;

    @Column(name = "direccionResAPI", length = 500)
    private String direccionResAPI;

    @Column(name = "telefonoResAPI", length = 20)
    private String telefonoResAPI;

    @Column(name = "numeroAutorizacionResAPI", length = 50)
    private String numeroAutorizacionResAPI;

    @Column(name = "referenciaResAPI", length = 40)
    private String referenciaResAPI;

    @Column(name = "respuestaXML", length = 4000)
    private String respuestaXML;

    @Column(name = "estado")
    private int estado;

    @Column(name = "fechaModificacion")
    private LocalDate fechaModificacion;

    @Column(name = "horaModificacion")
    private LocalTime horaModificacion;

    @Column(name = "idUsuarioModificacion")
    private int idUsuarioModificacion;

    public erpEncabezadoFacturas() {}
    // getters y setters...

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

    public erpClientes getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(erpClientes idCliente) {
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

    public long  getPreimpresoResAPI() {
        return preimpresoResAPI;
    }

    public void setPreimpresoResAPI(long preimpresoResAPI) {
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
