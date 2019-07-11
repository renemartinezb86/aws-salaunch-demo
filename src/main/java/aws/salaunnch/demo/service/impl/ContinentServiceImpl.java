package aws.salaunnch.demo.service.impl;

import aws.salaunnch.demo.service.ContinentService;
import aws.salaunnch.demo.domain.Continent;
import aws.salaunnch.demo.repository.ContinentRepository;
import aws.salaunnch.demo.repository.search.ContinentSearchRepository;
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
 * Service Implementation for managing {@link Continent}.
 */
@Service
@Transactional
public class ContinentServiceImpl implements ContinentService {

    private final Logger log = LoggerFactory.getLogger(ContinentServiceImpl.class);

    private final ContinentRepository continentRepository;

    private final ContinentSearchRepository continentSearchRepository;

    public ContinentServiceImpl(ContinentRepository continentRepository, ContinentSearchRepository continentSearchRepository) {
        this.continentRepository = continentRepository;
        this.continentSearchRepository = continentSearchRepository;
    }

    /**
     * Save a continent.
     *
     * @param continent the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Continent save(Continent continent) {
        log.debug("Request to save Continent : {}", continent);
        Continent result = continentRepository.save(continent);
        continentSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the continents.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Continent> findAll() {
        log.debug("Request to get all Continents");
        return continentRepository.findAll();
    }


    /**
     * Get one continent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Continent> findOne(Long id) {
        log.debug("Request to get Continent : {}", id);
        return continentRepository.findById(id);
    }

    /**
     * Delete the continent by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Continent : {}", id);
        continentRepository.deleteById(id);
        continentSearchRepository.deleteById(id);
    }

    /**
     * Search for the continent corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Continent> search(String query) {
        log.debug("Request to search Continents for query {}", query);
        return StreamSupport
            .stream(continentSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
