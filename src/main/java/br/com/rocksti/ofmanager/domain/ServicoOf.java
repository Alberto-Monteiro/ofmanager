package br.com.rocksti.ofmanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A ServicoOf.
 */
@Entity
@Table(name = "servico_of")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ServicoOf implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "userid", nullable = false)
    private Long userid;

    @NotNull
    @Column(name = "numero", nullable = false)
    private Integer numero;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private Instant createdDate = Instant.now();

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

    public Long getUserid() {
        return userid;
    }

    public ServicoOf userid(Long userid) {
        this.userid = userid;
        return this;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
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
            ", userid=" + getUserid() +
            ", numero=" + getNumero() +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
