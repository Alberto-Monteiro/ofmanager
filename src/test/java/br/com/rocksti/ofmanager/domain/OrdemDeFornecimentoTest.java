package br.com.rocksti.ofmanager.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import br.com.rocksti.ofmanager.web.rest.TestUtil;

public class OrdemDeFornecimentoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrdemDeFornecimento.class);
        OrdemDeFornecimento ordemDeFornecimento1 = new OrdemDeFornecimento();
        ordemDeFornecimento1.setId(1L);
        OrdemDeFornecimento ordemDeFornecimento2 = new OrdemDeFornecimento();
        ordemDeFornecimento2.setId(ordemDeFornecimento1.getId());
        assertThat(ordemDeFornecimento1).isEqualTo(ordemDeFornecimento2);
        ordemDeFornecimento2.setId(2L);
        assertThat(ordemDeFornecimento1).isNotEqualTo(ordemDeFornecimento2);
        ordemDeFornecimento1.setId(null);
        assertThat(ordemDeFornecimento1).isNotEqualTo(ordemDeFornecimento2);
    }
}
