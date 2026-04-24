package BackERP.repository;

import BackERP.models.erpProductos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RepositoryProductos extends JpaRepository<erpProductos, Long>, JpaSpecificationExecutor<erpProductos> {

  @Query(value = "SELECT * FROM erpConfig.erpProductos p " +
    "WHERE p.estado = 1 " +
    "AND MATCH(p.descripcionProducto) AGAINST (?1 IN BOOLEAN MODE)",
    nativeQuery = true)
  List<erpProductos> buscarPorDescripcionFullText(String descripcion);

  @Query(value = "SELECT * FROM erpConfig.erpProductos p " +
    "WHERE p.estado = 1 " +
    "AND MATCH(p.codigoProducto, p.codigoProductoProveedor) AGAINST (?1 IN BOOLEAN MODE)",
    nativeQuery = true)
  List<erpProductos> buscarPorCodigoFullText(String texto);

}
