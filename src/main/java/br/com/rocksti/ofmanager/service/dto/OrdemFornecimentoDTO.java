package br.com.rocksti.ofmanager.service.dto;

import br.com.rocksti.ofmanager.domain.ArtefatoOrdemDeFornecimento;
import br.com.rocksti.ofmanager.domain.OrdemDeFornecimento;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrdemFornecimentoDTO implements Serializable {

    @NotNull
    private String listaDosArquivos;

    private OrdemDeFornecimento ordemDeFornecimento;

    public String getListaDosArquivos() {
        return listaDosArquivos;
    }

    public void setListaDosArquivos(String listaDosArquivos) {
        this.listaDosArquivos = listaDosArquivos;
    }

    public OrdemDeFornecimento getOrdemDeFornecimento() {
        return ordemDeFornecimento;
    }

    public void setOrdemDeFornecimento(OrdemDeFornecimento servicoOf) {
        this.ordemDeFornecimento = servicoOf;
    }

    public Map<String, List<ArtefatoOrdemDeFornecimento>> getMapArtefatoOrdemDeFornecimento() {
        Map<String, List<ArtefatoOrdemDeFornecimento>> map = new HashMap<>();

        ordemDeFornecimento.getArtefatoOrdemDeFornecimentos().forEach(arquivoDaOf -> map.put(arquivoDaOf.getArtefato().getExtensao(), new ArrayList<>()));
        ordemDeFornecimento.getArtefatoOrdemDeFornecimentos().forEach(arquivoDaOf -> map.get(arquivoDaOf.getArtefato().getExtensao()).add(arquivoDaOf));

        return map;
    }
}
