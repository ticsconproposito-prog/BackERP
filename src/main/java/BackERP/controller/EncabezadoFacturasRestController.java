package BackERP.controller;
import BackERP.helper.erpEncabezadoFacturasSpecs;
import BackERP.models.erpEncabezadoFacturas;
import BackERP.repository.RepositoryEncabezadoFacturas;
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
public class EncabezadoFacturasRestController {


    @Autowired
    private RepositoryEncabezadoFacturas repencfac;

    @GetMapping("erpEncabezadoFacturas")
    public Page<erpEncabezadoFacturas> getOrdenProductos(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(required = false) String nombreCliente,
            @RequestParam(required = false) String nit,
            @RequestParam(required = false) String referenciaFactura,
            @RequestParam(required = false) Integer tipoDocumento,
            @RequestParam(required = false) String preimpreso,
            @RequestParam(required = false) String facturaProcesada, // 🔎 nuevo parámetro
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "idEncabezadoFactura,desc") String sort
    ) {
        String[] sortParts = sort.split(",", 2);
        Sort.Direction dir = (sortParts.length > 1) ? Sort.Direction.fromString(sortParts[1]) : Sort.Direction.ASC;
        Sort s = Sort.by(dir, sortParts[0]);
        Pageable pageable = PageRequest.of(page, size, s);

        Specification<erpEncabezadoFacturas> spec = Specification
                .where(erpEncabezadoFacturasSpecs.estadoEquals(1))
                .and(erpEncabezadoFacturasSpecs.referenciaFacturaContains(referenciaFactura))
                .and(erpEncabezadoFacturasSpecs.tipoDocumentoContains(tipoDocumento))
                .and(erpEncabezadoFacturasSpecs.nombreClienteContains(nombreCliente))
                .and(erpEncabezadoFacturasSpecs.nitClienteContains(nit))
                .and(erpEncabezadoFacturasSpecs.fechaFacturaBetween(fechaInicio, fechaFin))
                .and(erpEncabezadoFacturasSpecs.facturaProcesaContains(facturaProcesada))
                .and(erpEncabezadoFacturasSpecs.numeroPreimpresoContains(preimpreso)); // 🔎 nuevo filtro


        return repencfac.findAll(spec, pageable);
    }

    @GetMapping("facturasPorFecha")
    public List<erpEncabezadoFacturas> getFacturasPorFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        Specification<erpEncabezadoFacturas> spec = (root, query, cb) ->
                cb.equal(root.get("FechaFactura"), fecha);

        return repencfac.findAll(spec);
    }


    @PostMapping("grabarEncabezadoFacturas")
    public ResponseEntity<Long> grabarEncabezadoFacturas(@RequestBody erpEncabezadoFacturas EncabezadoFacturas){

        // valor por defecto

        EncabezadoFacturas.setFechaModificacion(LocalDate.now());
        EncabezadoFacturas.setHoraModificacion(LocalTime.now());
        EncabezadoFacturas.setReversion("N");
        EncabezadoFacturas.setEstado(1);
        erpEncabezadoFacturas saved = repencfac.save(EncabezadoFacturas);

        return ResponseEntity.ok(saved.getIdEncabezadoFactura());
    }


    @DeleteMapping("anulacionEncabezadoFactura/{idEncabezadoFactura}")
    public String anulacionEncabezadoFactura(@PathVariable long idEncabezadoFactura,@RequestBody erpEncabezadoFacturas EncabezadoFacturas){

        erpEncabezadoFacturas updateEncabezadoFactura = repencfac.findById(idEncabezadoFactura).get();
        updateEncabezadoFactura.setFechaModificacion(LocalDate.now());
        updateEncabezadoFactura.setHoraModificacion(LocalTime.now());
        updateEncabezadoFactura.setIdUsuarioModificacion(EncabezadoFacturas.getIdUsuarioModificacion());
        updateEncabezadoFactura.setReversion("S");
        repencfac.save(updateEncabezadoFactura);
        return "Anulada";
    }

}
