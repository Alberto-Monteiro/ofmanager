package br.com.rocksti.ofmanager.repository;

import br.com.rocksti.ofmanager.domain.ServicoOf;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the ServicoOf entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServicoOfRepository extends JpaRepository<ServicoOf, Long> {

    Optional<ServicoOf> findOneByNumeroEquals(Integer numeroOF);

    Page<ServicoOf> findAllByUseridEquals(Pageable pageable, Long userId);
}
