package BackERP.controller;


import BackERP.helper.erpDetalleFacturaSpecs;
import BackERP.models.felResumenDiarioDTO;
import BackERP.models.erpDetalleFacturas;
import BackERP.models.erpInventario;
import BackERP.repository.RepositoryDetalleFacturas;
import BackERP.repository.RepositoryInventario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
//@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class DetalleFacturasRestController {

    @Autowired
    private RepositoryDetalleFacturas repdetfac;
    @Autowired
    private RepositoryInventario repinv;

    @GetMapping("detalleFactura")
    public List<erpDetalleFacturas> getDetalleFacturas(
            @RequestParam(required = false) Integer idEncabezadoFactura,
            @RequestParam(required = false) LocalDate fechaInicio,
            @RequestParam(required = false) LocalDate fechaFin) {

        Specification<erpDetalleFacturas> spec = Specification
                .where(erpDetalleFacturaSpecs.idEncabezadoFacturaContains(idEncabezadoFactura))
                .and(erpDetalleFacturaSpecs.fechaBetween(fechaInicio, fechaFin));

        return repdetfac.findAll(spec);
    }

    @GetMapping("resumenDiario")
    public felResumenDiarioDTO getResumenDiario(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        Object[] row = repdetfac.getResumenDiario(fecha);
        double totalVentas  = row[0] != null ? ((Number) row[0]).doubleValue() : 0.0;
        int totalProductos  = row[1] != null ? ((Number) row[1]).intValue()    : 0;
        return new felResumenDiarioDTO(fecha, totalVentas, totalProductos);
    }


    @PostMapping("grabarDetalleFactura")
    public String grabarDetalleFacturas(@RequestBody erpDetalleFacturas detalleFacturas){
        // valor por defecto
        if(detalleFacturas.getCantidadDeDescuento() != 0
                && detalleFacturas.getPorcentajeDeDescuento() == 0) {

            double PorcDescuento =
                    (detalleFacturas.getCantidadDeDescuento() / detalleFacturas.getImpBruto()) * 100;

            detalleFacturas.setPorcentajeDeDescuento(PorcDescuento);
        }

        detalleFacturas.setFechaModificacion(LocalDate.now());
        detalleFacturas.setHoraModificacion(LocalTime.now());
        detalleFacturas.setEstado(1);
        repdetfac.save(detalleFacturas);

        // 🔹 Descontar inventario
        List<erpInventario> inventarios = repinv.findByIdProducto_IdProducto((long) detalleFacturas.getIdProducto());
        erpInventario inventario = inventarios.isEmpty() ? null : inventarios.get(0);

     /*   if (inventario.getCantidadExistencias() < detalleFacturas.getCantidad()) {
            return "Error: stock insuficiente";
        }*/


        if (inventario != null) {
            int nuevaCantidad = inventario.getCantidadExistencias() - detalleFacturas.getCantidad();

            inventario.setCantidadExistencias(nuevaCantidad);// Permitir negativos

            // inventario.setCantidadExistencias(Math.max(nuevaCantidad, 0)); // evitar negativos

            inventario.setFechaModificacion(LocalDate.now());
            inventario.setHoraModificacion(LocalTime.now());
            repinv.save(inventario);
        }

        return "Grabado y actualizado inventario";
    }


    @DeleteMapping("eliminarDetalleFactura/{idDetalleFactura}")
    public String eliminarDetalleFactura(@PathVariable long idDetalleFactura, @RequestBody erpDetalleFacturas  DetalleFacturas){

        erpDetalleFacturas updateerpDetalleFacturas = repdetfac.findById(idDetalleFactura).get();
        updateerpDetalleFacturas.setFechaModificacion(LocalDate.now());
        updateerpDetalleFacturas.setHoraModificacion(LocalTime.now());
        updateerpDetalleFacturas.setIdUsuarioModificacion(DetalleFacturas.getIdUsuarioModificacion());
        updateerpDetalleFacturas.setEstado(0);
        repdetfac.save(updateerpDetalleFacturas);
        return "Eliminado";
    }

}
