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
   * Búsqueda OPTIMIZADA con OR entre campos:
   * - Las palabras de descripción usan CRUCE DE IDs (AND entre palabras)
   * - Los diferentes campos (descripción, código, código proveedor, ubicación) se combinan con OR
   *
   * @param descripcion Texto a buscar en descripción (se separa por palabras)
   * @param codigoProducto Código de producto
   * @param codigoProductoProveedor Código de proveedor
   * @param idUbicacion Ubicación
   * @param pageable Paginación
   * @return Page<erpInventario> Resultados optimizados
   */
  public Page<erpInventario> buscarPorPalabrasEnDescripcion(
    String descripcion,
    String codigoProducto,
    String codigoProductoProveedor,
    Integer idUbicacion,
    Pageable pageable) {

    // Colección de todos los IDs que cumplen AL MENOS UNA condición
    Set<Long> idsTotales = new HashSet<>();

    // ========== 1. BÚSQUEDA POR DESCRIPCIÓN (con cruce de IDs) ==========
    if (descripcion != null && !descripcion.trim().isEmpty()) {
      Set<Long> idsDescripcion = buscarPorDescripcionOptimizado(
        descripcion,
        codigoProducto,
        codigoProductoProveedor,
        idUbicacion
      );
      idsTotales.addAll(idsDescripcion);
    }

    // ========== 2. BÚSQUEDA POR CÓDIGO DE PRODUCTO ==========
    if (codigoProducto != null && !codigoProducto.trim().isEmpty()) {
      Specification<erpInventario> specCodigo = Specification
        .where(erpInventarioSpecs.estadoEquals(1))
        .and(erpInventarioSpecs.codigoProductoContains(codigoProducto));

      // Aplicar filtros adicionales si existen
      if (idUbicacion != null) {
        specCodigo = specCodigo.and(erpInventarioSpecs.idUbicacionEquals(idUbicacion));
      }
      if (codigoProductoProveedor != null && !codigoProductoProveedor.trim().isEmpty()) {
        specCodigo = specCodigo.and(erpInventarioSpecs.codigoProductoProveedorContains(codigoProductoProveedor));
      }

      Set<Long> idsCodigo = repinv.findAll(specCodigo).stream()
        .map(erpInventario::getIdInventario)
        .collect(Collectors.toSet());

      idsTotales.addAll(idsCodigo);
    }

    // ========== 3. BÚSQUEDA POR CÓDIGO DE PROVEEDOR ==========
    if (codigoProductoProveedor != null && !codigoProductoProveedor.trim().isEmpty()) {
      Specification<erpInventario> specProv = Specification
        .where(erpInventarioSpecs.estadoEquals(1))
        .and(erpInventarioSpecs.codigoProductoProveedorContains(codigoProductoProveedor));

      // Aplicar filtros adicionales si existen
      if (idUbicacion != null) {
        specProv = specProv.and(erpInventarioSpecs.idUbicacionEquals(idUbicacion));
      }
      if (codigoProducto != null && !codigoProducto.trim().isEmpty()) {
        specProv = specProv.and(erpInventarioSpecs.codigoProductoContains(codigoProducto));
      }

      Set<Long> idsProveedor = repinv.findAll(specProv).stream()
        .map(erpInventario::getIdInventario)
        .collect(Collectors.toSet());

      idsTotales.addAll(idsProveedor);
    }

    // ========== 4. BÚSQUEDA POR UBICACIÓN (solo si no hay otros filtros) ==========
    // Esto evita duplicar resultados cuando ya tenemos otros filtros
    boolean hayOtrosFiltros = (descripcion != null && !descripcion.trim().isEmpty()) ||
      (codigoProducto != null && !codigoProducto.trim().isEmpty()) ||
      (codigoProductoProveedor != null && !codigoProductoProveedor.trim().isEmpty());

    if (idUbicacion != null && !hayOtrosFiltros) {
      Specification<erpInventario> specUbicacion = Specification
        .where(erpInventarioSpecs.estadoEquals(1))
        .and(erpInventarioSpecs.idUbicacionEquals(idUbicacion));

      Set<Long> idsUbicacion = repinv.findAll(specUbicacion).stream()
        .map(erpInventario::getIdInventario)
        .collect(Collectors.toSet());

      idsTotales.addAll(idsUbicacion);
    }

    // Si no hay resultados
    if (idsTotales.isEmpty()) {
      return Page.empty(pageable);
    }

    // Paginación y ordenamiento
    return paginarResultados(idsTotales, pageable);
  }

  /**
   * Búsqueda OPTIMIZADA por descripción usando CRUCE DE IDs
   * Cada palabra se busca individualmente y luego se cruzan los resultados (AND)
   * Los filtros adicionales (código, proveedor, ubicación) se aplican a CADA palabra
   */
  private Set<Long> buscarPorDescripcionOptimizado(
    String descripcion,
    String codigoProducto,
    String codigoProductoProveedor,
    Integer idUbicacion) {

    String[] palabras = descripcion.toLowerCase().trim().split("\\s+");


    List<Set<Long>> resultadosPorPalabra = new ArrayList<>();


    for (String palabra : palabras) {
      // Base: estado activo + búsqueda de la palabra actual
      Specification<erpInventario> specPalabra = Specification
        .where(erpInventarioSpecs.estadoEquals(1))
        .and(erpInventarioSpecs.buscarPalabraEnDescripcion(palabra));

      // Aplicar filtros adicionales (pero SOLO los que NO son la descripción)
      if (codigoProducto != null && !codigoProducto.trim().isEmpty()) {
        specPalabra = specPalabra.and(erpInventarioSpecs.codigoProductoContains(codigoProducto));
      }

      if (codigoProductoProveedor != null && !codigoProductoProveedor.trim().isEmpty()) {
        specPalabra = specPalabra.and(erpInventarioSpecs.codigoProductoProveedorContains(codigoProductoProveedor));
      }

      if (idUbicacion != null) {
        specPalabra = specPalabra.and(erpInventarioSpecs.idUbicacionEquals(idUbicacion));
      }

      // Ejecutar consulta y obtener IDs
      Set<Long> ids = repinv.findAll(specPalabra).stream()

        .map(erpInventario::getIdInventario)
        .collect(Collectors.toSet());

      resultadosPorPalabra.add(ids);

      // Optimización: si alguna palabra no tiene resultados, terminar temprano
      if (ids.isEmpty()) {
        return new HashSet<>();
      }
    }

    // CRUCE DE IDs (AND entre palabras)
    Set<Long> idsFinales = new HashSet<>(resultadosPorPalabra.get(0));
    for (int i = 1; i < resultadosPorPalabra.size(); i++) {
      idsFinales.retainAll(resultadosPorPalabra.get(i));
      if (idsFinales.isEmpty()) {
        break;
      }
    }

    return idsFinales;
  }

  /**
   * Paginación eficiente de resultados manteniendo el orden
   */
  private Page<erpInventario> paginarResultados(Set<Long> idsTotales, Pageable pageable) {
    List<Long> idsList = new ArrayList<>(idsTotales);

    // Aplicar ordenamiento según el pageable

    if (pageable.getSort().isSorted()) {
      // Para ordenamiento por campos específicos, necesitamos obtener los registros completos
      // y ordenarlos según el criterio del pageable
      List<erpInventario> todosLosRegistros = repinv.findAllById(idsList);

      // Ordenar según el criterio del pageable
      Comparator<erpInventario> comparator = null;
      for (org.springframework.data.domain.Sort.Order order : pageable.getSort()) {
        Comparator<erpInventario> orderComparator = null;
        switch (order.getProperty()) {
          case "idInventario":
            orderComparator = Comparator.comparing(erpInventario::getIdInventario);
            break;
          case "cantidadExistencias":
            orderComparator = Comparator.comparing(erpInventario::getCantidadExistencias);
            break;
          case "cantidadDanados":
            orderComparator = Comparator.comparing(erpInventario::getCantidadDanados);
            break;
          case "idUbicacion":
            orderComparator = Comparator.comparing(erpInventario::getIdUbicacion);
            break;
          default:
            orderComparator = Comparator.comparing(erpInventario::getIdInventario);
        }

        if (order.isDescending()) {
          orderComparator = orderComparator.reversed();
        }

        if (comparator == null) {
          comparator = orderComparator;
        } else {
          comparator = comparator.thenComparing(orderComparator);
        }
      }

      if (comparator != null) {
        todosLosRegistros.sort(comparator);
      }

      // Paginar
      int start = (int) pageable.getOffset();
      int end = Math.min((start + pageable.getPageSize()), todosLosRegistros.size());

      if (start >= todosLosRegistros.size()) {
        return Page.empty(pageable);
      }

      List<erpInventario> resultadosPaginados = todosLosRegistros.subList(start, end);
      return new PageImpl<>(resultadosPaginados, pageable, todosLosRegistros.size());

    } else {
      // Sin ordenamiento específico, ordenar por ID
      idsList.sort(Long::compareTo);

      int start = (int) pageable.getOffset();
      int end = Math.min((start + pageable.getPageSize()), idsList.size());

      if (start >= idsList.size()) {
        return Page.empty(pageable);
      }

      List<Long> idsPaginados = idsList.subList(start, end);
      List<erpInventario> resultadosFinales = repinv.findAllById(idsPaginados);

      // Mantener el orden de los IDs
      Map<Long, erpInventario> mapaResultados = resultadosFinales.stream()
        .collect(Collectors.toMap(erpInventario::getIdInventario, r -> r));

      List<erpInventario> resultadosOrdenados = idsPaginados.stream()
        .map(mapaResultados::get)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());

      return new PageImpl<>(resultadosOrdenados, pageable, idsList.size());
    }
















  }
}
