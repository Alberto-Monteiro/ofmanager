package br.com.rocksti.ofmanager.service.mapper;


import br.com.rocksti.ofmanager.domain.*;
import br.com.rocksti.ofmanager.service.dto.ArquivoDaOfDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ArquivoDaOf} and its DTO {@link ArquivoDaOfDTO}.
 */
@Mapper(componentModel = "spring", uses = {ServicoOfMapper.class, ArquivoMapper.class})
public interface ArquivoDaOfMapper extends EntityMapper<ArquivoDaOfDTO, ArquivoDaOf> {

    @Mapping(source = "servicoOf.id", target = "servicoOfId")
    @Mapping(source = "arquivo.id", target = "arquivoId")
    ArquivoDaOfDTO toDto(ArquivoDaOf arquivoDaOf);

    @Mapping(source = "servicoOfId", target = "servicoOf")
    @Mapping(source = "arquivoId", target = "arquivo")
    ArquivoDaOf toEntity(ArquivoDaOfDTO arquivoDaOfDTO);

    default ArquivoDaOf fromId(Long id) {
        if (id == null) {
            return null;
        }
        ArquivoDaOf arquivoDaOf = new ArquivoDaOf();
        arquivoDaOf.setId(id);
        return arquivoDaOf;
    }
}
