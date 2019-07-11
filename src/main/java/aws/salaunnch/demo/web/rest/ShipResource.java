package aws.salaunnch.demo.web.rest;

import aws.salaunnch.demo.domain.Ship;
import aws.salaunnch.demo.service.ShipService;
import aws.salaunnch.demo.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link aws.salaunnch.demo.domain.Ship}.
 */
@RestController
@RequestMapping("/api")
public class ShipResource {

    private final Logger log = LoggerFactory.getLogger(ShipResource.class);

    private static final String ENTITY_NAME = "awSsaLaunchShip";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShipService shipService;

    public ShipResource(ShipService shipService) {
        this.shipService = shipService;
    }

    /**
     * {@code POST  /ships} : Create a new ship.
     *
     * @param ship the ship to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ship, or with status {@code 400 (Bad Request)} if the ship has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ships")
    public ResponseEntity<Ship> createShip(@Valid @RequestBody Ship ship) throws URISyntaxException {
        log.debug("REST request to save Ship : {}", ship);
        if (ship.getId() != null) {
            throw new BadRequestAlertException("A new ship cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ship result = shipService.save(ship);
        return ResponseEntity.created(new URI("/api/ships/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ships} : Updates an existing ship.
     *
     * @param ship the ship to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ship,
     * or with status {@code 400 (Bad Request)} if the ship is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ship couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ships")
    public ResponseEntity<Ship> updateShip(@Valid @RequestBody Ship ship) throws URISyntaxException {
        log.debug("REST request to update Ship : {}", ship);
        if (ship.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Ship result = shipService.save(ship);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ship.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /ships} : get all the ships.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ships in body.
     */
    @GetMapping("/ships")
    public List<Ship> getAllShips() {
        log.debug("REST request to get all Ships");
        return shipService.findAll();
    }

    /**
     * {@code GET  /ships/:id} : get the "id" ship.
     *
     * @param id the id of the ship to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ship, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ships/{id}")
    public ResponseEntity<Ship> getShip(@PathVariable Long id) {
        log.debug("REST request to get Ship : {}", id);
        Optional<Ship> ship = shipService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ship);
    }

    /**
     * {@code DELETE  /ships/:id} : delete the "id" ship.
     *
     * @param id the id of the ship to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ships/{id}")
    public ResponseEntity<Void> deleteShip(@PathVariable Long id) {
        log.debug("REST request to delete Ship : {}", id);
        shipService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/ships?query=:query} : search for the ship corresponding
     * to the query.
     *
     * @param query the query of the ship search.
     * @return the result of the search.
     */
    @GetMapping("/_search/ships")
    public List<Ship> searchShips(@RequestParam String query) {
        log.debug("REST request to search Ships for query {}", query);
        return shipService.search(query);
    }

}
