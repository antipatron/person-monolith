package com.fakecompany.personmonolith.mapper;

import com.fakecompany.personmonolith.dto.PersonDto;
import com.fakecompany.personmonolith.model.Person;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = {IdentificationTypeMapper.class})
public interface PersonMapper extends EntityMapper<PersonDto, Person>{
}
