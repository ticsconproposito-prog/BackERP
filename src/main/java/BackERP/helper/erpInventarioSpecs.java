package BackERP.helper;

import BackERP.models.erpInventario;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class erpInventarioSpecs {

  /**
   * 🔥 NUEVO: Busca una palabra específica en la descripción del producto
   * Esta es una búsqueda INDIVIDUAL para una palabra
   *
   * @param palabra Palabra a buscar (ya viene en minúsculas)
   * @return Specification para buscar SOLO esa palabra
   */
  public static Specification<erpInventario> buscarPalabraEnDescripcion(String palabra) {
    return (root, query, cb) -> {
      if (palabra == null || palabra.trim().isEmpty()) {
        return cb.conjunction();
      }

      String palabraEscapada = likeHelper.escapeForLike(palabra);
      String pattern = "%" + palabraEscapada + "%";

      return cb.like(
        cb.lower(root.get("idProducto").get("descripcionProducto")),
        pattern,
        '\\'
      );
    };
  }

  /**
   * Método original - se mantiene por compatibilidad
   * pero AHORA usa el servicio de cruce en el controlador
   */
  public static Specification<erpInventario> descripcionProductoContieneFlexible(String descripcion) {
    return (root, query, cb) -> {
      if (descripcion == null || descripcion.trim().isEmpty()) {
        return cb.conjunction();
      }

      // Dividir la frase en palabras
      String[] palabras = descripcion.toLowerCase().trim().split("\\s+");

      // Si solo hay una palabra, buscar directamente
      if (palabras.length == 1) {
        String palabraEscapada = likeHelper.escapeForLike(palabras[0]);
        return cb.like(
          cb.lower(root.get("idProducto").get("descripcionProducto")),
          "%" + palabraEscapada + "%",
          '\\'
        );
      }

      // Crear un predicado AND: todas las palabras deben aparecer en la descripción
      List<Predicate> predicates = new ArrayList<>();
      for (String palabra : palabras) {
        String palabraEscapada = likeHelper.escapeForLike(palabra);
        predicates.add(cb.like(
          cb.lower(root.get("idProducto").get("descripcionProducto")),
          "%" + palabraEscapada + "%",
          '\\'
        ));
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }

  // Los demás métodos se mantienen IGUAL
  public static Specification<erpInventario> idUbicacionEquals(Integer idUbicacion) {
    return (root, query, cb) -> {
      if (idUbicacion == null) {
        return cb.conjunction();
      }
      return cb.equal(root.get("idUbicacion").as(Integer.class), idUbicacion);
    };
  }

  public static Specification<erpInventario> codigoProductoContains(String codigoProducto) {
    return (root, query, cb) -> {
      if (codigoProducto == null || codigoProducto.trim().isEmpty()) {
        return cb.conjunction();
      }
      String pattern = likeHelper.buildLikePattern(codigoProducto, likeHelper.MatchMode.ANYWHERE, true);
      if (pattern == null) return cb.conjunction();
      return cb.like(
        cb.lower(root.get("idProducto").get("codigoProducto")),
        pattern,
        '\\'
      );
    };
  }

  public static Specification<erpInventario> codigoProductoProveedorContains(String codigoProductoProveedor) {
    return (root, query, cb) -> {
      if (codigoProductoProveedor == null || codigoProductoProveedor.trim().isEmpty()) {
        return cb.conjunction();
      }
      String pattern = likeHelper.buildLikePattern(codigoProductoProveedor, likeHelper.MatchMode.ANYWHERE, true);
      if (pattern == null) return cb.conjunction();
      return cb.like(
        cb.lower(root.get("idProducto").get("codigoProductoProveedor")),
        pattern,
        '\\'
      );
    };
  }

  public static Specification<erpInventario> estadoEquals(int estado) {
    return (root, query, cb) -> cb.equal(root.get("estado").as(Integer.class), estado);
  }
}
