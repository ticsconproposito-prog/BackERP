
package BackERP.config;

import BackERP.helper.erpDetalleFacturaSpecs;
import BackERP.models.*;

import BackERP.repository.RepositoryDetalleFacturas;
import BackERP.repository.RepositoryEncabezadoFacturas;
import BackERP.repository.RepositoryInventario;
import BackERP.repository.RepositoryMovimientosProductos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class FelService {

    private static final BigDecimal IVA_RATE = new BigDecimal("0.12");
    private static final BigDecimal TOL = new BigDecimal("0.01");

    private final felProperties props;
    private final FelXmlBuilder xmlBuilder;
    private final FelWsClient wsClient;
    private final FelResponseParser responseParser;

    @Autowired
    private RepositoryDetalleFacturas repDetFac;

    @Autowired
    private RepositoryInventario repInv;

    @Autowired 
    private RepositoryEncabezadoFacturas repEncFac;

    @Autowired
    private RepositoryMovimientosProductos repMovPro;
    @Autowired
    public FelService(felProperties props,
                      FelXmlBuilder xmlBuilder,
                      FelWsClient wsClient,
                      FelResponseParser responseParser) {
        this.props = props;
        this.xmlBuilder = xmlBuilder;
        this.wsClient = wsClient;
        this.responseParser = responseParser;
    }
    public FelResult generarDte(felDteRequestDto req) {
        validar(req);

        String pXml = xmlBuilder.buildDocElectronicoXml(req);
        String soapResponse = wsClient.generaDocumento(req.getTipoDoc(), pXml);
        FelResult result = responseParser.parse(soapResponse);

        if (result.isOk()) {
            try {
                String SID = req.getReferencia().substring(4);
                long ID = Long.parseLong(SID);

                Optional<erpEncabezadoFacturas> optEnc = repEncFac.findById(ID);
                if (optEnc.isPresent()) {
                    erpEncabezadoFacturas enc = optEnc.get();
                    enc.setSerieResAPI(result.getSerie());
                    enc.setPreimpresoResAPI(Long.parseLong(result.getPreimpreso()));
                    enc.setNumeroAutorizacionResAPI(result.getNumeroAutorizacion());
                    enc.setRespuestaXML(result.getRawResponse());
                    enc.setReferencia(req.getReferencia());
                    enc.setFacturaProcesada("S");
                    enc.setNombreResAPI(result.getNombre());
                    enc.setDireccionResAPI(result.getDireccion());
                    enc.setTelefonoResAPI(result.getTelefono());
                    enc.setReferenciaResAPI(result.getReferencia());

                    repEncFac.save(enc);
                } else {
                    // Registrar el error en el campo error del resultado
                    result.setError("No se encontró encabezado de factura con ID " + ID);
                    result.setOk(false);

                }
            } catch (Exception e) {
                // Captura cualquier otro error inesperado
                result.setError("Error al procesar encabezado de factura: " + e.getMessage());
                result.setOk(false);
            }
        }else {

            try {


                String SID = req.getReferencia().substring(4);
                long ID = Long.parseLong(SID);

                Optional<erpEncabezadoFacturas> optEnc = repEncFac.findById(ID);
                if (optEnc.isPresent()) {

                    erpEncabezadoFacturas enc = optEnc.get();
                    if ("2-NO EXISTE EL NIT/CUI DEL CONTRIBUYENTE".equals(result.getError())) {
                        enc.setEstado(0);

                        // Consultar los detalles de la factura
                        List<erpDetalleFacturas> detalles = repDetFac.findAll(
                                erpDetalleFacturaSpecs.idEncabezadoFacturaContains(Math.toIntExact(enc.getIdEncabezadoFactura()))
                        );

                        for (erpDetalleFacturas det : detalles) {
                            // Buscar inventario solo por producto
                            erpInventario inventario = repInv.findByIdProducto_IdProducto(Long.valueOf(det.getIdProducto()));

                            if (inventario != null) {
                                // Sumar de regreso la cantidad
                                inventario.setCantidadExistencias(
                                        inventario.getCantidadExistencias() + det.getCantidad()
                                );
                                inventario.setFechaModificacion(LocalDate.now());
                                inventario.setHoraModificacion(LocalTime.now());
                                inventario.setIdUsuarioModificacion(enc.getIdUsuarioModificacion());
                                repInv.save(inventario);
                            } else {
                                // Crear inventario nuevo si no existía
                                erpInventario nuevo = new erpInventario();
                                erpProductos prod = new erpProductos();
                                prod.setIdProducto((long) det.getIdProducto());

                                nuevo.setIdProducto(prod);
                                nuevo.setCantidadExistencias(det.getCantidad());
                                nuevo.setCantidadDanados(0);
                                nuevo.setEstado(1);
                                nuevo.setFechaModificacion(LocalDate.now());
                                nuevo.setHoraModificacion(LocalTime.now());
                                nuevo.setIdUsuarioModificacion(enc.getIdUsuarioModificacion());
                                repInv.save(nuevo);
                            }
                        }

                        // 🔎 Marcar movimientos como inactivos
                        List<erpMovimientosProductos> movimientos = repMovPro.findByIdOrdenProducto((long) enc.getIdEncabezadoFactura());
                        for (erpMovimientosProductos mov : movimientos) {
                            mov.setEstado(0);
                            mov.setFechaModificacion(LocalDate.now());
                            mov.setHoraModificacion(LocalTime.now());
                            mov.setIdUsuarioModificacion(enc.getIdUsuarioModificacion());
                            repMovPro.save(mov);
                        }
                    }



                    enc.setRespuestaXML(result.getRawResponse());
                    enc.setReferencia(req.getReferencia());
                    enc.setFacturaProcesada("N");
                    repEncFac.save(enc);




                } else {
                    // Registrar el error en el campo error del resultado
                    result.setError("No se encontró encabezado de factura con ID " + ID);
                    result.setOk(false);
                }
            } catch (Exception e) {
                // Captura cualquier otro error inesperado
                result.setError("Error al procesar encabezado de factura: " + e.getMessage());
                result.setOk(false);
            }

        }

        return result;
    }

    public FelResult anularFactura(String idFacturaEncabezado, String motivo) {
        String soapResponse = wsClient.anulaDocumento(idFacturaEncabezado, motivo);
        FelResult result = responseParser.parse(soapResponse);

        if (result.isOk()) {
            try {
                Optional<erpEncabezadoFacturas> optEnc = repEncFac.findByNumeroAutorizacionResAPI(idFacturaEncabezado);
                if (optEnc.isPresent()) {
                    erpEncabezadoFacturas enc = optEnc.get();
                    enc.setFacturaProcesada("A"); // A = Anulada
                    enc.setRespuestaXML(result.getRawResponse());
                    repEncFac.save(enc);
                }
            } catch (Exception e) {
                result.setError("Error al actualizar estado de factura: " + e.getMessage());
                result.setOk(false);

            }
        }

        return result;
    }



    private void validar(felDteRequestDto req) {
        // Validaciones por línea
        for (felItemDto it : req.getItems()) {
            // IVA = 12% del neto (±0.01)
            BigDecimal ivaCalc = it.getImpNeto().multiply(IVA_RATE).setScale(2, RoundingMode.HALF_UP);
            if (it.getImpIva().subtract(ivaCalc).abs().compareTo(TOL) > 0) {
                throw new IllegalArgumentException("IVA de la línea no respeta 12% ±0.01");
            }
            // ImpBruto = Cantidad * Precio (±0.01)
            BigDecimal brutoCalc = it.getCantidad().multiply(it.getPrecio()).setScale(2, RoundingMode.HALF_UP);
            if (it.getImpBruto().subtract(brutoCalc).abs().compareTo(TOL) > 0) {
                throw new IllegalArgumentException("ImpBruto ≠ Cantidad*Precio (±0.01)");
            }
        }

        // Totales vs sumatoria de líneas (±0.01 por campo)
        assertClose("Bruto",     req.getTotales().getBruto(),     req.getItems().stream().map(felItemDto::getImpBruto).reduce(BigDecimal.ZERO, BigDecimal::add));
        assertClose("Descuento", req.getTotales().getDescuento(), req.getItems().stream().map(felItemDto::getImpDescuento).reduce(BigDecimal.ZERO, BigDecimal::add));
        assertClose("Exento",    req.getTotales().getExento(),    req.getItems().stream().map(felItemDto::getImpExento).reduce(BigDecimal.ZERO, BigDecimal::add));
        assertClose("Otros",     req.getTotales().getOtros(),     req.getItems().stream().map(felItemDto::getImpOtros).reduce(BigDecimal.ZERO, BigDecimal::add));
        assertClose("Neto",      req.getTotales().getNeto(),      req.getItems().stream().map(felItemDto::getImpNeto).reduce(BigDecimal.ZERO, BigDecimal::add));
        assertClose("Iva",       req.getTotales().getIva(),       req.getItems().stream().map(felItemDto::getImpIva).reduce(BigDecimal.ZERO, BigDecimal::add));
        assertClose("Isr",       req.getTotales().getIsr(),       req.getItems().stream().map(felItemDto::getImpIsr).reduce(BigDecimal.ZERO, BigDecimal::add));
        assertClose("Total",     req.getTotales().getTotal(),     req.getItems().stream().map(felItemDto::getImpTotal).reduce(BigDecimal.ZERO, BigDecimal::add));

        // Moneda/Tasa
        if (req.getMoneda() == 1 && req.getTasa().compareTo(BigDecimal.ONE) != 0) {
            throw new IllegalArgumentException("Para Moneda=1 (GTQ) la Tasa debe ser 1.000000");
        }
    }

    private void assertClose(String name, BigDecimal expected, BigDecimal actual) {
        if (expected.subtract(actual).abs().compareTo(TOL) > 0) {
            throw new IllegalArgumentException("Total " + name + " no cuadra (±0.01).");
        }
    }
}
