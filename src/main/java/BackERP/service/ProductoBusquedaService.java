package BackERP.service;

import BackERP.helper.erpProductosSpecs;
import BackERP.models.erpProductos;
import BackERP.repository.RepositoryProductos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductoBusquedaService {

  @Autowired
  private RepositoryProductos repoProductos;

  /**
   * Búsqueda OPTIMIZADA con OR entre campos - PAGINACIÓN DIRECTA EN BD
   * UNA SOLA consulta SQL con paginación, sin cargar toda la tabla en memoria
   *
   * @param codigoProducto Código del producto
   * @param codigoProductoProveedor Código del producto del proveedor
   * @param descripcionProducto Descripción del producto
   * @param idProducto ID exacto del producto (opcional)
   * @param pageable Paginación y ordenamiento
   * @return Página de resultados paginados directamente desde la BD
   */
  public Page<erpProductos> buscarProductos(
    String codigoProducto,
    String codigoProductoProveedor,
    String descripcionProducto,
    Long idProducto,
    Pageable pageable) {

    Specification<erpProductos> spec = (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();

      // Estado activo SIEMPRE
      predicates.add(cb.equal(root.get("estado"), 1));

      // Filtro por ID exacto (si viene, es prioritario)
      if (idProducto != null) {
        predicates.add(cb.equal(root.get("idProducto"), idProducto));
      }

      // Grupo de filtros OR (por lo menos uno debe cumplirse)
      List<Predicate> orPredicates = new ArrayList<>();

      // Búsqueda por código de producto
      if (codigoProducto != null && !codigoProducto.trim().isEmpty()) {
        String pattern = "%" + escapeLike(codigoProducto.toLowerCase().trim()) + "%";
        orPredicates.add(cb.like(
          cb.lower(root.get("codigoProducto")),
          pattern, '\\'
        ));
      }

      // Búsqueda por código de proveedor
      if (codigoProductoProveedor != null && !codigoProductoProveedor.trim().isEmpty()) {
        String pattern = "%" + escapeLike(codigoProductoProveedor.toLowerCase().trim()) + "%";
        orPredicates.add(cb.like(
          cb.lower(root.get("codigoProductoProveedor")),
          pattern, '\\'
        ));
      }

      // Búsqueda por descripción (TODAS las palabras deben estar presentes - AND)
      if (descripcionProducto != null && !descripcionProducto.trim().isEmpty()) {
        String[] palabras = descripcionProducto.toLowerCase().trim().split("\\s+");
        for (String palabra : palabras) {
          if (!palabra.isEmpty()) {
            String pattern = "%" + escapeLike(palabra) + "%";
            orPredicates.add(cb.like(
              cb.lower(root.get("descripcionProducto")),
              pattern, '\\'
            ));
          }
        }
      }

      // Si hay filtros OR, agregamos la condición de que al menos uno se cumpla
      if (!orPredicates.isEmpty()) {
        predicates.add(cb.or(orPredicates.toArray(new Predicate[0])));
      } else if (idProducto == null) {
        // Si NO hay NINGÚN filtro (excepto posible ID), devolvemos TODOS los activos
        // No agregamos condiciones adicionales
        boolean hayFiltros = (codigoProducto != null && !codigoProducto.trim().isEmpty()) ||
          (codigoProductoProveedor != null && !codigoProductoProveedor.trim().isEmpty()) ||
          (descripcionProducto != null && !descripcionProducto.trim().isEmpty());

        if (hayFiltros) {
          // Si hay filtros pero todos vacíos, devolver nada
          predicates.add(cb.disjunction());
        }
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };

    return repoProductos.findAll(spec, pageable);
  }

  /**
   * Búsqueda SOLO por texto (para autocompletado o búsquedas rápidas)
   * Versión simplificada que busca en todos los campos de texto a la vez
   *
   * @param textoBusqueda Texto a buscar en código, código proveedor y descripción
   * @param pageable Paginación y ordenamiento
   * @return Página de resultados
   */
  public Page<erpProductos> buscarPorTextoLibre(String textoBusqueda, Pageable pageable) {
    if (textoBusqueda == null || textoBusqueda.trim().isEmpty()) {
      // Sin búsqueda, devolver todos los activos
      return repoProductos.findAll(
        (root, query, cb) -> cb.equal(root.get("estado"), 1),
        pageable
      );
    }

    Specification<erpProductos> spec = (root, query, cb) -> {
      String pattern = "%" + escapeLike(textoBusqueda.toLowerCase().trim()) + "%";

      return cb.and(
        cb.equal(root.get("estado"), 1),
        cb.or(
          cb.like(cb.lower(root.get("codigoProducto")), pattern, '\\'),
          cb.like(cb.lower(root.get("codigoProductoProveedor")), pattern, '\\'),
          cb.like(cb.lower(root.get("descripcionProducto")), pattern, '\\')
        )
      );
    };

    return repoProductos.findAll(spec, pageable);
  }

  /**
   * Escapa caracteres especiales de LIKE para evitar SQL injection y errores
   */
  private String escapeLike(String value) {
    return value.replace("\\", "\\\\")
      .replace("%", "\\%")
      .replace("_", "\\_");
  }
}
