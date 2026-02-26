package BackERP.repository;

import BackERP.models.segLogins;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RepositorySegLogins extends JpaRepository<segLogins, Integer>, JpaSpecificationExecutor<segLogins> {
}
