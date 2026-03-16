package BackERP.controller;


import BackERP.helper.erpDetalleFacturaSpecs;
import BackERP.models.felResumenDiarioDTO;
import BackERP.models.erpDetalleFacturas;
import BackERP.models.erpInventario;
import BackERP.repository.RepositoryDetalleFacturas;
import BackERP.repository.RepositoryInventario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
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
    public felResumenDiarioDTO getResumenDiario(@RequestParam LocalDate fecha) {
        List<erpDetalleFacturas> facturas = repdetfac.findAll(
                (root, query, cb) -> cb.equal(root.get("fechaModificacion"), fecha)
        );

        double totalVentas = facturas.stream()
                .mapToDouble(erpDetalleFacturas::getImpTotal)
                .sum();

        int totalProductos = facturas.stream()
                .mapToInt(erpDetalleFacturas::getCantidad)
                .sum();

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

        erpInventario inventario = repinv.findAll(
                (root, query, cb) -> cb.equal(root.get("idProducto").get("idProducto"), detalleFacturas.getIdProducto())
        ).stream().findFirst().orElse(null);

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
        System.out.println("eliminar");
        erpDetalleFacturas updateerpDetalleFacturas = repdetfac.findById(idDetalleFactura).get();
        updateerpDetalleFacturas.setFechaModificacion(LocalDate.now());
        updateerpDetalleFacturas.setHoraModificacion(LocalTime.now());
        updateerpDetalleFacturas.setIdUsuarioModificacion(DetalleFacturas.getIdUsuarioModificacion());
        updateerpDetalleFacturas.setEstado(0);
        repdetfac.save(updateerpDetalleFacturas);
        return "Eliminado";
    }

}
