package br.com.rocksti.ofmanager.service.mapper;


import br.com.rocksti.ofmanager.domain.*;
import br.com.rocksti.ofmanager.service.dto.ArtefatoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Artefato} and its DTO {@link ArtefatoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ArtefatoMapper extends EntityMapper<ArtefatoDTO, Artefato> {



    default Artefato fromId(Long id) {
        if (id == null) {
            return null;
        }
        Artefato artefato = new Artefato();
        artefato.setId(id);
        return artefato;
    }
}
