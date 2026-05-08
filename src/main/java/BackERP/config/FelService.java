package BackERP.config;

import BackERP.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.CompletableFuture;

@Service
public class FelService {

  private static final BigDecimal IVA_RATE = new BigDecimal("0.12");
  private static final BigDecimal TOL = new BigDecimal("0.01");

  private final felProperties props;
  private final FelXmlBuilder xmlBuilder;
  private final FelWsClient wsClient;
  private final FelResponseParser responseParser;

  @Autowired
  private FelPersistenceService felPersistenceService;

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

  // Usa el Executor específico para evitar fuga de hilos
  @Async("felTaskExecutor")
  public CompletableFuture<FelResult> generarDteAsync(felDteRequestDto req) {
    FelResult result = generarDte(req);
    return CompletableFuture.completedFuture(result);
  }

  @Async("felTaskExecutor")
  public CompletableFuture<FelResult> anularFacturaAsync(Long idEncabezadoFactura,
                                                         String serie,
                                                         String preimpreso,
                                                         String nitComprador,
                                                         String fechaAnulacion,
                                                         String motivo) {
    FelResult result = anularFactura(idEncabezadoFactura, serie, preimpreso,
      nitComprador, fechaAnulacion, motivo);
    return CompletableFuture.completedFuture(result);
  }

  public FelResult generarDte(felDteRequestDto req) {
    validar(req);

    String pXml = xmlBuilder.buildDocElectronicoXml(req);
    String soapResponse = wsClient.generaDocumento(req.getTipoDoc(), pXml);
    FelResult result = responseParser.parse(soapResponse);

    try {
      felPersistenceService.persistirResultadoDte(result, req);
    } catch (Exception e) {
      result.setError("Error al persistir resultado DTE: " + e.getMessage());
      result.setOk(false);
    }

    return result;
  }

  public FelResult anularFactura(Long idEncabezadoFactura,
                                 String serie,
                                 String preimpreso,
                                 String nitComprador,
                                 String fechaAnulacion,
                                 String motivo) {
    String soapResponse = wsClient.anulaDocumento(serie, preimpreso, nitComprador, fechaAnulacion, motivo);
    FelResult result = responseParser.parseAnular(soapResponse);

    try {
      felPersistenceService.persistirResultadoAnulacion(result, idEncabezadoFactura);
    } catch (Exception e) {
      result.setError("Error al actualizar estado de factura: " + e.getMessage());
      result.setOk(false);
    }

    return result;
  }

  private void validar(felDteRequestDto req) {
    for (felItemDto it : req.getItems()) {
      BigDecimal ivaCalc = it.getImpNeto().multiply(IVA_RATE).setScale(2, RoundingMode.HALF_UP);
      if (it.getImpIva().subtract(ivaCalc).abs().compareTo(TOL) > 0) {
        throw new IllegalArgumentException("IVA de la línea no respeta 12% ±0.01");
      }
      BigDecimal brutoCalc = it.getCantidad().multiply(it.getPrecio()).setScale(2, RoundingMode.HALF_UP);
      if (it.getImpBruto().subtract(brutoCalc).abs().compareTo(TOL) > 0) {
        throw new IllegalArgumentException("ImpBruto ≠ Cantidad*Precio (±0.01)");
      }
    }

    assertClose("Bruto", req.getTotales().getBruto(), req.getItems().stream().map(felItemDto::getImpBruto).reduce(BigDecimal.ZERO, BigDecimal::add));
    assertClose("Descuento", req.getTotales().getDescuento(), req.getItems().stream().map(felItemDto::getImpDescuento).reduce(BigDecimal.ZERO, BigDecimal::add));
    assertClose("Exento", req.getTotales().getExento(), req.getItems().stream().map(felItemDto::getImpExento).reduce(BigDecimal.ZERO, BigDecimal::add));
    assertClose("Otros", req.getTotales().getOtros(), req.getItems().stream().map(felItemDto::getImpOtros).reduce(BigDecimal.ZERO, BigDecimal::add));
    assertClose("Neto", req.getTotales().getNeto(), req.getItems().stream().map(felItemDto::getImpNeto).reduce(BigDecimal.ZERO, BigDecimal::add));
    assertClose("Iva", req.getTotales().getIva(), req.getItems().stream().map(felItemDto::getImpIva).reduce(BigDecimal.ZERO, BigDecimal::add));
    assertClose("Isr", req.getTotales().getIsr(), req.getItems().stream().map(felItemDto::getImpIsr).reduce(BigDecimal.ZERO, BigDecimal::add));
    assertClose("Total", req.getTotales().getTotal(), req.getItems().stream().map(felItemDto::getImpTotal).reduce(BigDecimal.ZERO, BigDecimal::add));

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
