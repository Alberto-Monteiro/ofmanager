package br.com.rocksti.ofmanager.repository;

import br.com.rocksti.ofmanager.domain.ArquivoDaOf;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ArquivoDaOf entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArquivoDaOfRepository extends JpaRepository<ArquivoDaOf, Long> {

}
