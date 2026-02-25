package BackERP.helper;

import BackERP.models.segusuarios;
import org.springframework.data.jpa.domain.Specification;

public class segUsuariosSpecs {

    public static Specification<segusuarios> usuarioContains(String nombre) {
        return (root, query, cb) -> {
            if (nombre == null || nombre.isEmpty()) {
                return null; // no aplica filtro si viene null
            }
            String pattern = LikeHelper.buildLikePattern(nombre, LikeHelper.MatchMode.ANYWHERE, true);

            return cb.like(cb.lower(root.get("usuario")),pattern, '\\');
        };
    }

    public static Specification<segusuarios> idEmpleadoContains(Integer id) {
        return (root, query, cb) ->{
            if (id == null) {
                return null; // <-- al devolver null, no se agrega restricción }
            }
            return cb.equal(root.get("idEmpleado"), id);
        };
    }

    public static Specification<segusuarios> idUsuarioContains(Integer id) {
        return (root, query, cb) ->{
            if (id == null) {
                return null; // <-- al devolver null, no se agrega restricción }
            }
            return cb.equal(root.get("idUsuario"), id);
        };
    }

    public static Specification<segusuarios> estadoEquals(int estado) {
        return (root, query, cb) ->
                cb.equal(root.get("estado").as(Integer.class), estado);

    }

}
