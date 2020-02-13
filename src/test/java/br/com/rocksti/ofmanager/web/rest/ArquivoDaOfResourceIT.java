package br.com.rocksti.ofmanager.web.rest;

import br.com.rocksti.ofmanager.OfmanagerApp;
import br.com.rocksti.ofmanager.domain.ArquivoDaOf;
import br.com.rocksti.ofmanager.repository.ArquivoDaOfRepository;
import br.com.rocksti.ofmanager.service.ArquivoDaOfService;
import br.com.rocksti.ofmanager.service.dto.ArquivoDaOfDTO;
import br.com.rocksti.ofmanager.service.mapper.ArquivoDaOfMapper;
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

import br.com.rocksti.ofmanager.domain.enumeration.EstadoArquivo;
/**
 * Integration tests for the {@link ArquivoDaOfResource} REST controller.
 */
@SpringBootTest(classes = OfmanagerApp.class)
public class ArquivoDaOfResourceIT {

    private static final EstadoArquivo DEFAULT_ESTADO_ARQUIVO = EstadoArquivo.CRIANDO;
    private static final EstadoArquivo UPDATED_ESTADO_ARQUIVO = EstadoArquivo.ALTERANDO;

    @Autowired
    private ArquivoDaOfRepository arquivoDaOfRepository;

    @Autowired
    private ArquivoDaOfMapper arquivoDaOfMapper;

    @Autowired
    private ArquivoDaOfService arquivoDaOfService;

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

    private MockMvc restArquivoDaOfMockMvc;

