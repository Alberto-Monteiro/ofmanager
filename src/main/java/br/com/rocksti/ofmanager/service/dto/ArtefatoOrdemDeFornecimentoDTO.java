package br.com.rocksti.ofmanager.service.dto;

import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;
import br.com.rocksti.ofmanager.domain.enumeration.EstadoArtefato;

/**
 * A DTO for the {@link br.com.rocksti.ofmanager.domain.ArtefatoOrdemDeFornecimento} entity.
 */
public class ArtefatoOrdemDeFornecimentoDTO implements Serializable {
    
    private Long id;

    private EstadoArtefato estado;

    private Instant createdDate;


    private Long artefatoId;

    private Long ordemDeFornecimentoId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EstadoArtefato getEstado() {
        return estado;
    }

    public void setEstado(EstadoArtefato estado) {
        this.estado = estado;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Long getArtefatoId() {
        return artefatoId;
    }

    public void setArtefatoId(Long artefatoId) {
        this.artefatoId = artefatoId;
    }

    public Long getOrdemDeFornecimentoId() {
        return ordemDeFornecimentoId;
    }

    public void setOrdemDeFornecimentoId(Long ordemDeFornecimentoId) {
        this.ordemDeFornecimentoId = ordemDeFornecimentoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ArtefatoOrdemDeFornecimentoDTO artefatoOrdemDeFornecimentoDTO = (ArtefatoOrdemDeFornecimentoDTO) o;
        if (artefatoOrdemDeFornecimentoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), artefatoOrdemDeFornecimentoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ArtefatoOrdemDeFornecimentoDTO{" +
            "id=" + getId() +
            ", estado='" + getEstado() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", artefatoId=" + getArtefatoId() +
            ", ordemDeFornecimentoId=" + getOrdemDeFornecimentoId() +
            "}";
    }
}
