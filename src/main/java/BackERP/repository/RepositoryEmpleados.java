package BackERP.repository;

import BackERP.models.erpempleados;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryEmpleados extends JpaRepository<erpempleados, Long> {
}
