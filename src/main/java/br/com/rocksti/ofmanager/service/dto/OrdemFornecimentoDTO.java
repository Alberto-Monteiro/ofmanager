package br.com.rocksti.ofmanager.service.dto;

import br.com.rocksti.ofmanager.domain.ArquivoDaOf;
import com.google.common.base.Objects;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;

public class OrdemFornecimentoDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer numero;
    @NotNull
    private String listaDosArquivos;

    private Set<ArquivoDaOf> arquivoDaOfs;

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

    public String getListaDosArquivos() {
        return listaDosArquivos;
    }

    public void setListaDosArquivos(String listaDosArquivos) {
        this.listaDosArquivos = listaDosArquivos;
    }

    public Set<ArquivoDaOf> getArquivoDaOfs() {
        return arquivoDaOfs;
    }

    public void setArquivoDaOfs(Set<ArquivoDaOf> arquivoDaOfs) {
        this.arquivoDaOfs = arquivoDaOfs;
    }

    public Map<String, List<ArquivoDaOf>> getMapArquivoDaOf() {
        Map<String, List<ArquivoDaOf>> map = new HashMap<>();

        arquivoDaOfs.forEach(arquivoDaOf -> map.put(arquivoDaOf.getArquivo().getExtensao(), new ArrayList<>()));
        arquivoDaOfs.forEach(arquivoDaOf -> map.get(arquivoDaOf.getArquivo().getExtensao()).add(arquivoDaOf));

        return map;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrdemFornecimentoDTO)) return false;
        OrdemFornecimentoDTO that = (OrdemFornecimentoDTO) o;
        return Objects.equal(id, that.id) &&
            Objects.equal(numero, that.numero);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, numero);
    }

    @Override
    public String toString() {
        return "OrdemFornecimentoDTO{" +
            "id=" + id +
            ", numero=" + numero +
            ", listaDosArquivos='" + listaDosArquivos + '\'' +
            '}';
    }
}
