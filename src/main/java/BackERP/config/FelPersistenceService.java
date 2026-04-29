package BackERP.config;

import BackERP.helper.erpDetalleFacturaSpecs;
import BackERP.models.*;
import BackERP.repository.RepositoryDetalleFacturas;
import BackERP.repository.RepositoryEncabezadoFacturas;
import BackERP.repository.RepositoryInventario;
import BackERP.repository.RepositoryMovimientosProductos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Concentra toda la persistencia JPA relacionada con FEL en transacciones cortas,
 * independientes de las llamadas SOAP realizadas en FelService.
 */
@Service
public class FelPersistenceService {

    @Autowired
    private RepositoryEncabezadoFacturas repEncFac;

    @Autowired
    private RepositoryDetalleFacturas repDetFac;

    @Autowired
    private RepositoryInventario repInv;

    @Autowired
    private RepositoryMovimientosProductos repMovPro;

    /**
     * Persiste el resultado de una generación DTE exitosa o con error,
     * fuera del ciclo de vida de la llamada SOAP.
     */
    @Transactional
    public void persistirResultadoDte(FelResult result, felDteRequestDto req) {
        String sid = req.getReferencia().substring(4);
        long id = Long.parseLong(sid);

        Optional<erpEncabezadoFacturas> optEnc = repEncFac.findById(id);
        if (optEnc.isEmpty()) {
            result.setError("No se encontró encabezado de factura con ID " + id);
            result.setOk(false);
            return;
        }

        erpEncabezadoFacturas enc = optEnc.get();

        if (result.isOk()) {
            enc.setSerieResAPI(result.getSerie());
            enc.setPreimpresoResAPI(Long.parseLong(result.getPreimpreso()));
            enc.setNumeroAutorizacionResAPI(result.getNumeroAutorizacion());
            enc.setRespuestaXML(result.getRawResponse());
            enc.setReferencia(req.getReferencia());
            enc.setFacturaProcesada("S");
            enc.setNombreResAPI(result.getNombre());
            enc.setDireccionResAPI(result.getDireccion());
            enc.setTelefonoResAPI(result.getTelefono());
            enc.setReferenciaResAPI(result.getReferencia());
            repEncFac.save(enc);
        } else {
            if ("2-NO EXISTE EL NIT/CUI DEL CONTRIBUYENTE".equals(result.getError()) ||
                    "186-NUMERO DE DOCUMENTO DE IDENTIFICACION INVALIDO".equals(result.getError())) {
                enc.setEstado(0);
                restaurarInventarioYMovimientos(enc);
            }
            enc.setRespuestaXML(result.getRawResponse());
            enc.setReferencia(req.getReferencia());
            enc.setFacturaProcesada("N");
            repEncFac.save(enc);
        }
    }

    /**
     * Persiste el resultado de una anulación FEL exitosa,
     * fuera del ciclo de vida de la llamada SOAP.
     */
    @Transactional
    public void persistirResultadoAnulacion(FelResult result, Long idEncabezadoFactura) {
        if (!result.isOk()) {
            return;
        }

        Optional<erpEncabezadoFacturas> optEnc = repEncFac.findById(idEncabezadoFactura);
        if (optEnc.isEmpty()) {
            return;
        }

        erpEncabezadoFacturas enc = optEnc.get();
        enc.setFacturaProcesada("A");
        enc.setRespuestaXML(result.getRawResponse());
        repEncFac.save(enc);

        restaurarInventarioYMovimientos(enc);
    }

    private void restaurarInventarioYMovimientos(erpEncabezadoFacturas enc) {
        List<erpDetalleFacturas> detalles = repDetFac.findAll(
                erpDetalleFacturaSpecs.idEncabezadoFacturaContains(Math.toIntExact(enc.getIdEncabezadoFactura()))
        );

        for (erpDetalleFacturas det : detalles) {
            List<erpInventario> inventarios = repInv.findByIdProducto_IdProducto((long) det.getIdProducto());
            if (inventarios != null && !inventarios.isEmpty()) {
                erpInventario inventario = inventarios.get(0);
                inventario.setCantidadExistencias(inventario.getCantidadExistencias() + det.getCantidad());
                inventario.setFechaModificacion(LocalDate.now());
                inventario.setHoraModificacion(LocalTime.now());
                inventario.setIdUsuarioModificacion(enc.getIdUsuarioModificacion());
                repInv.save(inventario);
            } else {
                erpInventario nuevo = new erpInventario();
                erpProductos prod = new erpProductos();
                prod.setIdProducto((long) det.getIdProducto());
                nuevo.setIdProducto(prod);
                nuevo.setCantidadExistencias(det.getCantidad());
                nuevo.setCantidadDanados(0);
                nuevo.setIdUbicacion(1);
                nuevo.setEstado(1);
                nuevo.setFechaModificacion(LocalDate.now());
                nuevo.setHoraModificacion(LocalTime.now());
                nuevo.setIdUsuarioModificacion(enc.getIdUsuarioModificacion());
                repInv.save(nuevo);
            }
        }

        List<erpMovimientosProductos> movimientos =
                repMovPro.findByIdOrdenProducto((long) enc.getIdEncabezadoFactura());
        for (erpMovimientosProductos mov : movimientos) {
            mov.setEstado(0);
            mov.setFechaModificacion(LocalDate.now());
            mov.setHoraModificacion(LocalTime.now());
            mov.setIdUsuarioModificacion(enc.getIdUsuarioModificacion());
            repMovPro.save(mov);
        }
    }
}
