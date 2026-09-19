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
   *
   * @param descripcion Texto para buscar en descripción (todas las palabras deben coincidir)
   * @param codigoProducto Código del producto
   * @param codigoProductoProveedor Código del proveedor
   * @param idUbicacion ID de ubicación
   * @param estadoExcluir Estado que se quiere EXCLUIR (si viene, trae todos los que NO sean este estado)
   * @param pageable Paginación y ordenamiento
   */
  public Page<erpInventario> buscarPorPalabrasEnDescripcion(
    String descripcion,
    String codigoProducto,
    String codigoProductoProveedor,
    Integer idUbicacion,
    Integer estadoExcluir,
    Pageable pageable) {

    Specification<erpInventario> spec = (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();

      // 🔥 FILTRO DE ESTADO - Opción B
      // Si viene estadoExcluir → excluir ese estado
      // Si NO viene (null) → mostrar TODOS
      if (estadoExcluir != null) {
        predicates.add(cb.notEqual(
          root.get("estado").as(Integer.class),   // ← FORZAR TIPO
          estadoExcluir
        ));
      }

      // Filtro de ubicación
      if (idUbicacion != null) {
        predicates.add(cb.equal(
          root.get("idUbicacion").as(Integer.class),
          idUbicacion
        ));
      }

      // Grupo de filtros OR - cada filtro es INDEPENDIENTE
      List<Predicate> orPredicates = new ArrayList<>();

      // 1. Búsqueda por código de producto
      if (codigoProducto != null && !codigoProducto.trim().isEmpty()) {
        String pattern = "%" + escapeLike(codigoProducto.toLowerCase().trim()) + "%";
        orPredicates.add(cb.like(
          cb.lower(root.get("idProducto").get("codigoProducto")),
          pattern, '\\'
        ));
      }

      // 2. Búsqueda por código de proveedor
      if (codigoProductoProveedor != null && !codigoProductoProveedor.trim().isEmpty()) {
        String pattern = "%" + escapeLike(codigoProductoProveedor.toLowerCase().trim()) + "%";
        orPredicates.add(cb.like(
          cb.lower(root.get("idProducto").get("codigoProductoProveedor")),
          pattern, '\\'
        ));
      }

      // 3. Búsqueda por descripción (TODAS las palabras deben coincidir)
      if (descripcion != null && !descripcion.trim().isEmpty()) {
        String[] palabras = descripcion.toLowerCase().trim().split("\\s+");

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
          orPredicates.add(cb.and(descripcionPredicates.toArray(new Predicate[0])));
        }
      }

      // Si hay filtros de texto, aplicar OR entre ellos
      if (!orPredicates.isEmpty()) {
        predicates.add(cb.or(orPredicates.toArray(new Predicate[0])));
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };

    return repinv.findAll(spec, pageable);
  }

  /**
   * Búsqueda EXACTA por frase completa (sin dividir palabras)
   * Útil cuando quieres buscar la frase exacta en cualquier campo
   *
   * @param textoBusqueda Texto a buscar exactamente
   * @param idUbicacion ID de ubicación
   * @param estadoExcluir Estado que se quiere EXCLUIR (si viene, trae todos los que NO sean este estado)
   * @param pageable Paginación y ordenamiento
   */
  public Page<erpInventario> buscarPorFraseExacta(
    String textoBusqueda,
    Integer idUbicacion,
    Integer estadoExcluir,
    Pageable pageable) {

    if (textoBusqueda == null || textoBusqueda.trim().isEmpty()) {
      return buscarPorPalabrasEnDescripcion(null, null, null, idUbicacion, estadoExcluir, pageable);
    }

    String textoBusquedaLower = escapeLike(textoBusqueda.toLowerCase().trim());
    String pattern = "%" + textoBusquedaLower + "%";

    Specification<erpInventario> spec = (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();

      // 🔥 FILTRO DE ESTADO - Opción B
      if (estadoExcluir != null) {
        predicates.add(cb.notEqual(
          root.get("estado").as(Integer.class),
          estadoExcluir
        ));
      }

      if (idUbicacion != null) {
        predicates.add(cb.equal(
          root.get("idUbicacion").as(Integer.class),
          idUbicacion
        ));
      }

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
