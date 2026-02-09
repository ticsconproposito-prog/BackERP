package BackERP.repository;

import BackERP.models.erpUbicaciones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RepositoryUbicaciones extends JpaRepository<erpUbicaciones, Long>, JpaSpecificationExecutor<erpUbicaciones> {


}
