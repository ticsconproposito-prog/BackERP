package BackERP.helper;

import BackERP.models.erpInventario;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.Arrays;

public class erpInventarioSpecs {


    public static Specification<erpInventario> descripcionProductoContieneFlexible(String descripcion) {
        return (root, query, cb) -> {
            if (descripcion == null || descripcion.trim().isEmpty()) {
                return cb.conjunction();
            }

            // Dividir la frase en palabras
            String[] palabras = descripcion.toLowerCase().trim().split("\\s+");

            // Crear un predicado AND: todas las palabras deben aparecer en la descripción del producto
            Predicate[] predicates = Arrays.stream(palabras)
                    .map(p -> cb.like(
                            cb.lower(root.get("idProducto").get("descripcionProducto")),
                            "%" + likeHelper.escapeForLike(p) + "%",
                            '\\'
                    ))
                    .toArray(Predicate[]::new);

            return cb.and(predicates);
        };
    }



  public static Specification<erpInventario> idUbicacionEquals(Integer idUbicacion) {
    return (root, query, cb) -> {

      if (idUbicacion == null) {
        return null; // <-- al devolver null, no se agrega restricción }
      }

      return cb.equal(root.get("idUbicacion").as(Integer.class), idUbicacion);
    };
  }
    public static Specification<erpInventario> descripcionProductoContains(String descripcion) {
        return (root, query, cb) -> {
            if (descripcion == null || descripcion.isEmpty()) {
                return null; // no aplica filtro si viene null
                 }
            String pattern = likeHelper.buildLikePattern(descripcion, likeHelper.MatchMode.ANYWHERE, true);

            return cb.like(cb.lower(root.get("idProducto").get("descripcionProducto")),pattern, '\\');
        };
    }

    public static Specification<erpInventario> codigoProductoContains(String codigoProducto) {
        return (root, query, cb) -> {
            if (codigoProducto == null || codigoProducto.isEmpty()) {
                return null; // no aplica filtro si viene null
            }
            String pattern = likeHelper.buildLikePattern(codigoProducto, likeHelper.MatchMode.ANYWHERE, true);
            return cb.like(cb.lower(root.get("idProducto").get("codigoProducto")),pattern, '\\');
        };
    }

    public static Specification<erpInventario> codigoProductoProveedorContains(String codigoProductoProveedor) {
        return (root, query, cb) -> {
            if (codigoProductoProveedor == null || codigoProductoProveedor.isEmpty()) {
                return null; // no aplica filtro si viene null
            }
            String pattern = likeHelper.buildLikePattern(codigoProductoProveedor, likeHelper.MatchMode.ANYWHERE, true);
            return cb.like(cb.lower(root.get("idProducto").get("codigoProductoProveedor")),pattern, '\\');
        };
    }

    public static Specification<erpInventario> estadoEquals(int estado) {
        return (root, query, cb) ->
                cb.equal(root.get("estado").as(Integer.class), estado);

    }
}
