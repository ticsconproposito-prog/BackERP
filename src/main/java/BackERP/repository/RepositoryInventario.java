package BackERP.repository;

import BackERP.models.erpInventario;
import BackERP.models.erpInventarioAgrupadoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RepositoryInventario extends JpaRepository<erpInventario, Long>, JpaSpecificationExecutor<erpInventario> {

    @Query("SELECT new BackERP.models.erpInventarioAgrupadoDTO(" +
            "i.idProducto, SUM(i.cantidadExistencias), SUM(i.cantidadDanados)) " +
            "FROM erpInventario i " +
            "WHERE i.estado = 1 " +
            "GROUP BY i.idProducto")
    List<erpInventarioAgrupadoDTO> obtenerInventarioAgrupado();

    // Nuevo metodo para buscar inventario por producto y ubicación

    erpInventario findByIdProducto_IdProductoAndIdUbicacion(Long idProducto, int idUbicacion);
    erpInventario findByIdProducto_IdProducto(Long idProducto);

  @Query(value = "SELECT i.* FROM erpInventario i " +
    "JOIN erpProductos p ON i.idProducto = p.idProducto " +
    "WHERE i.estado = 1 " +
    "AND MATCH(p.descripcionProducto) AGAINST (?1 IN BOOLEAN MODE)",
    nativeQuery = true)
  List<erpInventario> buscarPorDescripcionFullText(String descripcion);

}
