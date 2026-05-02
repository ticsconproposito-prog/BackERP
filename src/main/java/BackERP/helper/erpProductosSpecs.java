package BackERP.helper;

import BackERP.models.erpProductos;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class erpProductosSpecs {

  /**
   * 🔥 NUEVO: Busca una palabra específica en la descripción del producto
   * Esta es una búsqueda INDIVIDUAL para una sola palabra
   *
   * @param palabra Palabra a buscar (ya viene en minúsculas)
   * @return Specification para buscar SOLO esa palabra en la descripción
   *
   * @example
   * palabra = "tarro"
   * Genera: WHERE descripcionProducto LIKE '%tarro%'
   */
  public static Specification<erpProductos> buscarPalabraEnDescripcion(String palabra) {
    return (root, query, cb) -> {
      if (palabra == null || palabra.trim().isEmpty()) {
        return cb.conjunction();
      }

      String palabraEscapada = likeHelper.escapeForLike(palabra);
      String pattern = "%" + palabraEscapada + "%";

      return cb.like(
        cb.lower(root.get("descripcionProducto")),
        pattern,
        '\\'
      );
    };
  }

  /**
   * Búsqueda flexible SOLO en descripción del producto usando separación de palabras
   * Este método busca TODAS las palabras en UNA SOLA consulta usando AND
   *
   * @param descripcion Texto de búsqueda (se separará por espacios)
   * @return Specification que busca TODAS las palabras en la descripción
   *
   * @example
   * descripcion = "tarro plastico azul"
   * Genera: WHERE descripcionProducto LIKE '%tarro%'
   *         AND descripcionProducto LIKE '%plastico%'
   *         AND descripcionProducto LIKE '%azul%'
   */
  public static Specification<erpProductos> descripcionContieneFlexible(String descripcion) {
    return (root, query, cb) -> {
      if (descripcion == null || descripcion.trim().isEmpty()) {
        return cb.conjunction();
      }

      // Dividir la frase en palabras (eliminando espacios múltiples)
      String[] palabras = descripcion.toLowerCase().trim().split("\\s+");

      // Si solo hay una palabra, buscar directamente
      if (palabras.length == 1) {
        String palabraEscapada = likeHelper.escapeForLike(palabras[0]);
        return cb.like(
          cb.lower(root.get("descripcionProducto")),
          "%" + palabraEscapada + "%",
          '\\'
        );
      }

      // Crear un predicado AND: todas las palabras deben aparecer en la descripción
      List<Predicate> predicates = new ArrayList<>();
      for (String palabra : palabras) {
        String palabraEscapada = likeHelper.escapeForLike(palabra);
        predicates.add(cb.like(
          cb.lower(root.get("descripcionProducto")),
          "%" + palabraEscapada + "%",
          '\\'
        ));
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }

  /**
   * Búsqueda flexible en descripción (versión tradicional LIKE %texto%)
   * Este método se mantiene por compatibilidad
   *
   * @param descripcion Texto a buscar en la descripción
   * @return Specification para búsqueda tradicional
   */
  public static Specification<erpProductos> descripcionContiene(String descripcion) {
    return (root, query, cb) -> {
      if (descripcion == null || descripcion.trim().isEmpty()) {
        return cb.conjunction();
      }
      String pattern = likeHelper.buildLikePattern(descripcion, likeHelper.MatchMode.ANYWHERE, true);
      if (pattern == null) return cb.conjunction();
      return cb.like(cb.lower(root.get("descripcionProducto")), pattern, '\\');
    };
  }

  /**
   * Búsqueda flexible en código de producto (búsqueda tradicional LIKE %texto%)
   *
   * @param codigo Código del producto a buscar
   * @return Specification para buscar en código de producto
   */
  public static Specification<erpProductos> codigoProductoContains(String codigo) {
    return (root, query, cb) -> {
      if (codigo == null || codigo.trim().isEmpty()) {
        return cb.conjunction();
      }
      String pattern = likeHelper.buildLikePattern(codigo, likeHelper.MatchMode.ANYWHERE, false);
      if (pattern == null) return cb.conjunction();
      return cb.like(cb.lower(root.get("codigoProducto")), pattern, '\\');
    };
  }

  /**
   * Búsqueda flexible en código de producto del proveedor (búsqueda tradicional LIKE %texto%)
   *
   * @param codigoProv Código del proveedor a buscar
   * @return Specification para buscar en código de proveedor
   */
  public static Specification<erpProductos> codigoProductoProveedorContains(String codigoProv) {
    return (root, query, cb) -> {
      if (codigoProv == null || codigoProv.trim().isEmpty()) {
        return cb.conjunction();
      }
      String pattern = likeHelper.buildLikePattern(codigoProv, likeHelper.MatchMode.ANYWHERE, false);
      if (pattern == null) return cb.conjunction();
      return cb.like(cb.lower(root.get("codigoProductoProveedor")), pattern, '\\');
    };
  }

  /**
   * Filtro por estado (búsqueda exacta)
   *
   * @param estado Estado del producto (1=activo, 0=inactivo)
   * @return Specification para filtrar por estado
   */
  public static Specification<erpProductos> estadoEquals(int estado) {
    return (root, query, cb) -> cb.equal(root.get("estado").as(Integer.class), estado);
  }

  /**
   * Filtro por ID de producto (búsqueda exacta)
   *
   * @param id ID del producto
   * @return Specification para filtrar por ID exacto
   */
  public static Specification<erpProductos> idProductoContains(Long id) {
    return (root, query, cb) -> {
      if (id == null) {
        return cb.conjunction();
      }
      return cb.equal(root.get("idProducto"), id);
    };
  }

  /**
   * Búsqueda combinada en todos los campos del producto (descripción, código y código proveedor)
   * Esta versión también separa por palabras y busca en todos los campos
   *
   * @param textoBusqueda Texto a buscar en todos los campos
   * @return Specification para búsqueda multicampo
   *
   * @example
   * textoBusqueda = "teclado usb"
   * Genera: (descripcion LIKE '%teclado%' OR codigo LIKE '%teclado%' OR codigoProveedor LIKE '%teclado%')
   *         AND
   *         (descripcion LIKE '%usb%' OR codigo LIKE '%usb%' OR codigoProveedor LIKE '%usb%')
   */
  public static Specification<erpProductos> busquedaMulticampoFlexible(String textoBusqueda) {
    return (root, query, cb) -> {
      if (textoBusqueda == null || textoBusqueda.trim().isEmpty()) {
        return cb.conjunction();
      }

      // Dividir la frase en palabras
      String[] palabras = textoBusqueda.toLowerCase().trim().split("\\s+");

      // Para cada palabra, debe cumplir AL MENOS UNO de los campos
      List<Predicate> palabrasPredicates = new ArrayList<>();

      for (String palabra : palabras) {
        String palabraEscapada = likeHelper.escapeForLike(palabra);
        String pattern = "%" + palabraEscapada + "%";

        // Crear predicado OR para esta palabra (busca en varios campos)
        Predicate palabraPredicate = cb.or(
          cb.like(cb.lower(root.get("descripcionProducto")), pattern, '\\'),
          cb.like(cb.lower(root.get("codigoProducto")), pattern, '\\'),
          cb.like(cb.lower(root.get("codigoProductoProveedor")), pattern, '\\')
        );
        palabrasPredicates.add(palabraPredicate);
      }

      // TODAS las palabras deben cumplirse (AND entre palabras)
      return cb.and(palabrasPredicates.toArray(new Predicate[0]));
    };
  }

  /**
   * Búsqueda por rango de precios de compra
   *
   * @param precioMin Precio mínimo (inclusive)
   * @param precioMax Precio máximo (inclusive)
   * @return Specification para filtrar por rango de precios
   */
  public static Specification<erpProductos> precioCompraBetween(Double precioMin, Double precioMax) {
    return (root, query, cb) -> {
      if (precioMin == null && precioMax == null) {
        return cb.conjunction();
      }
      if (precioMin != null && precioMax != null) {
        return cb.between(root.get("precioCompra"), precioMin, precioMax);
      } else if (precioMin != null) {
        return cb.greaterThanOrEqualTo(root.get("precioCompra"), precioMin);
      } else {
        return cb.lessThanOrEqualTo(root.get("precioCompra"), precioMax);
      }
    };
  }

  /**
   * Búsqueda por rango de precios de venta
   *
   * @param precioMin Precio mínimo (inclusive)
   * @param precioMax Precio máximo (inclusive)
   * @return Specification para filtrar por rango de precios de venta
   */
  public static Specification<erpProductos> precioVentaBetween(Double precioMin, Double precioMax) {
    return (root, query, cb) -> {
      if (precioMin == null && precioMax == null) {
        return cb.conjunction();
      }
      if (precioMin != null && precioMax != null) {
        return cb.between(root.get("precioVenta"), precioMin, precioMax);
      } else if (precioMin != null) {
        return cb.greaterThanOrEqualTo(root.get("precioVenta"), precioMin);
      } else {
        return cb.lessThanOrEqualTo(root.get("precioVenta"), precioMax);
      }
    };
  }

  /**
   * Filtro por unidad de medida exacta
   *
   * @param unidadDeMedida Unidad de medida del producto
   * @return Specification para filtrar por unidad de medida
   */
  public static Specification<erpProductos> unidadDeMedidaEquals(Integer unidadDeMedida) {
    return (root, query, cb) -> {
      if (unidadDeMedida == null) {
        return cb.conjunction();
      }
      return cb.equal(root.get("unidadDeMedida").as(Integer.class), unidadDeMedida);
    };
  }

  /**
   * Búsqueda por rango de fechas de modificación
   *
   * @param desde Fecha inicial (inclusive)
   * @param hasta Fecha final (inclusive)
   * @return Specification para filtrar por rango de fechas
   */
  public static Specification<erpProductos> fechaModificacionBetween(java.time.LocalDate desde, java.time.LocalDate hasta) {
    return (root, query, cb) -> {
      if (desde == null && hasta == null) {
        return cb.conjunction();
      }
      if (desde != null && hasta != null) {
        return cb.between(root.get("fechaModificacion"), desde, hasta);
      } else if (desde != null) {
        return cb.greaterThanOrEqualTo(root.get("fechaModificacion"), desde);
      } else {
        return cb.lessThanOrEqualTo(root.get("fechaModificacion"), hasta);
      }
    };
  }
}
