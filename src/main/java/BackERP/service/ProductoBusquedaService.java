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

  public Page<erpProductos> buscarPorPalabrasEnDescripcion(
    String descripcion,
    String codigoProducto,
    String codigoProductoProveedor,
    Long idProducto,
    Pageable pageable) {

    if (descripcion == null || descripcion.trim().isEmpty()) {
      Specification<erpProductos> spec = Specification
        .where(erpProductosSpecs.estadoEquals(1))
        .and(erpProductosSpecs.codigoProductoContains(codigoProducto))
        .and(erpProductosSpecs.codigoProductoProveedorContains(codigoProductoProveedor))
        .and(erpProductosSpecs.idProductoContains(idProducto));
      return repoProductos.findAll(spec, pageable);
    }

    String[] palabras = descripcion.toLowerCase().trim().split("\\s+");
    List<Set<Long>> resultadosPorPalabra = new ArrayList<>();

    for (String palabra : palabras) {
      Specification<erpProductos> specPalabra = Specification
        .where(erpProductosSpecs.estadoEquals(1))
        .and(erpProductosSpecs.buscarPalabraEnDescripcion(palabra))
        .and(erpProductosSpecs.codigoProductoContains(codigoProducto))
        .and(erpProductosSpecs.codigoProductoProveedorContains(codigoProductoProveedor))
        .and(erpProductosSpecs.idProductoContains(idProducto));

      List<erpProductos> resultados = repoProductos.findAll(specPalabra);
      Set<Long> ids = resultados.stream()
        .map(erpProductos::getIdProducto)
        .collect(Collectors.toSet());

      resultadosPorPalabra.add(ids);

      if (ids.isEmpty()) {
        return Page.empty(pageable);
      }
    }

    Set<Long> idsFinales = new HashSet<>(resultadosPorPalabra.get(0));
    for (int i = 1; i < resultadosPorPalabra.size(); i++) {
      idsFinales.retainAll(resultadosPorPalabra.get(i));
      if (idsFinales.isEmpty()) {
        return Page.empty(pageable);
      }
    }

    // Paginación y obtención de resultados
    List<Long> idsList = new ArrayList<>(idsFinales);
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

    return new PageImpl<>(resultadosOrdenados, pageable, idsFinales.size());
  }
}
