package br.com.rocksti.ofmanager.service.mapper;


import br.com.rocksti.ofmanager.domain.*;
import br.com.rocksti.ofmanager.service.dto.ServicoOfDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ServicoOf} and its DTO {@link ServicoOfDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ServicoOfMapper extends EntityMapper<ServicoOfDTO, ServicoOf> {

    @Mapping(source = "gestorDaOf.id", target = "gestorDaOfId")
    @Mapping(source = "gestorDaOf.login", target = "gestorDaOfLogin")
    @Mapping(source = "donoDaOf.id", target = "donoDaOfId")
    @Mapping(source = "donoDaOf.login", target = "donoDaOfLogin")
    ServicoOfDTO toDto(ServicoOf servicoOf);

    @Mapping(target = "arquivoDaOfs", ignore = true)
    @Mapping(target = "removeArquivoDaOf", ignore = true)
    @Mapping(source = "gestorDaOfId", target = "gestorDaOf")
    @Mapping(source = "donoDaOfId", target = "donoDaOf")
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
