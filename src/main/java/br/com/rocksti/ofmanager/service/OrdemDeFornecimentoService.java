package br.com.rocksti.ofmanager.service;

import br.com.rocksti.ofmanager.domain.OrdemDeFornecimento;
import br.com.rocksti.ofmanager.repository.OrdemDeFornecimentoRepository;
import br.com.rocksti.ofmanager.service.dto.OrdemDeFornecimentoDTO;
import br.com.rocksti.ofmanager.service.mapper.OrdemDeFornecimentoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link OrdemDeFornecimento}.
 */
@Service
@Transactional
public class OrdemDeFornecimentoService {

    private final Logger log = LoggerFactory.getLogger(OrdemDeFornecimentoService.class);

    private final OrdemDeFornecimentoRepository ordemDeFornecimentoRepository;

    private final OrdemDeFornecimentoMapper ordemDeFornecimentoMapper;

    public OrdemDeFornecimentoService(OrdemDeFornecimentoRepository ordemDeFornecimentoRepository, OrdemDeFornecimentoMapper ordemDeFornecimentoMapper) {
        this.ordemDeFornecimentoRepository = ordemDeFornecimentoRepository;
        this.ordemDeFornecimentoMapper = ordemDeFornecimentoMapper;
    }

    /**
     * Save a ordemDeFornecimento.
     *
     * @param ordemDeFornecimentoDTO the entity to save.
     * @return the persisted entity.
     */
    public OrdemDeFornecimentoDTO save(OrdemDeFornecimentoDTO ordemDeFornecimentoDTO) {
        log.debug("Request to save OrdemDeFornecimento : {}", ordemDeFornecimentoDTO);
        OrdemDeFornecimento ordemDeFornecimento = ordemDeFornecimentoMapper.toEntity(ordemDeFornecimentoDTO);
        ordemDeFornecimento = ordemDeFornecimentoRepository.save(ordemDeFornecimento);
        return ordemDeFornecimentoMapper.toDto(ordemDeFornecimento);
    }

    /**
     * Get all the ordemDeFornecimentos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OrdemDeFornecimentoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OrdemDeFornecimentos");
        return ordemDeFornecimentoRepository.findAll(pageable)
            .map(ordemDeFornecimentoMapper::toDto);
    }

    /**
     * Get one ordemDeFornecimento by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrdemDeFornecimentoDTO> findOne(Long id) {
        log.debug("Request to get OrdemDeFornecimento : {}", id);
        return ordemDeFornecimentoRepository.findById(id)
            .map(ordemDeFornecimentoMapper::toDto);
    }

    /**
     * Delete the ordemDeFornecimento by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete OrdemDeFornecimento : {}", id);
        ordemDeFornecimentoRepository.deleteById(id);
    }
}
