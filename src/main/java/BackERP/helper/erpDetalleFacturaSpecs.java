package BackERP.helper;

import BackERP.models.erpDetalleFacturas;
import org.springframework.data.jpa.domain.Specification;

public class erpDetalleFacturaSpecs {

    public static Specification<erpDetalleFacturas> idEncabezadoFacturaContains(Integer id) {
        return (root, query, cb) ->{
            if (id == null) {
                return null; // <-- al devolver null, no se agrega restricción }
            }
            return cb.equal(root.get("idEncabezadoFactura"), id);
        };
    }

}
