package BackERP.helper;


import BackERP.models.erpProductos;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.Arrays;


public class erpProductosSpecs{



    public static Specification<erpProductos> codigoProductoContains(String codigo) {
        return (root, query, cb) -> {
            String pattern = likeHelper.buildLikePattern(codigo, likeHelper.MatchMode.ANYWHERE, false);
            if (pattern == null) return cb.conjunction();
            return cb.like(cb.lower(root.get("codigoProducto")), pattern, '\\');
        };
    }


    public static Specification<erpProductos> codigoProductoProveedorContains(String codigoProv) {
        return (root, query, cb) -> {
            String pattern = likeHelper.buildLikePattern(codigoProv, likeHelper.MatchMode.ANYWHERE, false);
            if (pattern == null) return cb.conjunction();
            return cb.like(cb.lower(root.get("codigoProductoProveedor")), pattern, '\\');
        };
    }

   public static Specification<erpProductos> descripcionContieneFlexible(String descripcion) {
        return (root, query, cb) -> {
            if (descripcion == null || descripcion.trim().isEmpty()) {
                return cb.conjunction();
            }

            // Dividir la frase en palabras
            String[] palabras = descripcion.toLowerCase().trim().split("\\s+");

            // Crear un predicado AND: todas las palabras deben aparecer en la descripción
            Predicate[] predicates = Arrays.stream(palabras)
                    .map(p -> cb.like(
                            cb.lower(root.get("descripcionProducto")),
                            "%" + likeHelper.escapeForLike(p) + "%",
                            '\\'
                    ))
                    .toArray(Predicate[]::new);

            return cb.and(predicates);
        };
    }
  public static Specification<erpProductos> descripcionFullText(String descripcion) {
    return (root, query, cb) -> {
      if (descripcion == null || descripcion.trim().isEmpty()) {
        return cb.conjunction();
      }
      // MATCH ... AGAINST devuelve un score de relevancia
      return cb.greaterThan(
        cb.function(
          "MATCH", Double.class,
          root.get("descripcionProducto"),
          cb.literal(descripcion)
        ),
        0.0
      );
    };
  }

    public static Specification<erpProductos> descripcionContiene(String descripcion) {
        return (root, query, cb) -> {
            String pattern = likeHelper.buildLikePattern(descripcion, likeHelper.MatchMode.ANYWHERE, true);
            if (pattern == null) return cb.conjunction();
            return cb.like(cb.lower(root.get("descripcionProducto")), pattern, '\\');
        };
    }

        public static Specification<erpProductos> estadoEquals(int estado) {
        return (root, query, cb) ->
               cb.equal(root.get("estado").as(Integer.class), estado);

    }

    public static Specification<erpProductos> idProductoContains(Long id) {
        return (root, query, cb) ->{
            if (id == null) {

                return null; // <-- al devolver null, no se agrega restricción

            }
            return cb.equal(root.get("idProducto"), id);
        };
    }


}
