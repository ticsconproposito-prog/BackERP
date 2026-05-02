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
   * Búsqueda por palabras separadas con cruce de resultados
   * Para cada palabra, se ejecuta una consulta independiente y luego se cruzan los IDs
   *
   * @param descripcion Texto completo a buscar (se separa por espacios)
   * @param codigoProducto Código de producto (filtro adicional)
   * @param codigoProductoProveedor Código de proveedor (filtro adicional)
   * @param idUbicacion Ubicación (filtro adicional)
   * @param pageable Paginación
   * @return Page<erpInventario> Resultados que contienen TODAS las palabras
   */
  public Page<erpInventario> buscarPorPalabrasEnDescripcion(
    String descripcion,
    String codigoProducto,
    String codigoProductoProveedor,
    Integer idUbicacion,
    Pageable pageable) {

    // Si no hay descripción, usar búsqueda normal
    if (descripcion == null || descripcion.trim().isEmpty()) {
      Specification<erpInventario> spec = Specification
        .where(erpInventarioSpecs.estadoEquals(1))
        .and(erpInventarioSpecs.codigoProductoContains(codigoProducto))
        .and(erpInventarioSpecs.idUbicacionEquals(idUbicacion))
        .and(erpInventarioSpecs.codigoProductoProveedorContains(codigoProductoProveedor));
      return repinv.findAll(spec, pageable);
    }

    // 1. Separar la frase en palabras individuales
    String[] palabras = descripcion.toLowerCase().trim().split("\\s+");

    // 2. Almacenar los resultados (IDs) de cada palabra
    List<Set<Long>> resultadosPorPalabra = new ArrayList<>();

    // 3. Ejecutar una búsqueda independiente para CADA palabra
    for (String palabra : palabras) {
      // Construir specification para esta palabra específica
      Specification<erpInventario> specPalabra = Specification
        .where(erpInventarioSpecs.estadoEquals(1))
        .and(erpInventarioSpecs.buscarPalabraEnDescripcion(palabra))  // ← Solo busca esta palabra
        .and(erpInventarioSpecs.codigoProductoContains(codigoProducto))
        .and(erpInventarioSpecs.idUbicacionEquals(idUbicacion))
        .and(erpInventarioSpecs.codigoProductoProveedorContains(codigoProductoProveedor));

      // Ejecutar consulta y obtener SOLO los IDs (más eficiente)
      List<erpInventario> resultados = repinv.findAll(specPalabra);
      Set<Long> ids = resultados.stream()
        .map(erpInventario::getIdInventario)
        .collect(Collectors.toSet());

      resultadosPorPalabra.add(ids);

      // Optimización: si alguna palabra no tiene resultados, terminar temprano
      if (ids.isEmpty()) {
        return Page.empty(pageable);
      }
    }

    // 4. CRUCE DE RESULTADOS: intersección de todos los sets
    Set<Long> idsFinales = new HashSet<>(resultadosPorPalabra.get(0));
    for (int i = 1; i < resultadosPorPalabra.size(); i++) {
      idsFinales.retainAll(resultadosPorPalabra.get(i));  // ← CRUCE (AND)
      if (idsFinales.isEmpty()) {
        return Page.empty(pageable);
      }
    }

    // 5. Obtener los registros completos con paginación
    if (idsFinales.isEmpty()) {
      return Page.empty(pageable);
    }

    // Convertir a lista y ordenar
    List<Long> idsList = new ArrayList<>(idsFinales);

    // Aplicar ordenamiento según el pageable
    idsList.sort((id1, id2) -> {
      if (pageable.getSort().isSorted()) {
        // Para ordenamiento por campos específicos, necesitaríamos una consulta adicional
        // Por simplicidad, ordenamos por ID
        return id1.compareTo(id2);
      }
      return id1.compareTo(id2);
    });

    // Paginar los IDs
    int start = (int) pageable.getOffset();
    int end = Math.min((start + pageable.getPageSize()), idsList.size());

    if (start >= idsList.size()) {
      return Page.empty(pageable);
    }

    List<Long> idsPaginados = idsList.subList(start, end);

    // Obtener los registros completos
    List<erpInventario> resultadosFinales = repinv.findAllById(idsPaginados);

    // Mantener el orden de los IDs
    Map<Long, erpInventario> mapaResultados = resultadosFinales.stream()
      .collect(Collectors.toMap(erpInventario::getIdInventario, r -> r));

    List<erpInventario> resultadosOrdenados = idsPaginados.stream()
      .map(mapaResultados::get)
      .filter(Objects::nonNull)
      .collect(Collectors.toList());

    return new PageImpl<>(resultadosOrdenados, pageable, idsFinales.size());
  }
}
