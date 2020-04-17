package br.com.rocksti.ofmanager.domain.enumeration;

/**
 * The ComplexidadeArtefato enumeration.
 */
public enum ComplexidadeArtefato {
    MUITO_BAIXA("Muito Baixa"),
    BAIXA("Baixa"),
    MEDIA("Média"),
    ALTA("Alta"),
    MUITO_ALTA("Muito Alta");

    private final String descricao;

    ComplexidadeArtefato(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
