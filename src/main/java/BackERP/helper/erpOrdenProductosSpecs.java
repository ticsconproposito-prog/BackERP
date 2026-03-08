package BackERP.helper;

import BackERP.models.erpOrdenProductos;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class erpOrdenProductosSpecs {

    public static Specification<erpOrdenProductos> idSucursalContains(String idSucursal) {
        return (root, query, cb) -> {

            if (idSucursal == null) return cb.conjunction();
            return cb.like(root.get("idSucursal"), idSucursal, '\\');
        };
    }

    public static Specification<erpOrdenProductos> idProveedorContains(String idProveedor) {
        return (root, query, cb) -> {

            if (idProveedor == null) return cb.conjunction();
            return cb.like(root.get("idProveedor"), idProveedor, '\\');
        };
    }
    public static Specification<erpOrdenProductos> idOrdenContains(Integer id) {
        return (root, query, cb) ->{
            if (id == null) {
                return null; // <-- al devolver null, no se agrega restricción }
            }
            return cb.equal(root.get("idOrdenProducto"), id);
        };
    }

    public static Specification<erpOrdenProductos> tipoDeMovimientoContains(Integer tipoDeMovimiento) {
        return (root, query, cb) ->{
        if (tipoDeMovimiento == null) {
            return null; // <-- al devolver null, no se agrega restricción }
        }
            return cb.equal(root.get("tipoDeMovimiento"), tipoDeMovimiento);
        };
    }

    public static Specification<erpOrdenProductos> numeroDeDocumentoContains(String numeroDeDocumento) {
        return (root, query, cb) -> {
            String pattern = likeHelper.buildLikePattern(numeroDeDocumento, likeHelper.MatchMode.ANYWHERE, false);
            if (pattern == null) return cb.conjunction();
            return cb.like(cb.lower(root.get("numeroDeDocumento")), pattern, '\\');
        };
    }

    public static Specification<erpOrdenProductos> fechaOrdenBetween(LocalDate fechaInicio, LocalDate fechaFin) {
        return (root, query, cb) -> {
            if (fechaInicio == null && fechaFin == null) {
                return cb.conjunction(); // no aplica filtro
            }
            if (fechaInicio != null && fechaFin != null) {
                return cb.between(root.get("fechaOrden"), fechaInicio, fechaFin);
            }
            if (fechaInicio != null) {
                return cb.greaterThanOrEqualTo(root.get("fechaOrden"), fechaInicio);
            }
            return cb.lessThanOrEqualTo(root.get("fechaOrden"), fechaFin);
        };
    }


    public static Specification<erpOrdenProductos> estadoEquals(int estado) {
        return (root, query, cb) ->
                cb.equal(root.get("estado").as(Integer.class), estado);

    }

}
