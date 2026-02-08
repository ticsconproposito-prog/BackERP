package BackERP.helper;

import BackERP.models.erpinventario;
import BackERP.models.erpProductos;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class erpInventarioSpecs {



    public static Specification<erpinventario> descripcionProductoContains(String descripcion) {
        return (root, query, cb) -> {
            if (descripcion == null || descripcion.isEmpty()) {
                return null; // no aplica filtro si viene null
                 }
            String pattern = LikeHelper.buildLikePattern(descripcion, LikeHelper.MatchMode.ANYWHERE, true);

            return cb.like(cb.lower(root.get("idProducto").get("descripcionProducto")),pattern, '\\');
        };
    }

    public static Specification<erpinventario> codigoProductoContains(String codigoProducto) {
        return (root, query, cb) -> {
            if (codigoProducto == null || codigoProducto.isEmpty()) {
                return null; // no aplica filtro si viene null
            }
            String pattern = LikeHelper.buildLikePattern(codigoProducto, LikeHelper.MatchMode.ANYWHERE, true);
            return cb.like(cb.lower(root.get("idProducto").get("codigoProducto")),pattern, '\\');
        };
    }

    public static Specification<erpinventario> codigoProductoProveedorContains(String codigoProductoProveedor) {
        return (root, query, cb) -> {
            if (codigoProductoProveedor == null || codigoProductoProveedor.isEmpty()) {
                return null; // no aplica filtro si viene null
            }
            String pattern = LikeHelper.buildLikePattern(codigoProductoProveedor, LikeHelper.MatchMode.ANYWHERE, true);
            return cb.like(cb.lower(root.get("idProducto").get("codigoProductoProveedor")),pattern, '\\');
        };
    }

    public static Specification<erpinventario> estadoEquals(int estado) {
        return (root, query, cb) ->
                cb.equal(root.get("estado").as(Integer.class), estado);

    }
}
