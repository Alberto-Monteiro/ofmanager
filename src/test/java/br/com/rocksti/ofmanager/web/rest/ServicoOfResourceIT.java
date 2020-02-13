package br.com.rocksti.ofmanager.web.rest;

import br.com.rocksti.ofmanager.OfmanagerApp;
import br.com.rocksti.ofmanager.domain.ServicoOf;
import br.com.rocksti.ofmanager.repository.ServicoOfRepository;
import br.com.rocksti.ofmanager.service.ServicoOfService;
import br.com.rocksti.ofmanager.service.dto.ServicoOfDTO;
import br.com.rocksti.ofmanager.service.mapper.ServicoOfMapper;
import br.com.rocksti.ofmanager.web.rest.errors.ExceptionTranslator;

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
import java.util.List;

import static br.com.rocksti.ofmanager.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ServicoOfResource} REST controller.
 */
@SpringBootTest(classes = OfmanagerApp.class)
public class ServicoOfResourceIT {

    private static final Long DEFAULT_USERID = 1L;
    private static final Long UPDATED_USERID = 2L;

    private static final Integer DEFAULT_NUMERO = 1;
    private static final Integer UPDATED_NUMERO = 2;

    @Autowired
    private ServicoOfRepository servicoOfRepository;

    @Autowired
    private ServicoOfMapper servicoOfMapper;

    @Autowired
    private ServicoOfService servicoOfService;

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

    private MockMvc restServicoOfMockMvc;

