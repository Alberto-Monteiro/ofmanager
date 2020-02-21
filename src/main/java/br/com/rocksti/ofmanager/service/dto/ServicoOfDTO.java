package br.com.rocksti.ofmanager.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.rocksti.ofmanager.domain.ServicoOf} entity.
 */
public class ServicoOfDTO implements Serializable {

    private Long id;

    @NotNull
    private Long userid;

    @NotNull
    private Integer numero;

    private Instant createdDate;


    private Long gestorDaOfId;

    private Long donoDaOfId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Long getGestorDaOfId() {
        return gestorDaOfId;
    }

    public void setGestorDaOfId(Long userId) {
        this.gestorDaOfId = userId;
    }

    public Long getDonoDaOfId() {
        return donoDaOfId;
    }

    public void setDonoDaOfId(Long userId) {
        this.donoDaOfId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ServicoOfDTO servicoOfDTO = (ServicoOfDTO) o;
        if (servicoOfDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), servicoOfDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ServicoOfDTO{" +
            "id=" + getId() +
            ", userid=" + getUserid() +
            ", numero=" + getNumero() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", gestorDaOfId=" + getGestorDaOfId() +
            ", donoDaOfId=" + getDonoDaOfId() +
            "}";
    }
}
