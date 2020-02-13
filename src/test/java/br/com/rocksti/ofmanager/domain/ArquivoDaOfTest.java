package br.com.rocksti.ofmanager.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import br.com.rocksti.ofmanager.web.rest.TestUtil;

public class ArquivoDaOfTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArquivoDaOf.class);
        ArquivoDaOf arquivoDaOf1 = new ArquivoDaOf();
        arquivoDaOf1.setId(1L);
        ArquivoDaOf arquivoDaOf2 = new ArquivoDaOf();
        arquivoDaOf2.setId(arquivoDaOf1.getId());
        assertThat(arquivoDaOf1).isEqualTo(arquivoDaOf2);
        arquivoDaOf2.setId(2L);
        assertThat(arquivoDaOf1).isNotEqualTo(arquivoDaOf2);
        arquivoDaOf1.setId(null);
        assertThat(arquivoDaOf1).isNotEqualTo(arquivoDaOf2);
    }
}
