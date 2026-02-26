package BackERP.helper;

import BackERP.models.segUsuariosPerfiles;
import org.springframework.data.jpa.domain.Specification;

public class segUsuariosPerfilesSpecs {

    public static Specification<segUsuariosPerfiles> idUsuarioEquals(Integer idUsuario) {
        return (root, query, cb) -> {
            if (idUsuario == null) return null;
            return cb.equal(root.get("idUsuario"), idUsuario);
        };
    }

    public static Specification<segUsuariosPerfiles> idPerfilEquals(Integer idPerfil) {
        return (root, query, cb) -> {
            if (idPerfil == null) return null;
            return cb.equal(root.get("idPerfil"), idPerfil);
        };
    }

    public static Specification<segUsuariosPerfiles> permisoContains(String permiso) {
        return (root, query, cb) -> {
            if (permiso == null || permiso.isEmpty()) return null;
            String pattern = "%" + permiso.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("permiso")), pattern);
        };
    }

    public static Specification<segUsuariosPerfiles> estadoEquals(int estado) {
        return (root, query, cb) -> cb.equal(root.get("estado"), estado);
    }
}
