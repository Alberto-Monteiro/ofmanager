package br.com.rocksti.ofmanager.web.rest;

import br.com.rocksti.ofmanager.OfmanagerApp;
import br.com.rocksti.ofmanager.domain.Arquivo;
import br.com.rocksti.ofmanager.repository.ArquivoRepository;
import br.com.rocksti.ofmanager.service.ArquivoService;
import br.com.rocksti.ofmanager.service.dto.ArquivoDTO;
import br.com.rocksti.ofmanager.service.mapper.ArquivoMapper;
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

import br.com.rocksti.ofmanager.domain.enumeration.Complexidade;
/**
 * Integration tests for the {@link ArquivoResource} REST controller.
 */
@SpringBootTest(classes = OfmanagerApp.class)
public class ArquivoResourceIT {

    private static final String DEFAULT_CAMINHO_DO_ARQUIVO = "AAAAAAAAAA";
    private static final String UPDATED_CAMINHO_DO_ARQUIVO = "BBBBBBBBBB";

    private static final String DEFAULT_EXTENSAO = "AAAAAAAAAA";
    private static final String UPDATED_EXTENSAO = "BBBBBBBBBB";

    private static final Complexidade DEFAULT_COMPLEXIDADE = Complexidade.MUITO_BAIXA;
    private static final Complexidade UPDATED_COMPLEXIDADE = Complexidade.BAIXA;

    @Autowired
    private ArquivoRepository arquivoRepository;

    @Autowired
    private ArquivoMapper arquivoMapper;

    @Autowired
    private ArquivoService arquivoService;

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

    private MockMvc restArquivoMockMvc;

    private Arquivo arquivo;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ArquivoResource arquivoResource = new ArquivoResource(arquivoService);
        this.restArquivoMockMvc = MockMvcBuilders.standaloneSetup(arquivoResource)
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
    public static Arquivo createEntity(EntityManager em) {
        Arquivo arquivo = new Arquivo()
            .caminhoDoArquivo(DEFAULT_CAMINHO_DO_ARQUIVO)
            .extensao(DEFAULT_EXTENSAO)
            .complexidade(DEFAULT_COMPLEXIDADE);
        return arquivo;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Arquivo createUpdatedEntity(EntityManager em) {
        Arquivo arquivo = new Arquivo()
            .caminhoDoArquivo(UPDATED_CAMINHO_DO_ARQUIVO)
            .extensao(UPDATED_EXTENSAO)
            .complexidade(UPDATED_COMPLEXIDADE);
        return arquivo;
    }

    @BeforeEach
    public void initTest() {
        arquivo = createEntity(em);
    }

    @Test
    @Transactional
    public void createArquivo() throws Exception {
        int databaseSizeBeforeCreate = arquivoRepository.findAll().size();

        // Create the Arquivo
        ArquivoDTO arquivoDTO = arquivoMapper.toDto(arquivo);
        restArquivoMockMvc.perform(post("/api/arquivos")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(arquivoDTO)))
            .andExpect(status().isCreated());

        // Validate the Arquivo in the database
        List<Arquivo> arquivoList = arquivoRepository.findAll();
        assertThat(arquivoList).hasSize(databaseSizeBeforeCreate + 1);
        Arquivo testArquivo = arquivoList.get(arquivoList.size() - 1);
        assertThat(testArquivo.getCaminhoDoArquivo()).isEqualTo(DEFAULT_CAMINHO_DO_ARQUIVO);
        assertThat(testArquivo.getExtensao()).isEqualTo(DEFAULT_EXTENSAO);
        assertThat(testArquivo.getComplexidade()).isEqualTo(DEFAULT_COMPLEXIDADE);
    }

    @Test
    @Transactional
    public void createArquivoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = arquivoRepository.findAll().size();

        // Create the Arquivo with an existing ID
        arquivo.setId(1L);
        ArquivoDTO arquivoDTO = arquivoMapper.toDto(arquivo);

