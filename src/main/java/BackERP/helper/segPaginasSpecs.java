package BackERP.helper;

import BackERP.models.segpaginas;
import org.springframework.data.jpa.domain.Specification;

public class segPaginasSpecs {


    public static Specification<segpaginas> nombrePaginaContains(String nombre) {
        return (root, query, cb) -> {
            if (nombre == null || nombre.isEmpty()) {
                return null; // no aplica filtro si viene null
            }
            String pattern = likeHelper.buildLikePattern(nombre, likeHelper.MatchMode.ANYWHERE, true);

            return cb.like(cb.lower(root.get("nombrePagina")),pattern, '\\');
        };
    }
    public static Specification<segpaginas> idPaginaContains(Integer id) {
        return (root, query, cb) ->{
            if (id == null) {
                return null; // <-- al devolver null, no se agrega restricción }
            }
            return cb.equal(root.get("idPagina"), id);
        };
    }

    public static Specification<segpaginas> estadoEquals(int estado) {
        return (root, query, cb) ->
                cb.equal(root.get("estado").as(Integer.class), estado);

    }

}
