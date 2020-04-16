package br.com.rocksti.ofmanager.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ArtefatoMapperTest {

    private ArtefatoMapper artefatoMapper;

    @BeforeEach
    public void setUp() {
        artefatoMapper = new ArtefatoMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(artefatoMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(artefatoMapper.fromId(null)).isNull();
    }
}
