
package BackERP.config;

import org.springframework.stereotype.Component;
import org.apache.commons.text.StringEscapeUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class FelResponseParser {

    public FelResult parse(String soapXml) {
        String payload = extractTag(soapXml, "result");
        if (payload != null) {
            payload = StringEscapeUtils.unescapeXml(payload);
        }


        String numeroAutorizacion   = findAny(payload, "NumeroAutorizacion", "UUID", "NumeroAutorizacion");
        String serie  = findAny(payload, "Serie", "SERIE");
        String preimpreso = findAny(payload, "Preimpreso", "NUMERO", "Numero");
        String nombre = findAny(payload, "Nombre");
        String direccion = findAny(payload, "Direccion");
        String telefono  = findAny(payload, "Telefono");
        String referencia = findAny(payload, "Referencia");
        String error  = findAny(payload, "ERROR", "Error", "MensajeError", "DescripcionError");
        String resultado  =  findAny(payload, "Resultado");


        FelResult res = new FelResult();
        res.setRawResponse(payload);
        res.setNumeroAutorizacion(numeroAutorizacion);
        res.setSerie(serie);
        res.setPreimpreso(preimpreso);
        if (numeroAutorizacion == null) {
            res.setError(resultado);
        }
        res.setOk(error == null && numeroAutorizacion != null);

// si quieres extender FelResult con más campos:
        res.setNombre(nombre);
        res.setDireccion(direccion);
        res.setTelefono(telefono);
        res.setReferencia(referencia);

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
