package br.com.rocksti.ofmanager.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class OrdemDeFornecimentoMapperTest {

    private OrdemDeFornecimentoMapper ordemDeFornecimentoMapper;

    @BeforeEach
    public void setUp() {
        ordemDeFornecimentoMapper = new OrdemDeFornecimentoMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(ordemDeFornecimentoMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(ordemDeFornecimentoMapper.fromId(null)).isNull();
    }
}
