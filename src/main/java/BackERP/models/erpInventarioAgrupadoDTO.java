package BackERP.models;


public class erpInventarioAgrupadoDTO {
    private erpProductos producto;
    private Long totalExistencias;
    private Long totalDanados;

    public erpInventarioAgrupadoDTO(erpProductos producto, Long totalExistencias, Long totalDanados) {
        this.producto = producto;
        this.totalExistencias = totalExistencias;
        this.totalDanados = totalDanados;
    }

    public erpProductos getProducto() {
        return producto;
    }

    public Long getTotalExistencias() {
        return totalExistencias;
    }

    public Long getTotalDanados() {
        return totalDanados;
    }
}
