package br.com.rocksti.ofmanager.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import br.com.rocksti.ofmanager.web.rest.TestUtil;

public class ArtefatoOrdemDeFornecimentoDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArtefatoOrdemDeFornecimentoDTO.class);
        ArtefatoOrdemDeFornecimentoDTO artefatoOrdemDeFornecimentoDTO1 = new ArtefatoOrdemDeFornecimentoDTO();
        artefatoOrdemDeFornecimentoDTO1.setId(1L);
        ArtefatoOrdemDeFornecimentoDTO artefatoOrdemDeFornecimentoDTO2 = new ArtefatoOrdemDeFornecimentoDTO();
        assertThat(artefatoOrdemDeFornecimentoDTO1).isNotEqualTo(artefatoOrdemDeFornecimentoDTO2);
        artefatoOrdemDeFornecimentoDTO2.setId(artefatoOrdemDeFornecimentoDTO1.getId());
        assertThat(artefatoOrdemDeFornecimentoDTO1).isEqualTo(artefatoOrdemDeFornecimentoDTO2);
        artefatoOrdemDeFornecimentoDTO2.setId(2L);
        assertThat(artefatoOrdemDeFornecimentoDTO1).isNotEqualTo(artefatoOrdemDeFornecimentoDTO2);
        artefatoOrdemDeFornecimentoDTO1.setId(null);
        assertThat(artefatoOrdemDeFornecimentoDTO1).isNotEqualTo(artefatoOrdemDeFornecimentoDTO2);
    }
}
