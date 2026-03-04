package BackERP.helper;

import BackERP.models.erpDetalleFacturas;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class erpDetalleFacturaSpecs {


    public static Specification<erpDetalleFacturas> fechaBetween(LocalDate fechaInicio, LocalDate fechaFin) {
        return (root, query, cb) -> {
            if (fechaInicio == null || fechaFin == null) {
                return null;
            }
            return cb.between(root.get("fechaModificacion"), fechaInicio, fechaFin);
        };
    }

    public static Specification<erpDetalleFacturas> idEncabezadoFacturaContains(Integer id) {
        return (root, query, cb) ->{
            if (id == null) {
                return null; // <-- al devolver null, no se agrega restricción }
            }
            return cb.equal(root.get("idEncabezadoFactura"), id);
        };
    }

}
