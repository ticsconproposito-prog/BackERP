package BackERP.config;


import BackERP.models.DteRequestDto;
import BackERP.models.ItemDto;
import org.springframework.stereotype.Component;

// FelXmlBuilder.java
@Component
public class FelXmlBuilder {

    public String buildDocElectronicoXml(DteRequestDto req) {
        StringBuilder sb = new StringBuilder();
        sb.append("<DocElectronico>");
        sb.append("<Encabezado>");

        // Receptor
        sb.append("<Receptor>");
        sb.append(tag("NITReceptor", req.getReceptor().getNitReceptor()));
        if (req.getReceptor().getNombre() != null) {
            sb.append(tag("Nombre", req.getReceptor().getNombre()));
        }
        if (req.getReceptor().getDireccion() != null) {
            sb.append(tag("Direccion", req.getReceptor().getDireccion()));
        }
        sb.append("</Receptor>");

        // InfoDoc
        sb.append("<InfoDoc>");
        sb.append(tag("TipoVenta", req.getTipoVenta()));              // B|S
        sb.append(tag("DestinoVenta", String.valueOf(req.getDestinoVenta()))); // 1=Guatemala
        sb.append(tag("Fecha", req.getFecha()));                      // DD/MM/AAAA
        sb.append(tag("Moneda", String.valueOf(req.getMoneda())));    // 1|2
        sb.append(tag("Tasa", req.getTasa().toPlainString()));
        sb.append(tag("Referencia", req.getReferencia()));            // obligatorio
        if (req.getNumeroAcceso() != null)   sb.append(tag("NumeroAcceso", String.valueOf(req.getNumeroAcceso())));
        if (req.getSerieAdmin() != null)     sb.append(tag("SerieAdmin", req.getSerieAdmin()));
        if (req.getNumeroAdmin() != null)    sb.append(tag("NumeroAdmin", String.valueOf(req.getNumeroAdmin())));
        sb.append("</InfoDoc>");

        // Totales
        sb.append("<Totales>");
        sb.append(tag("Bruto",      req.getTotales().getBruto().toPlainString()));
        sb.append(tag("Descuento",  req.getTotales().getDescuento().toPlainString()));
        sb.append(tag("Exento",     req.getTotales().getExento().toPlainString()));
        sb.append(tag("Otros",      req.getTotales().getOtros().toPlainString()));
        sb.append(tag("Neto",       req.getTotales().getNeto().toPlainString()));
        sb.append(tag("Isr",        req.getTotales().getIsr().toPlainString()));
        sb.append(tag("Iva",        req.getTotales().getIva().toPlainString()));
        sb.append(tag("Total",      req.getTotales().getTotal().toPlainString()));
        sb.append("</Totales>");

        // DatosAdicionales
        if (req.getDatosAdicionales() != null) {
            sb.append("<DatosAdicionales>");
            sb.append(tag("TipoReceptor", req.getDatosAdicionales().getTipoReceptor()));
            if (req.getDatosAdicionales().getEmail() != null) sb.append(tag("Email", req.getDatosAdicionales().getEmail()));
            if (req.getDatosAdicionales().getEnviar() != null) sb.append(tag("Enviar", req.getDatosAdicionales().getEnviar()));
            if (req.getDatosAdicionales().getAdicional01() != null) sb.append(tag("Adicional01", req.getDatosAdicionales().getAdicional01()));
            if (req.getDatosAdicionales().getAdicional02() != null) sb.append(tag("Adicional02", req.getDatosAdicionales().getAdicional02()));
            if (req.getDatosAdicionales().getAdicional03() != null) sb.append(tag("Adicional03", req.getDatosAdicionales().getAdicional03()));
            if (req.getDatosAdicionales().getAdicional04() != null) sb.append(tag("Adicional04", req.getDatosAdicionales().getAdicional04()));
            sb.append("</DatosAdicionales>");
        }


        sb.append("</Encabezado>");

        // Detalles
        sb.append("<Detalles>");

        for (ItemDto it : req.getItems()) {
            //sb.append("<Producto>");
            sb.append("<Productos>");
            sb.append(tag("Producto", it.getProducto()));
            sb.append(tag("Descripcion", it.getDescripcion()));
            sb.append(tag("Medida", String.valueOf(it.getMedida())));
            sb.append(tag("Cantidad", it.getCantidad().toPlainString()));
            sb.append(tag("Precio", it.getPrecio().toPlainString()));
            sb.append(tag("PorcDesc", it.getPorcDesc().toPlainString()));
            sb.append(tag("ImpBruto", it.getImpBruto().toPlainString()));
            sb.append(tag("ImpDescuento", it.getImpDescuento().toPlainString()));
            sb.append(tag("ImpExento", it.getImpExento().toPlainString()));
            sb.append(tag("ImpOtros", it.getImpOtros().toPlainString()));
            sb.append(tag("ImpNeto", it.getImpNeto().toPlainString()));
            sb.append(tag("ImpIsr", it.getImpIsr().toPlainString()));
            sb.append(tag("ImpIva", it.getImpIva().toPlainString()));
            sb.append(tag("ImpTotal", it.getImpTotal().toPlainString()));
            if (it.getDatosAdicionalesProd() != null) {
                sb.append("<DatosAdicionalesProd>");
                if (it.getDatosAdicionalesProd().getTipoVenta() != null) sb.append(tag("Tipo_Venta", it.getDatosAdicionalesProd().getTipoVenta()));
                if (it.getDatosAdicionalesProd().getAdicional01() != null) sb.append(tag("Adicional01", it.getDatosAdicionalesProd().getAdicional01()));
                sb.append("</DatosAdicionalesProd>");
            }
            sb.append("</Productos>");
            //   if (it.getTipoVentaDet() != null) sb.append(tag("TipoVentaDet", it.getTipoVentaDet()));
          //  sb.append("</Producto>");
        }


        // Documentos Asociados (solo NC/ND)
        if (req.getDaSerie() != null && req.getDaPreimpreso() != null) {
            sb.append("<DocAsociados>");
            sb.append(tag("DASerie", req.getDaSerie()));
            sb.append(tag("DAPreimpreso", String.valueOf(req.getDaPreimpreso())));
            sb.append("</DocAsociados>");
        }

        sb.append("</Detalles>");

        sb.append("</DocElectronico>");
        return sb.toString();
    }

    private String tag(String name, String value) {
        return "<" + name + ">" + escape(value) + "</" + name + ">";
    }
    private String escape(String value) {
        return value.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;")
                .replace("\"","&quot;").replace("'","&apos;");
    }
}
