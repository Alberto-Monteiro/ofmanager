package br.com.rocksti.ofmanager.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import br.com.rocksti.ofmanager.web.rest.TestUtil;

public class ArtefatoOrdemDeFornecimentoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArtefatoOrdemDeFornecimento.class);
        ArtefatoOrdemDeFornecimento artefatoOrdemDeFornecimento1 = new ArtefatoOrdemDeFornecimento();
        artefatoOrdemDeFornecimento1.setId(1L);
        ArtefatoOrdemDeFornecimento artefatoOrdemDeFornecimento2 = new ArtefatoOrdemDeFornecimento();
        artefatoOrdemDeFornecimento2.setId(artefatoOrdemDeFornecimento1.getId());
        assertThat(artefatoOrdemDeFornecimento1).isEqualTo(artefatoOrdemDeFornecimento2);
        artefatoOrdemDeFornecimento2.setId(2L);
        assertThat(artefatoOrdemDeFornecimento1).isNotEqualTo(artefatoOrdemDeFornecimento2);
        artefatoOrdemDeFornecimento1.setId(null);
        assertThat(artefatoOrdemDeFornecimento1).isNotEqualTo(artefatoOrdemDeFornecimento2);
    }
}
