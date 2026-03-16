package BackERP.helper;

import BackERP.models.segPerfiles;
import org.springframework.data.jpa.domain.Specification;

public class segPerfilesSpecs {


    public static Specification<segPerfiles> nombrePerfilContains(String nombre) {
        return (root, query, cb) -> {
            if (nombre == null || nombre.isEmpty()) {
                return null; // no aplica filtro si viene null
            }
            String pattern = likeHelper.buildLikePattern(nombre, likeHelper.MatchMode.ANYWHERE, true);

            return cb.like(cb.lower(root.get("nombrePerfil")),pattern, '\\');
        };
    }
    public static Specification<segPerfiles> idPaginaContains(Integer id) {
        return (root, query, cb) ->{
            if (id == null) {
                return null; // <-- al devolver null, no se agrega restricción }
            }
            return cb.equal(root.get("idPerfil"), id);
        };
    }

    public static Specification<segPerfiles> estadoEquals(int estado) {
        return (root, query, cb) ->
                cb.equal(root.get("estado").as(Integer.class), estado);

    }
}
