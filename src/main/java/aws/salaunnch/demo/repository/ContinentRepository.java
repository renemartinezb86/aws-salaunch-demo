package aws.salaunnch.demo.repository;

import aws.salaunnch.demo.domain.Continent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Continent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContinentRepository extends JpaRepository<Continent, Long> {

}
