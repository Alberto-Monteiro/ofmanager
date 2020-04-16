package br.com.rocksti.ofmanager.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ArtefatoOrdemDeFornecimentoMapperTest {

    private ArtefatoOrdemDeFornecimentoMapper artefatoOrdemDeFornecimentoMapper;

    @BeforeEach
    public void setUp() {
        artefatoOrdemDeFornecimentoMapper = new ArtefatoOrdemDeFornecimentoMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(artefatoOrdemDeFornecimentoMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(artefatoOrdemDeFornecimentoMapper.fromId(null)).isNull();
    }
}
