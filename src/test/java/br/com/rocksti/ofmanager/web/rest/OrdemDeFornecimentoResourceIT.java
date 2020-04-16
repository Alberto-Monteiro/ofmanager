package br.com.rocksti.ofmanager.web.rest;

import br.com.rocksti.ofmanager.RedisTestContainerExtension;
import br.com.rocksti.ofmanager.OfmanagerApp;
import br.com.rocksti.ofmanager.domain.OrdemDeFornecimento;
import br.com.rocksti.ofmanager.repository.OrdemDeFornecimentoRepository;
import br.com.rocksti.ofmanager.service.OrdemDeFornecimentoService;
import br.com.rocksti.ofmanager.service.dto.OrdemDeFornecimentoDTO;
import br.com.rocksti.ofmanager.service.mapper.OrdemDeFornecimentoMapper;

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
import org.springframework.util.Base64Utils;
import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.rocksti.ofmanager.domain.enumeration.EstadoOrdemDeFornecimento;
/**
 * Integration tests for the {@link OrdemDeFornecimentoResource} REST controller.
 */
@SpringBootTest(classes = OfmanagerApp.class)
@ExtendWith({ RedisTestContainerExtension.class, MockitoExtension.class })
@AutoConfigureMockMvc
@WithMockUser
public class OrdemDeFornecimentoResourceIT {

    private static final Integer DEFAULT_NUMERO = 1;
    private static final Integer UPDATED_NUMERO = 2;

    private static final EstadoOrdemDeFornecimento DEFAULT_ESTADO = EstadoOrdemDeFornecimento.NOVA;
    private static final EstadoOrdemDeFornecimento UPDATED_ESTADO = EstadoOrdemDeFornecimento.ANALISE;

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
    private OrdemDeFornecimentoRepository ordemDeFornecimentoRepository;

    @Autowired
    private OrdemDeFornecimentoMapper ordemDeFornecimentoMapper;

    @Autowired
    private OrdemDeFornecimentoService ordemDeFornecimentoService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrdemDeFornecimentoMockMvc;

