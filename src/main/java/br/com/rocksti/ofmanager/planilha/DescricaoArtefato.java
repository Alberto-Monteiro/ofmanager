package br.com.rocksti.ofmanager.planilha;

import br.com.rocksti.ofmanager.domain.enumeration.EstadoArquivo;

public enum DescricaoArtefato {
    CRIAR_JAVA("Criação de objetos de Integração e Negócio Java"),
    ALTERAR_JAVA("Alteração de Objetos de Integração e Negócio Java "),
    ALTERAR_JAVA2("Alteração de pacote de Objetos de Integração e Negócio Java "),
    CRIAR_HTML("Criação de tela HTML ou XHTML ou JSP ou XML ou VTL ou XSL ou Swing ou AWT ou XUI ou PHP"),
    ALTERAR_HTML("Alteração de tela HTML ou XHTML ou JSP ou XML ou VTL ou XSL ou Swing ou AWT ou XUI ou PHP"),
    CRIAR_CSS("Criação CSS ou SCSS "),
    ALTERAR_CSS("Alteração CSS ou SCSS "),
    CRIAR_JS("Criação JavaScript "),
    ALTERAR_JS("Alteração JavaScript "),
    CRIAR_XML("Criação de arquivo chave/valor ou tipo xml "),
    ALTERAR_XML("Alteração de arquivo chave/valor ou tipo xml "),
    CRIAR_C("Criação de objetos de Integração e Negócio C, C# e C++"),
    ALTERAR_C("Alteração de Objetos de Integração e Negócio C, C# e C++"),
    ALTERAR_C2("Alteração de pacote de Objetos de Integração e Negócio C, C# e C++"),
    CRIAR_NET("Criação de objetos de Integração e Negócio .Net "),
    ALTERAR_NET("Alteração de Objetos de Integração e Negócio .Net "),
    ALTERAR_NET2("Alteração de pacote de Objetos de Integração e Negócio .Net "),
    CRIAR_TEST("Criação de objeto de teste automatizado");

    private String descricao;

    DescricaoArtefato(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static String get(Boolean arquivoDeTest, String extensao, EstadoArquivo estadoArquivo) {
        if (arquivoDeTest != null && arquivoDeTest) {
            return CRIAR_TEST.getDescricao();
        }

        if (extensao == null || estadoArquivo == null) {
            return null;
        }

        switch (extensao.toLowerCase()) {
            case "java":
                return EstadoArquivo.CRIANDO.equals(estadoArquivo) ? CRIAR_JAVA.getDescricao() : ALTERAR_JAVA.getDescricao();
            case "html":
                return EstadoArquivo.CRIANDO.equals(estadoArquivo) ? CRIAR_HTML.getDescricao() : ALTERAR_HTML.getDescricao();
            case "ts":
                return EstadoArquivo.CRIANDO.equals(estadoArquivo) ? CRIAR_JS.getDescricao() : ALTERAR_JS.getDescricao();
            case "js":
                return EstadoArquivo.CRIANDO.equals(estadoArquivo) ? CRIAR_JS.getDescricao() : ALTERAR_JS.getDescricao();
            case "css":
                return EstadoArquivo.CRIANDO.equals(estadoArquivo) ? CRIAR_CSS.getDescricao() : ALTERAR_CSS.getDescricao();
            case "scss":
                return EstadoArquivo.CRIANDO.equals(estadoArquivo) ? CRIAR_CSS.getDescricao() : ALTERAR_CSS.getDescricao();
            case "json":
                return EstadoArquivo.CRIANDO.equals(estadoArquivo) ? CRIAR_XML.getDescricao() : ALTERAR_XML.getDescricao();
            case "xml":
                return EstadoArquivo.CRIANDO.equals(estadoArquivo) ? CRIAR_XML.getDescricao() : ALTERAR_XML.getDescricao();
            case "yml":
                return EstadoArquivo.CRIANDO.equals(estadoArquivo) ? CRIAR_XML.getDescricao() : ALTERAR_XML.getDescricao();
            case "sql":
                return EstadoArquivo.CRIANDO.equals(estadoArquivo) ? CRIAR_XML.getDescricao() : ALTERAR_XML.getDescricao();
        }

        return null;
    }
}
