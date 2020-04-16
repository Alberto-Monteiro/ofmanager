package br.com.rocksti.ofmanager.service;

import br.com.rocksti.ofmanager.domain.Artefato;
import br.com.rocksti.ofmanager.repository.ArtefatoRepository;
import br.com.rocksti.ofmanager.service.dto.ArtefatoDTO;
import br.com.rocksti.ofmanager.service.mapper.ArtefatoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Artefato}.
 */
@Service
@Transactional
public class ArtefatoService {

    private final Logger log = LoggerFactory.getLogger(ArtefatoService.class);

    private final ArtefatoRepository artefatoRepository;

    private final ArtefatoMapper artefatoMapper;

    public ArtefatoService(ArtefatoRepository artefatoRepository, ArtefatoMapper artefatoMapper) {
        this.artefatoRepository = artefatoRepository;
        this.artefatoMapper = artefatoMapper;
    }

    /**
     * Save a artefato.
     *
     * @param artefatoDTO the entity to save.
     * @return the persisted entity.
     */
    public ArtefatoDTO save(ArtefatoDTO artefatoDTO) {
        log.debug("Request to save Artefato : {}", artefatoDTO);
        Artefato artefato = artefatoMapper.toEntity(artefatoDTO);
        artefato = artefatoRepository.save(artefato);
        return artefatoMapper.toDto(artefato);
    }

    /**
     * Get all the artefatoes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ArtefatoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Artefatoes");
        return artefatoRepository.findAll(pageable)
            .map(artefatoMapper::toDto);
    }

    /**
     * Get one artefato by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ArtefatoDTO> findOne(Long id) {
        log.debug("Request to get Artefato : {}", id);
        return artefatoRepository.findById(id)
            .map(artefatoMapper::toDto);
    }

    /**
     * Delete the artefato by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Artefato : {}", id);
        artefatoRepository.deleteById(id);
    }
}
