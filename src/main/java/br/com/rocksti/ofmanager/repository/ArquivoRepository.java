package br.com.rocksti.ofmanager.repository;

import br.com.rocksti.ofmanager.domain.Arquivo;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Arquivo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArquivoRepository extends JpaRepository<Arquivo, Long> {

    List<Arquivo> findByCaminhoDoArquivoIn(List<String> caminhoDoArquivoList);
}
