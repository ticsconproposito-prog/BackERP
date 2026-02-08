package BackERP.helper;

import BackERP.models.erpMovimientosProductos;
import org.springframework.data.jpa.domain.Specification;

public class erpMovimientosProductosSpecs {

    public static Specification<erpMovimientosProductos> idOrdenProductosContains(Integer id) {
        return (root, query, cb) ->{
            if (id == null) {
                return null; // <-- al devolver null, no se agrega restricción }
            }
            return cb.equal(root.get("idOrdenProducto"), id);
        };
    }



}
