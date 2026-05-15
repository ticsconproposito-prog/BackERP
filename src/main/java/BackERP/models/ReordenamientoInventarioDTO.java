package BackERP.models;

import java.util.List;

public class ReordenamientoInventarioDTO {
  private List<MovimientoOrden> movimientos;

  // Getters y Setters
  public List<MovimientoOrden> getMovimientos() {
    return movimientos;
  }

  public void setMovimientos(List<MovimientoOrden> movimientos) {
    this.movimientos = movimientos;
  }

  public static class MovimientoOrden {
    private Long idInventarioMover;  // El que se va a mover
    private Long idInventarioPrevio; // El que está antes de donde se moverá

    public Long getIdInventarioMover() {
      return idInventarioMover;
    }

    public void setIdInventarioMover(Long idInventarioMover) {
      this.idInventarioMover = idInventarioMover;
    }

    public Long getIdInventarioPrevio() {
      return idInventarioPrevio;
    }

    public void setIdInventarioPrevio(Long idInventarioPrevio) {
      this.idInventarioPrevio = idInventarioPrevio;
    }
  }
}
