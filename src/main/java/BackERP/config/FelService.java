
package BackERP.config;

import BackERP.models.DteRequestDto;
import BackERP.models.FelProperties;
import BackERP.models.ItemDto;

import BackERP.models.erpEncabezadoFacturas;
import BackERP.repository.RepositoryEncabezadoFacturas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Service
public class FelService {

    private static final BigDecimal IVA_RATE = new BigDecimal("0.12");
    private static final BigDecimal TOL = new BigDecimal("0.01");

    private final FelProperties props;
    private final FelXmlBuilder xmlBuilder;
    private final FelWsClient wsClient;
    private final FelResponseParser responseParser;


    @Autowired 
    private RepositoryEncabezadoFacturas repEncFac;
    @Autowired
    public FelService(FelProperties props,
                      FelXmlBuilder xmlBuilder,
                      FelWsClient wsClient,
                      FelResponseParser responseParser) {
        this.props = props;
        this.xmlBuilder = xmlBuilder;
        this.wsClient = wsClient;
        this.responseParser = responseParser;
    }
    public FelResult generarDte(DteRequestDto req) {
        validar(req);

        String pXml = xmlBuilder.buildDocElectronicoXml(req);
        String soapResponse = wsClient.generaDocumento(req.getTipoDoc(), pXml);
        FelResult result = responseParser.parse(soapResponse);

        if (result.isOk()) {
            try {
                System.out.println("Ingresa insertar encabezadoFactura");

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
                }
            } catch (Exception e) {
                // Captura cualquier otro error inesperado
                result.setError("Error al procesar encabezado de factura: " + e.getMessage());
            }
        }

        return result;
    }


    private void validar(DteRequestDto req) {
        // Validaciones por línea
        for (ItemDto it : req.getItems()) {
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
        assertClose("Bruto",     req.getTotales().getBruto(),     req.getItems().stream().map(ItemDto::getImpBruto).reduce(BigDecimal.ZERO, BigDecimal::add));
        assertClose("Descuento", req.getTotales().getDescuento(), req.getItems().stream().map(ItemDto::getImpDescuento).reduce(BigDecimal.ZERO, BigDecimal::add));
        assertClose("Exento",    req.getTotales().getExento(),    req.getItems().stream().map(ItemDto::getImpExento).reduce(BigDecimal.ZERO, BigDecimal::add));
        assertClose("Otros",     req.getTotales().getOtros(),     req.getItems().stream().map(ItemDto::getImpOtros).reduce(BigDecimal.ZERO, BigDecimal::add));
        assertClose("Neto",      req.getTotales().getNeto(),      req.getItems().stream().map(ItemDto::getImpNeto).reduce(BigDecimal.ZERO, BigDecimal::add));
        assertClose("Iva",       req.getTotales().getIva(),       req.getItems().stream().map(ItemDto::getImpIva).reduce(BigDecimal.ZERO, BigDecimal::add));
        assertClose("Isr",       req.getTotales().getIsr(),       req.getItems().stream().map(ItemDto::getImpIsr).reduce(BigDecimal.ZERO, BigDecimal::add));
        assertClose("Total",     req.getTotales().getTotal(),     req.getItems().stream().map(ItemDto::getImpTotal).reduce(BigDecimal.ZERO, BigDecimal::add));

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