    private ArquivoDaOf arquivoDaOf;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ArquivoDaOfResource arquivoDaOfResource = new ArquivoDaOfResource(arquivoDaOfService);
        this.restArquivoDaOfMockMvc = MockMvcBuilders.standaloneSetup(arquivoDaOfResource)
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
    public static ArquivoDaOf createEntity(EntityManager em) {
        ArquivoDaOf arquivoDaOf = new ArquivoDaOf()
            .estadoArquivo(DEFAULT_ESTADO_ARQUIVO);
        return arquivoDaOf;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArquivoDaOf createUpdatedEntity(EntityManager em) {
        ArquivoDaOf arquivoDaOf = new ArquivoDaOf()
            .estadoArquivo(UPDATED_ESTADO_ARQUIVO);
        return arquivoDaOf;
    }

    @BeforeEach
    public void initTest() {
        arquivoDaOf = createEntity(em);
    }

    @Test
    @Transactional
    public void createArquivoDaOf() throws Exception {
        int databaseSizeBeforeCreate = arquivoDaOfRepository.findAll().size();

        // Create the ArquivoDaOf
        ArquivoDaOfDTO arquivoDaOfDTO = arquivoDaOfMapper.toDto(arquivoDaOf);
        restArquivoDaOfMockMvc.perform(post("/api/arquivo-da-ofs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(arquivoDaOfDTO)))
            .andExpect(status().isCreated());

        // Validate the ArquivoDaOf in the database
        List<ArquivoDaOf> arquivoDaOfList = arquivoDaOfRepository.findAll();
        assertThat(arquivoDaOfList).hasSize(databaseSizeBeforeCreate + 1);
        ArquivoDaOf testArquivoDaOf = arquivoDaOfList.get(arquivoDaOfList.size() - 1);
        assertThat(testArquivoDaOf.getEstadoArquivo()).isEqualTo(DEFAULT_ESTADO_ARQUIVO);
    }

    @Test
    @Transactional
    public void createArquivoDaOfWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = arquivoDaOfRepository.findAll().size();

        // Create the ArquivoDaOf with an existing ID
        arquivoDaOf.setId(1L);
        ArquivoDaOfDTO arquivoDaOfDTO = arquivoDaOfMapper.toDto(arquivoDaOf);

        // An entity with an existing ID cannot be created, so this API call must fail
        restArquivoDaOfMockMvc.perform(post("/api/arquivo-da-ofs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(arquivoDaOfDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ArquivoDaOf in the database
        List<ArquivoDaOf> arquivoDaOfList = arquivoDaOfRepository.findAll();
        assertThat(arquivoDaOfList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllArquivoDaOfs() throws Exception {
        // Initialize the database
        arquivoDaOfRepository.saveAndFlush(arquivoDaOf);

        // Get all the arquivoDaOfList
        restArquivoDaOfMockMvc.perform(get("/api/arquivo-da-ofs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(arquivoDaOf.getId().intValue())))
            .andExpect(jsonPath("$.[*].estadoArquivo").value(hasItem(DEFAULT_ESTADO_ARQUIVO.toString())));
    }
    
    @Test
    @Transactional
    public void getArquivoDaOf() throws Exception {
        // Initialize the database
        arquivoDaOfRepository.saveAndFlush(arquivoDaOf);

        // Get the arquivoDaOf
        restArquivoDaOfMockMvc.perform(get("/api/arquivo-da-ofs/{id}", arquivoDaOf.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(arquivoDaOf.getId().intValue()))
            .andExpect(jsonPath("$.estadoArquivo").value(DEFAULT_ESTADO_ARQUIVO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingArquivoDaOf() throws Exception {
        // Get the arquivoDaOf
        restArquivoDaOfMockMvc.perform(get("/api/arquivo-da-ofs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateArquivoDaOf() throws Exception {
        // Initialize the database
        arquivoDaOfRepository.saveAndFlush(arquivoDaOf);

        int databaseSizeBeforeUpdate = arquivoDaOfRepository.findAll().size();

        // Update the arquivoDaOf
        ArquivoDaOf updatedArquivoDaOf = arquivoDaOfRepository.findById(arquivoDaOf.getId()).get();
        // Disconnect from session so that the updates on updatedArquivoDaOf are not directly saved in db
        em.detach(updatedArquivoDaOf);
        updatedArquivoDaOf
            .estadoArquivo(UPDATED_ESTADO_ARQUIVO);
        ArquivoDaOfDTO arquivoDaOfDTO = arquivoDaOfMapper.toDto(updatedArquivoDaOf);

        restArquivoDaOfMockMvc.perform(put("/api/arquivo-da-ofs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(arquivoDaOfDTO)))
            .andExpect(status().isOk());

        // Validate the ArquivoDaOf in the database
        List<ArquivoDaOf> arquivoDaOfList = arquivoDaOfRepository.findAll();
        assertThat(arquivoDaOfList).hasSize(databaseSizeBeforeUpdate);
        ArquivoDaOf testArquivoDaOf = arquivoDaOfList.get(arquivoDaOfList.size() - 1);
        assertThat(testArquivoDaOf.getEstadoArquivo()).isEqualTo(UPDATED_ESTADO_ARQUIVO);
    }

    @Test
    @Transactional
    public void updateNonExistingArquivoDaOf() throws Exception {
        int databaseSizeBeforeUpdate = arquivoDaOfRepository.findAll().size();

        // Create the ArquivoDaOf
        ArquivoDaOfDTO arquivoDaOfDTO = arquivoDaOfMapper.toDto(arquivoDaOf);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArquivoDaOfMockMvc.perform(put("/api/arquivo-da-ofs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(arquivoDaOfDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ArquivoDaOf in the database
        List<ArquivoDaOf> arquivoDaOfList = arquivoDaOfRepository.findAll();
        assertThat(arquivoDaOfList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteArquivoDaOf() throws Exception {
        // Initialize the database
        arquivoDaOfRepository.saveAndFlush(arquivoDaOf);

        int databaseSizeBeforeDelete = arquivoDaOfRepository.findAll().size();

        // Delete the arquivoDaOf
        restArquivoDaOfMockMvc.perform(delete("/api/arquivo-da-ofs/{id}", arquivoDaOf.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ArquivoDaOf> arquivoDaOfList = arquivoDaOfRepository.findAll();
        assertThat(arquivoDaOfList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
