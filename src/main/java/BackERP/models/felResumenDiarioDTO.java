package BackERP.models;

import java.time.LocalDate;

public class felResumenDiarioDTO {
    private LocalDate fecha;
    private double totalVentas;
    private int totalProductos;

    public felResumenDiarioDTO(LocalDate fecha, double totalVentas, int totalProductos) {
        this.fecha = fecha;
        this.totalVentas = totalVentas;
        this.totalProductos = totalProductos;
    }

    // getters y setters
}
