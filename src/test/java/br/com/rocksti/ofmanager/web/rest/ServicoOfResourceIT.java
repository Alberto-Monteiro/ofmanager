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
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static br.com.rocksti.ofmanager.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.rocksti.ofmanager.domain.enumeration.EstadoOf;
/**
 * Integration tests for the {@link ServicoOfResource} REST controller.
 */
@SpringBootTest(classes = OfmanagerApp.class)
public class ServicoOfResourceIT {

    private static final Integer DEFAULT_NUMERO = 1;
    private static final Integer UPDATED_NUMERO = 2;

    private static final EstadoOf DEFAULT_ESTADO = EstadoOf.NOVA;
    private static final EstadoOf UPDATED_ESTADO = EstadoOf.ANALISE;

    private static final String DEFAULT_OBSERVACAO_DO_GESTOR = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACAO_DO_GESTOR = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_VALOR_USTIBB = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_USTIBB = new BigDecimal(2);

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
            .numero(DEFAULT_NUMERO)
            .estado(DEFAULT_ESTADO)
            .observacaoDoGestor(DEFAULT_OBSERVACAO_DO_GESTOR)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .valorUstibb(DEFAULT_VALOR_USTIBB);
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
            .numero(UPDATED_NUMERO)
            .estado(UPDATED_ESTADO)
            .observacaoDoGestor(UPDATED_OBSERVACAO_DO_GESTOR)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .valorUstibb(UPDATED_VALOR_USTIBB);
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
        assertThat(testServicoOf.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testServicoOf.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testServicoOf.getObservacaoDoGestor()).isEqualTo(DEFAULT_OBSERVACAO_DO_GESTOR);
        assertThat(testServicoOf.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testServicoOf.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testServicoOf.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testServicoOf.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testServicoOf.getValorUstibb()).isEqualTo(DEFAULT_VALOR_USTIBB);
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
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].observacaoDoGestor").value(hasItem(DEFAULT_OBSERVACAO_DO_GESTOR.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].valorUstibb").value(hasItem(DEFAULT_VALOR_USTIBB.intValue())));
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
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()))
            .andExpect(jsonPath("$.observacaoDoGestor").value(DEFAULT_OBSERVACAO_DO_GESTOR.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.valorUstibb").value(DEFAULT_VALOR_USTIBB.intValue()));
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
            .numero(UPDATED_NUMERO)
            .estado(UPDATED_ESTADO)
            .observacaoDoGestor(UPDATED_OBSERVACAO_DO_GESTOR)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .valorUstibb(UPDATED_VALOR_USTIBB);
        ServicoOfDTO servicoOfDTO = servicoOfMapper.toDto(updatedServicoOf);

        restServicoOfMockMvc.perform(put("/api/servico-ofs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(servicoOfDTO)))
            .andExpect(status().isOk());

        // Validate the ServicoOf in the database
        List<ServicoOf> servicoOfList = servicoOfRepository.findAll();
        assertThat(servicoOfList).hasSize(databaseSizeBeforeUpdate);
        ServicoOf testServicoOf = servicoOfList.get(servicoOfList.size() - 1);
        assertThat(testServicoOf.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testServicoOf.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testServicoOf.getObservacaoDoGestor()).isEqualTo(UPDATED_OBSERVACAO_DO_GESTOR);
        assertThat(testServicoOf.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testServicoOf.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testServicoOf.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testServicoOf.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testServicoOf.getValorUstibb()).isEqualTo(UPDATED_VALOR_USTIBB);
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
