package aws.salaunnch.demo.service.impl;

import aws.salaunnch.demo.service.ShipLogService;
import aws.salaunnch.demo.domain.ShipLog;
import aws.salaunnch.demo.repository.ShipLogRepository;
import aws.salaunnch.demo.repository.search.ShipLogSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link ShipLog}.
 */
@Service
@Transactional
public class ShipLogServiceImpl implements ShipLogService {

    private final Logger log = LoggerFactory.getLogger(ShipLogServiceImpl.class);

    private final ShipLogRepository shipLogRepository;

    private final ShipLogSearchRepository shipLogSearchRepository;

    public ShipLogServiceImpl(ShipLogRepository shipLogRepository, ShipLogSearchRepository shipLogSearchRepository) {
        this.shipLogRepository = shipLogRepository;
        this.shipLogSearchRepository = shipLogSearchRepository;
    }

    /**
     * Save a shipLog.
     *
     * @param shipLog the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ShipLog save(ShipLog shipLog) {
        log.debug("Request to save ShipLog : {}", shipLog);
        ShipLog result = shipLogRepository.save(shipLog);
        shipLogSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the shipLogs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ShipLog> findAll(Pageable pageable) {
        log.debug("Request to get all ShipLogs");
        return shipLogRepository.findAll(pageable);
    }


    /**
     * Get one shipLog by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ShipLog> findOne(Long id) {
        log.debug("Request to get ShipLog : {}", id);
        return shipLogRepository.findById(id);
    }

    /**
     * Delete the shipLog by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ShipLog : {}", id);
        shipLogRepository.deleteById(id);
        shipLogSearchRepository.deleteById(id);
    }

    /**
     * Search for the shipLog corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ShipLog> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ShipLogs for query {}", query);
        return shipLogSearchRepository.search(queryStringQuery(query), pageable);    }
}
