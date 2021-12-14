package com.pragma.personmonolith.mapper;

import com.pragma.personmonolith.dto.PersonDto;
import com.pragma.personmonolith.model.Person;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = {IdentificationTypeMapper.class})
public interface PersonMapper extends EntityMapper<PersonDto, Person>{
}
