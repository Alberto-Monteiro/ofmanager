package br.com.rocksti.ofmanager.service.dto;

import java.io.Serializable;
import java.util.Objects;
import br.com.rocksti.ofmanager.domain.enumeration.EstadoArquivo;

/**
 * A DTO for the {@link br.com.rocksti.ofmanager.domain.ArquivoDaOf} entity.
 */
public class ArquivoDaOfDTO implements Serializable {

    private Long id;

    private EstadoArquivo estadoArquivo;


    private Long servicoOfId;

    private Long arquivoId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EstadoArquivo getEstadoArquivo() {
        return estadoArquivo;
    }

    public void setEstadoArquivo(EstadoArquivo estadoArquivo) {
        this.estadoArquivo = estadoArquivo;
    }

    public Long getServicoOfId() {
        return servicoOfId;
    }

    public void setServicoOfId(Long servicoOfId) {
        this.servicoOfId = servicoOfId;
    }

    public Long getArquivoId() {
        return arquivoId;
    }

    public void setArquivoId(Long arquivoId) {
        this.arquivoId = arquivoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ArquivoDaOfDTO arquivoDaOfDTO = (ArquivoDaOfDTO) o;
        if (arquivoDaOfDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), arquivoDaOfDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ArquivoDaOfDTO{" +
            "id=" + getId() +
            ", estadoArquivo='" + getEstadoArquivo() + "'" +
            ", servicoOfId=" + getServicoOfId() +
            ", arquivoId=" + getArquivoId() +
            "}";
    }
}
