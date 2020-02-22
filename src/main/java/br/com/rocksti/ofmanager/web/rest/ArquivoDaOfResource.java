package br.com.rocksti.ofmanager.web.rest;

import br.com.rocksti.ofmanager.service.ArquivoDaOfService;
import br.com.rocksti.ofmanager.web.rest.errors.BadRequestAlertException;
import br.com.rocksti.ofmanager.service.dto.ArquivoDaOfDTO;

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
 * REST controller for managing {@link br.com.rocksti.ofmanager.domain.ArquivoDaOf}.
 */
@RestController
@RequestMapping("/api")
public class ArquivoDaOfResource {

    private final Logger log = LoggerFactory.getLogger(ArquivoDaOfResource.class);

    private static final String ENTITY_NAME = "arquivoDaOf";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArquivoDaOfService arquivoDaOfService;

    public ArquivoDaOfResource(ArquivoDaOfService arquivoDaOfService) {
        this.arquivoDaOfService = arquivoDaOfService;
    }

    /**
     * {@code POST  /arquivo-da-ofs} : Create a new arquivoDaOf.
     *
     * @param arquivoDaOfDTO the arquivoDaOfDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new arquivoDaOfDTO, or with status {@code 400 (Bad Request)} if the arquivoDaOf has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/arquivo-da-ofs")
    public ResponseEntity<ArquivoDaOfDTO> createArquivoDaOf(@RequestBody ArquivoDaOfDTO arquivoDaOfDTO) throws URISyntaxException {
        log.debug("REST request to save ArquivoDaOf : {}", arquivoDaOfDTO);
        if (arquivoDaOfDTO.getId() != null) {
            throw new BadRequestAlertException("A new arquivoDaOf cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ArquivoDaOfDTO result = arquivoDaOfService.save(arquivoDaOfDTO);
        return ResponseEntity.created(new URI("/api/arquivo-da-ofs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /arquivo-da-ofs} : Updates an existing arquivoDaOf.
     *
     * @param arquivoDaOfDTO the arquivoDaOfDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated arquivoDaOfDTO,
     * or with status {@code 400 (Bad Request)} if the arquivoDaOfDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the arquivoDaOfDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/arquivo-da-ofs")
    public ResponseEntity<ArquivoDaOfDTO> updateArquivoDaOf(@RequestBody ArquivoDaOfDTO arquivoDaOfDTO) throws URISyntaxException {
        log.debug("REST request to update ArquivoDaOf : {}", arquivoDaOfDTO);
        if (arquivoDaOfDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ArquivoDaOfDTO result = arquivoDaOfService.save(arquivoDaOfDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, arquivoDaOfDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /arquivo-da-ofs} : get all the arquivoDaOfs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of arquivoDaOfs in body.
     */
    @GetMapping("/arquivo-da-ofs")
    public ResponseEntity<List<ArquivoDaOfDTO>> getAllArquivoDaOfs(Pageable pageable) {
        log.debug("REST request to get a page of ArquivoDaOfs");
        Page<ArquivoDaOfDTO> page = arquivoDaOfService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /arquivo-da-ofs/:id} : get the "id" arquivoDaOf.
     *
     * @param id the id of the arquivoDaOfDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the arquivoDaOfDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/arquivo-da-ofs/{id}")
    public ResponseEntity<ArquivoDaOfDTO> getArquivoDaOf(@PathVariable Long id) {
        log.debug("REST request to get ArquivoDaOf : {}", id);
        Optional<ArquivoDaOfDTO> arquivoDaOfDTO = arquivoDaOfService.findOne(id);
        return ResponseUtil.wrapOrNotFound(arquivoDaOfDTO);
    }

    /**
     * {@code DELETE  /arquivo-da-ofs/:id} : delete the "id" arquivoDaOf.
     *
     * @param id the id of the arquivoDaOfDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/arquivo-da-ofs/{id}")
    public ResponseEntity<Void> deleteArquivoDaOf(@PathVariable Long id) {
        log.debug("REST request to delete ArquivoDaOf : {}", id);
        arquivoDaOfService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
