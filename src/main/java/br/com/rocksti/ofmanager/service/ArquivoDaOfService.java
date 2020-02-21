package br.com.rocksti.ofmanager.service;

import br.com.rocksti.ofmanager.domain.ArquivoDaOf;
import br.com.rocksti.ofmanager.repository.ArquivoDaOfRepository;
import br.com.rocksti.ofmanager.service.dto.ArquivoDaOfDTO;
import br.com.rocksti.ofmanager.service.mapper.ArquivoDaOfMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ArquivoDaOf}.
 */
@Service
@Transactional
public class ArquivoDaOfService {

    private final Logger log = LoggerFactory.getLogger(ArquivoDaOfService.class);

    private final ArquivoDaOfRepository arquivoDaOfRepository;

    private final ArquivoDaOfMapper arquivoDaOfMapper;

    public ArquivoDaOfService(ArquivoDaOfRepository arquivoDaOfRepository, ArquivoDaOfMapper arquivoDaOfMapper) {
        this.arquivoDaOfRepository = arquivoDaOfRepository;
        this.arquivoDaOfMapper = arquivoDaOfMapper;
    }

    /**
     * Save a arquivoDaOf.
     *
     * @param arquivoDaOfDTO the entity to save.
     * @return the persisted entity.
     */
    public ArquivoDaOfDTO save(ArquivoDaOfDTO arquivoDaOfDTO) {
        log.debug("Request to save ArquivoDaOf : {}", arquivoDaOfDTO);
        ArquivoDaOf arquivoDaOf = arquivoDaOfMapper.toEntity(arquivoDaOfDTO);
        arquivoDaOf = arquivoDaOfRepository.save(arquivoDaOf);
        return arquivoDaOfMapper.toDto(arquivoDaOf);
    }

    /**
     * Get all the arquivoDaOfs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ArquivoDaOfDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ArquivoDaOfs");
        return arquivoDaOfRepository.findAll(pageable)
            .map(arquivoDaOfMapper::toDto);
    }

    /**
     * Get one arquivoDaOf by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ArquivoDaOfDTO> findOne(Long id) {
        log.debug("Request to get ArquivoDaOf : {}", id);
        return arquivoDaOfRepository.findById(id)
            .map(arquivoDaOfMapper::toDto);
    }

    /**
     * Delete the arquivoDaOf by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ArquivoDaOf : {}", id);
        arquivoDaOfRepository.deleteById(id);
    }
}
