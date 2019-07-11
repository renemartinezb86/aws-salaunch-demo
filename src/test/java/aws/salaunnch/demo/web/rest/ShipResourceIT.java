package aws.salaunnch.demo.web.rest;

import aws.salaunnch.demo.AwSsaLaunchApp;
import aws.salaunnch.demo.domain.Ship;
import aws.salaunnch.demo.repository.ShipRepository;
import aws.salaunnch.demo.repository.search.ShipSearchRepository;
import aws.salaunnch.demo.service.ShipService;
import aws.salaunnch.demo.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static aws.salaunnch.demo.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link ShipResource} REST controller.
 */
@SpringBootTest(classes = AwSsaLaunchApp.class)
public class ShipResourceIT {

    private static final String DEFAULT_SHIP_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SHIP_NAME = "BBBBBBBBBB";

    @Autowired
    private ShipRepository shipRepository;

    @Autowired
    private ShipService shipService;

    /**
     * This repository is mocked in the aws.salaunnch.demo.repository.search test package.
     *
     * @see aws.salaunnch.demo.repository.search.ShipSearchRepositoryMockConfiguration
     */
    @Autowired
    private ShipSearchRepository mockShipSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restShipMockMvc;

    private Ship ship;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ShipResource shipResource = new ShipResource(shipService);
        this.restShipMockMvc = MockMvcBuilders.standaloneSetup(shipResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ship createEntity(EntityManager em) {
        Ship ship = new Ship()
            .shipName(DEFAULT_SHIP_NAME);
        return ship;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ship createUpdatedEntity(EntityManager em) {
        Ship ship = new Ship()
            .shipName(UPDATED_SHIP_NAME);
        return ship;
    }

    @BeforeEach
    public void initTest() {
        ship = createEntity(em);
    }

    @Test
    @Transactional
    public void createShip() throws Exception {
        int databaseSizeBeforeCreate = shipRepository.findAll().size();

        // Create the Ship
        restShipMockMvc.perform(post("/api/ships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ship)))
            .andExpect(status().isCreated());

        // Validate the Ship in the database
        List<Ship> shipList = shipRepository.findAll();
        assertThat(shipList).hasSize(databaseSizeBeforeCreate + 1);
        Ship testShip = shipList.get(shipList.size() - 1);
        assertThat(testShip.getShipName()).isEqualTo(DEFAULT_SHIP_NAME);

        // Validate the Ship in Elasticsearch
        verify(mockShipSearchRepository, times(1)).save(testShip);
    }

    @Test
    @Transactional
    public void createShipWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = shipRepository.findAll().size();

        // Create the Ship with an existing ID
        ship.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restShipMockMvc.perform(post("/api/ships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ship)))
            .andExpect(status().isBadRequest());

        // Validate the Ship in the database
        List<Ship> shipList = shipRepository.findAll();
        assertThat(shipList).hasSize(databaseSizeBeforeCreate);

        // Validate the Ship in Elasticsearch
        verify(mockShipSearchRepository, times(0)).save(ship);
    }


    @Test
    @Transactional
    public void checkShipNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = shipRepository.findAll().size();
        // set the field null
        ship.setShipName(null);

        // Create the Ship, which fails.

        restShipMockMvc.perform(post("/api/ships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ship)))
            .andExpect(status().isBadRequest());

        List<Ship> shipList = shipRepository.findAll();
        assertThat(shipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllShips() throws Exception {
        // Initialize the database
        shipRepository.saveAndFlush(ship);

        // Get all the shipList
        restShipMockMvc.perform(get("/api/ships?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ship.getId().intValue())))
            .andExpect(jsonPath("$.[*].shipName").value(hasItem(DEFAULT_SHIP_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getShip() throws Exception {
        // Initialize the database
        shipRepository.saveAndFlush(ship);

        // Get the ship
        restShipMockMvc.perform(get("/api/ships/{id}", ship.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ship.getId().intValue()))
            .andExpect(jsonPath("$.shipName").value(DEFAULT_SHIP_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingShip() throws Exception {
        // Get the ship
        restShipMockMvc.perform(get("/api/ships/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShip() throws Exception {
        // Initialize the database
        shipService.save(ship);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockShipSearchRepository);

        int databaseSizeBeforeUpdate = shipRepository.findAll().size();

        // Update the ship
        Ship updatedShip = shipRepository.findById(ship.getId()).get();
        // Disconnect from session so that the updates on updatedShip are not directly saved in db
        em.detach(updatedShip);
        updatedShip
            .shipName(UPDATED_SHIP_NAME);

        restShipMockMvc.perform(put("/api/ships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedShip)))
            .andExpect(status().isOk());

        // Validate the Ship in the database
        List<Ship> shipList = shipRepository.findAll();
        assertThat(shipList).hasSize(databaseSizeBeforeUpdate);
        Ship testShip = shipList.get(shipList.size() - 1);
        assertThat(testShip.getShipName()).isEqualTo(UPDATED_SHIP_NAME);

        // Validate the Ship in Elasticsearch
        verify(mockShipSearchRepository, times(1)).save(testShip);
    }

    @Test
    @Transactional
    public void updateNonExistingShip() throws Exception {
        int databaseSizeBeforeUpdate = shipRepository.findAll().size();

        // Create the Ship

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipMockMvc.perform(put("/api/ships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ship)))
            .andExpect(status().isBadRequest());

        // Validate the Ship in the database
        List<Ship> shipList = shipRepository.findAll();
        assertThat(shipList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Ship in Elasticsearch
        verify(mockShipSearchRepository, times(0)).save(ship);
    }

    @Test
    @Transactional
    public void deleteShip() throws Exception {
        // Initialize the database
        shipService.save(ship);

        int databaseSizeBeforeDelete = shipRepository.findAll().size();

        // Delete the ship
        restShipMockMvc.perform(delete("/api/ships/{id}", ship.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ship> shipList = shipRepository.findAll();
        assertThat(shipList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Ship in Elasticsearch
        verify(mockShipSearchRepository, times(1)).deleteById(ship.getId());
    }

    @Test
    @Transactional
    public void searchShip() throws Exception {
        // Initialize the database
        shipService.save(ship);
        when(mockShipSearchRepository.search(queryStringQuery("id:" + ship.getId())))
            .thenReturn(Collections.singletonList(ship));
        // Search the ship
        restShipMockMvc.perform(get("/api/_search/ships?query=id:" + ship.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ship.getId().intValue())))
            .andExpect(jsonPath("$.[*].shipName").value(hasItem(DEFAULT_SHIP_NAME)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ship.class);
        Ship ship1 = new Ship();
        ship1.setId(1L);
        Ship ship2 = new Ship();
        ship2.setId(ship1.getId());
        assertThat(ship1).isEqualTo(ship2);
        ship2.setId(2L);
        assertThat(ship1).isNotEqualTo(ship2);
        ship1.setId(null);
        assertThat(ship1).isNotEqualTo(ship2);
    }
}
