package br.com.rocksti.ofmanager.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import br.com.rocksti.ofmanager.web.rest.TestUtil;

public class ArtefatoDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArtefatoDTO.class);
        ArtefatoDTO artefatoDTO1 = new ArtefatoDTO();
        artefatoDTO1.setId(1L);
        ArtefatoDTO artefatoDTO2 = new ArtefatoDTO();
        assertThat(artefatoDTO1).isNotEqualTo(artefatoDTO2);
        artefatoDTO2.setId(artefatoDTO1.getId());
        assertThat(artefatoDTO1).isEqualTo(artefatoDTO2);
        artefatoDTO2.setId(2L);
        assertThat(artefatoDTO1).isNotEqualTo(artefatoDTO2);
        artefatoDTO1.setId(null);
        assertThat(artefatoDTO1).isNotEqualTo(artefatoDTO2);
    }
}
