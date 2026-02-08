package BackERP.controller;


import BackERP.helper.erpOrdenProductosSpecs;
import BackERP.models.erpOrdenProductos;
import BackERP.repository.RepositoryOrdenProductos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class OrdenProductosRestController {

    @Autowired
    private RepositoryOrdenProductos repordpro;

    @GetMapping("ordenProductos")
    public Page<erpOrdenProductos> getOrdenProductos(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(required = false) String idSucursal,
            @RequestParam(required = false) Integer tipoDeMovimiento,
            @RequestParam(required = false) String idProveedor,
            @RequestParam(required = false) String  numeroDeDocumento,
            @RequestParam(required = false) Integer  id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "idOrdenProducto,asc") String sort
        ) {


        String[] sortParts = sort.split(",", 2);

        Sort.Direction dir = (sortParts.length > 1) ? Sort.Direction.fromString(sortParts[1]) : Sort.Direction.ASC;

        Sort s = Sort.by(dir, sortParts[0]);

        Pageable pageable = PageRequest.of(page, size, s);

        Specification<erpOrdenProductos> spec = Specification.where(erpOrdenProductosSpecs.estadoEquals(1))
                .and(erpOrdenProductosSpecs.idSucursalContains(idSucursal))
                .and(erpOrdenProductosSpecs.idProveedorContains(idProveedor))
                .and(erpOrdenProductosSpecs.numeroDeDocumentoContains(numeroDeDocumento))
                .and(erpOrdenProductosSpecs.tipoDeMovimientoContains(tipoDeMovimiento))
                .and (erpOrdenProductosSpecs.idOrdenContains(id))
                .and(erpOrdenProductosSpecs.fechaOrdenBetween(fechaInicio, fechaFin));

        return repordpro.findAll(spec, pageable);
    }

    @PostMapping("grabarOrdenProducto")
    public ResponseEntity<Long>  grabarOrdenProductos(@RequestBody erpOrdenProductos OrdenProductos){

        // valor por defecto
        OrdenProductos.setFechaModificacion(LocalDate.now());
        OrdenProductos.setHoraModificacion(LocalTime.now());
        OrdenProductos.setEstado(1);
        erpOrdenProductos saved = repordpro.save(OrdenProductos);

        return ResponseEntity.ok(saved.getIdOrdenProducto());
    }
    @PutMapping("editarOrdenProducto/{idOrdenProductos}")
    public String editarOrdenProductos(@PathVariable long idOrdenProductos, @RequestBody erpOrdenProductos OrdenProductos){
        erpOrdenProductos updateOrdenProductos = repordpro.findById(idOrdenProductos).get();
        updateOrdenProductos.setIdSucursal(updateOrdenProductos.getIdSucursal());
        updateOrdenProductos.setFechaOrden(updateOrdenProductos.getFechaOrden());
        updateOrdenProductos.setIdProveedor(OrdenProductos.getIdProveedor());
        updateOrdenProductos.setNumeroDeDocumento(OrdenProductos.getNumeroDeDocumento());
        updateOrdenProductos.setTipoDeMovimiento(OrdenProductos.getTipoDeMovimiento());
        updateOrdenProductos.setTipoDeOrden(OrdenProductos.getTipoDeOrden());
        updateOrdenProductos.setEstadoFactura(OrdenProductos.getEstadoFactura());
        updateOrdenProductos.setValorCancelado(OrdenProductos.getValorCancelado());
        updateOrdenProductos.setPrecioTotalOrden(OrdenProductos.getPrecioTotalOrden());
        updateOrdenProductos.setComentario(OrdenProductos.getComentario());
        repordpro.save(updateOrdenProductos);

        return "Editado";
    }

    @DeleteMapping("eliminarOrdenProducto/{idOrdenProductos}")
    public String eliminarOrdenProductos(@PathVariable long idOrdenProductos){
        System.out.println("eliminar");
        erpOrdenProductos updateOrdenProductos = repordpro.findById(idOrdenProductos).get();
        updateOrdenProductos.setEstado(0);
        repordpro.save(updateOrdenProductos);
        return "Eliminado";
    }


}
