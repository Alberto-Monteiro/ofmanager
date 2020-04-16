package br.com.rocksti.ofmanager.service;

import br.com.rocksti.ofmanager.domain.ArtefatoOrdemDeFornecimento;
import br.com.rocksti.ofmanager.repository.ArtefatoOrdemDeFornecimentoRepository;
import br.com.rocksti.ofmanager.service.dto.ArtefatoOrdemDeFornecimentoDTO;
import br.com.rocksti.ofmanager.service.mapper.ArtefatoOrdemDeFornecimentoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ArtefatoOrdemDeFornecimento}.
 */
@Service
@Transactional
public class ArtefatoOrdemDeFornecimentoService {

    private final Logger log = LoggerFactory.getLogger(ArtefatoOrdemDeFornecimentoService.class);

    private final ArtefatoOrdemDeFornecimentoRepository artefatoOrdemDeFornecimentoRepository;

    private final ArtefatoOrdemDeFornecimentoMapper artefatoOrdemDeFornecimentoMapper;

    public ArtefatoOrdemDeFornecimentoService(ArtefatoOrdemDeFornecimentoRepository artefatoOrdemDeFornecimentoRepository, ArtefatoOrdemDeFornecimentoMapper artefatoOrdemDeFornecimentoMapper) {
        this.artefatoOrdemDeFornecimentoRepository = artefatoOrdemDeFornecimentoRepository;
        this.artefatoOrdemDeFornecimentoMapper = artefatoOrdemDeFornecimentoMapper;
    }

    /**
     * Save a artefatoOrdemDeFornecimento.
     *
     * @param artefatoOrdemDeFornecimentoDTO the entity to save.
     * @return the persisted entity.
     */
    public ArtefatoOrdemDeFornecimentoDTO save(ArtefatoOrdemDeFornecimentoDTO artefatoOrdemDeFornecimentoDTO) {
        log.debug("Request to save ArtefatoOrdemDeFornecimento : {}", artefatoOrdemDeFornecimentoDTO);
        ArtefatoOrdemDeFornecimento artefatoOrdemDeFornecimento = artefatoOrdemDeFornecimentoMapper.toEntity(artefatoOrdemDeFornecimentoDTO);
        artefatoOrdemDeFornecimento = artefatoOrdemDeFornecimentoRepository.save(artefatoOrdemDeFornecimento);
        return artefatoOrdemDeFornecimentoMapper.toDto(artefatoOrdemDeFornecimento);
    }

    /**
     * Get all the artefatoOrdemDeFornecimentos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ArtefatoOrdemDeFornecimentoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ArtefatoOrdemDeFornecimentos");
        return artefatoOrdemDeFornecimentoRepository.findAll(pageable)
            .map(artefatoOrdemDeFornecimentoMapper::toDto);
    }

    /**
     * Get one artefatoOrdemDeFornecimento by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ArtefatoOrdemDeFornecimentoDTO> findOne(Long id) {
        log.debug("Request to get ArtefatoOrdemDeFornecimento : {}", id);
        return artefatoOrdemDeFornecimentoRepository.findById(id)
            .map(artefatoOrdemDeFornecimentoMapper::toDto);
    }

    /**
     * Delete the artefatoOrdemDeFornecimento by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ArtefatoOrdemDeFornecimento : {}", id);
        artefatoOrdemDeFornecimentoRepository.deleteById(id);
    }
}
