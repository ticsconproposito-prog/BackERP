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
            String pattern = LikeHelper.buildLikePattern(nombreCliente, LikeHelper.MatchMode.ANYWHERE, true);

            return cb.like(cb.lower(root.get("idCliente").get("nombreCliente")),pattern, '\\');
        };
    }

    public static Specification<erpEncabezadoFacturas> nitClienteContains(String nit) {
        return (root, query, cb) -> {
            if (nit == null || nit.isEmpty()) {
                return null; // no aplica filtro si viene null
            }
            String pattern = LikeHelper.buildLikePattern(nit, LikeHelper.MatchMode.ANYWHERE, true);

            return cb.like(cb.lower(root.get("idCliente").get("nit")),pattern, '\\');
        };
    }

    public static Specification<erpEncabezadoFacturas> referenciaFacturaContains(String referencia) {
        return (root, query, cb) -> {
            String pattern = LikeHelper.buildLikePattern(referencia, LikeHelper.MatchMode.ANYWHERE, false);
            if (pattern == null) return cb.conjunction();
            return cb.like(cb.lower(root.get("referencia")), pattern, '\\');
        };
    }

    public static Specification<erpEncabezadoFacturas> tipoDocumentoContains(String tipoDocumento) {
        return (root, query, cb) -> {
            String pattern = LikeHelper.buildLikePattern(tipoDocumento, LikeHelper.MatchMode.ANYWHERE, false);
            if (pattern == null) return cb.conjunction();
            return cb.like(cb.lower(root.get("facturaProsesada")), pattern, '\\');
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
}
