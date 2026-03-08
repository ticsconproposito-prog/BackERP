package BackERP.controller;

import BackERP.helper.erpMovimientosProductosSpecs;
import BackERP.models.erpInventario;
import BackERP.models.erpMovimientosProductos;
import BackERP.models.erpProductos;
import BackERP.repository.RepositoryInventario;
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

    @Autowired
    private RepositoryInventario repinv;

    @GetMapping("movimientosProductos")
    public List<erpMovimientosProductos> getMovimientosProductos( @RequestParam(required = false) Integer idOrdenProducto){


        Specification<erpMovimientosProductos> spec = Specification.where(erpMovimientosProductosSpecs.estadoEquals(1))
                .and(erpMovimientosProductosSpecs.idOrdenProductosContains(idOrdenProducto));

        return removpro.findAll(spec);
    }

    @PostMapping("grabarMovimientosProductos")
    public String grabarMovimientosProductos(@RequestBody erpMovimientosProductos movimientosProductos,
                                             @RequestParam int tipoDeMovimiento,
                                             @RequestParam (defaultValue = "0") int ubicacionSalida,
                                             @RequestParam (defaultValue = "0") int ubicacionIngreso) {
        movimientosProductos.setFechaModificacion(LocalDate.now());
        movimientosProductos.setHoraModificacion(LocalTime.now());
        movimientosProductos.setEstado(1);
        removpro.save(movimientosProductos);

        System.out.println("Buscando inventario con producto=" + movimientosProductos.getIdProducto().getIdProducto() +
                " ubicacion=" + movimientosProductos.getIdUbicacion() +" tipoDeMovimiento "+ tipoDeMovimiento);


        if (tipoDeMovimiento == 0) {
            erpInventario inventarioExistente = repinv.findByIdProducto_IdProductoAndIdUbicacion(
                    movimientosProductos.getIdProducto().getIdProducto(),
                    movimientosProductos.getIdUbicacion()
            );

            if (inventarioExistente != null) {
                inventarioExistente.setCantidadExistencias(
                        inventarioExistente.getCantidadExistencias() + movimientosProductos.getCantidad()
                );
                inventarioExistente.setFechaModificacion(LocalDate.now());
                inventarioExistente.setHoraModificacion(LocalTime.now());
                inventarioExistente.setIdUsuarioModificacion(movimientosProductos.getIdUsuarioModificacion());
                repinv.save(inventarioExistente);
            } else {
                erpInventario nuevoInventario = new erpInventario();
                nuevoInventario.setIdProducto(movimientosProductos.getIdProducto()); // usar el objeto completo
                nuevoInventario.setIdUbicacion(movimientosProductos.getIdUbicacion());
                nuevoInventario.setCantidadExistencias(movimientosProductos.getCantidad());
                nuevoInventario.setCantidadDanados(0);
                nuevoInventario.setEstado(1);
                nuevoInventario.setFechaModificacion(LocalDate.now());
                nuevoInventario.setHoraModificacion(LocalTime.now());
                nuevoInventario.setIdUsuarioModificacion(movimientosProductos.getIdUsuarioModificacion());
                repinv.save(nuevoInventario);

            }
        }

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
