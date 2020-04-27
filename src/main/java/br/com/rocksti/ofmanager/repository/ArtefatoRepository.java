package br.com.rocksti.ofmanager.repository;

import br.com.rocksti.ofmanager.domain.Artefato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Artefato entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArtefatoRepository extends JpaRepository<Artefato, Long> {

    List<Artefato> findByLocalDoArtefatoIn(List<String> listaLocalArtefatos);

    Optional<Artefato> findByLocalDoArtefato(String localArtefatos);
}
