package aws.salaunnch.demo.service.impl;

import aws.salaunnch.demo.service.ShipService;
import aws.salaunnch.demo.domain.Ship;
import aws.salaunnch.demo.repository.ShipRepository;
import aws.salaunnch.demo.repository.search.ShipSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Ship}.
 */
@Service
@Transactional
public class ShipServiceImpl implements ShipService {

    private final Logger log = LoggerFactory.getLogger(ShipServiceImpl.class);

    private final ShipRepository shipRepository;

    private final ShipSearchRepository shipSearchRepository;

    public ShipServiceImpl(ShipRepository shipRepository, ShipSearchRepository shipSearchRepository) {
        this.shipRepository = shipRepository;
        this.shipSearchRepository = shipSearchRepository;
    }

    /**
     * Save a ship.
     *
     * @param ship the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Ship save(Ship ship) {
        log.debug("Request to save Ship : {}", ship);
        Ship result = shipRepository.save(ship);
        shipSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the ships.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Ship> findAll() {
        log.debug("Request to get all Ships");
        return shipRepository.findAll();
    }


    /**
     * Get one ship by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Ship> findOne(Long id) {
        log.debug("Request to get Ship : {}", id);
        return shipRepository.findById(id);
    }

    /**
     * Delete the ship by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ship : {}", id);
        shipRepository.deleteById(id);
        shipSearchRepository.deleteById(id);
    }

    /**
     * Search for the ship corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Ship> search(String query) {
        log.debug("Request to search Ships for query {}", query);
        return StreamSupport
            .stream(shipSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
