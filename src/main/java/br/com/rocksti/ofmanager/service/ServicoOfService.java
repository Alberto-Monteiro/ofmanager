package br.com.rocksti.ofmanager.service;

import br.com.rocksti.ofmanager.domain.ServicoOf;
import br.com.rocksti.ofmanager.repository.ServicoOfRepository;
import br.com.rocksti.ofmanager.security.AuthoritiesConstants;
import br.com.rocksti.ofmanager.service.dto.ServicoOfDTO;
import br.com.rocksti.ofmanager.service.mapper.ServicoOfMapper;
import br.com.rocksti.ofmanager.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ServicoOf}.
 */
@Service
@Transactional
public class ServicoOfService {

    private final Logger log = LoggerFactory.getLogger(ServicoOfService.class);

    private final ServicoOfRepository servicoOfRepository;

    private final ServicoOfMapper servicoOfMapper;

    private final UserService userService;

    public ServicoOfService(ServicoOfRepository servicoOfRepository,
                            ServicoOfMapper servicoOfMapper,
                            UserService userService) {
        this.servicoOfRepository = servicoOfRepository;
        this.servicoOfMapper = servicoOfMapper;
        this.userService = userService;
    }

    /**
     * Save a servicoOf.
     *
     * @param servicoOfDTO the entity to save.
     * @return the persisted entity.
     */
    public ServicoOfDTO save(ServicoOfDTO servicoOfDTO) {
        log.debug("Request to save ServicoOf : {}", servicoOfDTO);
        ServicoOf servicoOf = servicoOfMapper.toEntity(servicoOfDTO);
        servicoOf = servicoOfRepository.save(servicoOf);
        return servicoOfMapper.toDto(servicoOf);
    }

    /**
     * Get all the servicoOfs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ServicoOfDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ServicoOfs");
        return servicoOfRepository.findAll(pageable)
            .map(servicoOfMapper::toDto);
    }

    /**
     * Get one servicoOf by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ServicoOfDTO> findOne(Long id) {
        log.debug("Request to get ServicoOf : {}", id);
        return servicoOfRepository.findById(id)
            .map(servicoOfMapper::toDto);
    }

    /**
     * Delete the servicoOf by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ServicoOf : {}", id);

        userService.getUserWithAuthorities()
            .filter(user -> user.getAuthorities()
                .stream()
                .anyMatch(authority -> authority.getName().equals(AuthoritiesConstants.ADMIN)
                    || authority.getName().equals(AuthoritiesConstants.GESTOR_OF)))
            .map(user -> {
        servicoOfRepository.deleteById(id);
                return Optional.of(user);
            })
            .orElseGet(() -> {
                servicoOfRepository.findById(id)
                    .filter(servicoOf -> userService.getUserWithAuthorities().filter(user -> user.getId().equals(servicoOf.getDonoDaOf().getId())).isPresent())
                    .map(servicoOf -> {
                        servicoOfRepository.deleteById(id);
                        return Optional.of(servicoOf);
                    })
                    .orElseGet(() -> {
                        throw new BadRequestAlertException("Of pertencente a outro usuário", "servicoOf", "servicoOfNaoPertencente");
                    });
                return Optional.empty();
            });
    }
}
