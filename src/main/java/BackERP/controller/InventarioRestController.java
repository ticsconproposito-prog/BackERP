package BackERP.controller;

import BackERP.models.erpInventario;
import BackERP.models.erpInventarioAgrupadoDTO;
import BackERP.models.erpProductos;
import BackERP.repository.RepositoryInventario;
import BackERP.service.InventarioBusquedaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class InventarioRestController {

  @Autowired
  private RepositoryInventario repinv;

  @Autowired
  private InventarioBusquedaService busquedaService;  // ← NUEVO servicio

  @GetMapping("inventario")
  public Page<erpInventario> getInventario(
    @RequestParam(required = false) String descripcion,
    @RequestParam(required = false) String codigoProductoProveedor,
    @RequestParam(required = false) String codigoProducto,
    @RequestParam(required = false) Integer idUbicacion,
    @RequestParam(defaultValue = "0") Integer estadoExcluido,  // ← NUEVO PARÁMETRO OPCIONAL
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size,
    @RequestParam(defaultValue = "idInventario,asc") String sort
  ) {

    String[] sortParts = sort.split(",", 2);
    Sort.Direction dir = (sortParts.length > 1) ? Sort.Direction.fromString(sortParts[1]) : Sort.Direction.ASC;
    Sort s = Sort.by(dir, sortParts[0]);
    Pageable pageable = PageRequest.of(page, size, s);

    // 🔥 LLAMAR AL SERVICIO CON EL PARÁMETRO ESTADO
    return busquedaService.buscarPorPalabrasEnDescripcion(
      descripcion,
      codigoProducto,
      codigoProductoProveedor,
      idUbicacion,
      estadoExcluido,  // ← AGREGAR ESTE PARÁMETRO
      pageable
    );
  }

  @GetMapping("inventarioAgrupado")
  public Page<erpInventarioAgrupadoDTO> getInventarioAgrupado(
    @RequestParam(required = false) String descripcion,
    @RequestParam(required = false) String codigoProducto,
    @RequestParam(required = false) String codigoProductoProveedor,
    @RequestParam(defaultValue = "0") Integer estadoExcluido,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size,
    @RequestParam(defaultValue = "idInventario,asc") String sort
  ) {

    String[] sortParts = sort.split(",", 2);
    Sort.Direction dir = (sortParts.length > 1) ? Sort.Direction.fromString(sortParts[1]) : Sort.Direction.ASC;
    Sort s = Sort.by(dir, sortParts[0]);
    Pageable pageable = PageRequest.of(page, size, s);

    // Usar el mismo servicio para agrupado
    Page<erpInventario> inventariosPage = busquedaService.buscarPorPalabrasEnDescripcion(
      descripcion,
      codigoProducto,
      codigoProductoProveedor,
      null,  // Sin filtro de ubicación para el agrupado
      estadoExcluido,  // ← AGREGAR ESTE PARÁMETRO
      pageable
    );

    // Agrupar por producto
    Map<erpProductos, List<erpInventario>> agrupado = inventariosPage.getContent().stream()
      .collect(Collectors.groupingBy(
        erpInventario::getIdProducto,
        LinkedHashMap::new,
        Collectors.toList()
      ));

    List<erpInventarioAgrupadoDTO> resultado = new ArrayList<>();
    for (Map.Entry<erpProductos, List<erpInventario>> entry : agrupado.entrySet()) {
      erpProductos producto = entry.getKey();
      Long totalExistencias = entry.getValue().stream().mapToLong(erpInventario::getCantidadExistencias).sum();
      Long totalDanados = entry.getValue().stream().mapToLong(erpInventario::getCantidadDanados).sum();
      resultado.add(new erpInventarioAgrupadoDTO(producto, totalExistencias, totalDanados));
    }

    return new PageImpl<>(resultado, pageable, inventariosPage.getTotalElements());
  }

  // Los métodos POST, PUT, DELETE se mantienen IGUALES
  @PostMapping("grabarInventario")
  public String grabarInventario(@RequestBody erpInventario inventario) {
    inventario.setFechaModificacion(LocalDate.now());
    inventario.setHoraModificacion(LocalTime.now());
    inventario.setEstado(1);
    repinv.save(inventario);
    return "Grabado";
  }

  @PutMapping("editarInventario/{idProductoInventario}")
  public String editarInventario(@PathVariable long idProductoInventario, @RequestBody erpInventario inventario) {
    erpInventario updateInventario = repinv.findById(idProductoInventario).get();
    updateInventario.setCantidadExistencias(inventario.getCantidadExistencias());
    updateInventario.setCantidadDanados(inventario.getCantidadDanados());
    updateInventario.setFechaModificacion(LocalDate.now());
    updateInventario.setHoraModificacion(LocalTime.now());
    updateInventario.setIdUsuarioModificacion(inventario.getIdUsuarioModificacion());
    repinv.save(updateInventario);
    return "Editado";
  }

  @DeleteMapping("eliminarInventario/{idProductoInventario}")
  public String eliminarInventario(@PathVariable long idProductoInventario, @RequestBody erpInventario inventario) {
    erpInventario updateInventario = repinv.findById(idProductoInventario).get();
    updateInventario.setFechaModificacion(LocalDate.now());
    updateInventario.setHoraModificacion(LocalTime.now());
    updateInventario.setIdUsuarioModificacion(inventario.getIdUsuarioModificacion());
    updateInventario.setEstado(0);
    repinv.save(updateInventario);
    return "Eliminado";
  }
}
