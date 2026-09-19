package BackERP.controller;

import BackERP.helper.erpProductosSpecs;
import BackERP.models.erpInventario;
import BackERP.models.erpProductos;
import BackERP.repository.RepositoryInventario;
import BackERP.repository.RepositoryProductos;
import org.springframework.beans.factory.annotation.Autowired;
import BackERP.service.ProductoBusquedaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

//@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class ProductoRestController {

  @Autowired
  private RepositoryProductos repro;

  @Autowired
  private RepositoryInventario repinv;   // ← NUEVO

  @Autowired
  private ProductoBusquedaService busquedaService;

  @GetMapping("productos")
  public Page<erpProductos> getProductos(
    @RequestParam(required = false) String codigoProducto,
    @RequestParam(required = false) String codigoProductoProveedor,
    @RequestParam(required = false) String descripcionProducto,
    @RequestParam(required = false) Long idProducto,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size,
    @RequestParam(defaultValue = "idProducto,asc") String sort
  ) {
    String[] sortParts = sort.split(",", 2);
    Sort.Direction dir = (sortParts.length > 1) ? Sort.Direction.fromString(sortParts[1]) : Sort.Direction.ASC;
    Sort s = Sort.by(dir, sortParts[0]);
    Pageable pageable = PageRequest.of(page, size, s);

    // 🔥 USAR EL NUEVO SERVICIO con lógica OR optimizada
    return busquedaService.buscarProductos(
      codigoProducto,
      codigoProductoProveedor,
      descripcionProducto,
      idProducto,
      pageable
    );
  }

    @PostMapping("grabarProducto")
    public String grabarProducto(@RequestBody erpProductos producto){

        // normalizar código
        producto.setCodigoProducto(
                producto.getCodigoProducto().toUpperCase().trim()
        );

        // valor por defecto
            producto.setFechaModificacion(LocalDate.now());
            producto.setHoraModificacion(LocalTime.now());
            producto.setEstado(1);
            repro.save(producto);

        return "Grabado";
    }
    @PutMapping("editarProducto/{idProducto}")
    public String editarProducto(@PathVariable long idProducto, @RequestBody erpProductos producto){


    erpProductos updateProducto = repro.findById(idProducto).get();
    updateProducto.setCodigoProducto(producto.getCodigoProducto());
    updateProducto.setCodigoProductoProveedor(producto.getCodigoProductoProveedor());
    updateProducto.setDescripcionProducto(producto.getDescripcionProducto());
    updateProducto.setUnidadDeMedida(producto.getUnidadDeMedida());
    updateProducto.setFechaModificacion(LocalDate.now());
    updateProducto.setHoraModificacion(LocalTime.now());
    updateProducto.setPrecioCompra(producto.getPrecioCompra());
    updateProducto.setPrecioVenta(producto.getPrecioVenta());
    updateProducto.setIdUsuarioModificacion(producto.getIdUsuarioModificacion());
    repro.save(updateProducto);

    return "Editado";
}

  @DeleteMapping("eliminarProducto/{idProducto}")
  @Transactional   // ← IMPORTANTE: una sola transacción para ambas operaciones
  public String eliminarProducto(@PathVariable long idProducto, @RequestBody erpProductos producto) {

    // 1. Borrado lógico del producto
    erpProductos updateProducto = repro.findById(idProducto).get();
    updateProducto.setFechaModificacion(LocalDate.now());
    updateProducto.setHoraModificacion(LocalTime.now());
    updateProducto.setIdUsuarioModificacion(producto.getIdUsuarioModificacion());
    updateProducto.setEstado(0);
    repro.save(updateProducto);

    // 2. Borrado lógico de TODOS los inventarios asociados
    List<erpInventario> inventarios = repinv.findByIdProducto_IdProducto(idProducto);

    for (erpInventario inv : inventarios) {
      inv.setEstado(0);
      inv.setFechaModificacion(LocalDate.now());
      inv.setHoraModificacion(LocalTime.now());
      inv.setIdUsuarioModificacion(producto.getIdUsuarioModificacion());
      repinv.save(inv);
    }

    return "Producto eliminado y " + inventarios.size() + " registros de inventario desactivados";
  }

}
