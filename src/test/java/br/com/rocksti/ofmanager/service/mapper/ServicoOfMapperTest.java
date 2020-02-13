package br.com.rocksti.ofmanager.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ServicoOfMapperTest {

    private ServicoOfMapper servicoOfMapper;

    @BeforeEach
    public void setUp() {
        servicoOfMapper = new ServicoOfMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(servicoOfMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(servicoOfMapper.fromId(null)).isNull();
    }
}
