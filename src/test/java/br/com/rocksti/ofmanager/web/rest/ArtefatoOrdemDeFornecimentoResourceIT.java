package br.com.rocksti.ofmanager.web.rest;

import br.com.rocksti.ofmanager.RedisTestContainerExtension;
import br.com.rocksti.ofmanager.OfmanagerApp;
import br.com.rocksti.ofmanager.domain.ArtefatoOrdemDeFornecimento;
import br.com.rocksti.ofmanager.repository.ArtefatoOrdemDeFornecimentoRepository;
import br.com.rocksti.ofmanager.service.ArtefatoOrdemDeFornecimentoService;
import br.com.rocksti.ofmanager.service.dto.ArtefatoOrdemDeFornecimentoDTO;
import br.com.rocksti.ofmanager.service.mapper.ArtefatoOrdemDeFornecimentoMapper;

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

import br.com.rocksti.ofmanager.domain.enumeration.EstadoArtefato;
/**
 * Integration tests for the {@link ArtefatoOrdemDeFornecimentoResource} REST controller.
 */
@SpringBootTest(classes = OfmanagerApp.class)
@ExtendWith({ RedisTestContainerExtension.class, MockitoExtension.class })
@AutoConfigureMockMvc
@WithMockUser
public class ArtefatoOrdemDeFornecimentoResourceIT {

    private static final EstadoArtefato DEFAULT_ESTADO = EstadoArtefato.CRIANDO;
    private static final EstadoArtefato UPDATED_ESTADO = EstadoArtefato.ALTERANDO;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private ArtefatoOrdemDeFornecimentoRepository artefatoOrdemDeFornecimentoRepository;

    @Autowired
    private ArtefatoOrdemDeFornecimentoMapper artefatoOrdemDeFornecimentoMapper;

    @Autowired
    private ArtefatoOrdemDeFornecimentoService artefatoOrdemDeFornecimentoService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArtefatoOrdemDeFornecimentoMockMvc;

