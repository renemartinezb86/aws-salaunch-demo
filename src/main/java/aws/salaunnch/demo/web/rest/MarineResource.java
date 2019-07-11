package aws.salaunnch.demo.web.rest;

import aws.salaunnch.demo.domain.Marine;
import aws.salaunnch.demo.repository.MarineRepository;
import aws.salaunnch.demo.repository.search.MarineSearchRepository;
import aws.salaunnch.demo.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link aws.salaunnch.demo.domain.Marine}.
 */
@RestController
@RequestMapping("/api")
public class MarineResource {

    private final Logger log = LoggerFactory.getLogger(MarineResource.class);

    private static final String ENTITY_NAME = "awSsaLaunchMarine";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MarineRepository marineRepository;

    private final MarineSearchRepository marineSearchRepository;

    public MarineResource(MarineRepository marineRepository, MarineSearchRepository marineSearchRepository) {
        this.marineRepository = marineRepository;
        this.marineSearchRepository = marineSearchRepository;
    }

    /**
     * {@code POST  /marines} : Create a new marine.
     *
     * @param marine the marine to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new marine, or with status {@code 400 (Bad Request)} if the marine has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/marines")
    public ResponseEntity<Marine> createMarine(@RequestBody Marine marine) throws URISyntaxException {
        log.debug("REST request to save Marine : {}", marine);
        if (marine.getId() != null) {
            throw new BadRequestAlertException("A new marine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Marine result = marineRepository.save(marine);
        marineSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/marines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /marines} : Updates an existing marine.
     *
     * @param marine the marine to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated marine,
     * or with status {@code 400 (Bad Request)} if the marine is not valid,
     * or with status {@code 500 (Internal Server Error)} if the marine couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/marines")
    public ResponseEntity<Marine> updateMarine(@RequestBody Marine marine) throws URISyntaxException {
        log.debug("REST request to update Marine : {}", marine);
        if (marine.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Marine result = marineRepository.save(marine);
        marineSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, marine.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /marines} : get all the marines.
     *
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of marines in body.
     */
    @GetMapping("/marines")
    public ResponseEntity<List<Marine>> getAllMarines(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of Marines");
        Page<Marine> page = marineRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /marines/:id} : get the "id" marine.
     *
     * @param id the id of the marine to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the marine, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/marines/{id}")
    public ResponseEntity<Marine> getMarine(@PathVariable Long id) {
        log.debug("REST request to get Marine : {}", id);
        Optional<Marine> marine = marineRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(marine);
    }

    /**
     * {@code DELETE  /marines/:id} : delete the "id" marine.
     *
     * @param id the id of the marine to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/marines/{id}")
    public ResponseEntity<Void> deleteMarine(@PathVariable Long id) {
        log.debug("REST request to delete Marine : {}", id);
        marineRepository.deleteById(id);
        marineSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/marines?query=:query} : search for the marine corresponding
     * to the query.
     *
     * @param query the query of the marine search.
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @return the result of the search.
     */
    @GetMapping("/_search/marines")
    public ResponseEntity<List<Marine>> searchMarines(@RequestParam String query, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to search for a page of Marines for query {}", query);
        Page<Marine> page = marineSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
