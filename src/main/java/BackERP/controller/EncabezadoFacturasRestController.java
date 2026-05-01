package BackERP.controller;
import BackERP.helper.erpEncabezadoFacturasSpecs;
import BackERP.models.erpDetalleFacturas;
import BackERP.models.erpEncabezadoFacturas;
import BackERP.models.AnulacionFacturaRequest;
import BackERP.models.erpInventario;
import BackERP.repository.RepositoryDetalleFacturas;
import BackERP.repository.RepositoryEncabezadoFacturas;
import BackERP.repository.RepositoryInventario;
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

//@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class EncabezadoFacturasRestController {


    @Autowired
    private RepositoryEncabezadoFacturas repencfac;

    @Autowired
    private RepositoryDetalleFacturas repdetfac;

    @Autowired
    private RepositoryInventario repinv;

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
    public Page<erpEncabezadoFacturas> getFacturasPorFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(defaultValue = "idEncabezadoFactura,desc") String sort) {

        String[] sortParts = sort.split(",", 2);
        Sort.Direction dir = (sortParts.length > 1) ? Sort.Direction.fromString(sortParts[1]) : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sortParts[0]));

        Specification<erpEncabezadoFacturas> spec = (root, query, cb) ->
                cb.equal(root.get("FechaFactura"), fecha);

        return repencfac.findAll(spec, pageable);
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


  @DeleteMapping("anularFacturaCompleta/{idEncabezadoFactura}")
  public ResponseEntity<?> anularFacturaCompleta(
    @PathVariable Long idEncabezadoFactura,
    @RequestBody AnulacionFacturaRequest request) {

    try {
      // 1. Verificar que la factura existe y está activa
      erpEncabezadoFacturas encabezado = repencfac
        .findByIdEncabezadoFacturaAndEstado(idEncabezadoFactura, 1)
        .orElseThrow(() -> new RuntimeException("Factura no encontrada o ya está anulada"));

      // 2. Obtener los detalles activos de la factura
      List<erpDetalleFacturas> detalles = repdetfac
        .findByIdEncabezadoFacturaAndEstado(Math.toIntExact(idEncabezadoFactura), 1);

      if (detalles.isEmpty()) {
        return ResponseEntity.badRequest()
          .body("La factura no tiene detalles para anular");
      }

      // 3. Restaurar el inventario por cada detalle
      for (erpDetalleFacturas detalle : detalles) {
        // Buscar el registro de inventario correspondiente
        erpInventario inventario = repinv
          .findByIdProducto_IdProductoAndIdUbicacion(
            (long) detalle.getIdProducto(),
            getUbicacionPorDefecto() // Necesitas definir cómo obtener la ubicación
          );

        if (inventario != null && inventario.getEstado() == 1) {
          // Restaurar la cantidad
          repinv.restaurarExistencia(
            inventario.getIdInventario(),
            detalle.getCantidad(),
            request.getIdUsuarioModificacion()
          );
        } else {
          // Log del error pero continuar con la anulación
          System.err.println("No se encontró inventario para producto: " + detalle.getIdProducto());
        }
      }

      // 4. Anular detalles de factura (cambio lógico masivo)
      repdetfac.anularDetallesPorEncabezado(
        Math.toIntExact(idEncabezadoFactura),
        request.getIdUsuarioModificacion()
      );

      // 5. Anular encabezado de factura
      encabezado.setEstado(0);
      encabezado.setReversion("N");
      encabezado.setFechaModificacion(LocalDate.now());
      encabezado.setHoraModificacion(LocalTime.now());
      encabezado.setIdUsuarioModificacion(request.getIdUsuarioModificacion());
      repencfac.save(encabezado);

      return ResponseEntity.ok()
        .body("Factura anulada exitosamente. Se restauraron " + detalles.size() + " productos al inventario.");

    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.internalServerError()
        .body("Error al anular la factura: " + e.getMessage());
    }
  }

  // Método auxiliar para obtener la ubicación por defecto
// Ajusta según tu lógica de negocio
  private int getUbicacionPorDefecto() {
    // Puedes obtenerla de una configuración, o hacerla configurable
    // Por ahora retornamos un valor por defecto (ejemplo: 1)
    return 1;
  }


}
