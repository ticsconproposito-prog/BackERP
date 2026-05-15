package BackERP.repository;

import BackERP.models.erpInventario;
import BackERP.models.erpInventarioAgrupadoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface RepositoryInventario extends JpaRepository<erpInventario, Long>, JpaSpecificationExecutor<erpInventario> {

  // ============================================
  // MÉTODOS EXISTENTES (optimizados con EntityGraph)
  // ============================================

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

  // ============================================
  // MÉTODOS DE ACTUALIZACIÓN EXISTENTES
  // ============================================

  // Restaurar inventario (incrementar existencias)
  @Modifying
  @Transactional
  @Query("UPDATE erpInventario i SET i.cantidadExistencias = i.cantidadExistencias + :cantidad, " +
    "i.fechaModificacion = CURRENT_DATE, i.horaModificacion = CURRENT_TIME, " +
    "i.idUsuarioModificacion = :idUsuario WHERE i.idInventario = :idInventario")
  void restaurarExistencia(@Param("idInventario") Long idInventario,
                           @Param("cantidad") int cantidad,
                           @Param("idUsuario") int idUsuarioModificacion);

  // ============================================
  // NUEVOS MÉTODOS PARA REORDENAMIENTO
  // ============================================

  /**
   * Obtener todos los inventarios activos ordenados por ordenInventario
   * Útil para reordenar la lista completa
   */
  @Query("SELECT i FROM erpInventario i WHERE i.estado = 1 ORDER BY i.ordenInventario ASC")
  @EntityGraph(attributePaths = {"idProducto"})
  List<erpInventario> findAllActivosOrderByOrden();

  /**
   * Obtener el máximo ordenInventario actual
   * Útil para saber el último número de orden
   */
  @Query("SELECT MAX(i.ordenInventario) FROM erpInventario i WHERE i.estado = 1")
  Integer getMaxOrdenInventario();

  /**
   * Obtener el mínimo ordenInventario actual
   * Útil para saber el primer número de orden
   */
  @Query("SELECT MIN(i.ordenInventario) FROM erpInventario i WHERE i.estado = 1")
  Integer getMinOrdenInventario();

  /**
   * Obtener un inventario por su ordenInventario
   * Útil para encontrar qué registro está en una posición específica
   */
  @EntityGraph(attributePaths = {"idProducto"})
  Optional<erpInventario> findByOrdenInventarioAndEstado(int ordenInventario, int estado);

  /**
   * Obtener todos los inventarios con ordenInventario mayor o igual a un valor
   * Útil para desplazar órdenes hacia arriba o abajo
   */
  @Query("SELECT i FROM erpInventario i WHERE i.estado = 1 AND i.ordenInventario >= :ordenInicial ORDER BY i.ordenInventario ASC")
  @EntityGraph(attributePaths = {"idProducto"})
  List<erpInventario> findAllByOrdenInventarioGreaterThanEqual(@Param("ordenInicial") int ordenInicial);

  /**
   * Actualizar el ordenInventario de un inventario específico
   * Útil para actualizaciones individuales
   */
  @Modifying
  @Transactional
  @Query("UPDATE erpInventario i SET i.ordenInventario = :nuevoOrden, " +
    "i.fechaModificacion = CURRENT_DATE, i.horaModificacion = CURRENT_TIME, " +
    "i.idUsuarioModificacion = :idUsuario WHERE i.idInventario = :idInventario")
  void actualizarOrdenInventario(@Param("idInventario") Long idInventario,
                                 @Param("nuevoOrden") int nuevoOrden,
                                 @Param("idUsuario") int idUsuario);

  /**
   * Desplazar órdenes en un rango específico
   * Útil para hacer espacio o cerrar huecos
   */
  @Modifying
  @Transactional
  @Query("UPDATE erpInventario i SET i.ordenInventario = i.ordenInventario + :incremento " +
    "WHERE i.estado = 1 AND i.ordenInventario >= :desde AND i.ordenInventario <= :hasta")
  void desplazarOrdenes(@Param("desde") int desde,
                        @Param("hasta") int hasta,
                        @Param("incremento") int incremento);

  /**
   * Verificar si existe un ordenInventario específico
   */
  boolean existsByOrdenInventarioAndEstado(int ordenInventario, int estado);

  /**
   * Contar el número total de inventarios activos
   */
  long countByEstado(int estado);
}
