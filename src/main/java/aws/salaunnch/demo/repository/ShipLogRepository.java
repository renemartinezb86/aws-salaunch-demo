package aws.salaunnch.demo.repository;

import aws.salaunnch.demo.domain.ShipLog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ShipLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShipLogRepository extends JpaRepository<ShipLog, Long> {

}
