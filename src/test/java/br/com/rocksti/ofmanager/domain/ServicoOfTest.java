package br.com.rocksti.ofmanager.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import br.com.rocksti.ofmanager.web.rest.TestUtil;

public class ServicoOfTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServicoOf.class);
        ServicoOf servicoOf1 = new ServicoOf();
        servicoOf1.setId(1L);
        ServicoOf servicoOf2 = new ServicoOf();
        servicoOf2.setId(servicoOf1.getId());
        assertThat(servicoOf1).isEqualTo(servicoOf2);
        servicoOf2.setId(2L);
        assertThat(servicoOf1).isNotEqualTo(servicoOf2);
        servicoOf1.setId(null);
        assertThat(servicoOf1).isNotEqualTo(servicoOf2);
    }
}
