package aws.salaunnch.demo.service;

import aws.salaunnch.demo.domain.ShipLog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link ShipLog}.
 */
public interface ShipLogService {

    /**
     * Save a shipLog.
     *
     * @param shipLog the entity to save.
     * @return the persisted entity.
     */
    ShipLog save(ShipLog shipLog);

    /**
     * Get all the shipLogs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ShipLog> findAll(Pageable pageable);


    /**
     * Get the "id" shipLog.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ShipLog> findOne(Long id);

    /**
     * Delete the "id" shipLog.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the shipLog corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ShipLog> search(String query, Pageable pageable);
}
