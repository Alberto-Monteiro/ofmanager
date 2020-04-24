package br.com.rocksti.ofmanager.planilha;

import br.com.rocksti.ofmanager.domain.enumeration.EstadoArtefato;

public enum DescricaoArtefato {
    /* 5.10.1  */CRIAR_HTML("Criação de tela HTML ou XHTML ou JSP ou XML ou VTL ou XSL ou Swing ou AWT ou XUI ou PHP"),
    /* 5.10.2  */ALTERAR_HTML("Alteração de tela HTML ou XHTML ou JSP ou XML ou VTL ou XSL ou Swing ou AWT ou XUI ou PHP"),
    /* 5.10.3  */CRIAR_CSS("Criação CSS ou SCSS "),
    /* 5.10.4  */ALTERAR_CSS("Alteração CSS ou SCSS "),
    /* 5.10.5  */CRIAR_JS("Criação JavaScript "),
    /* 5.10.6  */ALTERAR_JS("Alteração JavaScript "),
    /* 5.10.7  */CRIAR_XML("Criação de arquivo chave/valor ou tipo xml "),
    /* 5.10.8  */ALTERAR_XML("Alteração de arquivo chave/valor ou tipo xml "),
    /* 5.10.9  */CRIAR_JAVA("Criação de objetos de Integração e Negócio Java"),
    /* 5.10.10 */ALTERAR_JAVA("Alteração de Objetos de Integração e Negócio Java "),
    /* 5.10.11 */ALTERAR_JAVA2("Alteração de pacote de Objetos de Integração e Negócio Java "),
    /* 5.10.12 */CRIAR_C("Criação de objetos de Integração e Negócio C, C# e C++"),
    /* 5.10.13 */ALTERAR_C("Alteração de Objetos de Integração e Negócio C, C# e C++"),
    /* 5.10.14 */ALTERAR_C2("Alteração de pacote de Objetos de Integração e Negócio C, C# e C++"),
    /* 5.10.15 */CRIAR_NET("Criação de objetos de Integração e Negócio .Net "),
    /* 5.10.16 */ALTERAR_NET("Alteração de Objetos de Integração e Negócio .Net "),
    /* 5.10.17 */ALTERAR_NET2("Alteração de pacote de Objetos de Integração e Negócio .Net "),
    /* 5.10.18 */CRIAR_TEST("Criação de objeto de teste automatizado"),
    /* 5.10.19 */CRIAR_VXML("Criação de Objeto Java Componente VXML"),
    /* 5.10.20 */ALTERAR_VXML("Alteração de Objeto Java Componente VXML");

    private String descricao;

    DescricaoArtefato(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static String get(Boolean arquivoDeTest, String extensao, EstadoArtefato estadoArquivo) {
        if (arquivoDeTest != null && arquivoDeTest) {
            return CRIAR_TEST.getDescricao();
        }

        if (extensao == null || estadoArquivo == null) {
            return null;
        }

        switch (extensao.toLowerCase()) {
            case "java":
                return EstadoArtefato.CRIANDO.equals(estadoArquivo) ? CRIAR_JAVA.getDescricao() : ALTERAR_JAVA.getDescricao();
            case "html":
                return EstadoArtefato.CRIANDO.equals(estadoArquivo) ? CRIAR_HTML.getDescricao() : ALTERAR_HTML.getDescricao();
            case "ts":
                return EstadoArtefato.CRIANDO.equals(estadoArquivo) ? CRIAR_JS.getDescricao() : ALTERAR_JS.getDescricao();
            case "js":
                return EstadoArtefato.CRIANDO.equals(estadoArquivo) ? CRIAR_JS.getDescricao() : ALTERAR_JS.getDescricao();
            case "css":
                return EstadoArtefato.CRIANDO.equals(estadoArquivo) ? CRIAR_CSS.getDescricao() : ALTERAR_CSS.getDescricao();
            case "scss":
                return EstadoArtefato.CRIANDO.equals(estadoArquivo) ? CRIAR_CSS.getDescricao() : ALTERAR_CSS.getDescricao();
            case "json":
                return EstadoArtefato.CRIANDO.equals(estadoArquivo) ? CRIAR_XML.getDescricao() : ALTERAR_XML.getDescricao();
            case "xml":
                return EstadoArtefato.CRIANDO.equals(estadoArquivo) ? CRIAR_XML.getDescricao() : ALTERAR_XML.getDescricao();
            case "yml":
                return EstadoArtefato.CRIANDO.equals(estadoArquivo) ? CRIAR_XML.getDescricao() : ALTERAR_XML.getDescricao();
            case "sql":
                return EstadoArtefato.CRIANDO.equals(estadoArquivo) ? CRIAR_XML.getDescricao() : ALTERAR_XML.getDescricao();
        }

        return null;
    }
}
