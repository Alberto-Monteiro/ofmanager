package br.com.rocksti.ofmanager.repository;

import br.com.rocksti.ofmanager.domain.OrdemDeFornecimento;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the OrdemDeFornecimento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrdemDeFornecimentoRepository extends JpaRepository<OrdemDeFornecimento, Long> {

    @Query("select ordemDeFornecimento from OrdemDeFornecimento ordemDeFornecimento where ordemDeFornecimento.gestorDaOf.login = ?#{principal.username}")
    List<OrdemDeFornecimento> findByGestorDaOfIsCurrentUser();

    @Query("select ordemDeFornecimento from OrdemDeFornecimento ordemDeFornecimento where ordemDeFornecimento.donoDaOf.login = ?#{principal.username}")
    List<OrdemDeFornecimento> findByDonoDaOfIsCurrentUser();
}
