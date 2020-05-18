package br.com.rocksti.ofmanager.repository;

import br.com.rocksti.ofmanager.domain.OrdemDeFornecimento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    Optional<OrdemDeFornecimento> findOneByNumeroEquals(Integer numeroOF);

    @Query("select s from OrdemDeFornecimento s where (:numeroOF is null or s.numero = :numeroOF) and (s.donoDaOf.id  = :userId)")
    Page<OrdemDeFornecimento> findAllByDonoDaOf_IdEqualsAndNumeroEquals(Pageable pageable, @Param("userId") Long userId, @Param("numeroOF") Integer numeroOF);

    @Query("select s from OrdemDeFornecimento s where (:numeroOF is null or s.numero = :numeroOF) and (:idUsuarioGestor is null or s.gestorDaOf.id = :idUsuarioGestor)")
    Page<OrdemDeFornecimento> findAllByNumeroEqualsAndGestorDaOf_Id(Pageable pageable, @Param("numeroOF") Integer numeroOF, @Param("idUsuarioGestor") Long idUsuarioGestor);
}
