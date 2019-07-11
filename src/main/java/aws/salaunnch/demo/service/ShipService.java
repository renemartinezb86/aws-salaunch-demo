package aws.salaunnch.demo.service;

import aws.salaunnch.demo.domain.Ship;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Ship}.
 */
public interface ShipService {

    /**
     * Save a ship.
     *
     * @param ship the entity to save.
     * @return the persisted entity.
     */
    Ship save(Ship ship);

    /**
     * Get all the ships.
     *
     * @return the list of entities.
     */
    List<Ship> findAll();


    /**
     * Get the "id" ship.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Ship> findOne(Long id);

    /**
     * Delete the "id" ship.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the ship corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @return the list of entities.
     */
    List<Ship> search(String query);
}
