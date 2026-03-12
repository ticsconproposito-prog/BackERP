
package BackERP.config;

import BackERP.models.felProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class FelWsClient {

    private final felProperties props;
    private final WebClient webClient;

    @Autowired
    public FelWsClient(felProperties props) {
        this.props = props;
        this.webClient = WebClient.builder().build();
    }

    public String generaDocumento(int tipoDoc, String pXml) {
        // Construir el envelope SOAP con las credenciales de consumo WS
        String soap = buildSoapEnvelope(
                props.getUsuario(),        // Usuario WS FEL
                props.getPassword(),       // Password WS FEL
                props.getNitEmisor(),
                props.getEstablecimiento(),
                tipoDoc,
                props.getIdMaquina(),
                props.getTipoRespuesta(),
                pXml
        );

        // imprimir XML previo al envio
        System.out.println("XML a enviar:\n" + soap
        //        +"end point"+ props.getEndpoint()
        //        + "usuario "+props.getBasicUser()
        //        +"contrasena "+ props.getBasicPass()
        );
 /* imprimir errores consumo WSDL
        try { String response =    webClient.post()
                .uri(props.getEndpoint())
                .contentType(MediaType.TEXT_XML)
                .header(HttpHeaders.AUTHORIZATION, basic(props.getBasicUser(), props.getBasicPass()))
                .bodyValue(soap)
                .retrieve()
                .bodyToMono(String.class)
                .block();
            System.out.println(response);
        }
        catch (WebClientResponseException e) {
            System.err.println("Status: " + e.getStatusCode());
            System.err.println("Response body: " + e.getResponseBodyAsString()); }
*/

        try {
            // Enviar la petición con Basic Auth (usr_guatefac / usrguatefac)
            return webClient.post()
                    .uri(props.getEndpoint())
                    .contentType(MediaType.TEXT_XML)
                    .header(HttpHeaders.AUTHORIZATION, basic(props.getBasicUser(), props.getBasicPass()))
                    .bodyValue(soap)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            // Retornar un XML simulado con el error para que el parser lo capture
            return "<result><Error>Error de conexión FEL al generar documento: " + e.getMessage() + "</Error></result>";
        }

    }

    public String anulaDocumento(String uuid, String motivo) {
        String soap = ""
                + "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:guat=\"http://dbguatefac/Guatefac.wsdl\">"
                + "  <soapenv:Header/>"
                + "  <soapenv:Body>"
                + "    <guat:anulaDocumento>"
                + tag("pUsuario", props.getUsuario())
                + tag("pPassword", props.getPassword())
                + tag("pNitEmisor", props.getNitEmisor())
                + tag("pUUID", uuid)
                + tag("pMotivo", motivo)
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
                    .block();
        } catch (Exception e) {
            // Retornar un XML simulado con el error para que el parser lo capture
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