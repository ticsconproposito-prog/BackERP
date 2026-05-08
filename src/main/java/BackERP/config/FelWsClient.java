package BackERP.config;

import BackERP.models.felProperties;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

@Component
public class FelWsClient {

  private static final int CONNECTION_TIMEOUT = 10000;  // 10 segundos
  private static final int READ_TIMEOUT = 30000;       // 30 segundos
  private static final int WRITE_TIMEOUT = 15000;      // 15 segundos

  private final felProperties props;
  private final WebClient webClient;

  @Autowired
  public FelWsClient(felProperties props) {
    this.props = props;

    HttpClient httpClient = HttpClient.create()
      .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECTION_TIMEOUT)
      .doOnConnected(conn -> conn
        .addHandlerLast(new ReadTimeoutHandler(READ_TIMEOUT, TimeUnit.MILLISECONDS))
        .addHandlerLast(new WriteTimeoutHandler(WRITE_TIMEOUT, TimeUnit.MILLISECONDS)))
      .responseTimeout(Duration.ofMillis(READ_TIMEOUT));

    this.webClient = WebClient.builder()
      .clientConnector(new ReactorClientHttpConnector(httpClient))
      .build();
  }

  public String generaDocumento(int tipoDoc, String pXml) {
    String soap = buildSoapEnvelope(
      props.getUsuario(),
      props.getPassword(),
      props.getNitEmisor(),
      props.getEstablecimiento(),
      tipoDoc,
      props.getIdMaquina(),
      props.getTipoRespuesta(),
      pXml
    );

    try {
      return webClient.post()
        .uri(props.getEndpoint())
        .contentType(MediaType.TEXT_XML)
        .header(HttpHeaders.AUTHORIZATION, basic(props.getBasicUser(), props.getBasicPass()))
        .bodyValue(soap)
        .retrieve()
        .bodyToMono(String.class)
        .block(Duration.ofMillis(READ_TIMEOUT));
    } catch (Exception e) {
      return "<result><Error>Error de conexión FEL al generar documento: " + e.getMessage() + "</Error></result>";
    }
  }

  public String anulaDocumento(String serie, String preimpreso, String nitComprador,
                               String fechaAnulacion, String motivo) {
    String soap = ""
      + "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:guat=\"http://dbguatefac/Guatefac.wsdl\">"
      + "  <soapenv:Header/>"
      + "  <soapenv:Body>"
      + "    <guat:anulaDocumento>"
      + tag("pUsuario", props.getUsuario())
      + tag("pPassword", props.getPassword())
      + tag("pNitEmisor", props.getNitEmisor())
      + tag("pSerie", serie)
      + tag("pPreimpreso", preimpreso)
      + tag("pNitComprador", nitComprador)
      + tag("pFechaAnulacion", fechaAnulacion)
      + tag("pMotivoAnulacion", motivo)
      + "    </guat:anulaDocumento>"
      + "  </soapenv:Body>"
      + "</soapenv:Envelope>";

    try {
      return webClient.post()
        .uri(props.getEndpoint())
        .contentType(MediaType.TEXT_XML)
        .header(HttpHeaders.AUTHORIZATION, basic(props.getBasicUser(), props.getBasicPass()))
        .bodyValue(soap)
        .retrieve()
        .bodyToMono(String.class)
        .block(Duration.ofMillis(READ_TIMEOUT));
    } catch (Exception e) {
      return "<result><Error>Error de conexión FEL al anular: " + e.getMessage() + "</Error></result>";
    }
  }

  private String buildSoapEnvelope(String pUsuario, String pPassword, String pNitEmisor,
                                   int pEstablecimiento, int pTipoDoc, String pIdMaquina,
                                   String pTipoRespuesta, String pXml) {
    return ""
      + "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:guat=\"http://dbguatefac/Guatefac.wsdl\">"
      + "  <soapenv:Header/>"
      + "  <soapenv:Body>"
      + "    <guat:generaDocumento>"
      + tag("pUsuario", pUsuario)
      + tag("pPassword", pPassword)
      + tag("pNitEmisor", pNitEmisor)
      + tag("pEstablecimiento", String.valueOf(pEstablecimiento))
      + tag("pTipoDoc", String.valueOf(pTipoDoc))
      + tag("pIdMaquina", pIdMaquina)
      + tag("pTipoRespuesta", pTipoRespuesta)
      + tag("pXml", "<![CDATA[" + pXml + "]]>")
      + "    </guat:generaDocumento>"
      + "  </soapenv:Body>"
      + "</soapenv:Envelope>";
  }

  private String tag(String name, String value) {
    return "<" + name + ">" + value + "</" + name + ">";
  }

  private String basic(String u, String p) {
    String token = u + ":" + p;
    return "Basic " + Base64.getEncoder().encodeToString(token.getBytes(StandardCharsets.UTF_8));
  }
}
