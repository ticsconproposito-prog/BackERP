
package BackERP.config;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class FelResponseParser {

    public FelResult parse(String soapXml) {
        String payload = extractTag(soapXml, "return");
        if (payload == null) payload = soapXml;

        String uuid   = findAny(payload, "NumeroAutorizacion", "UUID", "Numero_Autorizacion");
        String serie  = findAny(payload, "Serie", "SERIE");
        String numero = findAny(payload, "Preimpreso", "NUMERO", "Numero");
        String error  = findAny(payload, "ERROR", "Error", "MensajeError", "DescripcionError");

        FelResult res = new FelResult();
        res.setRawResponse(payload);
        res.setUuid(uuid);
        res.setSerie(serie);
        res.setNumero(numero);
        res.setError(error);
        res.setOk(error == null && uuid != null);
        return res;
    }

    private String extractTag(String xml, String tag) {
        Pattern p = Pattern.compile("<" + tag + "[^>]*>(.*?)</" + tag + ">", Pattern.DOTALL);
        Matcher m = p.matcher(xml);
        return m.find() ? m.group(1).trim() : null;
    }

    private String findAny(String xml, String... tags) {
        for (String t : tags) {
            String v = extractTag(xml, t);
            if (v != null && !v.isEmpty()) return v;
        }
        return null;
    }
}
