package br.com.rocksti.ofmanager.service.mapper;


import br.com.rocksti.ofmanager.domain.*;
import br.com.rocksti.ofmanager.service.dto.ArtefatoOrdemDeFornecimentoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ArtefatoOrdemDeFornecimento} and its DTO {@link ArtefatoOrdemDeFornecimentoDTO}.
 */
@Mapper(componentModel = "spring", uses = {ArtefatoMapper.class, OrdemDeFornecimentoMapper.class})
public interface ArtefatoOrdemDeFornecimentoMapper extends EntityMapper<ArtefatoOrdemDeFornecimentoDTO, ArtefatoOrdemDeFornecimento> {

    @Mapping(source = "artefato.id", target = "artefatoId")
    @Mapping(source = "ordemDeFornecimento.id", target = "ordemDeFornecimentoId")
    ArtefatoOrdemDeFornecimentoDTO toDto(ArtefatoOrdemDeFornecimento artefatoOrdemDeFornecimento);

    @Mapping(source = "artefatoId", target = "artefato")
    @Mapping(source = "ordemDeFornecimentoId", target = "ordemDeFornecimento")
    ArtefatoOrdemDeFornecimento toEntity(ArtefatoOrdemDeFornecimentoDTO artefatoOrdemDeFornecimentoDTO);

    default ArtefatoOrdemDeFornecimento fromId(Long id) {
        if (id == null) {
            return null;
        }
        ArtefatoOrdemDeFornecimento artefatoOrdemDeFornecimento = new ArtefatoOrdemDeFornecimento();
        artefatoOrdemDeFornecimento.setId(id);
        return artefatoOrdemDeFornecimento;
    }
}
