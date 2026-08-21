package BackERP.controller;

import BackERP.models.ReordenamientoInventarioDTO;
import BackERP.service.InventarioReordenamientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventario")
public class InventarioReordenamientoController {

  @Autowired
  private InventarioReordenamientoService reordenamientoService;

  /**
   * Endpoint para reordenar múltiples movimientos
   * Ejemplo de body JSON:
   * {
   *   "movimientos": [
   *     {"idInventarioMover": 12, "idInventarioPrevio": 10},
   *     {"idInventarioMover": 6, "idInventarioPrevio": null}
   *   ]
   * }
   */
  @PostMapping("/reordenar")
  public String reordenarInventario(
    @RequestBody ReordenamientoInventarioDTO request,
    @RequestParam int idUsuario) {
    return reordenamientoService.reordenarInventario(request.getMovimientos(), idUsuario);
  }

  /**
   * Endpoint para mover un solo elemento
   * GET: /api/inventario/mover?mover=12&previo=10&usuario=1
   */
  @PostMapping("/mover")
  public String moverElemento(
    @RequestParam Long mover,
    @RequestParam(required = false) Long previo,
    @RequestParam int usuario) {
    return reordenamientoService.moverUnElemento(mover, previo, usuario);
  }
}