    private ArtefatoOrdemDeFornecimento artefatoOrdemDeFornecimento;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArtefatoOrdemDeFornecimento createEntity(EntityManager em) {
        ArtefatoOrdemDeFornecimento artefatoOrdemDeFornecimento = new ArtefatoOrdemDeFornecimento()
            .estado(DEFAULT_ESTADO)
            .createdDate(DEFAULT_CREATED_DATE);
        return artefatoOrdemDeFornecimento;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArtefatoOrdemDeFornecimento createUpdatedEntity(EntityManager em) {
        ArtefatoOrdemDeFornecimento artefatoOrdemDeFornecimento = new ArtefatoOrdemDeFornecimento()
            .estado(UPDATED_ESTADO)
            .createdDate(UPDATED_CREATED_DATE);
        return artefatoOrdemDeFornecimento;
    }

    @BeforeEach
    public void initTest() {
        artefatoOrdemDeFornecimento = createEntity(em);
    }

    @Test
    @Transactional
    public void createArtefatoOrdemDeFornecimento() throws Exception {
        int databaseSizeBeforeCreate = artefatoOrdemDeFornecimentoRepository.findAll().size();

        // Create the ArtefatoOrdemDeFornecimento
        ArtefatoOrdemDeFornecimentoDTO artefatoOrdemDeFornecimentoDTO = artefatoOrdemDeFornecimentoMapper.toDto(artefatoOrdemDeFornecimento);
        restArtefatoOrdemDeFornecimentoMockMvc.perform(post("/api/artefato-ordem-de-fornecimentos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(artefatoOrdemDeFornecimentoDTO)))
            .andExpect(status().isCreated());

        // Validate the ArtefatoOrdemDeFornecimento in the database
        List<ArtefatoOrdemDeFornecimento> artefatoOrdemDeFornecimentoList = artefatoOrdemDeFornecimentoRepository.findAll();
        assertThat(artefatoOrdemDeFornecimentoList).hasSize(databaseSizeBeforeCreate + 1);
        ArtefatoOrdemDeFornecimento testArtefatoOrdemDeFornecimento = artefatoOrdemDeFornecimentoList.get(artefatoOrdemDeFornecimentoList.size() - 1);
        assertThat(testArtefatoOrdemDeFornecimento.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testArtefatoOrdemDeFornecimento.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    public void createArtefatoOrdemDeFornecimentoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = artefatoOrdemDeFornecimentoRepository.findAll().size();

        // Create the ArtefatoOrdemDeFornecimento with an existing ID
        artefatoOrdemDeFornecimento.setId(1L);
        ArtefatoOrdemDeFornecimentoDTO artefatoOrdemDeFornecimentoDTO = artefatoOrdemDeFornecimentoMapper.toDto(artefatoOrdemDeFornecimento);

        // An entity with an existing ID cannot be created, so this API call must fail
        restArtefatoOrdemDeFornecimentoMockMvc.perform(post("/api/artefato-ordem-de-fornecimentos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(artefatoOrdemDeFornecimentoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ArtefatoOrdemDeFornecimento in the database
        List<ArtefatoOrdemDeFornecimento> artefatoOrdemDeFornecimentoList = artefatoOrdemDeFornecimentoRepository.findAll();
        assertThat(artefatoOrdemDeFornecimentoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllArtefatoOrdemDeFornecimentos() throws Exception {
        // Initialize the database
        artefatoOrdemDeFornecimentoRepository.saveAndFlush(artefatoOrdemDeFornecimento);

        // Get all the artefatoOrdemDeFornecimentoList
        restArtefatoOrdemDeFornecimentoMockMvc.perform(get("/api/artefato-ordem-de-fornecimentos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(artefatoOrdemDeFornecimento.getId().intValue())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getArtefatoOrdemDeFornecimento() throws Exception {
        // Initialize the database
        artefatoOrdemDeFornecimentoRepository.saveAndFlush(artefatoOrdemDeFornecimento);

        // Get the artefatoOrdemDeFornecimento
        restArtefatoOrdemDeFornecimentoMockMvc.perform(get("/api/artefato-ordem-de-fornecimentos/{id}", artefatoOrdemDeFornecimento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(artefatoOrdemDeFornecimento.getId().intValue()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingArtefatoOrdemDeFornecimento() throws Exception {
        // Get the artefatoOrdemDeFornecimento
        restArtefatoOrdemDeFornecimentoMockMvc.perform(get("/api/artefato-ordem-de-fornecimentos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateArtefatoOrdemDeFornecimento() throws Exception {
        // Initialize the database
        artefatoOrdemDeFornecimentoRepository.saveAndFlush(artefatoOrdemDeFornecimento);

        int databaseSizeBeforeUpdate = artefatoOrdemDeFornecimentoRepository.findAll().size();

        // Update the artefatoOrdemDeFornecimento
        ArtefatoOrdemDeFornecimento updatedArtefatoOrdemDeFornecimento = artefatoOrdemDeFornecimentoRepository.findById(artefatoOrdemDeFornecimento.getId()).get();
        // Disconnect from session so that the updates on updatedArtefatoOrdemDeFornecimento are not directly saved in db
        em.detach(updatedArtefatoOrdemDeFornecimento);
        updatedArtefatoOrdemDeFornecimento
            .estado(UPDATED_ESTADO)
            .createdDate(UPDATED_CREATED_DATE);
        ArtefatoOrdemDeFornecimentoDTO artefatoOrdemDeFornecimentoDTO = artefatoOrdemDeFornecimentoMapper.toDto(updatedArtefatoOrdemDeFornecimento);

        restArtefatoOrdemDeFornecimentoMockMvc.perform(put("/api/artefato-ordem-de-fornecimentos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(artefatoOrdemDeFornecimentoDTO)))
            .andExpect(status().isOk());

        // Validate the ArtefatoOrdemDeFornecimento in the database
        List<ArtefatoOrdemDeFornecimento> artefatoOrdemDeFornecimentoList = artefatoOrdemDeFornecimentoRepository.findAll();
        assertThat(artefatoOrdemDeFornecimentoList).hasSize(databaseSizeBeforeUpdate);
        ArtefatoOrdemDeFornecimento testArtefatoOrdemDeFornecimento = artefatoOrdemDeFornecimentoList.get(artefatoOrdemDeFornecimentoList.size() - 1);
        assertThat(testArtefatoOrdemDeFornecimento.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testArtefatoOrdemDeFornecimento.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingArtefatoOrdemDeFornecimento() throws Exception {
        int databaseSizeBeforeUpdate = artefatoOrdemDeFornecimentoRepository.findAll().size();

        // Create the ArtefatoOrdemDeFornecimento
        ArtefatoOrdemDeFornecimentoDTO artefatoOrdemDeFornecimentoDTO = artefatoOrdemDeFornecimentoMapper.toDto(artefatoOrdemDeFornecimento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArtefatoOrdemDeFornecimentoMockMvc.perform(put("/api/artefato-ordem-de-fornecimentos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(artefatoOrdemDeFornecimentoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ArtefatoOrdemDeFornecimento in the database
        List<ArtefatoOrdemDeFornecimento> artefatoOrdemDeFornecimentoList = artefatoOrdemDeFornecimentoRepository.findAll();
        assertThat(artefatoOrdemDeFornecimentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteArtefatoOrdemDeFornecimento() throws Exception {
        // Initialize the database
        artefatoOrdemDeFornecimentoRepository.saveAndFlush(artefatoOrdemDeFornecimento);

        int databaseSizeBeforeDelete = artefatoOrdemDeFornecimentoRepository.findAll().size();

        // Delete the artefatoOrdemDeFornecimento
        restArtefatoOrdemDeFornecimentoMockMvc.perform(delete("/api/artefato-ordem-de-fornecimentos/{id}", artefatoOrdemDeFornecimento.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ArtefatoOrdemDeFornecimento> artefatoOrdemDeFornecimentoList = artefatoOrdemDeFornecimentoRepository.findAll();
        assertThat(artefatoOrdemDeFornecimentoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
