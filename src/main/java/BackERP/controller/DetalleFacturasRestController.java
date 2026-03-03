package BackERP.controller;


import BackERP.helper.erpDetalleFacturaSpecs;
import BackERP.models.erpDetalleFacturas;
import BackERP.repository.RepositoryDetalleFacturas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class DetalleFacturasRestController {

    @Autowired
    private RepositoryDetalleFacturas repdetfac;

    @GetMapping("detalleFactura")
    public List<erpDetalleFacturas> getDetalleFacturas(@RequestParam(required = false) Integer idEncabezadoFactura){

        Specification<erpDetalleFacturas> spec = Specification.where(erpDetalleFacturaSpecs.idEncabezadoFacturaContains(idEncabezadoFactura));

        return repdetfac.findAll(spec);
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

        return "Grabado";
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
