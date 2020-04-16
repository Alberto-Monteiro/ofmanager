package br.com.rocksti.ofmanager.web.rest;

import br.com.rocksti.ofmanager.service.ArtefatoService;
import br.com.rocksti.ofmanager.web.rest.errors.BadRequestAlertException;
import br.com.rocksti.ofmanager.service.dto.ArtefatoDTO;

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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link br.com.rocksti.ofmanager.domain.Artefato}.
 */
@RestController
@RequestMapping("/api")
public class ArtefatoResource {

    private final Logger log = LoggerFactory.getLogger(ArtefatoResource.class);

    private static final String ENTITY_NAME = "artefato";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArtefatoService artefatoService;

    public ArtefatoResource(ArtefatoService artefatoService) {
        this.artefatoService = artefatoService;
    }

    /**
     * {@code POST  /artefatoes} : Create a new artefato.
     *
     * @param artefatoDTO the artefatoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new artefatoDTO, or with status {@code 400 (Bad Request)} if the artefato has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/artefatoes")
    public ResponseEntity<ArtefatoDTO> createArtefato(@Valid @RequestBody ArtefatoDTO artefatoDTO) throws URISyntaxException {
        log.debug("REST request to save Artefato : {}", artefatoDTO);
        if (artefatoDTO.getId() != null) {
            throw new BadRequestAlertException("A new artefato cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ArtefatoDTO result = artefatoService.save(artefatoDTO);
        return ResponseEntity.created(new URI("/api/artefatoes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /artefatoes} : Updates an existing artefato.
     *
     * @param artefatoDTO the artefatoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated artefatoDTO,
     * or with status {@code 400 (Bad Request)} if the artefatoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the artefatoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/artefatoes")
    public ResponseEntity<ArtefatoDTO> updateArtefato(@Valid @RequestBody ArtefatoDTO artefatoDTO) throws URISyntaxException {
        log.debug("REST request to update Artefato : {}", artefatoDTO);
        if (artefatoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ArtefatoDTO result = artefatoService.save(artefatoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, artefatoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /artefatoes} : get all the artefatoes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of artefatoes in body.
     */
    @GetMapping("/artefatoes")
    public ResponseEntity<List<ArtefatoDTO>> getAllArtefatoes(Pageable pageable) {
        log.debug("REST request to get a page of Artefatoes");
        Page<ArtefatoDTO> page = artefatoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /artefatoes/:id} : get the "id" artefato.
     *
     * @param id the id of the artefatoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the artefatoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/artefatoes/{id}")
    public ResponseEntity<ArtefatoDTO> getArtefato(@PathVariable Long id) {
        log.debug("REST request to get Artefato : {}", id);
        Optional<ArtefatoDTO> artefatoDTO = artefatoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(artefatoDTO);
    }

    /**
     * {@code DELETE  /artefatoes/:id} : delete the "id" artefato.
     *
     * @param id the id of the artefatoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/artefatoes/{id}")
    public ResponseEntity<Void> deleteArtefato(@PathVariable Long id) {
        log.debug("REST request to delete Artefato : {}", id);
        artefatoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
