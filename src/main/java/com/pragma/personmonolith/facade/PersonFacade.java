package com.pragma.personmonolith.facade;

import com.pragma.personmonolith.dto.PersonDto;
import com.pragma.personmonolith.mapper.PersonMapper;
import com.pragma.personmonolith.service.PersonService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PersonFacade {
    private PersonService personService;
    private PersonMapper personMapper;

    public PersonFacade(PersonService personService, PersonMapper personMapper) {
        this.personService = personService;
        this.personMapper = personMapper;
    }

    public PersonDto createPerson(PersonDto personDto){
        return personMapper.toDto(personService.createPerson(personMapper.toEntity(personDto)));

    }

    public PersonDto editPerson(PersonDto personDto){
        System.out.println("ID: ##"+personDto.getId());

        return personMapper.toDto(personService.editPerson(personMapper.toEntity(personDto)));

    }

    public void deletePerson(Integer id){

    }

    public List<PersonDto> findAll(){
        return null;
    }


}
