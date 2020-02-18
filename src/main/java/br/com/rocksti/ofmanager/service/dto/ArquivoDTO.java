package br.com.rocksti.ofmanager.service.dto;

import br.com.rocksti.ofmanager.domain.enumeration.Complexidade;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.rocksti.ofmanager.domain.Arquivo} entity.
 */
public class ArquivoDTO implements Serializable {

    private Long id;

    @NotNull
    private String caminhoDoArquivo;

    @NotNull
    private String extensao;

    private Complexidade complexidade;

    private Boolean arquivoDeTest;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCaminhoDoArquivo() {
        return caminhoDoArquivo;
    }

    public void setCaminhoDoArquivo(String caminhoDoArquivo) {
        this.caminhoDoArquivo = caminhoDoArquivo;
    }

    public String getExtensao() {
        return extensao;
    }

    public void setExtensao(String extensao) {
        this.extensao = extensao;
    }

    public Complexidade getComplexidade() {
        return complexidade;
    }

    public void setComplexidade(Complexidade complexidade) {
        this.complexidade = complexidade;
    }

    public Boolean getArquivoDeTest() {
        return arquivoDeTest;
    }

    public void setArquivoDeTest(Boolean arquivoDeTest) {
        this.arquivoDeTest = arquivoDeTest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ArquivoDTO arquivoDTO = (ArquivoDTO) o;
        if (arquivoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), arquivoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ArquivoDTO{" +
            "id=" + getId() +
            ", caminhoDoArquivo='" + getCaminhoDoArquivo() + "'" +
            ", extensao='" + getExtensao() + "'" +
            ", complexidade='" + getComplexidade() + "'" +
            "}";
    }
}