        // An entity with an existing ID cannot be created, so this API call must fail
        restArquivoMockMvc.perform(post("/api/arquivos")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(arquivoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Arquivo in the database
        List<Arquivo> arquivoList = arquivoRepository.findAll();
        assertThat(arquivoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCaminhoDoArquivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = arquivoRepository.findAll().size();
        // set the field null
        arquivo.setCaminhoDoArquivo(null);

        // Create the Arquivo, which fails.
        ArquivoDTO arquivoDTO = arquivoMapper.toDto(arquivo);

        restArquivoMockMvc.perform(post("/api/arquivos")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(arquivoDTO)))
            .andExpect(status().isBadRequest());

        List<Arquivo> arquivoList = arquivoRepository.findAll();
        assertThat(arquivoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkExtensaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = arquivoRepository.findAll().size();
        // set the field null
        arquivo.setExtensao(null);

        // Create the Arquivo, which fails.
        ArquivoDTO arquivoDTO = arquivoMapper.toDto(arquivo);

        restArquivoMockMvc.perform(post("/api/arquivos")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(arquivoDTO)))
            .andExpect(status().isBadRequest());

        List<Arquivo> arquivoList = arquivoRepository.findAll();
        assertThat(arquivoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllArquivos() throws Exception {
        // Initialize the database
        arquivoRepository.saveAndFlush(arquivo);

        // Get all the arquivoList
        restArquivoMockMvc.perform(get("/api/arquivos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(arquivo.getId().intValue())))
            .andExpect(jsonPath("$.[*].caminhoDoArquivo").value(hasItem(DEFAULT_CAMINHO_DO_ARQUIVO)))
            .andExpect(jsonPath("$.[*].extensao").value(hasItem(DEFAULT_EXTENSAO)))
            .andExpect(jsonPath("$.[*].complexidade").value(hasItem(DEFAULT_COMPLEXIDADE.toString())));
    }
    
    @Test
    @Transactional
    public void getArquivo() throws Exception {
        // Initialize the database
        arquivoRepository.saveAndFlush(arquivo);

        // Get the arquivo
        restArquivoMockMvc.perform(get("/api/arquivos/{id}", arquivo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(arquivo.getId().intValue()))
            .andExpect(jsonPath("$.caminhoDoArquivo").value(DEFAULT_CAMINHO_DO_ARQUIVO))
            .andExpect(jsonPath("$.extensao").value(DEFAULT_EXTENSAO))
            .andExpect(jsonPath("$.complexidade").value(DEFAULT_COMPLEXIDADE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingArquivo() throws Exception {
        // Get the arquivo
        restArquivoMockMvc.perform(get("/api/arquivos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateArquivo() throws Exception {
        // Initialize the database
        arquivoRepository.saveAndFlush(arquivo);

        int databaseSizeBeforeUpdate = arquivoRepository.findAll().size();

        // Update the arquivo
        Arquivo updatedArquivo = arquivoRepository.findById(arquivo.getId()).get();
        // Disconnect from session so that the updates on updatedArquivo are not directly saved in db
        em.detach(updatedArquivo);
        updatedArquivo
            .caminhoDoArquivo(UPDATED_CAMINHO_DO_ARQUIVO)
            .extensao(UPDATED_EXTENSAO)
            .complexidade(UPDATED_COMPLEXIDADE);
        ArquivoDTO arquivoDTO = arquivoMapper.toDto(updatedArquivo);

        restArquivoMockMvc.perform(put("/api/arquivos")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(arquivoDTO)))
            .andExpect(status().isOk());

        // Validate the Arquivo in the database
        List<Arquivo> arquivoList = arquivoRepository.findAll();
        assertThat(arquivoList).hasSize(databaseSizeBeforeUpdate);
        Arquivo testArquivo = arquivoList.get(arquivoList.size() - 1);
        assertThat(testArquivo.getCaminhoDoArquivo()).isEqualTo(UPDATED_CAMINHO_DO_ARQUIVO);
        assertThat(testArquivo.getExtensao()).isEqualTo(UPDATED_EXTENSAO);
        assertThat(testArquivo.getComplexidade()).isEqualTo(UPDATED_COMPLEXIDADE);
    }

    @Test
    @Transactional
    public void updateNonExistingArquivo() throws Exception {
        int databaseSizeBeforeUpdate = arquivoRepository.findAll().size();

        // Create the Arquivo
        ArquivoDTO arquivoDTO = arquivoMapper.toDto(arquivo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArquivoMockMvc.perform(put("/api/arquivos")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(arquivoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Arquivo in the database
        List<Arquivo> arquivoList = arquivoRepository.findAll();
        assertThat(arquivoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteArquivo() throws Exception {
        // Initialize the database
        arquivoRepository.saveAndFlush(arquivo);

        int databaseSizeBeforeDelete = arquivoRepository.findAll().size();

        // Delete the arquivo
        restArquivoMockMvc.perform(delete("/api/arquivos/{id}", arquivo.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Arquivo> arquivoList = arquivoRepository.findAll();
        assertThat(arquivoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
