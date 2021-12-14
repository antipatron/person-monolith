package com.pragma.personmonolith.service;

import com.pragma.personmonolith.exception.DataConstraintViolationException;
import com.pragma.personmonolith.exception.DataDuplicatedException;
import com.pragma.personmonolith.exception.DataNotFoundException;
import com.pragma.personmonolith.model.Person;
import com.pragma.personmonolith.model.PersonRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class PersonService{
    private PersonRepository personRepository;


    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person createPerson(Person person){
        if(Objects.nonNull(person.getId())){
            Optional<Person> personOptional = personRepository.findById(person.getId());
            if(personOptional.isPresent()){
                throw new DataDuplicatedException("exception.data_duplicated.person");
            }
        }

        try {
            return personRepository.save(person);
        }catch (DataIntegrityViolationException e) {
            throw new DataConstraintViolationException("exception.data_constraint_violation.person");
        }
    }

    public Person editPerson(Person person){
        return null;
    }

    public void deletePerson(Integer id){
    }

    public List<Person> findAll(){
        return null;
    }





}
