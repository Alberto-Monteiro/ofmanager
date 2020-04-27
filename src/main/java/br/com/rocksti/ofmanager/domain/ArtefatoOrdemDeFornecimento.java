package br.com.rocksti.ofmanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.Instant;

import br.com.rocksti.ofmanager.domain.enumeration.EstadoArtefato;
import org.springframework.data.annotation.CreatedDate;

/**
 * A ArtefatoOrdemDeFornecimento.
 */
@Entity
@Table(name = "artefato_ordem_de_fornecimento")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ArtefatoOrdemDeFornecimento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoArtefato estado;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private Instant createdDate = Instant.now();

    @ManyToOne
    @JsonIgnoreProperties("artefatoOrdemDeFornecimentos")
    private Artefato artefato;

    @ManyToOne
    @JsonIgnoreProperties("artefatoOrdemDeFornecimentos")
    private OrdemDeFornecimento ordemDeFornecimento;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EstadoArtefato getEstado() {
        return estado;
    }

    public ArtefatoOrdemDeFornecimento estado(EstadoArtefato estado) {
        this.estado = estado;
        return this;
    }

    public void setEstado(EstadoArtefato estado) {
        this.estado = estado;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public ArtefatoOrdemDeFornecimento createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Artefato getArtefato() {
        return artefato;
    }

    public ArtefatoOrdemDeFornecimento artefato(Artefato artefato) {
        this.artefato = artefato;
        return this;
    }

    public void setArtefato(Artefato artefato) {
        this.artefato = artefato;
    }

    public OrdemDeFornecimento getOrdemDeFornecimento() {
        return ordemDeFornecimento;
    }

    public ArtefatoOrdemDeFornecimento ordemDeFornecimento(OrdemDeFornecimento ordemDeFornecimento) {
        this.ordemDeFornecimento = ordemDeFornecimento;
        return this;
    }

    public void setOrdemDeFornecimento(OrdemDeFornecimento ordemDeFornecimento) {
        this.ordemDeFornecimento = ordemDeFornecimento;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArtefatoOrdemDeFornecimento)) {
            return false;
        }
        return id != null && id.equals(((ArtefatoOrdemDeFornecimento) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ArtefatoOrdemDeFornecimento{" +
            "id=" + getId() +
            ", estado='" + getEstado() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
