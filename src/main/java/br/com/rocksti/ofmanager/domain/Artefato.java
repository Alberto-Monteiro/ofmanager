package br.com.rocksti.ofmanager.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.Instant;

import br.com.rocksti.ofmanager.domain.enumeration.ComplexidadeArtefato;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * A Artefato.
 */
@Entity
@Table(name = "artefato")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@EntityListeners(AuditingEntityListener.class)
public class Artefato implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "local_do_artefato", nullable = false)
    private String localDoArtefato;

    @NotNull
    @Column(name = "extensao", nullable = false)
    private String extensao;

    @Enumerated(EnumType.STRING)
    @Column(name = "complexidade")
    private ComplexidadeArtefato complexidade;

    @Column(name = "artefato_de_test")
    private Boolean artefatoDeTest;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private Instant createdDate = Instant.now();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocalDoArtefato() {
        return localDoArtefato;
    }

    public Artefato localDoArtefato(String localDoArtefato) {
        this.localDoArtefato = localDoArtefato;
        return this;
    }

    public void setLocalDoArtefato(String localDoArtefato) {
        this.localDoArtefato = localDoArtefato;
    }

    public String getExtensao() {
        return extensao;
    }

    public Artefato extensao(String extensao) {
        this.extensao = extensao;
        return this;
    }

    public void setExtensao(String extensao) {
        this.extensao = extensao;
    }

    public ComplexidadeArtefato getComplexidade() {
        return complexidade;
    }

    public Artefato complexidade(ComplexidadeArtefato complexidade) {
        this.complexidade = complexidade;
        return this;
    }

    public void setComplexidade(ComplexidadeArtefato complexidade) {
        this.complexidade = complexidade;
    }

    public Boolean isArtefatoDeTest() {
        return artefatoDeTest;
    }

    public Artefato artefatoDeTest(Boolean artefatoDeTest) {
        this.artefatoDeTest = artefatoDeTest;
        return this;
    }

    public void setArtefatoDeTest(Boolean artefatoDeTest) {
        this.artefatoDeTest = artefatoDeTest;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public Artefato createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Artefato)) {
            return false;
        }
        return id != null && id.equals(((Artefato) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Artefato{" +
            "id=" + getId() +
            ", localDoArtefato='" + getLocalDoArtefato() + "'" +
            ", extensao='" + getExtensao() + "'" +
            ", complexidade='" + getComplexidade() + "'" +
            ", artefatoDeTest='" + isArtefatoDeTest() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
