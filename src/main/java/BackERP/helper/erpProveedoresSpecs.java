package BackERP.helper;

import BackERP.models.erpProveedores;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;

public class erpProveedoresSpecs {

  public static Specification<erpProveedores> nombreContains(String nombre) {
    return (root, query, cb) -> {
      if (nombre == null || nombre.trim().isEmpty()) {
        return cb.conjunction();
      }

      String[] palabras = nombre.toLowerCase().trim().split("\\s+");

      Predicate[] predicates = Arrays.stream(palabras)
        .map(p -> cb.like(
          cb.lower(root.get("nombre")),
          "%" + likeHelper.escapeForLike(p) + "%",
          '\\'
        ))
        .toArray(Predicate[]::new);

      return cb.and(predicates);
    };
  }



  public static Specification<erpProveedores> nombreDeContacto1Contains(String nombre) {
    return (root, query, cb) -> {
      if (nombre == null || nombre.trim().isEmpty()) {
        return cb.conjunction();
      }

      String[] palabras = nombre.toLowerCase().trim().split("\\s+");

      Predicate[] predicates = Arrays.stream(palabras)
        .map(p -> cb.like(
          cb.lower(root.get("nombreDeContacto1")),
          "%" + likeHelper.escapeForLike(p) + "%",
          '\\'
        ))
        .toArray(Predicate[]::new);

      return cb.and(predicates);
    };
  }



  public static Specification<erpProveedores> idProveedorContains(Integer id) {
        return (root, query, cb) ->{
            if (id == null) {
                return null; // <-- al devolver null, no se agrega restricción }
            }
            return cb.equal(root.get("idProveedor"), id);
        };
    }
    public static Specification<erpProveedores> estadoEquals(int estado) {
        return (root, query, cb) ->
                cb.equal(root.get("estado").as(Integer.class), estado);

    }
}
