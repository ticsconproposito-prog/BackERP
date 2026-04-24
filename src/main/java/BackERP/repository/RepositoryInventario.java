package BackERP.repository;

import BackERP.models.erpInventario;
import BackERP.models.erpInventarioAgrupadoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RepositoryInventario extends JpaRepository<erpInventario, Long>, JpaSpecificationExecutor<erpInventario> {

  // 🔥 1. MÉTODO CRÍTICO: El que usa paginación con Specifications
  @Override
  @EntityGraph(attributePaths = {"idProducto"})
  Page<erpInventario> findAll(Specification<erpInventario> spec, Pageable pageable);

  // 🔥 2. Misma mejora para búsquedas sin paginación
  @Override
  @EntityGraph(attributePaths = {"idProducto"})
  List<erpInventario> findAll(Specification<erpInventario> spec);

  // 🔥 3. Versión optimizada de findById (evita lazy loading)
  @Override
  @EntityGraph(attributePaths = {"idProducto"})
  Optional<erpInventario> findById(Long id);

  // 🔥 4. Query agrupada (sin cambios)
  @Query("SELECT new BackERP.models.erpInventarioAgrupadoDTO(" +
    "i.idProducto, SUM(i.cantidadExistencias), SUM(i.cantidadDanados)) " +
    "FROM erpInventario i " +
    "WHERE i.estado = 1 " +
    "GROUP BY i.idProducto")
  List<erpInventarioAgrupadoDTO> obtenerInventarioAgrupado();

  // 🔥 5. Buscar por producto y ubicación (retorna UN solo inventario)
  @EntityGraph(attributePaths = {"idProducto"})
  erpInventario findByIdProducto_IdProductoAndIdUbicacion(Long idProducto, int idUbicacion);

  // 🔥 6. Buscar TODOS los inventarios de un producto (retorna LISTA)
  @EntityGraph(attributePaths = {"idProducto"})
  List<erpInventario> findByIdProducto_IdProducto(Long idProducto);

  // 🔥 7. Búsqueda por producto con paginación
  @EntityGraph(attributePaths = {"idProducto"})
  Page<erpInventario> findByIdProducto_IdProducto(Long idProducto, Pageable pageable);

  // 🔥 8. Método adicional: buscar por ubicación
  @EntityGraph(attributePaths = {"idProducto"})
  List<erpInventario> findByIdUbicacion(int idUbicacion);
}
