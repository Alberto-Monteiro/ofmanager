package br.com.rocksti.ofmanager.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import br.com.rocksti.ofmanager.web.rest.TestUtil;

public class ServicoOfDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServicoOfDTO.class);
        ServicoOfDTO servicoOfDTO1 = new ServicoOfDTO();
        servicoOfDTO1.setId(1L);
        ServicoOfDTO servicoOfDTO2 = new ServicoOfDTO();
        assertThat(servicoOfDTO1).isNotEqualTo(servicoOfDTO2);
        servicoOfDTO2.setId(servicoOfDTO1.getId());
        assertThat(servicoOfDTO1).isEqualTo(servicoOfDTO2);
        servicoOfDTO2.setId(2L);
        assertThat(servicoOfDTO1).isNotEqualTo(servicoOfDTO2);
        servicoOfDTO1.setId(null);
        assertThat(servicoOfDTO1).isNotEqualTo(servicoOfDTO2);
    }
}
