package br.com.rocksti.ofmanager.repository;

import br.com.rocksti.ofmanager.domain.ServicoOf;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ServicoOf entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServicoOfRepository extends JpaRepository<ServicoOf, Long> {

}
