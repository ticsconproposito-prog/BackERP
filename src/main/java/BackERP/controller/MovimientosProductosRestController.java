package BackERP.controller;

import BackERP.helper.erpMovimientosProductosSpecs;
import BackERP.models.erpMovimientosProductos;
import BackERP.repository.RepositoryMovimientosProductos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class MovimientosProductosRestController {


    @Autowired
    private RepositoryMovimientosProductos removpro;

    @GetMapping("movimientosProductos")
    public List<erpMovimientosProductos> getMovimientosProductos( @RequestParam(required = false) Integer idOrdenProducto){


        Specification<erpMovimientosProductos> spec = Specification.where(erpMovimientosProductosSpecs.estadoEquals(1))
                .and(erpMovimientosProductosSpecs.idOrdenProductosContains(idOrdenProducto));

        return removpro.findAll(spec);
    }

    @PostMapping("grabarMovimientosProductos")
    public String grabarMovimientosProductos(@RequestBody erpMovimientosProductos MovimientosProductos){
        // valor por defecto
        MovimientosProductos.setFechaModificacion(LocalDate.now());
        MovimientosProductos.setHoraModificacion(LocalTime.now());
        MovimientosProductos.setEstado(1);
        removpro.save(MovimientosProductos);

        return "Grabado";
    }

    @PutMapping("editarMovimientosProductos/{idMovimientosProductos}")
    public String editarMovimientosProductos(@PathVariable long idMovimientosProductos, @RequestBody erpMovimientosProductos MovimientosProductos){
        erpMovimientosProductos updatemovimientosProductos = removpro.findById(idMovimientosProductos).get();
        updatemovimientosProductos.setIdMovimientoProducto(MovimientosProductos.getIdMovimientoProducto());
        updatemovimientosProductos.setIdOrdenProducto(MovimientosProductos.getIdOrdenProducto());
        updatemovimientosProductos.setIdProducto(MovimientosProductos.getIdProducto());
        updatemovimientosProductos.setCantidad(MovimientosProductos.getCantidad());
        updatemovimientosProductos.setPrecioCompra(MovimientosProductos.getPrecioCompra());
        updatemovimientosProductos.setIdUbicacion(MovimientosProductos.getIdUbicacion());
        updatemovimientosProductos.setFechaModificacion(LocalDate.now());
        updatemovimientosProductos.setHoraModificacion(LocalTime.now());
        updatemovimientosProductos.setIdUsuarioModificacion(MovimientosProductos.getIdUsuarioModificacion());
        removpro.save(updatemovimientosProductos);

        return "Editado";
    }

    @DeleteMapping("eliminarMovimientosProductos/{idMovimientoProducto}")
    public String eliminarMovimientoProducto(@PathVariable long idMovimientoProducto, @RequestBody erpMovimientosProductos MovimientosProductos){
        System.out.println("eliminar");
        erpMovimientosProductos updateerpMovimientosProductos = removpro.findById(idMovimientoProducto).get();
        updateerpMovimientosProductos.setFechaModificacion(LocalDate.now());
        updateerpMovimientosProductos.setHoraModificacion(LocalTime.now());
        updateerpMovimientosProductos.setIdUsuarioModificacion(MovimientosProductos.getIdUsuarioModificacion());
        updateerpMovimientosProductos.setEstado(0);
        removpro.save(updateerpMovimientosProductos);
        return "Eliminado";
    }

    @DeleteMapping("eliminarMovProXIdOrden/{idOrdenProducto}")
    public String eliminarMovProXIdOrden(@PathVariable long idOrdenProducto, @RequestBody erpMovimientosProductos MovimientosProductos){
        System.out.println("eliminar");
        List<erpMovimientosProductos> updateerpMovimientosProductos = removpro.findByIdOrdenProducto(idOrdenProducto);
        if (updateerpMovimientosProductos.isEmpty()) {
            throw new RuntimeException("No se encontraron registros con idOrdenProducto: " + idOrdenProducto);
        }

        updateerpMovimientosProductos.forEach(ump -> {
            ump.setEstado(0);
            ump.setFechaModificacion(LocalDate.now());
            ump.setHoraModificacion(LocalTime.now());
            ump.setIdUsuarioModificacion(MovimientosProductos.getIdUsuarioModificacion());
            removpro.save(ump);
        });
        return "Eliminado";
    }
}
