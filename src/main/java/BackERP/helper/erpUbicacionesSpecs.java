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

    public static Specification<erpUbicaciones> estadoEquals(int estado) {
        return (root, query, cb) ->
                cb.equal(root.get("estado").as(Integer.class), estado);

    }

}
