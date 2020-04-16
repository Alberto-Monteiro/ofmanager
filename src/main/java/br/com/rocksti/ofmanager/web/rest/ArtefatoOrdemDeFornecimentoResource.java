package br.com.rocksti.ofmanager.web.rest;

import br.com.rocksti.ofmanager.service.ArtefatoOrdemDeFornecimentoService;
import br.com.rocksti.ofmanager.web.rest.errors.BadRequestAlertException;
import br.com.rocksti.ofmanager.service.dto.ArtefatoOrdemDeFornecimentoDTO;

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
 * REST controller for managing {@link br.com.rocksti.ofmanager.domain.ArtefatoOrdemDeFornecimento}.
 */
@RestController
@RequestMapping("/api")
public class ArtefatoOrdemDeFornecimentoResource {

    private final Logger log = LoggerFactory.getLogger(ArtefatoOrdemDeFornecimentoResource.class);

    private static final String ENTITY_NAME = "artefatoOrdemDeFornecimento";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArtefatoOrdemDeFornecimentoService artefatoOrdemDeFornecimentoService;

    public ArtefatoOrdemDeFornecimentoResource(ArtefatoOrdemDeFornecimentoService artefatoOrdemDeFornecimentoService) {
        this.artefatoOrdemDeFornecimentoService = artefatoOrdemDeFornecimentoService;
    }

    /**
     * {@code POST  /artefato-ordem-de-fornecimentos} : Create a new artefatoOrdemDeFornecimento.
     *
     * @param artefatoOrdemDeFornecimentoDTO the artefatoOrdemDeFornecimentoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new artefatoOrdemDeFornecimentoDTO, or with status {@code 400 (Bad Request)} if the artefatoOrdemDeFornecimento has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/artefato-ordem-de-fornecimentos")
    public ResponseEntity<ArtefatoOrdemDeFornecimentoDTO> createArtefatoOrdemDeFornecimento(@RequestBody ArtefatoOrdemDeFornecimentoDTO artefatoOrdemDeFornecimentoDTO) throws URISyntaxException {
        log.debug("REST request to save ArtefatoOrdemDeFornecimento : {}", artefatoOrdemDeFornecimentoDTO);
        if (artefatoOrdemDeFornecimentoDTO.getId() != null) {
            throw new BadRequestAlertException("A new artefatoOrdemDeFornecimento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ArtefatoOrdemDeFornecimentoDTO result = artefatoOrdemDeFornecimentoService.save(artefatoOrdemDeFornecimentoDTO);
        return ResponseEntity.created(new URI("/api/artefato-ordem-de-fornecimentos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /artefato-ordem-de-fornecimentos} : Updates an existing artefatoOrdemDeFornecimento.
     *
     * @param artefatoOrdemDeFornecimentoDTO the artefatoOrdemDeFornecimentoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated artefatoOrdemDeFornecimentoDTO,
     * or with status {@code 400 (Bad Request)} if the artefatoOrdemDeFornecimentoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the artefatoOrdemDeFornecimentoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/artefato-ordem-de-fornecimentos")
    public ResponseEntity<ArtefatoOrdemDeFornecimentoDTO> updateArtefatoOrdemDeFornecimento(@RequestBody ArtefatoOrdemDeFornecimentoDTO artefatoOrdemDeFornecimentoDTO) throws URISyntaxException {
        log.debug("REST request to update ArtefatoOrdemDeFornecimento : {}", artefatoOrdemDeFornecimentoDTO);
        if (artefatoOrdemDeFornecimentoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ArtefatoOrdemDeFornecimentoDTO result = artefatoOrdemDeFornecimentoService.save(artefatoOrdemDeFornecimentoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, artefatoOrdemDeFornecimentoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /artefato-ordem-de-fornecimentos} : get all the artefatoOrdemDeFornecimentos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of artefatoOrdemDeFornecimentos in body.
     */
    @GetMapping("/artefato-ordem-de-fornecimentos")
    public ResponseEntity<List<ArtefatoOrdemDeFornecimentoDTO>> getAllArtefatoOrdemDeFornecimentos(Pageable pageable) {
        log.debug("REST request to get a page of ArtefatoOrdemDeFornecimentos");
        Page<ArtefatoOrdemDeFornecimentoDTO> page = artefatoOrdemDeFornecimentoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /artefato-ordem-de-fornecimentos/:id} : get the "id" artefatoOrdemDeFornecimento.
     *
     * @param id the id of the artefatoOrdemDeFornecimentoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the artefatoOrdemDeFornecimentoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/artefato-ordem-de-fornecimentos/{id}")
    public ResponseEntity<ArtefatoOrdemDeFornecimentoDTO> getArtefatoOrdemDeFornecimento(@PathVariable Long id) {
        log.debug("REST request to get ArtefatoOrdemDeFornecimento : {}", id);
        Optional<ArtefatoOrdemDeFornecimentoDTO> artefatoOrdemDeFornecimentoDTO = artefatoOrdemDeFornecimentoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(artefatoOrdemDeFornecimentoDTO);
    }

    /**
     * {@code DELETE  /artefato-ordem-de-fornecimentos/:id} : delete the "id" artefatoOrdemDeFornecimento.
     *
     * @param id the id of the artefatoOrdemDeFornecimentoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/artefato-ordem-de-fornecimentos/{id}")
    public ResponseEntity<Void> deleteArtefatoOrdemDeFornecimento(@PathVariable Long id) {
        log.debug("REST request to delete ArtefatoOrdemDeFornecimento : {}", id);
        artefatoOrdemDeFornecimentoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
