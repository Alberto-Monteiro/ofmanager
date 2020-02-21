package br.com.rocksti.ofmanager.repository;

import br.com.rocksti.ofmanager.domain.ServicoOf;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the ServicoOf entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServicoOfRepository extends JpaRepository<ServicoOf, Long> {

    @Query("select servicoOf from ServicoOf servicoOf where servicoOf.gestorDaOf.login = ?#{principal.username}")
    List<ServicoOf> findByGestorDaOfIsCurrentUser();

    @Query("select servicoOf from ServicoOf servicoOf where servicoOf.donoDaOf.login = ?#{principal.username}")
    List<ServicoOf> findByDonoDaOfIsCurrentUser();

    Optional<ServicoOf> findOneByNumeroEquals(Integer numeroOF);

    Page<ServicoOf> findAllByUseridEquals(Pageable pageable, Long userId);
}
