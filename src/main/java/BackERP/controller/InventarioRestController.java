package BackERP.controller;


import BackERP.helper.erpInventarioSpecs;
import BackERP.models.erpinventario;
import BackERP.repository.RepositoryInventario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class InventarioRestController {

    @Autowired
    private RepositoryInventario repinv;

    @GetMapping("inventario")
    public Page<erpinventario> getInventario(
            @RequestParam(required = false) String descripcion,
            @RequestParam(required = false) String codigoProductoProveedor,
            @RequestParam(required = false) String codigoProducto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "idProductoInventario,asc") String sort
    ) {

        String[] sortParts = sort.split(",", 2);

        Sort.Direction dir = (sortParts.length > 1) ? Sort.Direction.fromString(sortParts[1]) : Sort.Direction.ASC;

        Sort s = Sort.by(dir, sortParts[0]);

        Pageable pageable = PageRequest.of(page, size, s);

        Specification<erpinventario> spec = Specification
                .where(erpInventarioSpecs.estadoEquals(1))
                .and(erpInventarioSpecs.descripcionProductoContains(descripcion))
                .and(erpInventarioSpecs.codigoProductoContains(codigoProducto))
                .and(erpInventarioSpecs.codigoProductoProveedorContains(codigoProductoProveedor));

        return repinv.findAll(spec, pageable);
    }

    @PostMapping("grabarInventario")
    public String grabarInventario(@RequestBody erpinventario inventario){


        // valor por defecto
        inventario.setFechaModificacion(LocalDate.now());
        inventario.setHoraModificacion(LocalTime.now());
        inventario.setEstado(1);
        repinv.save(inventario);

        return "Grabado";
    }
    @PutMapping("editarInventario/{idProductoInventario}")
    public String editarInventario(@PathVariable long idProductoInventario, @RequestBody erpinventario inventario){

        erpinventario updateInventario = repinv.findById(idProductoInventario).get();
        updateInventario.setPrecioCompra(inventario.getPrecioCompra());
        updateInventario.setPrecioVenta(inventario.getPrecioVenta());
        updateInventario.setFechaModificacion(LocalDate.now());
        updateInventario.setHoraModificacion(LocalTime.now());
        updateInventario.setIdUsuarioModificacion(inventario.getIdUsuarioModificacion());
        repinv.save(updateInventario);

        return "Editado";
    }

    @DeleteMapping("eliminarInventario/{idProductoInventario}")
    public String eliminarInventario(@PathVariable long idProductoInventario){
        System.out.println("eliminar");
        erpinventario updateInventario = repinv.findById(idProductoInventario).get();
        updateInventario.setEstado(0);
        repinv.save(updateInventario);
        return "Eliminado";
    }

}
