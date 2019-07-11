package aws.salaunnch.demo.repository;

import aws.salaunnch.demo.domain.Marine;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Marine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MarineRepository extends JpaRepository<Marine, Long> {

}
