package BackERP.service;

import BackERP.helper.erpInventarioSpecs;
import BackERP.models.erpInventario;
import BackERP.repository.RepositoryInventario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InventarioBusquedaService {

  @Autowired
  private RepositoryInventario repinv;

  /**
   * Búsqueda que combina todos los filtros con OR
   * - Si vienen múltiples filtros, hace OR entre ellos
   * - La ubicación es filtro adicional (AND) si viene
   */
  public Page<erpInventario> buscarPorPalabrasEnDescripcion(
    String descripcion,
    String codigoProducto,
    String codigoProductoProveedor,
    Integer idUbicacion,
    Pageable pageable) {

    // Verificar si NO hay NINGÚN filtro
    boolean hayFiltrosTexto = (descripcion != null && !descripcion.trim().isEmpty()) ||
      (codigoProducto != null && !codigoProducto.trim().isEmpty()) ||
      (codigoProductoProveedor != null && !codigoProductoProveedor.trim().isEmpty());

    // Si no hay filtros de texto, devolver todos (con posible filtro de ubicación)
    if (!hayFiltrosTexto) {
      Specification<erpInventario> spec = Specification.where(erpInventarioSpecs.estadoEquals(1));
      if (idUbicacion != null) {
        spec = spec.and(erpInventarioSpecs.idUbicacionEquals(idUbicacion));
      }
      return repinv.findAll(spec, pageable);
    }

    // Colección para acumular todos los IDs encontrados por cualquier filtro
    Set<Long> idsTotales = new HashSet<>();

    // 🔥 1. BÚSQUEDA POR DESCRIPCIÓN (múltiples palabras con AND)
    if (descripcion != null && !descripcion.trim().isEmpty()) {
      Set<Long> idsDescripcion = buscarPorDescripcionMultiplePalabras(descripcion);
      System.out.println("IDs encontrados por descripción '" + descripcion + "': " + idsDescripcion.size());
      idsTotales.addAll(idsDescripcion);
    }

    // 🔥 2. BÚSQUEDA POR CÓDIGO PRODUCTO
    if (codigoProducto != null && !codigoProducto.trim().isEmpty()) {
      Specification<erpInventario> specCodigo = Specification
        .where(erpInventarioSpecs.estadoEquals(1))
        .and(erpInventarioSpecs.codigoProductoContains(codigoProducto));

      Set<Long> idsCodigo = repinv.findAll(specCodigo).stream()
        .map(erpInventario::getIdInventario)
        .collect(Collectors.toSet());

      System.out.println("IDs encontrados por código producto '" + codigoProducto + "': " + idsCodigo.size());
      idsTotales.addAll(idsCodigo);
    }

    // 🔥 3. BÚSQUEDA POR CÓDIGO PROVEEDOR
    if (codigoProductoProveedor != null && !codigoProductoProveedor.trim().isEmpty()) {
      Specification<erpInventario> specProveedor = Specification
        .where(erpInventarioSpecs.estadoEquals(1))
        .and(erpInventarioSpecs.codigoProductoProveedorContains(codigoProductoProveedor));

      Set<Long> idsProveedor = repinv.findAll(specProveedor).stream()
        .map(erpInventario::getIdInventario)
        .collect(Collectors.toSet());

      System.out.println("IDs encontrados por código proveedor '" + codigoProductoProveedor + "': " + idsProveedor.size());
      idsTotales.addAll(idsProveedor);
    }

    System.out.println("Total IDs únicos sin filtro de ubicación: " + idsTotales.size());

    // Si no se encontraron resultados con ningún filtro
    if (idsTotales.isEmpty()) {
      System.out.println("NO se encontraron resultados con ningún filtro");
      return Page.empty(pageable);
    }

    // 🔥 4. APLICAR FILTRO DE UBICACIÓN (si viene)
    if (idUbicacion != null) {
      System.out.println("Aplicando filtro de ubicación: " + idUbicacion);

      // Obtener los IDs que cumplen con la ubicación
      Specification<erpInventario> specUbicacion = Specification
        .where(erpInventarioSpecs.estadoEquals(1))
        .and(erpInventarioSpecs.idUbicacionEquals(idUbicacion));

      Set<Long> idsUbicacionValidos = repinv.findAll(specUbicacion).stream()
        .map(erpInventario::getIdInventario)
        .collect(Collectors.toSet());

      System.out.println("IDs válidos por ubicación: " + idsUbicacionValidos.size());

      // Intersectar: solo los que cumplen con la ubicación
      idsTotales.retainAll(idsUbicacionValidos);
      System.out.println("IDs después de filtro de ubicación: " + idsTotales.size());
    }

    if (idsTotales.isEmpty()) {
      System.out.println("NO hay resultados después de aplicar filtro de ubicación");
      return Page.empty(pageable);
    }

    return paginarResultados(idsTotales, pageable);
  }

  /**
   * Busca por descripción con AND entre múltiples palabras
   */
  private Set<Long> buscarPorDescripcionMultiplePalabras(String descripcion) {
    String[] palabras = descripcion.toLowerCase().trim().split("\\s+");

    // Construir Specification con AND entre todas las palabras
    Specification<erpInventario> specCompleta = Specification
      .where(erpInventarioSpecs.estadoEquals(1));

    // Cada palabra como condición AND
    for (String palabra : palabras) {
      specCompleta = specCompleta.and(erpInventarioSpecs.buscarPalabraEnDescripcion(palabra));
    }

    return repinv.findAll(specCompleta).stream()
      .map(erpInventario::getIdInventario)
      .collect(Collectors.toSet());
  }

  /**
   * Paginación de resultados
   */
  private Page<erpInventario> paginarResultados(Set<Long> idsTotales, Pageable pageable) {
    List<Long> idsList = new ArrayList<>(idsTotales);
    idsList.sort(Long::compareTo);

    int start = (int) pageable.getOffset();
    int end = Math.min((start + pageable.getPageSize()), idsList.size());

    if (start >= idsList.size()) {
      return Page.empty(pageable);
    }

    List<Long> idsPaginados = idsList.subList(start, end);
    List<erpInventario> resultadosFinales = repinv.findAllById(idsPaginados);

    // Mantener el orden original
    Map<Long, erpInventario> mapaResultados = resultadosFinales.stream()
      .collect(Collectors.toMap(erpInventario::getIdInventario, r -> r));

    List<erpInventario> resultadosOrdenados = idsPaginados.stream()
      .map(mapaResultados::get)
      .filter(Objects::nonNull)
      .collect(Collectors.toList());

    return new PageImpl<>(resultadosOrdenados, pageable, idsTotales.size());
  }
}
