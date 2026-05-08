package BackERP.service;

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
   * Búsqueda OPTIMIZADA - CADA filtro es independiente
   * - Si buscas por código, SOLO busca en código
   * - Si buscas por descripción, TODAS las palabras deben estar en la descripción
   * - Si buscas por código de proveedor, SOLO busca ahí
   */
  public Page<erpProductos> buscarProductos(
    String codigoProducto,
    String codigoProductoProveedor,
    String descripcionProducto,
    Long idProducto,
    Pageable pageable) {

    Specification<erpProductos> spec = (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();
      predicates.add(cb.equal(root.get("estado"), 1));

      // Filtro por ID exacto (prioritario)
      if (idProducto != null) {
        predicates.add(cb.equal(root.get("idProducto"), idProducto));
      }

      // Grupo OR para filtros de texto
      List<Predicate> orPredicates = new ArrayList<>();

      // 🔥 1. Código de producto - búsqueda por texto completo
      if (codigoProducto != null && !codigoProducto.trim().isEmpty()) {
        String pattern = "%" + escapeLike(codigoProducto.toLowerCase().trim()) + "%";
        orPredicates.add(cb.like(
          cb.lower(root.get("codigoProducto")),
          pattern, '\\'
        ));
      }

      // 🔥 2. Código de proveedor - búsqueda por texto completo
      if (codigoProductoProveedor != null && !codigoProductoProveedor.trim().isEmpty()) {
        String pattern = "%" + escapeLike(codigoProductoProveedor.toLowerCase().trim()) + "%";
        orPredicates.add(cb.like(
          cb.lower(root.get("codigoProductoProveedor")),
          pattern, '\\'
        ));
      }

      // 🔥 3. Descripción - TODAS las palabras deben estar en la descripción
      if (descripcionProducto != null && !descripcionProducto.trim().isEmpty()) {
        String[] palabras = descripcionProducto.toLowerCase().trim().split("\\s+");

        List<Predicate> descripcionPredicates = new ArrayList<>();
        for (String palabra : palabras) {
          if (!palabra.isEmpty()) {
            String pattern = "%" + escapeLike(palabra) + "%";
            descripcionPredicates.add(cb.like(
              cb.lower(root.get("descripcionProducto")),
              pattern, '\\'
            ));
          }
        }

        if (!descripcionPredicates.isEmpty()) {
          // AND entre todas las palabras - SOLO en descripción
          orPredicates.add(cb.and(descripcionPredicates.toArray(new Predicate[0])));
        }
      }

      // Aplicar OR solo si hay filtros
      if (!orPredicates.isEmpty()) {
        predicates.add(cb.or(orPredicates.toArray(new Predicate[0])));
      } else if (idProducto == null) {
        // Verificar si se enviaron filtros vacíos
        boolean hayFiltros = (codigoProducto != null && !codigoProducto.trim().isEmpty()) ||
          (codigoProductoProveedor != null && !codigoProductoProveedor.trim().isEmpty()) ||
          (descripcionProducto != null && !descripcionProducto.trim().isEmpty());

        if (hayFiltros) {
          predicates.add(cb.disjunction());
        }
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };

    return repoProductos.findAll(spec, pageable);
  }

  /**
   * Búsqueda por frase EXACTA (sin dividir palabras)
   * Busca la misma frase en cualquier campo
   */
  public Page<erpProductos> buscarPorFraseExacta(String textoBusqueda, Pageable pageable) {
    if (textoBusqueda == null || textoBusqueda.trim().isEmpty()) {
      return repoProductos.findAll(
        (root, query, cb) -> cb.equal(root.get("estado"), 1),
        pageable
      );
    }

    String pattern = "%" + escapeLike(textoBusqueda.toLowerCase().trim()) + "%";

    Specification<erpProductos> spec = (root, query, cb) -> cb.and(
      cb.equal(root.get("estado"), 1),
      cb.or(
        cb.like(cb.lower(root.get("codigoProducto")), pattern, '\\'),
        cb.like(cb.lower(root.get("codigoProductoProveedor")), pattern, '\\'),
        cb.like(cb.lower(root.get("descripcionProducto")), pattern, '\\')
      )
    );

    return repoProductos.findAll(spec, pageable);
  }

  /**
   * Búsqueda COMBINADA pero con AND entre campos (más restrictiva)
   * Útil cuando quieres que TODOS los campos coincidan
   */
  public Page<erpProductos> buscarConAndEntreCampos(
    String codigoProducto,
    String codigoProductoProveedor,
    String descripcionProducto,
    Pageable pageable) {

    Specification<erpProductos> spec = (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();
      predicates.add(cb.equal(root.get("estado"), 1));

      if (codigoProducto != null && !codigoProducto.trim().isEmpty()) {
        String pattern = "%" + escapeLike(codigoProducto.toLowerCase().trim()) + "%";
        predicates.add(cb.like(cb.lower(root.get("codigoProducto")), pattern, '\\'));
      }

      if (codigoProductoProveedor != null && !codigoProductoProveedor.trim().isEmpty()) {
        String pattern = "%" + escapeLike(codigoProductoProveedor.toLowerCase().trim()) + "%";
        predicates.add(cb.like(cb.lower(root.get("codigoProductoProveedor")), pattern, '\\'));
      }

      if (descripcionProducto != null && !descripcionProducto.trim().isEmpty()) {
        String[] palabras = descripcionProducto.toLowerCase().trim().split("\\s+");
        for (String palabra : palabras) {
          if (!palabra.isEmpty()) {
            String pattern = "%" + escapeLike(palabra) + "%";
            predicates.add(cb.like(cb.lower(root.get("descripcionProducto")), pattern, '\\'));
          }
        }
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };

    return repoProductos.findAll(spec, pageable);
  }

  private String escapeLike(String value) {
    return value.replace("\\", "\\\\")
      .replace("%", "\\%")
      .replace("_", "\\_");
  }
}
