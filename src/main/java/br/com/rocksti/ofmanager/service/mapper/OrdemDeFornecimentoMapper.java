package br.com.rocksti.ofmanager.service.mapper;


import br.com.rocksti.ofmanager.domain.*;
import br.com.rocksti.ofmanager.service.dto.OrdemDeFornecimentoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrdemDeFornecimento} and its DTO {@link OrdemDeFornecimentoDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface OrdemDeFornecimentoMapper extends EntityMapper<OrdemDeFornecimentoDTO, OrdemDeFornecimento> {

    @Mapping(source = "gestorDaOf.id", target = "gestorDaOfId")
    @Mapping(source = "gestorDaOf.login", target = "gestorDaOfLogin")
    @Mapping(source = "donoDaOf.id", target = "donoDaOfId")
    @Mapping(source = "donoDaOf.login", target = "donoDaOfLogin")
    @Mapping(source = "donoDaOf.firstName", target = "donoDaOfFirstName")
    @Mapping(source = "donoDaOf.lastName", target = "donoDaOfLastName")
    @Mapping(source = "gestorDaOf.firstName", target = "gestorDaOfFirstName")
    @Mapping(source = "gestorDaOf.lastName", target = "gestorDaOfLastName")
    OrdemDeFornecimentoDTO toDto(OrdemDeFornecimento ordemDeFornecimento);

    @Mapping(target = "artefatoOrdemDeFornecimentos", ignore = true)
    @Mapping(target = "removeArtefatoOrdemDeFornecimento", ignore = true)
    @Mapping(source = "gestorDaOfId", target = "gestorDaOf")
    @Mapping(source = "donoDaOfId", target = "donoDaOf")
    OrdemDeFornecimento toEntity(OrdemDeFornecimentoDTO ordemDeFornecimentoDTO);

    default OrdemDeFornecimento fromId(Long id) {
        if (id == null) {
            return null;
        }
        OrdemDeFornecimento ordemDeFornecimento = new OrdemDeFornecimento();
        ordemDeFornecimento.setId(id);
        return ordemDeFornecimento;
    }
}
