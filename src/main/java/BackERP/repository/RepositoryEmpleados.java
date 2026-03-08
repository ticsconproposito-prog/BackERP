package BackERP.repository;


import BackERP.models.erpEmpleados;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RepositoryEmpleados extends JpaRepository<erpEmpleados, Long>, JpaSpecificationExecutor<erpEmpleados> {
}
