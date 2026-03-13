package BackERP.helper;

import BackERP.models.segUsuarios;
import org.springframework.data.jpa.domain.Specification;

public class segUsuariosSpecs {

    public static Specification<segUsuarios> usuarioContains(String nombre) {
        return (root, query, cb) -> {
            if (nombre == null || nombre.isEmpty()) {
                return null; // no aplica filtro si viene null
            }
            String pattern = likeHelper.buildLikePattern(nombre, likeHelper.MatchMode.ANYWHERE, true);

            return cb.like(cb.lower(root.get("usuario")),pattern, '\\');
        };
    }

    public static Specification<segUsuarios> idEmpleadoContains(Integer id) {
        return (root, query, cb) ->{
            if (id == null) {
                return null; // <-- al devolver null, no se agrega restricción }
            }
            return cb.equal(root.get("idEmpleado"), id);
        };
    }

    public static Specification<segUsuarios> idUsuarioContains(Integer id) {
        return (root, query, cb) ->{
            if (id == null) {
                return null; // <-- al devolver null, no se agrega restricción }
            }
            return cb.equal(root.get("idUsuario"), id);
        };
    }

    public static Specification<segUsuarios> estadoEquals(int estado) {
        return (root, query, cb) ->
                cb.equal(root.get("estado").as(Integer.class), estado);

    }

}
