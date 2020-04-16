package br.com.rocksti.ofmanager.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import br.com.rocksti.ofmanager.domain.enumeration.ComplexidadeArtefato;

/**
 * A DTO for the {@link br.com.rocksti.ofmanager.domain.Artefato} entity.
 */
public class ArtefatoDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String localDoArtefato;

    @NotNull
    private String extensao;

    private ComplexidadeArtefato complexidade;

    private Boolean artefatoDeTest;

    private Instant createdDate;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocalDoArtefato() {
        return localDoArtefato;
    }

    public void setLocalDoArtefato(String localDoArtefato) {
        this.localDoArtefato = localDoArtefato;
    }

    public String getExtensao() {
        return extensao;
    }

    public void setExtensao(String extensao) {
        this.extensao = extensao;
    }

    public ComplexidadeArtefato getComplexidade() {
        return complexidade;
    }

    public void setComplexidade(ComplexidadeArtefato complexidade) {
        this.complexidade = complexidade;
    }

    public Boolean isArtefatoDeTest() {
        return artefatoDeTest;
    }

    public void setArtefatoDeTest(Boolean artefatoDeTest) {
        this.artefatoDeTest = artefatoDeTest;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ArtefatoDTO artefatoDTO = (ArtefatoDTO) o;
        if (artefatoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), artefatoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ArtefatoDTO{" +
            "id=" + getId() +
            ", localDoArtefato='" + getLocalDoArtefato() + "'" +
            ", extensao='" + getExtensao() + "'" +
            ", complexidade='" + getComplexidade() + "'" +
            ", artefatoDeTest='" + isArtefatoDeTest() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
