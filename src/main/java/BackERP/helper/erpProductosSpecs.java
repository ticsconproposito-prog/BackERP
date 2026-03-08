package BackERP.helper;


import BackERP.models.erpProductos;
import org.springframework.data.jpa.domain.Specification;



public class erpProductosSpecs{



    public static Specification<erpProductos> codigoProductoContains(String codigo) {
        return (root, query, cb) -> {
            String pattern = LikeHelper.buildLikePattern(codigo, LikeHelper.MatchMode.ANYWHERE, false);
            if (pattern == null) return cb.conjunction();
            return cb.like(cb.lower(root.get("codigoProducto")), pattern, '\\');
        };
    }


    public static Specification<erpProductos> codigoProductoProveedorContains(String codigoProv) {
        return (root, query, cb) -> {
            String pattern = LikeHelper.buildLikePattern(codigoProv, LikeHelper.MatchMode.ANYWHERE, false);
            if (pattern == null) return cb.conjunction();
            return cb.like(cb.lower(root.get("codigoProductoProveedor")), pattern, '\\');
        };
    }


    public static Specification<erpProductos> descripcionContiene(String descripcion) {
        return (root, query, cb) -> {
            String pattern = LikeHelper.buildLikePattern(descripcion, LikeHelper.MatchMode.ANYWHERE, true);
            if (pattern == null) return cb.conjunction();
            return cb.like(cb.lower(root.get("descripcionProducto")), pattern, '\\');
        };
    }

        public static Specification<erpProductos> estadoEquals(int estado) {
        return (root, query, cb) ->
               cb.equal(root.get("estado").as(Integer.class), estado);

    }

    public static Specification<erpProductos> idProductoContains(Long id) {
        return (root, query, cb) ->{
            if (id == null) {
                System.out.println("entre"+id);
                return null; // <-- al devolver null, no se agrega restricción

            }
            return cb.equal(root.get("idProducto"), id);
        };
    }


}
