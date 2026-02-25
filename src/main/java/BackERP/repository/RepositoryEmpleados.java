package BackERP.repository;


import BackERP.models.erpempleados;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RepositoryEmpleados extends JpaRepository<erpempleados, Long>, JpaSpecificationExecutor<erpempleados> {
}