    private ServicoOf servicoOf;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ServicoOfResource servicoOfResource = new ServicoOfResource(servicoOfService);
        this.restServicoOfMockMvc = MockMvcBuilders.standaloneSetup(servicoOfResource)
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
    public static ServicoOf createEntity(EntityManager em) {
        ServicoOf servicoOf = new ServicoOf()
            .userid(DEFAULT_USERID)
            .numero(DEFAULT_NUMERO);
        return servicoOf;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServicoOf createUpdatedEntity(EntityManager em) {
        ServicoOf servicoOf = new ServicoOf()
            .userid(UPDATED_USERID)
            .numero(UPDATED_NUMERO);
        return servicoOf;
    }

    @BeforeEach
    public void initTest() {
        servicoOf = createEntity(em);
    }

    @Test
    @Transactional
    public void createServicoOf() throws Exception {
        int databaseSizeBeforeCreate = servicoOfRepository.findAll().size();

        // Create the ServicoOf
        ServicoOfDTO servicoOfDTO = servicoOfMapper.toDto(servicoOf);
        restServicoOfMockMvc.perform(post("/api/servico-ofs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(servicoOfDTO)))
            .andExpect(status().isCreated());

        // Validate the ServicoOf in the database
        List<ServicoOf> servicoOfList = servicoOfRepository.findAll();
        assertThat(servicoOfList).hasSize(databaseSizeBeforeCreate + 1);
        ServicoOf testServicoOf = servicoOfList.get(servicoOfList.size() - 1);
        assertThat(testServicoOf.getUserid()).isEqualTo(DEFAULT_USERID);
        assertThat(testServicoOf.getNumero()).isEqualTo(DEFAULT_NUMERO);
    }

    @Test
    @Transactional
    public void createServicoOfWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = servicoOfRepository.findAll().size();

        // Create the ServicoOf with an existing ID
        servicoOf.setId(1L);
        ServicoOfDTO servicoOfDTO = servicoOfMapper.toDto(servicoOf);

        // An entity with an existing ID cannot be created, so this API call must fail
        restServicoOfMockMvc.perform(post("/api/servico-ofs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(servicoOfDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ServicoOf in the database
        List<ServicoOf> servicoOfList = servicoOfRepository.findAll();
        assertThat(servicoOfList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkUseridIsRequired() throws Exception {
        int databaseSizeBeforeTest = servicoOfRepository.findAll().size();
        // set the field null
        servicoOf.setUserid(null);

        // Create the ServicoOf, which fails.
        ServicoOfDTO servicoOfDTO = servicoOfMapper.toDto(servicoOf);

        restServicoOfMockMvc.perform(post("/api/servico-ofs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(servicoOfDTO)))
            .andExpect(status().isBadRequest());

        List<ServicoOf> servicoOfList = servicoOfRepository.findAll();
        assertThat(servicoOfList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNumeroIsRequired() throws Exception {
        int databaseSizeBeforeTest = servicoOfRepository.findAll().size();
        // set the field null
        servicoOf.setNumero(null);

        // Create the ServicoOf, which fails.
        ServicoOfDTO servicoOfDTO = servicoOfMapper.toDto(servicoOf);

        restServicoOfMockMvc.perform(post("/api/servico-ofs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(servicoOfDTO)))
            .andExpect(status().isBadRequest());

        List<ServicoOf> servicoOfList = servicoOfRepository.findAll();
        assertThat(servicoOfList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllServicoOfs() throws Exception {
        // Initialize the database
        servicoOfRepository.saveAndFlush(servicoOf);

        // Get all the servicoOfList
        restServicoOfMockMvc.perform(get("/api/servico-ofs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(servicoOf.getId().intValue())))
            .andExpect(jsonPath("$.[*].userid").value(hasItem(DEFAULT_USERID.intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)));
    }
    
    @Test
    @Transactional
    public void getServicoOf() throws Exception {
        // Initialize the database
        servicoOfRepository.saveAndFlush(servicoOf);

        // Get the servicoOf
        restServicoOfMockMvc.perform(get("/api/servico-ofs/{id}", servicoOf.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(servicoOf.getId().intValue()))
            .andExpect(jsonPath("$.userid").value(DEFAULT_USERID.intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO));
    }

    @Test
    @Transactional
    public void getNonExistingServicoOf() throws Exception {
        // Get the servicoOf
        restServicoOfMockMvc.perform(get("/api/servico-ofs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateServicoOf() throws Exception {
        // Initialize the database
        servicoOfRepository.saveAndFlush(servicoOf);

        int databaseSizeBeforeUpdate = servicoOfRepository.findAll().size();

        // Update the servicoOf
        ServicoOf updatedServicoOf = servicoOfRepository.findById(servicoOf.getId()).get();
        // Disconnect from session so that the updates on updatedServicoOf are not directly saved in db
        em.detach(updatedServicoOf);
        updatedServicoOf
            .userid(UPDATED_USERID)
            .numero(UPDATED_NUMERO);
        ServicoOfDTO servicoOfDTO = servicoOfMapper.toDto(updatedServicoOf);

        restServicoOfMockMvc.perform(put("/api/servico-ofs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(servicoOfDTO)))
            .andExpect(status().isOk());

        // Validate the ServicoOf in the database
        List<ServicoOf> servicoOfList = servicoOfRepository.findAll();
        assertThat(servicoOfList).hasSize(databaseSizeBeforeUpdate);
        ServicoOf testServicoOf = servicoOfList.get(servicoOfList.size() - 1);
        assertThat(testServicoOf.getUserid()).isEqualTo(UPDATED_USERID);
        assertThat(testServicoOf.getNumero()).isEqualTo(UPDATED_NUMERO);
    }

    @Test
    @Transactional
    public void updateNonExistingServicoOf() throws Exception {
        int databaseSizeBeforeUpdate = servicoOfRepository.findAll().size();

        // Create the ServicoOf
        ServicoOfDTO servicoOfDTO = servicoOfMapper.toDto(servicoOf);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServicoOfMockMvc.perform(put("/api/servico-ofs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(servicoOfDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ServicoOf in the database
        List<ServicoOf> servicoOfList = servicoOfRepository.findAll();
        assertThat(servicoOfList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteServicoOf() throws Exception {
        // Initialize the database
        servicoOfRepository.saveAndFlush(servicoOf);

        int databaseSizeBeforeDelete = servicoOfRepository.findAll().size();

        // Delete the servicoOf
        restServicoOfMockMvc.perform(delete("/api/servico-ofs/{id}", servicoOf.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ServicoOf> servicoOfList = servicoOfRepository.findAll();
        assertThat(servicoOfList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
