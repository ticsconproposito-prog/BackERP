package BackERP.service;

import BackERP.models.ReordenamientoInventarioDTO;
import BackERP.models.erpInventario;
import BackERP.repository.RepositoryInventario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InventarioReordenamientoService {

  @Autowired
  private RepositoryInventario repinv;

  @Transactional
  public String reordenarInventario(List<ReordenamientoInventarioDTO.MovimientoOrden> movimientos, int idUsuario) {

    // Obtener todos los inventarios activos ordenados por ordenInventario
    List<erpInventario> inventarios = repinv.findAll().stream()
      .filter(i -> i.getEstado() == 1)
      .sorted(Comparator.comparingInt(erpInventario::getOrdenInventario))
      .collect(Collectors.toList());

    // Mapa para acceso rápido por ID
    Map<Long, erpInventario> mapaInventarios = inventarios.stream()
      .collect(Collectors.toMap(erpInventario::getIdInventario, i -> i));

    // Lista actualizada de inventarios en orden
    List<Long> ordenActual = inventarios.stream()
      .map(erpInventario::getIdInventario)
      .collect(Collectors.toList());

    // Aplicar cada movimiento
    for (ReordenamientoInventarioDTO.MovimientoOrden movimiento : movimientos) {
      if (!aplicarMovimiento(ordenActual, movimiento.getIdInventarioMover(), movimiento.getIdInventarioPrevio())) {
        throw new RuntimeException("Error al aplicar movimiento");
      }
    }

    // Actualizar todos los ordenInventario
    int nuevoOrden = 1;
    for (Long idInventario : ordenActual) {
      erpInventario inventario = mapaInventarios.get(idInventario);
      inventario.setOrdenInventario(nuevoOrden++);
      inventario.setFechaModificacion(LocalDate.now());
      inventario.setHoraModificacion(LocalTime.now());
      inventario.setIdUsuarioModificacion(idUsuario);
      repinv.save(inventario);
    }

    return "Reordenamiento completado exitosamente";
  }

  /**
   * Aplica un movimiento individual a la lista de orden
   * @param listaOrden Lista actual de IDs en orden
   * @param idMover ID del inventario a mover
   * @param idPrevio ID del inventario previo (antes de donde se moverá)
   * @return true si se aplicó correctamente
   */
  private boolean aplicarMovimiento(List<Long> listaOrden, Long idMover, Long idPrevio) {
    // Verificar que ambos IDs existan en la lista
    if (!listaOrden.contains(idMover)) {
      return false;
    }

    // Remover el elemento de su posición actual
    listaOrden.remove(idMover);

    if (idPrevio == null) {
      // Mover al inicio (si no hay previo)
      listaOrden.add(0, idMover);
    } else if (listaOrden.contains(idPrevio)) {
      // Encontrar índice del previo
      int indicePrevio = listaOrden.indexOf(idPrevio);
      // Insertar después del previo
      listaOrden.add(indicePrevio + 1, idMover);
    } else {
      // Si no se encuentra el previo, mover al final
      listaOrden.add(idMover);
    }

    return true;
  }

  /**
   * Método para mover un solo elemento (simplificado)
   */
  @Transactional
  public String moverUnElemento(Long idInventarioMover, Long idInventarioPrevio, int idUsuario) {
    List<ReordenamientoInventarioDTO.MovimientoOrden> movimientos = new ArrayList<>();
    ReordenamientoInventarioDTO.MovimientoOrden movimiento = new ReordenamientoInventarioDTO.MovimientoOrden();
    movimiento.setIdInventarioMover(idInventarioMover);
    movimiento.setIdInventarioPrevio(idInventarioPrevio);
    movimientos.add(movimiento);
    return reordenarInventario(movimientos, idUsuario);
  }
}