    private OrdemDeFornecimento ordemDeFornecimento;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdemDeFornecimento createEntity(EntityManager em) {
        OrdemDeFornecimento ordemDeFornecimento = new OrdemDeFornecimento()
            .numero(DEFAULT_NUMERO)
            .estado(DEFAULT_ESTADO)
            .observacaoDoGestor(DEFAULT_OBSERVACAO_DO_GESTOR)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .valorUstibb(DEFAULT_VALOR_USTIBB);
        return ordemDeFornecimento;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdemDeFornecimento createUpdatedEntity(EntityManager em) {
        OrdemDeFornecimento ordemDeFornecimento = new OrdemDeFornecimento()
            .numero(UPDATED_NUMERO)
            .estado(UPDATED_ESTADO)
            .observacaoDoGestor(UPDATED_OBSERVACAO_DO_GESTOR)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .valorUstibb(UPDATED_VALOR_USTIBB);
        return ordemDeFornecimento;
    }

    @BeforeEach
    public void initTest() {
        ordemDeFornecimento = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrdemDeFornecimento() throws Exception {
        int databaseSizeBeforeCreate = ordemDeFornecimentoRepository.findAll().size();

        // Create the OrdemDeFornecimento
        OrdemDeFornecimentoDTO ordemDeFornecimentoDTO = ordemDeFornecimentoMapper.toDto(ordemDeFornecimento);
        restOrdemDeFornecimentoMockMvc.perform(post("/api/ordem-de-fornecimentos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ordemDeFornecimentoDTO)))
            .andExpect(status().isCreated());

        // Validate the OrdemDeFornecimento in the database
        List<OrdemDeFornecimento> ordemDeFornecimentoList = ordemDeFornecimentoRepository.findAll();
        assertThat(ordemDeFornecimentoList).hasSize(databaseSizeBeforeCreate + 1);
        OrdemDeFornecimento testOrdemDeFornecimento = ordemDeFornecimentoList.get(ordemDeFornecimentoList.size() - 1);
        assertThat(testOrdemDeFornecimento.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testOrdemDeFornecimento.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testOrdemDeFornecimento.getObservacaoDoGestor()).isEqualTo(DEFAULT_OBSERVACAO_DO_GESTOR);
        assertThat(testOrdemDeFornecimento.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testOrdemDeFornecimento.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testOrdemDeFornecimento.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testOrdemDeFornecimento.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testOrdemDeFornecimento.getValorUstibb()).isEqualTo(DEFAULT_VALOR_USTIBB);
    }

    @Test
    @Transactional
    public void createOrdemDeFornecimentoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ordemDeFornecimentoRepository.findAll().size();

        // Create the OrdemDeFornecimento with an existing ID
        ordemDeFornecimento.setId(1L);
        OrdemDeFornecimentoDTO ordemDeFornecimentoDTO = ordemDeFornecimentoMapper.toDto(ordemDeFornecimento);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdemDeFornecimentoMockMvc.perform(post("/api/ordem-de-fornecimentos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ordemDeFornecimentoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrdemDeFornecimento in the database
        List<OrdemDeFornecimento> ordemDeFornecimentoList = ordemDeFornecimentoRepository.findAll();
        assertThat(ordemDeFornecimentoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNumeroIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordemDeFornecimentoRepository.findAll().size();
        // set the field null
        ordemDeFornecimento.setNumero(null);

        // Create the OrdemDeFornecimento, which fails.
        OrdemDeFornecimentoDTO ordemDeFornecimentoDTO = ordemDeFornecimentoMapper.toDto(ordemDeFornecimento);

        restOrdemDeFornecimentoMockMvc.perform(post("/api/ordem-de-fornecimentos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ordemDeFornecimentoDTO)))
            .andExpect(status().isBadRequest());

        List<OrdemDeFornecimento> ordemDeFornecimentoList = ordemDeFornecimentoRepository.findAll();
        assertThat(ordemDeFornecimentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrdemDeFornecimentos() throws Exception {
        // Initialize the database
        ordemDeFornecimentoRepository.saveAndFlush(ordemDeFornecimento);

        // Get all the ordemDeFornecimentoList
        restOrdemDeFornecimentoMockMvc.perform(get("/api/ordem-de-fornecimentos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordemDeFornecimento.getId().intValue())))
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
    public void getOrdemDeFornecimento() throws Exception {
        // Initialize the database
        ordemDeFornecimentoRepository.saveAndFlush(ordemDeFornecimento);

        // Get the ordemDeFornecimento
        restOrdemDeFornecimentoMockMvc.perform(get("/api/ordem-de-fornecimentos/{id}", ordemDeFornecimento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ordemDeFornecimento.getId().intValue()))
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
    public void getNonExistingOrdemDeFornecimento() throws Exception {
        // Get the ordemDeFornecimento
        restOrdemDeFornecimentoMockMvc.perform(get("/api/ordem-de-fornecimentos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrdemDeFornecimento() throws Exception {
        // Initialize the database
        ordemDeFornecimentoRepository.saveAndFlush(ordemDeFornecimento);

        int databaseSizeBeforeUpdate = ordemDeFornecimentoRepository.findAll().size();

        // Update the ordemDeFornecimento
        OrdemDeFornecimento updatedOrdemDeFornecimento = ordemDeFornecimentoRepository.findById(ordemDeFornecimento.getId()).get();
        // Disconnect from session so that the updates on updatedOrdemDeFornecimento are not directly saved in db
        em.detach(updatedOrdemDeFornecimento);
        updatedOrdemDeFornecimento
            .numero(UPDATED_NUMERO)
            .estado(UPDATED_ESTADO)
            .observacaoDoGestor(UPDATED_OBSERVACAO_DO_GESTOR)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .valorUstibb(UPDATED_VALOR_USTIBB);
        OrdemDeFornecimentoDTO ordemDeFornecimentoDTO = ordemDeFornecimentoMapper.toDto(updatedOrdemDeFornecimento);

        restOrdemDeFornecimentoMockMvc.perform(put("/api/ordem-de-fornecimentos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ordemDeFornecimentoDTO)))
            .andExpect(status().isOk());

        // Validate the OrdemDeFornecimento in the database
        List<OrdemDeFornecimento> ordemDeFornecimentoList = ordemDeFornecimentoRepository.findAll();
        assertThat(ordemDeFornecimentoList).hasSize(databaseSizeBeforeUpdate);
        OrdemDeFornecimento testOrdemDeFornecimento = ordemDeFornecimentoList.get(ordemDeFornecimentoList.size() - 1);
        assertThat(testOrdemDeFornecimento.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testOrdemDeFornecimento.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testOrdemDeFornecimento.getObservacaoDoGestor()).isEqualTo(UPDATED_OBSERVACAO_DO_GESTOR);
        assertThat(testOrdemDeFornecimento.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testOrdemDeFornecimento.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testOrdemDeFornecimento.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testOrdemDeFornecimento.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testOrdemDeFornecimento.getValorUstibb()).isEqualTo(UPDATED_VALOR_USTIBB);
    }

    @Test
    @Transactional
    public void updateNonExistingOrdemDeFornecimento() throws Exception {
        int databaseSizeBeforeUpdate = ordemDeFornecimentoRepository.findAll().size();

        // Create the OrdemDeFornecimento
        OrdemDeFornecimentoDTO ordemDeFornecimentoDTO = ordemDeFornecimentoMapper.toDto(ordemDeFornecimento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdemDeFornecimentoMockMvc.perform(put("/api/ordem-de-fornecimentos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ordemDeFornecimentoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrdemDeFornecimento in the database
        List<OrdemDeFornecimento> ordemDeFornecimentoList = ordemDeFornecimentoRepository.findAll();
        assertThat(ordemDeFornecimentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOrdemDeFornecimento() throws Exception {
        // Initialize the database
        ordemDeFornecimentoRepository.saveAndFlush(ordemDeFornecimento);

        int databaseSizeBeforeDelete = ordemDeFornecimentoRepository.findAll().size();

        // Delete the ordemDeFornecimento
        restOrdemDeFornecimentoMockMvc.perform(delete("/api/ordem-de-fornecimentos/{id}", ordemDeFornecimento.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrdemDeFornecimento> ordemDeFornecimentoList = ordemDeFornecimentoRepository.findAll();
        assertThat(ordemDeFornecimentoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
