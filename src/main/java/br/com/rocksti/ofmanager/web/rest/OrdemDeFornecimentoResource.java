package br.com.rocksti.ofmanager.web.rest;

import br.com.rocksti.ofmanager.service.OrdemDeFornecimentoService;
import br.com.rocksti.ofmanager.web.rest.errors.BadRequestAlertException;
import br.com.rocksti.ofmanager.service.dto.OrdemDeFornecimentoDTO;

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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link br.com.rocksti.ofmanager.domain.OrdemDeFornecimento}.
 */
@RestController
@RequestMapping("/api")
public class OrdemDeFornecimentoResource {

    private final Logger log = LoggerFactory.getLogger(OrdemDeFornecimentoResource.class);

    private static final String ENTITY_NAME = "ordemDeFornecimento";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrdemDeFornecimentoService ordemDeFornecimentoService;

    public OrdemDeFornecimentoResource(OrdemDeFornecimentoService ordemDeFornecimentoService) {
        this.ordemDeFornecimentoService = ordemDeFornecimentoService;
    }

    /**
     * {@code POST  /ordem-de-fornecimentos} : Create a new ordemDeFornecimento.
     *
     * @param ordemDeFornecimentoDTO the ordemDeFornecimentoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ordemDeFornecimentoDTO, or with status {@code 400 (Bad Request)} if the ordemDeFornecimento has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ordem-de-fornecimentos")
    public ResponseEntity<OrdemDeFornecimentoDTO> createOrdemDeFornecimento(@RequestBody OrdemDeFornecimentoDTO ordemDeFornecimentoDTO) throws URISyntaxException {
        log.debug("REST request to save OrdemDeFornecimento : {}", ordemDeFornecimentoDTO);
        if (ordemDeFornecimentoDTO.getId() != null) {
            throw new BadRequestAlertException("A new ordemDeFornecimento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrdemDeFornecimentoDTO result = ordemDeFornecimentoService.save(ordemDeFornecimentoDTO);
        return ResponseEntity.created(new URI("/api/ordem-de-fornecimentos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ordem-de-fornecimentos} : Updates an existing ordemDeFornecimento.
     *
     * @param ordemDeFornecimentoDTO the ordemDeFornecimentoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordemDeFornecimentoDTO,
     * or with status {@code 400 (Bad Request)} if the ordemDeFornecimentoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ordemDeFornecimentoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ordem-de-fornecimentos")
    public ResponseEntity<OrdemDeFornecimentoDTO> updateOrdemDeFornecimento(@RequestBody OrdemDeFornecimentoDTO ordemDeFornecimentoDTO) throws URISyntaxException {
        log.debug("REST request to update OrdemDeFornecimento : {}", ordemDeFornecimentoDTO);
        if (ordemDeFornecimentoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OrdemDeFornecimentoDTO result = ordemDeFornecimentoService.save(ordemDeFornecimentoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordemDeFornecimentoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /ordem-de-fornecimentos} : get all the ordemDeFornecimentos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ordemDeFornecimentos in body.
     */
    @GetMapping("/ordem-de-fornecimentos")
    public ResponseEntity<List<OrdemDeFornecimentoDTO>> getAllOrdemDeFornecimentos(Pageable pageable) {
        log.debug("REST request to get a page of OrdemDeFornecimentos");
        Page<OrdemDeFornecimentoDTO> page = ordemDeFornecimentoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ordem-de-fornecimentos/:id} : get the "id" ordemDeFornecimento.
     *
     * @param id the id of the ordemDeFornecimentoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ordemDeFornecimentoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ordem-de-fornecimentos/{id}")
    public ResponseEntity<OrdemDeFornecimentoDTO> getOrdemDeFornecimento(@PathVariable Long id) {
        log.debug("REST request to get OrdemDeFornecimento : {}", id);
        Optional<OrdemDeFornecimentoDTO> ordemDeFornecimentoDTO = ordemDeFornecimentoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ordemDeFornecimentoDTO);
    }

    /**
     * {@code DELETE  /ordem-de-fornecimentos/:id} : delete the "id" ordemDeFornecimento.
     *
     * @param id the id of the ordemDeFornecimentoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ordem-de-fornecimentos/{id}")
    public ResponseEntity<Void> deleteOrdemDeFornecimento(@PathVariable Long id) {
        log.debug("REST request to delete OrdemDeFornecimento : {}", id);
        ordemDeFornecimentoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
