package BackERP.helper;

import BackERP.models.erpProveedores;
import org.springframework.data.jpa.domain.Specification;

public class erpProveedoresSpecs {



    public static Specification<erpProveedores> nombreContains(String nombre) {
        return (root, query, cb) -> {
            String pattern = LikeHelper.buildLikePattern(nombre, LikeHelper.MatchMode.ANYWHERE, false);
            if (pattern == null) return cb.conjunction();
            return cb.like(cb.lower(root.get("nombre")), pattern, '\\');
        };
    }

    public static Specification<erpProveedores> nombreDeContacto1Contains(String nombre) {
        return (root, query, cb) -> {
            String pattern = LikeHelper.buildLikePattern(nombre, LikeHelper.MatchMode.ANYWHERE, false);
            if (pattern == null) return cb.conjunction();
            return cb.like(cb.lower(root.get("nombreDeContacto1")), pattern, '\\');
        };
    }


    public static Specification<erpProveedores> idProveedorContains(Integer id) {
        return (root, query, cb) ->{
            if (id == null) {
                return null; // <-- al devolver null, no se agrega restricción }
            }
            return cb.equal(root.get("idProveedor"), id);
        };
    }
    public static Specification<erpProveedores> estadoEquals(int estado) {
        return (root, query, cb) ->
                cb.equal(root.get("estado").as(Integer.class), estado);

    }
}
