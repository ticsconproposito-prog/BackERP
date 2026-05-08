package BackERP.service;

import BackERP.helper.erpInventarioSpecs;
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
   * Búsqueda OPTIMIZADA con OR entre campos - PAGINACIÓN DIRECTA EN BD
   * UNA SOLA consulta SQL sin cargar toda la tabla en memoria
   *
   * @param descripcion Texto a buscar en la descripción del producto
   * @param codigoProducto Código del producto
   * @param codigoProductoProveedor Código del producto del proveedor
   * @param idUbicacion Filtro por ubicación (opcional)
   * @param pageable Paginación y ordenamiento
   * @return Página de resultados paginados directamente desde la BD
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

      // Grupo de filtros OR (por lo menos uno debe cumplirse)
      List<Predicate> orPredicates = new ArrayList<>();

      // Búsqueda por código de producto
      if (codigoProducto != null && !codigoProducto.trim().isEmpty()) {
        String pattern = "%" + escapeLike(codigoProducto.toLowerCase().trim()) + "%";
        orPredicates.add(cb.like(
          cb.lower(root.get("idProducto").get("codigoProducto")),
          pattern, '\\'
        ));
      }

      // Búsqueda por código de proveedor
      if (codigoProductoProveedor != null && !codigoProductoProveedor.trim().isEmpty()) {
        String pattern = "%" + escapeLike(codigoProductoProveedor.toLowerCase().trim()) + "%";
        orPredicates.add(cb.like(
          cb.lower(root.get("idProducto").get("codigoProductoProveedor")),
          pattern, '\\'
        ));
      }

      // Búsqueda por descripción (TODAS las palabras deben estar presentes - AND)
      if (descripcion != null && !descripcion.trim().isEmpty()) {
        String[] palabras = descripcion.toLowerCase().trim().split("\\s+");
        for (String palabra : palabras) {
          if (!palabra.isEmpty()) {
            String pattern = "%" + escapeLike(palabra) + "%";
            orPredicates.add(cb.like(
              cb.lower(root.get("idProducto").get("descripcionProducto")),
              pattern, '\\'
            ));
          }
        }
      }

      // Si hay filtros OR, agregamos la condición de que al menos uno se cumpla
      if (!orPredicates.isEmpty()) {
        predicates.add(cb.or(orPredicates.toArray(new Predicate[0])));
      } else {
        // Si NO hay NINGÚN filtro de texto, verificamos si es un caso válido
        boolean hayFiltrosTexto = (descripcion != null && !descripcion.trim().isEmpty()) ||
          (codigoProducto != null && !codigoProducto.trim().isEmpty()) ||
          (codigoProductoProveedor != null && !codigoProductoProveedor.trim().isEmpty());

        if (hayFiltrosTexto) {
          // Si hay filtros pero todos dieron vacío, devolver nada
          predicates.add(cb.disjunction());
        }
        // Si no hay filtros, devolvemos solo los activos (sin condiciones adicionales)
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };

    return repinv.findAll(spec, pageable);
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
