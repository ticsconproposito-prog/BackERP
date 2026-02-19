
package BackERP.config;

import BackERP.models.FelProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class FelWsClient {

    private final FelProperties props;
    private final WebClient webClient;

    @Autowired
    public FelWsClient(FelProperties props) {
        this.props = props;
        this.webClient = WebClient.builder().build();
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

        return webClient.post()
                .uri(props.getEndpoint())
                .contentType(MediaType.TEXT_XML)
                .header(HttpHeaders.AUTHORIZATION, basic(props.getUsuario(), props.getPassword()))
                .bodyValue(soap)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    private String buildSoapEnvelope(String pUsuario, String pPassword, String pNitEmisor,
                                     int pEstablecimiento, int pTipoDoc, String pIdMaquina,
                                     String pTipoRespuesta, String pXml) {
        return ""
                + "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">"
                + "  <soapenv:Header/>"
                + "  <soapenv:Body>"
                + "    <generaDocumento>"
                + tag("pUsuario", pUsuario)
                + tag("pPassword", pPassword)
                + tag("pNitEmisor", pNitEmisor)
                + tag("pEstablecimiento", String.valueOf(pEstablecimiento))
                + tag("pTipoDoc", String.valueOf(pTipoDoc))
                + tag("pIdMaquina", pIdMaquina)
                + tag("pTipoRespuesta", pTipoRespuesta)
                + tag("pXml", "<![CDATA[" + pXml + "]]>")
                + "    </generaDocumento>"
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
