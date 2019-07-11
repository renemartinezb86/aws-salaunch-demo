package aws.salaunnch.demo.web.rest;

import aws.salaunnch.demo.domain.Continent;
import aws.salaunnch.demo.service.ContinentService;
import aws.salaunnch.demo.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link aws.salaunnch.demo.domain.Continent}.
 */
@RestController
@RequestMapping("/api")
public class ContinentResource {

    private final Logger log = LoggerFactory.getLogger(ContinentResource.class);

    private static final String ENTITY_NAME = "awSsaLaunchContinent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContinentService continentService;

    public ContinentResource(ContinentService continentService) {
        this.continentService = continentService;
    }

    /**
     * {@code POST  /continents} : Create a new continent.
     *
     * @param continent the continent to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new continent, or with status {@code 400 (Bad Request)} if the continent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/continents")
    public ResponseEntity<Continent> createContinent(@RequestBody Continent continent) throws URISyntaxException {
        log.debug("REST request to save Continent : {}", continent);
        if (continent.getId() != null) {
            throw new BadRequestAlertException("A new continent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Continent result = continentService.save(continent);
        return ResponseEntity.created(new URI("/api/continents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /continents} : Updates an existing continent.
     *
     * @param continent the continent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated continent,
     * or with status {@code 400 (Bad Request)} if the continent is not valid,
     * or with status {@code 500 (Internal Server Error)} if the continent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/continents")
    public ResponseEntity<Continent> updateContinent(@RequestBody Continent continent) throws URISyntaxException {
        log.debug("REST request to update Continent : {}", continent);
        if (continent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Continent result = continentService.save(continent);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, continent.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /continents} : get all the continents.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of continents in body.
     */
    @GetMapping("/continents")
    public List<Continent> getAllContinents() {
        log.debug("REST request to get all Continents");
        return continentService.findAll();
    }

    /**
     * {@code GET  /continents/:id} : get the "id" continent.
     *
     * @param id the id of the continent to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the continent, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/continents/{id}")
    public ResponseEntity<Continent> getContinent(@PathVariable Long id) {
        log.debug("REST request to get Continent : {}", id);
        Optional<Continent> continent = continentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(continent);
    }

    /**
     * {@code DELETE  /continents/:id} : delete the "id" continent.
     *
     * @param id the id of the continent to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/continents/{id}")
    public ResponseEntity<Void> deleteContinent(@PathVariable Long id) {
        log.debug("REST request to delete Continent : {}", id);
        continentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/continents?query=:query} : search for the continent corresponding
     * to the query.
     *
     * @param query the query of the continent search.
     * @return the result of the search.
     */
    @GetMapping("/_search/continents")
    public List<Continent> searchContinents(@RequestParam String query) {
        log.debug("REST request to search Continents for query {}", query);
        return continentService.search(query);
    }

}
