
package BackERP.config;

import org.springframework.stereotype.Component;
import org.apache.commons.text.StringEscapeUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class FelResponseParser {

  public FelResult parse(String soapXml) {
    String payload = extractTag(soapXml, "result");
    if (payload == null) {
      payload = soapXml; // si no viene envuelto en <result>, usar el XML completo
    } else {
      payload = StringEscapeUtils.unescapeXml(payload);
    }

    String numeroAutorizacion = findAny(payload, "NumeroAutorizacion", "UUID");
    String serie = findAny(payload, "Serie", "SERIE");
    String preimpreso = findAny(payload, "Preimpreso", "PREIMPRESO", "NUMERO");
    String nombre = findAny(payload, "Nombre", "COMPRADOR");
    String direccion = findAny(payload, "Direccion");
    String telefono = findAny(payload, "Telefono");
    String referencia = findAny(payload, "Referencia");
    String estado = findAny(payload, "ESTADO");
    String error = findAny(payload, "ERROR", "Error", "MensajeError", "DescripcionError");

    FelResult res = new FelResult();
    res.setRawResponse(payload);
    res.setNumeroAutorizacion(numeroAutorizacion);
    res.setSerie(serie);
    res.setPreimpreso(preimpreso);
    res.setNombre(nombre);
    res.setDireccion(direccion);
    res.setTelefono(telefono);
    res.setReferencia(referencia);

    if ("ANULADO".equalsIgnoreCase(estado) && error == null) {
      res.setOk(true);
    } else {
      res.setError(error != null ? error : estado);
      res.setOk(false);
    }

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
