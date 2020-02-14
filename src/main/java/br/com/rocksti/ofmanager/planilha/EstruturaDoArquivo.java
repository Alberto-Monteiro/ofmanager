package br.com.rocksti.ofmanager.planilha;

import br.com.rocksti.ofmanager.domain.enumeration.Complexidade;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class EstruturaDoArquivo {

    private String disciplina = "IMPLEMENTAÇÃO DE SOFTWARE";
    private String atividade = "Plataforma Distribuída";
    private String descricaoArtefato = DescricaoArtefato.ALTERAR_JAVA.getDescricao();
    private String complexidade = Complexidade.BAIXA.getDescricao();
    private String componenteItem = "N/A";
    private List<String> nomeDoArtefato = new ArrayList<>();

    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    public String getAtividade() {
        return atividade;
    }

    public void setAtividade(String atividade) {
        this.atividade = atividade;
    }

    public String getDescricaoArtefato() {
        return descricaoArtefato;
    }

    public void setDescricaoArtefato(String descricaoArtefato) {
        this.descricaoArtefato = descricaoArtefato;
    }

    public String getComplexidade() {
        return complexidade;
    }

    public void setComplexidade(String complexidade) {
        this.complexidade = complexidade;
    }

    public String getComponenteItem() {
        return componenteItem;
    }

    public void setComponenteItem(String componenteItem) {
        this.componenteItem = componenteItem;
    }

    public String getQtd() {
        return String.valueOf(nomeDoArtefato.size());
    }

    public List<String> getNomeDoArtefato() {
        return nomeDoArtefato;
    }

    public void setNomeDoArtefato(List<String> nomeDoArtefato) {
        this.nomeDoArtefato = nomeDoArtefato;
    }

    public String getStringNomeDoArtefato() {
        StringJoiner stringJoiner = new StringJoiner("\n");
        nomeDoArtefato.forEach(stringJoiner::add);
        return stringJoiner.toString();
    }

    public void addNomeDoArtefato(String nomeDoArtefato) {
        this.nomeDoArtefato.add(nomeDoArtefato);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EstruturaDoArquivo)) return false;
        EstruturaDoArquivo that = (EstruturaDoArquivo) o;
        return com.google.common.base.Objects.equal(descricaoArtefato, that.descricaoArtefato) &&
            com.google.common.base.Objects.equal(complexidade, that.complexidade);
    }

    @Override
    public int hashCode() {
        return com.google.common.base.Objects.hashCode(descricaoArtefato, complexidade);
    }
}
