package br.com.rocksti.ofmanager.web.rest;

import br.com.rocksti.ofmanager.RedisTestContainerExtension;
import br.com.rocksti.ofmanager.OfmanagerApp;
import br.com.rocksti.ofmanager.domain.Artefato;
import br.com.rocksti.ofmanager.repository.ArtefatoRepository;
import br.com.rocksti.ofmanager.service.ArtefatoService;
import br.com.rocksti.ofmanager.service.dto.ArtefatoDTO;
import br.com.rocksti.ofmanager.service.mapper.ArtefatoMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.rocksti.ofmanager.domain.enumeration.ComplexidadeArtefato;
/**
 * Integration tests for the {@link ArtefatoResource} REST controller.
 */
@SpringBootTest(classes = OfmanagerApp.class)
@ExtendWith({ RedisTestContainerExtension.class, MockitoExtension.class })
@AutoConfigureMockMvc
@WithMockUser
public class ArtefatoResourceIT {

    private static final String DEFAULT_LOCAL_DO_ARTEFATO = "AAAAAAAAAA";
    private static final String UPDATED_LOCAL_DO_ARTEFATO = "BBBBBBBBBB";

    private static final String DEFAULT_EXTENSAO = "AAAAAAAAAA";
    private static final String UPDATED_EXTENSAO = "BBBBBBBBBB";

    private static final ComplexidadeArtefato DEFAULT_COMPLEXIDADE = ComplexidadeArtefato.MUITO_BAIXA;
    private static final ComplexidadeArtefato UPDATED_COMPLEXIDADE = ComplexidadeArtefato.BAIXA;

    private static final Boolean DEFAULT_ARTEFATO_DE_TEST = false;
    private static final Boolean UPDATED_ARTEFATO_DE_TEST = true;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private ArtefatoRepository artefatoRepository;

    @Autowired
    private ArtefatoMapper artefatoMapper;

    @Autowired
    private ArtefatoService artefatoService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArtefatoMockMvc;

