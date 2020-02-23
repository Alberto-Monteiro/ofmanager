package br.com.rocksti.ofmanager.filtropesquisa;

import br.com.rocksti.ofmanager.service.dto.UserDTO;

public class FiltroPesquisaServicoOf {
    private Integer numeroOF;
    private UserDTO usuarioGestor;

    public Integer getNumeroOF() {
        return numeroOF;
    }

    public void setNumeroOF(Integer numeroOF) {
        this.numeroOF = numeroOF;
    }

    public UserDTO getUsuarioGestor() {
        return usuarioGestor;
    }

    public void setUsuarioGestor(UserDTO usuarioGestor) {
        this.usuarioGestor = usuarioGestor;
    }
}
