package BackERP.helper;

import BackERP.models.segPerfilesPaginas;
import org.springframework.data.jpa.domain.Specification;

public class segPerfilesPaginasSpecs {

    public static Specification<segPerfilesPaginas> idPerfilEquals(Integer idPerfil) {
        return (root, query, cb) -> {
            if (idPerfil == null) return null;
            return cb.equal(root.get("idPerfil"), idPerfil);
        };
    }

    public static Specification<segPerfilesPaginas> idPaginaEquals(Integer idPagina) {
        return (root, query, cb) -> {
            if (idPagina == null) return null;
            return cb.equal(root.get("idPagina"), idPagina);
        };
    }

    public static Specification<segPerfilesPaginas> permisoEquals(Integer permiso) {
        return (root, query, cb) -> {
            if (permiso == null) return null;
            return cb.equal(root.get("permiso"), permiso);
        };
    }

    public static Specification<segPerfilesPaginas> estadoEquals(int estado) {
        return (root, query, cb) -> cb.equal(root.get("estado"), estado);
    }
}
