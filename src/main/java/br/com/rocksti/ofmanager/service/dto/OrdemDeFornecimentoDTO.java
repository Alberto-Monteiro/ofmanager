package br.com.rocksti.ofmanager.service.dto;

import br.com.rocksti.ofmanager.domain.enumeration.EstadoOrdemDeFornecimento;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.rocksti.ofmanager.domain.OrdemDeFornecimento} entity.
 */
public class OrdemDeFornecimentoDTO implements Serializable {

    private Long id;

    private Integer numero;

    private EstadoOrdemDeFornecimento estado;

    @Lob
    private String observacaoDoGestor;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private BigDecimal valorUstibb;

    private Long gestorDaOfId;

    private String gestorDaOfLogin;

    private Long donoDaOfId;

    private String donoDaOfLogin;

    private String donoDaOfFirstName;

    private String donoDaOfLastName;

    private String gestorDaOfFirstName;

    private String gestorDaOfLastName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public EstadoOrdemDeFornecimento getEstado() {
        return estado;
    }

    public void setEstado(EstadoOrdemDeFornecimento estado) {
        this.estado = estado;
    }

    public String getObservacaoDoGestor() {
        return observacaoDoGestor;
    }

    public void setObservacaoDoGestor(String observacaoDoGestor) {
        this.observacaoDoGestor = observacaoDoGestor;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public BigDecimal getValorUstibb() {
        return valorUstibb;
    }

    public void setValorUstibb(BigDecimal valorUstibb) {
        this.valorUstibb = valorUstibb;
    }

    public Long getGestorDaOfId() {
        return gestorDaOfId;
    }

    public void setGestorDaOfId(Long userId) {
        this.gestorDaOfId = userId;
    }

    public String getGestorDaOfLogin() {
        return gestorDaOfLogin;
    }

    public void setGestorDaOfLogin(String userLogin) {
        this.gestorDaOfLogin = userLogin;
    }

    public Long getDonoDaOfId() {
        return donoDaOfId;
    }

    public void setDonoDaOfId(Long userId) {
        this.donoDaOfId = userId;
    }

    public String getDonoDaOfLogin() {
        return donoDaOfLogin;
    }

    public void setDonoDaOfLogin(String userLogin) {
        this.donoDaOfLogin = userLogin;
    }

    public String getDonoDaOfFirstName() {
        return donoDaOfFirstName;
    }

    public void setDonoDaOfFirstName(String donoDaOfFirstName) {
        this.donoDaOfFirstName = donoDaOfFirstName;
    }

    public String getDonoDaOfLastName() {
        return donoDaOfLastName;
    }

    public void setDonoDaOfLastName(String donoDaOfLastName) {
        this.donoDaOfLastName = donoDaOfLastName;
    }

    public String getGestorDaOfFirstName() {
        return gestorDaOfFirstName;
    }

    public void setGestorDaOfFirstName(String gestorDaOfFirstName) {
        this.gestorDaOfFirstName = gestorDaOfFirstName;
    }

    public String getGestorDaOfLastName() {
        return gestorDaOfLastName;
    }

    public void setGestorDaOfLastName(String gestorDaOfLastName) {
        this.gestorDaOfLastName = gestorDaOfLastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrdemDeFornecimentoDTO ordemDeFornecimentoDTO = (OrdemDeFornecimentoDTO) o;
        if (ordemDeFornecimentoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ordemDeFornecimentoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrdemDeFornecimentoDTO{" +
            "id=" + getId() +
            ", numero=" + getNumero() +
            ", estado='" + getEstado() + "'" +
            ", observacaoDoGestor='" + getObservacaoDoGestor() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", valorUstibb=" + getValorUstibb() +
            ", gestorDaOfId=" + getGestorDaOfId() +
            ", gestorDaOfLogin='" + getGestorDaOfLogin() + "'" +
            ", donoDaOfId=" + getDonoDaOfId() +
            ", donoDaOfLogin='" + getDonoDaOfLogin() + "'" +
            ", donoDaOfFirstName='" + getDonoDaOfFirstName() + "'" +
            ", donoDaOfLastName='" + getDonoDaOfLastName() + "'" +
            ", gestorDaOfFirstName='" + getGestorDaOfFirstName() + "'" +
            ", gestorDaOfLastName='" + getGestorDaOfLastName() + "'" +
            "}";
    }
}
