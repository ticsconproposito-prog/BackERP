package BackERP.helper;

import BackERP.models.segLogins;
import org.springframework.data.jpa.domain.Specification;

public class segLoginsSpecs {

    public static Specification<segLogins> idUsuarioEquals(Integer idUsuario) {
        return (root, query, cb) -> {
            if (idUsuario == null) return null;
            return cb.equal(root.get("idUsuario"), idUsuario);
        };
    }

    public static Specification<segLogins> estadoEquals(int estado) {
        return (root, query, cb) -> cb.equal(root.get("estado"), estado);
    }

    public static Specification<segLogins> estadoConexionContains(String estadoConexion) {
        return (root, query, cb) -> {
            if (estadoConexion == null || estadoConexion.isEmpty()) return null;
            String pattern = "%" + estadoConexion.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("estadoConexion")), pattern);
        };
    }
}
