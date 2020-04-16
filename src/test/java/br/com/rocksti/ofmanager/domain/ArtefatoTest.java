package br.com.rocksti.ofmanager.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import br.com.rocksti.ofmanager.web.rest.TestUtil;

public class ArtefatoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Artefato.class);
        Artefato artefato1 = new Artefato();
        artefato1.setId(1L);
        Artefato artefato2 = new Artefato();
        artefato2.setId(artefato1.getId());
        assertThat(artefato1).isEqualTo(artefato2);
        artefato2.setId(2L);
        assertThat(artefato1).isNotEqualTo(artefato2);
        artefato1.setId(null);
        assertThat(artefato1).isNotEqualTo(artefato2);
    }
}
