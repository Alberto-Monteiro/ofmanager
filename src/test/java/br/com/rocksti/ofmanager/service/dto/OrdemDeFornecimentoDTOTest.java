package br.com.rocksti.ofmanager.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import br.com.rocksti.ofmanager.web.rest.TestUtil;

public class OrdemDeFornecimentoDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrdemDeFornecimentoDTO.class);
        OrdemDeFornecimentoDTO ordemDeFornecimentoDTO1 = new OrdemDeFornecimentoDTO();
        ordemDeFornecimentoDTO1.setId(1L);
        OrdemDeFornecimentoDTO ordemDeFornecimentoDTO2 = new OrdemDeFornecimentoDTO();
        assertThat(ordemDeFornecimentoDTO1).isNotEqualTo(ordemDeFornecimentoDTO2);
        ordemDeFornecimentoDTO2.setId(ordemDeFornecimentoDTO1.getId());
        assertThat(ordemDeFornecimentoDTO1).isEqualTo(ordemDeFornecimentoDTO2);
        ordemDeFornecimentoDTO2.setId(2L);
        assertThat(ordemDeFornecimentoDTO1).isNotEqualTo(ordemDeFornecimentoDTO2);
        ordemDeFornecimentoDTO1.setId(null);
        assertThat(ordemDeFornecimentoDTO1).isNotEqualTo(ordemDeFornecimentoDTO2);
    }
}
