package BackERP.helper;

import BackERP.models.erpConsignacionPagos;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class erpConsignacionPagosSpecs {

  public static Specification<erpConsignacionPagos> estadoEquals(String estado) {
    return (root, query, cb) -> {
      if (estado == null || estado.isEmpty()) {
        return null;
      }
      return cb.equal(root.get("estado"), estado);
    };
  }

  public static Specification<erpConsignacionPagos> idEncabezadoFacturaEquals(Integer idFactura) {
    return (root, query, cb) -> {
      if (idFactura == null) {
        return null;
      }
      return cb.equal(root.get("idEncabezadoFactura"), idFactura);
    };
  }

  public static Specification<erpConsignacionPagos> fechaPagoBetween(LocalDate fechaInicio, LocalDate fechaFin) {
    return (root, query, cb) -> {
      if (fechaInicio == null && fechaFin == null) {
        return null;
      }
      if (fechaInicio != null && fechaFin != null) {
        return cb.between(root.get("fechaPago"), fechaInicio, fechaFin);
      }
      if (fechaInicio != null) {
        return cb.greaterThanOrEqualTo(root.get("fechaPago"), fechaInicio);
      }
      return cb.lessThanOrEqualTo(root.get("fechaPago"), fechaFin);
    };
  }
}
