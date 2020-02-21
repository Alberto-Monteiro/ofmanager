package br.com.rocksti.ofmanager.service.dto;

import br.com.rocksti.ofmanager.domain.ArquivoDaOf;
import br.com.rocksti.ofmanager.domain.ServicoOf;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrdemFornecimentoDTO implements Serializable {

    @NotNull
    private String listaDosArquivos;

    private ServicoOf servicoOf;

    public String getListaDosArquivos() {
        return listaDosArquivos;
    }

    public void setListaDosArquivos(String listaDosArquivos) {
        this.listaDosArquivos = listaDosArquivos;
    }

    public ServicoOf getServicoOf() {
        return servicoOf;
    }

    public void setServicoOf(ServicoOf servicoOf) {
        this.servicoOf = servicoOf;
    }

    public Map<String, List<ArquivoDaOf>> getMapArquivoDaOf() {
        Map<String, List<ArquivoDaOf>> map = new HashMap<>();

        servicoOf.getArquivoDaOfs().forEach(arquivoDaOf -> map.put(arquivoDaOf.getArquivo().getExtensao(), new ArrayList<>()));
        servicoOf.getArquivoDaOfs().forEach(arquivoDaOf -> map.get(arquivoDaOf.getArquivo().getExtensao()).add(arquivoDaOf));

        return map;
    }
}
