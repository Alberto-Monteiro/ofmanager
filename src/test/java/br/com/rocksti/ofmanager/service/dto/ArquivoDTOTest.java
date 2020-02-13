package br.com.rocksti.ofmanager.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import br.com.rocksti.ofmanager.web.rest.TestUtil;

public class ArquivoDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArquivoDTO.class);
        ArquivoDTO arquivoDTO1 = new ArquivoDTO();
        arquivoDTO1.setId(1L);
        ArquivoDTO arquivoDTO2 = new ArquivoDTO();
        assertThat(arquivoDTO1).isNotEqualTo(arquivoDTO2);
        arquivoDTO2.setId(arquivoDTO1.getId());
        assertThat(arquivoDTO1).isEqualTo(arquivoDTO2);
        arquivoDTO2.setId(2L);
        assertThat(arquivoDTO1).isNotEqualTo(arquivoDTO2);
        arquivoDTO1.setId(null);
        assertThat(arquivoDTO1).isNotEqualTo(arquivoDTO2);
    }
}
