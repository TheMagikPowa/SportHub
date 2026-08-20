package com.generation.SportHub.converters;

import com.generation.SportHub.dto.GenericDTO;
import com.generation.SportHub.entity.GenericEntity;

public interface GenericConverter<D extends GenericDTO, E extends GenericEntity> {

    D fromEntityToDto(E entity);
    E fromDtoToEntity(D dto);

}
