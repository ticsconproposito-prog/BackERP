package BackERP.helper;


import BackERP.models.erpEncabezadoFacturas;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class erpEncabezadoFacturasSpecs {



    public static Specification<erpEncabezadoFacturas> nombreClienteContains(String nombreCliente) {
        return (root, query, cb) -> {
            if (nombreCliente == null || nombreCliente.isEmpty()) {
                return null; // no aplica filtro si viene null
            }
            String pattern = likeHelper.buildLikePattern(nombreCliente, likeHelper.MatchMode.ANYWHERE, true);

            return cb.like(cb.lower(root.get("idCliente").get("nombreCliente")),pattern, '\\');
        };
    }

    public static Specification<erpEncabezadoFacturas> nitClienteContains(String nit) {
        return (root, query, cb) -> {
            if (nit == null || nit.isEmpty()) {
                return null; // no aplica filtro si viene null
            }
            // limpiar espacios y guiones
            String nitLimpio = nit.replaceAll("[\\s-]", "");

            String pattern = likeHelper.buildLikePattern(nitLimpio, likeHelper.MatchMode.ANYWHERE, true);

            return cb.like(cb.lower(root.get("idCliente").get("nit")),pattern, '\\');
        };
    }

    public static Specification<erpEncabezadoFacturas> referenciaFacturaContains(String referencia) {
        return (root, query, cb) -> {
            String pattern = likeHelper.buildLikePattern(referencia, likeHelper.MatchMode.ANYWHERE, false);
            if (pattern == null) return cb.conjunction();
            return cb.like(cb.lower(root.get("referencia")), pattern, '\\');
        };
    }

    public static Specification<erpEncabezadoFacturas> tipoDocumentoContains(String tipoDocumento) {
        return (root, query, cb) -> {
            String pattern = likeHelper.buildLikePattern(tipoDocumento, likeHelper.MatchMode.ANYWHERE, false);
            if (pattern == null) return cb.conjunction();
            return cb.like(cb.lower(root.get("tipoDocumento")), pattern, '\\');
        };
    }

    public static Specification<erpEncabezadoFacturas> fechaFacturaBetween(LocalDate fechaInicio, LocalDate fechaFin) {
        return (root, query, cb) -> {
            if (fechaInicio == null && fechaFin == null) {
                return cb.conjunction(); // no aplica filtro
            }
            if (fechaInicio != null && fechaFin != null) {
                return cb.between(root.get("FechaFactura"), fechaInicio, fechaFin);
            }
            if (fechaInicio != null) {
                return cb.greaterThanOrEqualTo(root.get("FechaFactura"), fechaInicio);
            }
            return cb.lessThanOrEqualTo(root.get("FechaFactura"), fechaFin);
        };
    }

    public static Specification<erpEncabezadoFacturas> numeroPreimpresoContains(String numeroPreimpreso) {
        return (root, query, cb) -> {
            if (numeroPreimpreso == null || numeroPreimpreso.isEmpty()) {
                return cb.conjunction(); // no aplica filtro si viene null
            }
            String pattern = likeHelper.buildLikePattern(numeroPreimpreso, likeHelper.MatchMode.ANYWHERE, false);
            return cb.like(cb.lower(root.get("preimpreso")), pattern, '\\');
        };
    }

}
