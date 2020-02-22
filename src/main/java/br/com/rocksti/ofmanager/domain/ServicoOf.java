package br.com.rocksti.ofmanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import br.com.rocksti.ofmanager.domain.enumeration.EstadoOf;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * A ServicoOf.
 */
@Entity
@Table(name = "servico_of")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@EntityListeners(AuditingEntityListener.class)
public class ServicoOf implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "numero", nullable = false)
    private Integer numero;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoOf estado;

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

    @OneToMany(mappedBy = "servicoOf", cascade = {CascadeType.ALL}, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ArquivoDaOf> arquivoDaOfs = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("servicoOfs")
    private User gestorDaOf;

    @ManyToOne
    @JsonIgnoreProperties("servicoOfs")
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

    public ServicoOf numero(Integer numero) {
        this.numero = numero;
        return this;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public EstadoOf getEstado() {
        return estado;
    }

    public ServicoOf estado(EstadoOf estado) {
        this.estado = estado;
        return this;
    }

    public void setEstado(EstadoOf estado) {
        this.estado = estado;
    }

    public String getObservacaoDoGestor() {
        return observacaoDoGestor;
    }

    public ServicoOf observacaoDoGestor(String observacaoDoGestor) {
        this.observacaoDoGestor = observacaoDoGestor;
        return this;
    }

    public void setObservacaoDoGestor(String observacaoDoGestor) {
        this.observacaoDoGestor = observacaoDoGestor;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public ServicoOf createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public ServicoOf createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public ServicoOf lastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public ServicoOf lastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<ArquivoDaOf> getArquivoDaOfs() {
        return arquivoDaOfs;
    }

    public ServicoOf arquivoDaOfs(Set<ArquivoDaOf> arquivoDaOfs) {
        this.arquivoDaOfs = arquivoDaOfs;
        return this;
    }

    public ServicoOf addArquivoDaOf(ArquivoDaOf arquivoDaOf) {
        this.arquivoDaOfs.add(arquivoDaOf);
        arquivoDaOf.setServicoOf(this);
        return this;
    }

    public ServicoOf removeArquivoDaOf(ArquivoDaOf arquivoDaOf) {
        this.arquivoDaOfs.remove(arquivoDaOf);
        arquivoDaOf.setServicoOf(null);
        return this;
    }

    public void setArquivoDaOfs(Set<ArquivoDaOf> arquivoDaOfs) {
        this.arquivoDaOfs = arquivoDaOfs;
    }

    public User getGestorDaOf() {
        return gestorDaOf;
    }

    public ServicoOf gestorDaOf(User user) {
        this.gestorDaOf = user;
        return this;
    }

    public void setGestorDaOf(User user) {
        this.gestorDaOf = user;
    }

    public User getDonoDaOf() {
        return donoDaOf;
    }

    public ServicoOf donoDaOf(User user) {
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
        if (!(o instanceof ServicoOf)) {
            return false;
        }
        return id != null && id.equals(((ServicoOf) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ServicoOf{" +
            "id=" + getId() +
            ", numero=" + getNumero() +
            ", estado='" + getEstado() + "'" +
            ", observacaoDoGestor='" + getObservacaoDoGestor() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
