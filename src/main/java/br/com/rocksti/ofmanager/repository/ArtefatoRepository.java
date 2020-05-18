package br.com.rocksti.ofmanager.repository;

import br.com.rocksti.ofmanager.domain.Artefato;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Artefato entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArtefatoRepository extends JpaRepository<Artefato, Long> {
}
