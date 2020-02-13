package br.com.rocksti.ofmanager.domain.enumeration;

/**
 * The Complexidade enumeration.
 */
public enum Complexidade {
    MUITO_BAIXA("Muito Baixa"),
    BAIXA("Baixa"),
    MEDIA("Média"),
    ALTA("Alta"),
    MUITO_ALTA("Muito Alta");

    private String descricao;

    Complexidade(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
