package BackERP.helper;


import BackERP.models.erpUbicaciones;
import org.springframework.data.jpa.domain.Specification;

public class erpUbicacionesSpecs {

    public static Specification<erpUbicaciones> idUbicacionContains(Integer id) {
        return (root, query, cb) ->{
            if (id == null) {
                return null; // <-- al devolver null, no se agrega restricción }
            }
            return cb.equal(root.get("idUbicacion"), id);
        };
    }

    public static Specification<erpUbicaciones> nombreUbicacionContains(String nombreUbicacion) {
        return (root, query, cb) -> {
            String pattern = likeHelper.buildLikePattern(nombreUbicacion, likeHelper.MatchMode.ANYWHERE, false);
            if (pattern == null) return cb.conjunction();
            return cb.like(cb.lower(root.get("nombreUbicacion")), pattern, '\\');
        };
    }

    public static Specification<erpUbicaciones> estadoEquals(int estado) {
        return (root, query, cb) ->
                cb.equal(root.get("estado").as(Integer.class), estado);

    }

}
