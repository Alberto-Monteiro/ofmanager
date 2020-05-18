package br.com.rocksti.ofmanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import br.com.rocksti.ofmanager.domain.enumeration.EstadoOrdemDeFornecimento;

/**
 * A OrdemDeFornecimento.
 */
@Entity
@Table(name = "ordem_de_fornecimento")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class OrdemDeFornecimento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero")
    private Integer numero;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoOrdemDeFornecimento estado;

    @Lob
    @Column(name = "observacao_do_gestor")
    private String observacaoDoGestor;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @Column(name = "valor_ustibb", precision = 21, scale = 2)
    private BigDecimal valorUstibb;

    @OneToMany(mappedBy = "ordemDeFornecimento")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ArtefatoOrdemDeFornecimento> artefatoOrdemDeFornecimentos = new HashSet<>();

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

    public Set<ArtefatoOrdemDeFornecimento> getArtefatoOrdemDeFornecimentos() {
        return artefatoOrdemDeFornecimentos;
    }

    public OrdemDeFornecimento artefatoOrdemDeFornecimentos(Set<ArtefatoOrdemDeFornecimento> artefatoOrdemDeFornecimentos) {
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

    public void setArtefatoOrdemDeFornecimentos(Set<ArtefatoOrdemDeFornecimento> artefatoOrdemDeFornecimentos) {
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
