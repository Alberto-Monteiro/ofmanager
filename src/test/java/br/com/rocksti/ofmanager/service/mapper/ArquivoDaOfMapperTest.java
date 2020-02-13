package br.com.rocksti.ofmanager.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ArquivoDaOfMapperTest {

    private ArquivoDaOfMapper arquivoDaOfMapper;

    @BeforeEach
    public void setUp() {
        arquivoDaOfMapper = new ArquivoDaOfMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(arquivoDaOfMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(arquivoDaOfMapper.fromId(null)).isNull();
    }
}
