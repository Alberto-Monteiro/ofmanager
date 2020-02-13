package br.com.rocksti.ofmanager.web.rest;

import br.com.rocksti.ofmanager.service.ServicoOfService;
import br.com.rocksti.ofmanager.web.rest.errors.BadRequestAlertException;
import br.com.rocksti.ofmanager.service.dto.ServicoOfDTO;

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
 * REST controller for managing {@link br.com.rocksti.ofmanager.domain.ServicoOf}.
 */
@RestController
@RequestMapping("/api")
public class ServicoOfResource {

    private final Logger log = LoggerFactory.getLogger(ServicoOfResource.class);

    private static final String ENTITY_NAME = "servicoOf";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ServicoOfService servicoOfService;

    public ServicoOfResource(ServicoOfService servicoOfService) {
        this.servicoOfService = servicoOfService;
    }

    /**
     * {@code POST  /servico-ofs} : Create a new servicoOf.
     *
     * @param servicoOfDTO the servicoOfDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new servicoOfDTO, or with status {@code 400 (Bad Request)} if the servicoOf has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/servico-ofs")
    public ResponseEntity<ServicoOfDTO> createServicoOf(@Valid @RequestBody ServicoOfDTO servicoOfDTO) throws URISyntaxException {
        log.debug("REST request to save ServicoOf : {}", servicoOfDTO);
        if (servicoOfDTO.getId() != null) {
            throw new BadRequestAlertException("A new servicoOf cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ServicoOfDTO result = servicoOfService.save(servicoOfDTO);
        return ResponseEntity.created(new URI("/api/servico-ofs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /servico-ofs} : Updates an existing servicoOf.
     *
     * @param servicoOfDTO the servicoOfDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated servicoOfDTO,
     * or with status {@code 400 (Bad Request)} if the servicoOfDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the servicoOfDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/servico-ofs")
    public ResponseEntity<ServicoOfDTO> updateServicoOf(@Valid @RequestBody ServicoOfDTO servicoOfDTO) throws URISyntaxException {
        log.debug("REST request to update ServicoOf : {}", servicoOfDTO);
        if (servicoOfDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ServicoOfDTO result = servicoOfService.save(servicoOfDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, servicoOfDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /servico-ofs} : get all the servicoOfs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of servicoOfs in body.
     */
    @GetMapping("/servico-ofs")
    public ResponseEntity<List<ServicoOfDTO>> getAllServicoOfs(Pageable pageable) {
        log.debug("REST request to get a page of ServicoOfs");
        Page<ServicoOfDTO> page = servicoOfService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /servico-ofs/:id} : get the "id" servicoOf.
     *
     * @param id the id of the servicoOfDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the servicoOfDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/servico-ofs/{id}")
    public ResponseEntity<ServicoOfDTO> getServicoOf(@PathVariable Long id) {
        log.debug("REST request to get ServicoOf : {}", id);
        Optional<ServicoOfDTO> servicoOfDTO = servicoOfService.findOne(id);
        return ResponseUtil.wrapOrNotFound(servicoOfDTO);
    }

    /**
     * {@code DELETE  /servico-ofs/:id} : delete the "id" servicoOf.
     *
     * @param id the id of the servicoOfDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/servico-ofs/{id}")
    public ResponseEntity<Void> deleteServicoOf(@PathVariable Long id) {
        log.debug("REST request to delete ServicoOf : {}", id);
        servicoOfService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
