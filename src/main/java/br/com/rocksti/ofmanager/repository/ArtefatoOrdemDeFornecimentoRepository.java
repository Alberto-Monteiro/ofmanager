package br.com.rocksti.ofmanager.repository;

import br.com.rocksti.ofmanager.domain.ArtefatoOrdemDeFornecimento;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ArtefatoOrdemDeFornecimento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArtefatoOrdemDeFornecimentoRepository extends JpaRepository<ArtefatoOrdemDeFornecimento, Long> {
}
