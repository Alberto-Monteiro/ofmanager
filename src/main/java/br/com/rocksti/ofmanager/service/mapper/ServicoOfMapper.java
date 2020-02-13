package br.com.rocksti.ofmanager.service.mapper;


import br.com.rocksti.ofmanager.domain.*;
import br.com.rocksti.ofmanager.service.dto.ServicoOfDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ServicoOf} and its DTO {@link ServicoOfDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ServicoOfMapper extends EntityMapper<ServicoOfDTO, ServicoOf> {


    @Mapping(target = "arquivoDaOfs", ignore = true)
    @Mapping(target = "removeArquivoDaOf", ignore = true)
    ServicoOf toEntity(ServicoOfDTO servicoOfDTO);

    default ServicoOf fromId(Long id) {
        if (id == null) {
            return null;
        }
        ServicoOf servicoOf = new ServicoOf();
        servicoOf.setId(id);
        return servicoOf;
    }
}