    private Artefato artefato;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Artefato createEntity(EntityManager em) {
        Artefato artefato = new Artefato()
            .localDoArtefato(DEFAULT_LOCAL_DO_ARTEFATO)
            .extensao(DEFAULT_EXTENSAO)
            .complexidade(DEFAULT_COMPLEXIDADE)
            .artefatoDeTest(DEFAULT_ARTEFATO_DE_TEST)
            .createdDate(DEFAULT_CREATED_DATE);
        return artefato;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Artefato createUpdatedEntity(EntityManager em) {
        Artefato artefato = new Artefato()
            .localDoArtefato(UPDATED_LOCAL_DO_ARTEFATO)
            .extensao(UPDATED_EXTENSAO)
            .complexidade(UPDATED_COMPLEXIDADE)
            .artefatoDeTest(UPDATED_ARTEFATO_DE_TEST)
            .createdDate(UPDATED_CREATED_DATE);
        return artefato;
    }

    @BeforeEach
    public void initTest() {
        artefato = createEntity(em);
    }

    @Test
    @Transactional
    public void createArtefato() throws Exception {
        int databaseSizeBeforeCreate = artefatoRepository.findAll().size();

        // Create the Artefato
        ArtefatoDTO artefatoDTO = artefatoMapper.toDto(artefato);
        restArtefatoMockMvc.perform(post("/api/artefatoes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(artefatoDTO)))
            .andExpect(status().isCreated());

        // Validate the Artefato in the database
        List<Artefato> artefatoList = artefatoRepository.findAll();
        assertThat(artefatoList).hasSize(databaseSizeBeforeCreate + 1);
        Artefato testArtefato = artefatoList.get(artefatoList.size() - 1);
        assertThat(testArtefato.getLocalDoArtefato()).isEqualTo(DEFAULT_LOCAL_DO_ARTEFATO);
        assertThat(testArtefato.getExtensao()).isEqualTo(DEFAULT_EXTENSAO);
        assertThat(testArtefato.getComplexidade()).isEqualTo(DEFAULT_COMPLEXIDADE);
        assertThat(testArtefato.isArtefatoDeTest()).isEqualTo(DEFAULT_ARTEFATO_DE_TEST);
        assertThat(testArtefato.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    public void createArtefatoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = artefatoRepository.findAll().size();

        // Create the Artefato with an existing ID
        artefato.setId(1L);
        ArtefatoDTO artefatoDTO = artefatoMapper.toDto(artefato);

        // An entity with an existing ID cannot be created, so this API call must fail
        restArtefatoMockMvc.perform(post("/api/artefatoes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(artefatoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Artefato in the database
        List<Artefato> artefatoList = artefatoRepository.findAll();
        assertThat(artefatoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkLocalDoArtefatoIsRequired() throws Exception {
        int databaseSizeBeforeTest = artefatoRepository.findAll().size();
        // set the field null
        artefato.setLocalDoArtefato(null);

        // Create the Artefato, which fails.
        ArtefatoDTO artefatoDTO = artefatoMapper.toDto(artefato);

        restArtefatoMockMvc.perform(post("/api/artefatoes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(artefatoDTO)))
            .andExpect(status().isBadRequest());

        List<Artefato> artefatoList = artefatoRepository.findAll();
        assertThat(artefatoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkExtensaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = artefatoRepository.findAll().size();
        // set the field null
        artefato.setExtensao(null);

        // Create the Artefato, which fails.
        ArtefatoDTO artefatoDTO = artefatoMapper.toDto(artefato);

        restArtefatoMockMvc.perform(post("/api/artefatoes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(artefatoDTO)))
            .andExpect(status().isBadRequest());

        List<Artefato> artefatoList = artefatoRepository.findAll();
        assertThat(artefatoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllArtefatoes() throws Exception {
        // Initialize the database
        artefatoRepository.saveAndFlush(artefato);

        // Get all the artefatoList
        restArtefatoMockMvc.perform(get("/api/artefatoes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(artefato.getId().intValue())))
            .andExpect(jsonPath("$.[*].localDoArtefato").value(hasItem(DEFAULT_LOCAL_DO_ARTEFATO)))
            .andExpect(jsonPath("$.[*].extensao").value(hasItem(DEFAULT_EXTENSAO)))
            .andExpect(jsonPath("$.[*].complexidade").value(hasItem(DEFAULT_COMPLEXIDADE.toString())))
            .andExpect(jsonPath("$.[*].artefatoDeTest").value(hasItem(DEFAULT_ARTEFATO_DE_TEST.booleanValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getArtefato() throws Exception {
        // Initialize the database
        artefatoRepository.saveAndFlush(artefato);

        // Get the artefato
        restArtefatoMockMvc.perform(get("/api/artefatoes/{id}", artefato.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(artefato.getId().intValue()))
            .andExpect(jsonPath("$.localDoArtefato").value(DEFAULT_LOCAL_DO_ARTEFATO))
            .andExpect(jsonPath("$.extensao").value(DEFAULT_EXTENSAO))
            .andExpect(jsonPath("$.complexidade").value(DEFAULT_COMPLEXIDADE.toString()))
            .andExpect(jsonPath("$.artefatoDeTest").value(DEFAULT_ARTEFATO_DE_TEST.booleanValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingArtefato() throws Exception {
        // Get the artefato
        restArtefatoMockMvc.perform(get("/api/artefatoes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateArtefato() throws Exception {
        // Initialize the database
        artefatoRepository.saveAndFlush(artefato);

        int databaseSizeBeforeUpdate = artefatoRepository.findAll().size();

        // Update the artefato
        Artefato updatedArtefato = artefatoRepository.findById(artefato.getId()).get();
        // Disconnect from session so that the updates on updatedArtefato are not directly saved in db
        em.detach(updatedArtefato);
        updatedArtefato
            .localDoArtefato(UPDATED_LOCAL_DO_ARTEFATO)
            .extensao(UPDATED_EXTENSAO)
            .complexidade(UPDATED_COMPLEXIDADE)
            .artefatoDeTest(UPDATED_ARTEFATO_DE_TEST)
            .createdDate(UPDATED_CREATED_DATE);
        ArtefatoDTO artefatoDTO = artefatoMapper.toDto(updatedArtefato);

        restArtefatoMockMvc.perform(put("/api/artefatoes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(artefatoDTO)))
            .andExpect(status().isOk());

        // Validate the Artefato in the database
        List<Artefato> artefatoList = artefatoRepository.findAll();
        assertThat(artefatoList).hasSize(databaseSizeBeforeUpdate);
        Artefato testArtefato = artefatoList.get(artefatoList.size() - 1);
        assertThat(testArtefato.getLocalDoArtefato()).isEqualTo(UPDATED_LOCAL_DO_ARTEFATO);
        assertThat(testArtefato.getExtensao()).isEqualTo(UPDATED_EXTENSAO);
        assertThat(testArtefato.getComplexidade()).isEqualTo(UPDATED_COMPLEXIDADE);
        assertThat(testArtefato.isArtefatoDeTest()).isEqualTo(UPDATED_ARTEFATO_DE_TEST);
        assertThat(testArtefato.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingArtefato() throws Exception {
        int databaseSizeBeforeUpdate = artefatoRepository.findAll().size();

        // Create the Artefato
        ArtefatoDTO artefatoDTO = artefatoMapper.toDto(artefato);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArtefatoMockMvc.perform(put("/api/artefatoes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(artefatoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Artefato in the database
        List<Artefato> artefatoList = artefatoRepository.findAll();
        assertThat(artefatoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteArtefato() throws Exception {
        // Initialize the database
        artefatoRepository.saveAndFlush(artefato);

        int databaseSizeBeforeDelete = artefatoRepository.findAll().size();

        // Delete the artefato
        restArtefatoMockMvc.perform(delete("/api/artefatoes/{id}", artefato.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Artefato> artefatoList = artefatoRepository.findAll();
        assertThat(artefatoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
