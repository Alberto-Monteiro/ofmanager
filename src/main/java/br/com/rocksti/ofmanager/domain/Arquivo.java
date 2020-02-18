package br.com.rocksti.ofmanager.domain;

import br.com.rocksti.ofmanager.domain.enumeration.Complexidade;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Arquivo.
 */
@Entity
@Table(name = "arquivo")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Arquivo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "caminho_do_arquivo", nullable = false)
    private String caminhoDoArquivo;

    @NotNull
    @Column(name = "extensao", nullable = false)
    private String extensao;

    @Enumerated(EnumType.STRING)
    @Column(name = "complexidade")
    private Complexidade complexidade;

    @OneToMany(mappedBy = "arquivo")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ArquivoDaOf> arquivoDaOfs = new HashSet<>();

    @Column(name = "arquivo_de_test")
    private Boolean arquivoDeTest;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCaminhoDoArquivo() {
        return caminhoDoArquivo;
    }

    public Arquivo caminhoDoArquivo(String caminhoDoArquivo) {
        this.caminhoDoArquivo = caminhoDoArquivo;
        return this;
    }

    public void setCaminhoDoArquivo(String caminhoDoArquivo) {
        this.caminhoDoArquivo = caminhoDoArquivo;
    }

    public String getExtensao() {
        return extensao;
    }

    public Arquivo extensao(String extensao) {
        this.extensao = extensao;
        return this;
    }

    public void setExtensao(String extensao) {
        this.extensao = extensao;
    }

    public Complexidade getComplexidade() {
        return complexidade;
    }

    public Arquivo complexidade(Complexidade complexidade) {
        this.complexidade = complexidade;
        return this;
    }

    public void setComplexidade(Complexidade complexidade) {
        this.complexidade = complexidade;
    }

    public Set<ArquivoDaOf> getArquivoDaOfs() {
        return arquivoDaOfs;
    }

    public Arquivo arquivoDaOfs(Set<ArquivoDaOf> arquivoDaOfs) {
        this.arquivoDaOfs = arquivoDaOfs;
        return this;
    }

    public Arquivo addArquivoDaOf(ArquivoDaOf arquivoDaOf) {
        this.arquivoDaOfs.add(arquivoDaOf);
        arquivoDaOf.setArquivo(this);
        return this;
    }

    public Arquivo removeArquivoDaOf(ArquivoDaOf arquivoDaOf) {
        this.arquivoDaOfs.remove(arquivoDaOf);
        arquivoDaOf.setArquivo(null);
        return this;
    }

    public void setArquivoDaOfs(Set<ArquivoDaOf> arquivoDaOfs) {
        this.arquivoDaOfs = arquivoDaOfs;
    }

    public Boolean getArquivoDeTest() {
        return arquivoDeTest;
    }

    public void setArquivoDeTest(Boolean arquivoDeTest) {
        this.arquivoDeTest = arquivoDeTest;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Arquivo)) {
            return false;
        }
        return id != null && id.equals(((Arquivo) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Arquivo{" +
            "id=" + getId() +
            ", caminhoDoArquivo='" + getCaminhoDoArquivo() + "'" +
            ", extensao='" + getExtensao() + "'" +
            ", complexidade='" + getComplexidade() + "'" +
            "}";
    }
}
