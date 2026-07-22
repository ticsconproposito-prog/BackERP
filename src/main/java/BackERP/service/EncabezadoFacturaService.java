// BackERP/service/EncabezadoFacturaService.java
package BackERP.service;

import BackERP.models.erpEncabezadoFacturas;
import BackERP.repository.RepositoryConsignacionPagos;
import BackERP.repository.RepositoryEncabezadoFacturas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service // ← Importante: debe tener @Service
public class EncabezadoFacturaService {

  @Autowired
  private RepositoryEncabezadoFacturas repencfac;

  @Autowired
  private RepositoryConsignacionPagos repConsignacionPagos;

  // Método NO estático
  public Page<erpEncabezadoFacturas> findWithPaymentInfo(Specification<erpEncabezadoFacturas> spec, Pageable pageable) {
    Page<erpEncabezadoFacturas> page = repencfac.findAll(spec, pageable);

    if (page.isEmpty()) {
      return page;
    }

    List<Long> idsFactura = page.getContent().stream()
      .map(erpEncabezadoFacturas::getIdEncabezadoFactura)
      .collect(Collectors.toList());

    Map<Long, Double> pagosMap = getTotalPagosByFacturas(idsFactura);

    page.getContent().forEach(factura -> {
      factura.setTotalPagado(pagosMap.getOrDefault(factura.getIdEncabezadoFactura(), 0.0));
    });

    return page;
  }

  public erpEncabezadoFacturas findByIdWithPaymentInfo(Long idFactura) {
    erpEncabezadoFacturas factura = repencfac.findById(idFactura).orElse(null);

    if (factura != null) {
      Double totalPagado = repConsignacionPagos.sumMontoPagosByFactura(idFactura.intValue());
      factura.setTotalPagado(totalPagado != null ? totalPagado : 0.0);
    }

    return factura;
  }

  private Map<Long, Double> getTotalPagosByFacturas(List<Long> idsFactura) {
    Map<Long, Double> pagosMap = new HashMap<>();

    if (idsFactura.isEmpty()) {
      return pagosMap;
    }

    List<Integer> idsInteger = idsFactura.stream()
      .map(Long::intValue)
      .collect(Collectors.toList());

    List<Object[]> resultados = repConsignacionPagos.sumMontoPagosByFacturas(idsInteger);

    // Inicializar todas las facturas con 0.0
    for (Long id : idsFactura) {
      pagosMap.put(id, 0.0);
    }

    // Actualizar con los valores reales
    for (Object[] resultado : resultados) {
      if (resultado[0] != null) {
        Integer idFactura = (Integer) resultado[0];
        Double totalPagado = resultado[1] != null ? (Double) resultado[1] : 0.0;
        pagosMap.put(idFactura.longValue(), totalPagado);
      }
    }

    return pagosMap;
  }
}
