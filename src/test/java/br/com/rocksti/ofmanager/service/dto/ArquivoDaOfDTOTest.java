package br.com.rocksti.ofmanager.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import br.com.rocksti.ofmanager.web.rest.TestUtil;

public class ArquivoDaOfDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArquivoDaOfDTO.class);
        ArquivoDaOfDTO arquivoDaOfDTO1 = new ArquivoDaOfDTO();
        arquivoDaOfDTO1.setId(1L);
        ArquivoDaOfDTO arquivoDaOfDTO2 = new ArquivoDaOfDTO();
        assertThat(arquivoDaOfDTO1).isNotEqualTo(arquivoDaOfDTO2);
        arquivoDaOfDTO2.setId(arquivoDaOfDTO1.getId());
        assertThat(arquivoDaOfDTO1).isEqualTo(arquivoDaOfDTO2);
        arquivoDaOfDTO2.setId(2L);
        assertThat(arquivoDaOfDTO1).isNotEqualTo(arquivoDaOfDTO2);
        arquivoDaOfDTO1.setId(null);
        assertThat(arquivoDaOfDTO1).isNotEqualTo(arquivoDaOfDTO2);
    }
}
