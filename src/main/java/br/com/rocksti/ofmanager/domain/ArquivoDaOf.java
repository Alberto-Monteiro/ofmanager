package br.com.rocksti.ofmanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

import br.com.rocksti.ofmanager.domain.enumeration.EstadoArquivo;

/**
 * A ArquivoDaOf.
 */
@Entity
@Table(name = "arquivo_da_of")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ArquivoDaOf implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_arquivo")
    private EstadoArquivo estadoArquivo;

    @ManyToOne
    @JsonIgnoreProperties("arquivoDaOfs")
    private ServicoOf servicoOf;

    @ManyToOne
    @JsonIgnoreProperties("arquivoDaOfs")
    private Arquivo arquivo;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EstadoArquivo getEstadoArquivo() {
        return estadoArquivo;
    }

    public ArquivoDaOf estadoArquivo(EstadoArquivo estadoArquivo) {
        this.estadoArquivo = estadoArquivo;
        return this;
    }

    public void setEstadoArquivo(EstadoArquivo estadoArquivo) {
        this.estadoArquivo = estadoArquivo;
    }

    public ServicoOf getServicoOf() {
        return servicoOf;
    }

    public ArquivoDaOf servicoOf(ServicoOf servicoOf) {
        this.servicoOf = servicoOf;
        return this;
    }

    public void setServicoOf(ServicoOf servicoOf) {
        this.servicoOf = servicoOf;
    }

    public Arquivo getArquivo() {
        return arquivo;
    }

    public ArquivoDaOf arquivo(Arquivo arquivo) {
        this.arquivo = arquivo;
        return this;
    }

    public void setArquivo(Arquivo arquivo) {
        this.arquivo = arquivo;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArquivoDaOf)) {
            return false;
        }
        return id != null && id.equals(((ArquivoDaOf) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ArquivoDaOf{" +
            "id=" + getId() +
            ", estadoArquivo='" + getEstadoArquivo() + "'" +
            "}";
    }
}
