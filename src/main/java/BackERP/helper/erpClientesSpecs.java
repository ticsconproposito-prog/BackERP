package BackERP.helper;

import BackERP.models.erpclientes;
import org.springframework.data.jpa.domain.Specification;

public class erpClientesSpecs {
    public static Specification<erpclientes> nombreClienteContains(String nombreCliente) {
        return (root, query, cb) -> {
            if (nombreCliente == null || nombreCliente.isEmpty()) {
                return null; // no aplica filtro si viene null
            }
            String pattern = LikeHelper.buildLikePattern(nombreCliente, LikeHelper.MatchMode.ANYWHERE, true);

            return cb.like(cb.lower(root.get("nombreCliente")),pattern, '\\');
        };
    }

    public static Specification<erpclientes> nitClienteContains(String nit) {
        return (root, query, cb) -> {
            if (nit == null || nit.isEmpty()) {
                return null; // no aplica filtro si viene null
            }
            String pattern = LikeHelper.buildLikePattern(nit, LikeHelper.MatchMode.ANYWHERE, true);

            return cb.like(cb.lower(root.get("nit")),pattern, '\\');
        };
    }
    public static Specification<erpclientes> estadoEquals(int estado) {
        return (root, query, cb) ->
                cb.equal(root.get("estado").as(Integer.class), estado);

    }

}
