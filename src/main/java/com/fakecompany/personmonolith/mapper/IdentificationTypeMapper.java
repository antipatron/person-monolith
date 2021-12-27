package com.fakecompany.personmonolith.mapper;

import com.fakecompany.personmonolith.model.IdentificationType;
import com.fakecompany.personmonolith.dto.IdentificationTypeDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IdentificationTypeMapper extends EntityMapper<IdentificationTypeDto, IdentificationType>{
}


