package br.com.rocksti.ofmanager.domain;

import br.com.rocksti.ofmanager.domain.enumeration.EstadoOrdemDeFornecimento;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * A OrdemDeFornecimento.
 */
@Entity
@Table(name = "ordem_de_fornecimento")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@EntityListeners(AuditingEntityListener.class)
public class OrdemDeFornecimento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "numero", nullable = false)
    private Integer numero;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoOrdemDeFornecimento estado;

    @Lob
    @Column(name = "observacao_do_gestor")
    private String observacaoDoGestor;

    @CreatedBy
    @Column(name = "created_by", length = 50, updatable = false)
    private String createdBy;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private Instant createdDate = Instant.now();

    @LastModifiedBy
    @Column(name = "last_modified_by", length = 50)
    private String lastModifiedBy;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Instant lastModifiedDate = Instant.now();

    @Column(name = "valor_ustibb", precision = 21, scale = 2)
    private BigDecimal valorUstibb;

    @OneToMany(mappedBy = "ordemDeFornecimento")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<ArtefatoOrdemDeFornecimento> artefatoOrdemDeFornecimentos = new ArrayList<>();

    @ManyToOne
    @JsonIgnoreProperties("ordemDeFornecimentos")
    private User gestorDaOf;

    @ManyToOne
    @JsonIgnoreProperties("ordemDeFornecimentos")
    private User donoDaOf;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumero() {
        return numero;
    }

    public OrdemDeFornecimento numero(Integer numero) {
        this.numero = numero;
        return this;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public EstadoOrdemDeFornecimento getEstado() {
        return estado;
    }

    public OrdemDeFornecimento estado(EstadoOrdemDeFornecimento estado) {
        this.estado = estado;
        return this;
    }

    public void setEstado(EstadoOrdemDeFornecimento estado) {
        this.estado = estado;
    }

    public String getObservacaoDoGestor() {
        return observacaoDoGestor;
    }

    public OrdemDeFornecimento observacaoDoGestor(String observacaoDoGestor) {
        this.observacaoDoGestor = observacaoDoGestor;
        return this;
    }

    public void setObservacaoDoGestor(String observacaoDoGestor) {
        this.observacaoDoGestor = observacaoDoGestor;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public OrdemDeFornecimento createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public OrdemDeFornecimento createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public OrdemDeFornecimento lastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public OrdemDeFornecimento lastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public BigDecimal getValorUstibb() {
        return valorUstibb;
    }

    public OrdemDeFornecimento valorUstibb(BigDecimal valorUstibb) {
        this.valorUstibb = valorUstibb;
        return this;
    }

    public void setValorUstibb(BigDecimal valorUstibb) {
        this.valorUstibb = valorUstibb;
    }

    public List<ArtefatoOrdemDeFornecimento> getArtefatoOrdemDeFornecimentos() {
        return artefatoOrdemDeFornecimentos;
    }

    public OrdemDeFornecimento artefatoOrdemDeFornecimentos(List<ArtefatoOrdemDeFornecimento> artefatoOrdemDeFornecimentos) {
        this.artefatoOrdemDeFornecimentos = artefatoOrdemDeFornecimentos;
        return this;
    }

    public OrdemDeFornecimento addArtefatoOrdemDeFornecimento(ArtefatoOrdemDeFornecimento artefatoOrdemDeFornecimento) {
        this.artefatoOrdemDeFornecimentos.add(artefatoOrdemDeFornecimento);
        artefatoOrdemDeFornecimento.setOrdemDeFornecimento(this);
        return this;
    }

    public OrdemDeFornecimento removeArtefatoOrdemDeFornecimento(ArtefatoOrdemDeFornecimento artefatoOrdemDeFornecimento) {
        this.artefatoOrdemDeFornecimentos.remove(artefatoOrdemDeFornecimento);
        artefatoOrdemDeFornecimento.setOrdemDeFornecimento(null);
        return this;
    }

    public void setArtefatoOrdemDeFornecimentos(List<ArtefatoOrdemDeFornecimento> artefatoOrdemDeFornecimentos) {
        this.artefatoOrdemDeFornecimentos = artefatoOrdemDeFornecimentos;
    }

    public User getGestorDaOf() {
        return gestorDaOf;
    }

    public OrdemDeFornecimento gestorDaOf(User user) {
        this.gestorDaOf = user;
        return this;
    }

    public void setGestorDaOf(User user) {
        this.gestorDaOf = user;
    }

    public User getDonoDaOf() {
        return donoDaOf;
    }

    public OrdemDeFornecimento donoDaOf(User user) {
        this.donoDaOf = user;
        return this;
    }

    public void setDonoDaOf(User user) {
        this.donoDaOf = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrdemDeFornecimento)) {
            return false;
        }
        return id != null && id.equals(((OrdemDeFornecimento) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "OrdemDeFornecimento{" +
            "id=" + getId() +
            ", numero=" + getNumero() +
            ", estado='" + getEstado() + "'" +
            ", observacaoDoGestor='" + getObservacaoDoGestor() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", valorUstibb=" + getValorUstibb() +
            "}";
    }
}
