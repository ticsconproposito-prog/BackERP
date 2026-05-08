package BackERP.service;

import BackERP.models.erpInventario;
import BackERP.repository.RepositoryInventario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class InventarioBusquedaService {

  @Autowired
  private RepositoryInventario repinv;

  /**
   * Búsqueda OPTIMIZADA - TODAS las palabras deben coincidir en ALGÚN campo
   * No se permite que una palabra esté en un campo y otra en otro campo diferente
   */
  public Page<erpInventario> buscarPorPalabrasEnDescripcion(
    String descripcion,
    String codigoProducto,
    String codigoProductoProveedor,
    Integer idUbicacion,
    Pageable pageable) {

    Specification<erpInventario> spec = (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();

      // Estado activo SIEMPRE
      predicates.add(cb.equal(root.get("estado"), 1));

      // Filtro de ubicación (AND obligatorio si viene)
      if (idUbicacion != null) {
        predicates.add(cb.equal(root.get("idUbicacion"), idUbicacion));
      }

      // Grupo de filtros OR - cada filtro es INDEPENDIENTE
      List<Predicate> orPredicates = new ArrayList<>();

      // 🔥 1. Búsqueda por código de producto (texto completo)
      if (codigoProducto != null && !codigoProducto.trim().isEmpty()) {
        String pattern = "%" + escapeLike(codigoProducto.toLowerCase().trim()) + "%";
        orPredicates.add(cb.like(
          cb.lower(root.get("idProducto").get("codigoProducto")),
          pattern, '\\'
        ));
      }

      // 🔥 2. Búsqueda por código de proveedor (texto completo)
      if (codigoProductoProveedor != null && !codigoProductoProveedor.trim().isEmpty()) {
        String pattern = "%" + escapeLike(codigoProductoProveedor.toLowerCase().trim()) + "%";
        orPredicates.add(cb.like(
          cb.lower(root.get("idProducto").get("codigoProductoProveedor")),
          pattern, '\\'
        ));
      }

      // 🔥 3. Búsqueda por descripción (TODAS las palabras en el MISMO campo)
      if (descripcion != null && !descripcion.trim().isEmpty()) {
        String[] palabras = descripcion.toLowerCase().trim().split("\\s+");

        // Construir condición: cada palabra debe estar en la descripción
        List<Predicate> descripcionPredicates = new ArrayList<>();
        for (String palabra : palabras) {
          if (!palabra.isEmpty()) {
            String pattern = "%" + escapeLike(palabra) + "%";
            descripcionPredicates.add(cb.like(
              cb.lower(root.get("idProducto").get("descripcionProducto")),
              pattern, '\\'
            ));
          }
        }

        if (!descripcionPredicates.isEmpty()) {
          // AND entre todas las palabras en la descripción
          orPredicates.add(cb.and(descripcionPredicates.toArray(new Predicate[0])));
        }
      }

      // Si hay filtros, aplicamos OR entre ellos
      if (!orPredicates.isEmpty()) {
        predicates.add(cb.or(orPredicates.toArray(new Predicate[0])));
      } else {
        // Sin filtros de texto
        boolean hayFiltrosTexto = (descripcion != null && !descripcion.trim().isEmpty()) ||
          (codigoProducto != null && !codigoProducto.trim().isEmpty()) ||
          (codigoProductoProveedor != null && !codigoProductoProveedor.trim().isEmpty());

        if (hayFiltrosTexto) {
          predicates.add(cb.disjunction());
        }
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };

    return repinv.findAll(spec, pageable);
  }

  /**
   * Búsqueda EXACTA por frase completa (sin dividir palabras)
   * Útil cuando quieres buscar la frase exacta en cualquier campo
   */
  public Page<erpInventario> buscarPorFraseExacta(
    String textoBusqueda,
    Integer idUbicacion,
    Pageable pageable) {

    if (textoBusqueda == null || textoBusqueda.trim().isEmpty()) {
      return buscarPorPalabrasEnDescripcion(null, null, null, idUbicacion, pageable);
    }

    String textoBusquedaLower = escapeLike(textoBusqueda.toLowerCase().trim());
    String pattern = "%" + textoBusquedaLower + "%";

    Specification<erpInventario> spec = (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();
      predicates.add(cb.equal(root.get("estado"), 1));

      if (idUbicacion != null) {
        predicates.add(cb.equal(root.get("idUbicacion"), idUbicacion));
      }

      // Buscar la frase exacta en CUALQUIER campo
      predicates.add(cb.or(
        cb.like(cb.lower(root.get("idProducto").get("codigoProducto")), pattern, '\\'),
        cb.like(cb.lower(root.get("idProducto").get("codigoProductoProveedor")), pattern, '\\'),
        cb.like(cb.lower(root.get("idProducto").get("descripcionProducto")), pattern, '\\')
      ));

      return cb.and(predicates.toArray(new Predicate[0]));
    };

    return repinv.findAll(spec, pageable);
  }

  private String escapeLike(String value) {
    return value.replace("\\", "\\\\")
      .replace("%", "\\%")
      .replace("_", "\\_");
  }
}
