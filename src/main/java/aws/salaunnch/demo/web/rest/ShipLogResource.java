package aws.salaunnch.demo.web.rest;

import aws.salaunnch.demo.domain.ShipLog;
import aws.salaunnch.demo.service.ShipLogService;
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
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link aws.salaunnch.demo.domain.ShipLog}.
 */
@RestController
@RequestMapping("/api")
public class ShipLogResource {

    private final Logger log = LoggerFactory.getLogger(ShipLogResource.class);

    private static final String ENTITY_NAME = "awSsaLaunchShipLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShipLogService shipLogService;

    public ShipLogResource(ShipLogService shipLogService) {
        this.shipLogService = shipLogService;
    }

    /**
     * {@code POST  /ship-logs} : Create a new shipLog.
     *
     * @param shipLog the shipLog to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shipLog, or with status {@code 400 (Bad Request)} if the shipLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ship-logs")
    public ResponseEntity<ShipLog> createShipLog(@RequestBody ShipLog shipLog) throws URISyntaxException {
        log.debug("REST request to save ShipLog : {}", shipLog);
        if (shipLog.getId() != null) {
            throw new BadRequestAlertException("A new shipLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ShipLog result = shipLogService.save(shipLog);
        return ResponseEntity.created(new URI("/api/ship-logs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ship-logs} : Updates an existing shipLog.
     *
     * @param shipLog the shipLog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipLog,
     * or with status {@code 400 (Bad Request)} if the shipLog is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shipLog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ship-logs")
    public ResponseEntity<ShipLog> updateShipLog(@RequestBody ShipLog shipLog) throws URISyntaxException {
        log.debug("REST request to update ShipLog : {}", shipLog);
        if (shipLog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ShipLog result = shipLogService.save(shipLog);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipLog.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /ship-logs} : get all the shipLogs.
     *
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shipLogs in body.
     */
    @GetMapping("/ship-logs")
    public ResponseEntity<List<ShipLog>> getAllShipLogs(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of ShipLogs");
        Page<ShipLog> page = shipLogService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ship-logs/:id} : get the "id" shipLog.
     *
     * @param id the id of the shipLog to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shipLog, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ship-logs/{id}")
    public ResponseEntity<ShipLog> getShipLog(@PathVariable Long id) {
        log.debug("REST request to get ShipLog : {}", id);
        Optional<ShipLog> shipLog = shipLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shipLog);
    }

    /**
     * {@code DELETE  /ship-logs/:id} : delete the "id" shipLog.
     *
     * @param id the id of the shipLog to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ship-logs/{id}")
    public ResponseEntity<Void> deleteShipLog(@PathVariable Long id) {
        log.debug("REST request to delete ShipLog : {}", id);
        shipLogService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/ship-logs?query=:query} : search for the shipLog corresponding
     * to the query.
     *
     * @param query the query of the shipLog search.
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @return the result of the search.
     */
    @GetMapping("/_search/ship-logs")
    public ResponseEntity<List<ShipLog>> searchShipLogs(@RequestParam String query, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to search for a page of ShipLogs for query {}", query);
        Page<ShipLog> page = shipLogService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
