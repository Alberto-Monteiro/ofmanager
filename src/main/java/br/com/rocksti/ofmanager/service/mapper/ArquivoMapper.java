package br.com.rocksti.ofmanager.service.mapper;


import br.com.rocksti.ofmanager.domain.*;
import br.com.rocksti.ofmanager.service.dto.ArquivoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Arquivo} and its DTO {@link ArquivoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ArquivoMapper extends EntityMapper<ArquivoDTO, Arquivo> {


    @Mapping(target = "arquivoDaOfs", ignore = true)
    @Mapping(target = "removeArquivoDaOf", ignore = true)
    Arquivo toEntity(ArquivoDTO arquivoDTO);

    default Arquivo fromId(Long id) {
        if (id == null) {
            return null;
        }
        Arquivo arquivo = new Arquivo();
        arquivo.setId(id);
        return arquivo;
    }
}
