package BackERP.service;

import BackERP.helper.erpProductosSpecs;
import BackERP.models.erpProductos;
import BackERP.repository.RepositoryProductos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductoBusquedaService {

  @Autowired
  private RepositoryProductos repoProductos;

  /**
   * Búsqueda OPTIMIZADA con OR entre campos
   * Si no viene NINGÚN parámetro, devuelve TODOS los productos activos
   */
  public Page<erpProductos> buscarProductos(
    String codigoProducto,
    String codigoProductoProveedor,
    String descripcionProducto,
    Long idProducto,
    Pageable pageable) {

    // 🔥 NUEVO: Verificar si NO hay NINGÚN filtro
    boolean hayFiltros = (codigoProducto != null && !codigoProducto.trim().isEmpty()) ||
      (codigoProductoProveedor != null && !codigoProductoProveedor.trim().isEmpty()) ||
      (descripcionProducto != null && !descripcionProducto.trim().isEmpty()) ||
      (idProducto != null);

    // Si no hay filtros, devolver TODOS los productos activos
    if (!hayFiltros) {
      Specification<erpProductos> spec = Specification
        .where(erpProductosSpecs.estadoEquals(1));
      return repoProductos.findAll(spec, pageable);
    }

    Set<Long> idsTotales = new HashSet<>();

    // Búsqueda por código de producto
    if (codigoProducto != null && !codigoProducto.trim().isEmpty()) {
      Specification<erpProductos> spec = Specification
        .where(erpProductosSpecs.estadoEquals(1))
        .and(erpProductosSpecs.codigoProductoContains(codigoProducto));

      Set<Long> ids = repoProductos.findAll(spec).stream()
        .map(erpProductos::getIdProducto)
        .collect(Collectors.toSet());
      idsTotales.addAll(ids);
    }

    // Búsqueda por código de proveedor
    if (codigoProductoProveedor != null && !codigoProductoProveedor.trim().isEmpty()) {
      Specification<erpProductos> spec = Specification
        .where(erpProductosSpecs.estadoEquals(1))
        .and(erpProductosSpecs.codigoProductoProveedorContains(codigoProductoProveedor));

      Set<Long> ids = repoProductos.findAll(spec).stream()
        .map(erpProductos::getIdProducto)
        .collect(Collectors.toSet());
      idsTotales.addAll(ids);
    }

    // Búsqueda por descripción (con optimización de palabras)
    if (descripcionProducto != null && !descripcionProducto.trim().isEmpty()) {
      Set<Long> idsDescripcion = buscarPorDescripcionProducto(descripcionProducto);
      idsTotales.addAll(idsDescripcion);
    }

    // Búsqueda por ID exacto
    if (idProducto != null) {
      Optional<erpProductos> producto = repoProductos.findById(idProducto);
      producto.ifPresent(p -> idsTotales.add(p.getIdProducto()));
    }

    if (idsTotales.isEmpty()) {
      return Page.empty(pageable);
    }

    return paginarResultadosProductos(idsTotales, pageable);
  }

  /**
   * Búsqueda optimizada por descripción con CRUCE de IDs
   */
  private Set<Long> buscarPorDescripcionProducto(String descripcion) {
    String[] palabras = descripcion.toLowerCase().trim().split("\\s+");
    List<Set<Long>> resultadosPorPalabra = new ArrayList<>();

    for (String palabra : palabras) {
      Specification<erpProductos> spec = Specification
        .where(erpProductosSpecs.estadoEquals(1))
        .and(erpProductosSpecs.buscarPalabraEnDescripcion(palabra));

      Set<Long> ids = repoProductos.findAll(spec).stream()
        .map(erpProductos::getIdProducto)
        .collect(Collectors.toSet());

      resultadosPorPalabra.add(ids);

      if (ids.isEmpty()) {
        return new HashSet<>();
      }
    }

    // CRUCE de IDs
    Set<Long> idsFinales = new HashSet<>(resultadosPorPalabra.get(0));
    for (int i = 1; i < resultadosPorPalabra.size(); i++) {
      idsFinales.retainAll(resultadosPorPalabra.get(i));
      if (idsFinales.isEmpty()) {
        break;
      }
    }

    return idsFinales;
  }

  private Page<erpProductos> paginarResultadosProductos(Set<Long> idsTotales, Pageable pageable) {
    List<Long> idsList = new ArrayList<>(idsTotales);
    idsList.sort(Long::compareTo);

    int start = (int) pageable.getOffset();
    int end = Math.min((start + pageable.getPageSize()), idsList.size());

    if (start >= idsList.size()) {
      return Page.empty(pageable);
    }

    List<Long> idsPaginados = idsList.subList(start, end);
    List<erpProductos> resultadosFinales = repoProductos.findAllById(idsPaginados);

    Map<Long, erpProductos> mapaResultados = resultadosFinales.stream()
      .collect(Collectors.toMap(erpProductos::getIdProducto, r -> r));

    List<erpProductos> resultadosOrdenados = idsPaginados.stream()
      .map(mapaResultados::get)
      .filter(Objects::nonNull)
      .collect(Collectors.toList());

    return new PageImpl<>(resultadosOrdenados, pageable, idsTotales.size());
  }
}
