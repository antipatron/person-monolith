package com.pragma.personmonolith.service;


import com.pragma.personmonolith.model.Person;
import com.pragma.personmonolith.model.PersonRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@SpringBootTest
class PersonServiceTest {


    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonService personService;

    private Person person;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        person = new Person();
        person.setId(1);
        person.setName("Cantinflas");
        person.setLastName("Infabtes");
        person.setIdentification("123456901");
        person.setIdentificationTypeId(1);
        person.setAge("56");
        person.setCityBirth("leticia");


       Mockito.when(personRepository.findById(1))
                .thenReturn(Optional.of(person));

       Mockito.when(personRepository.save(person)).thenReturn(person);

       Mockito.when(personRepository.findAll()).thenReturn(Arrays.asList(person));


    }

    @Test
    void createPerson() {
        assertNotNull(personService.createPerson(person));
    }

    @Test
    void editPerson() {
        Person person = new Person();
        person.setId(1);
        person.setName("Cantinflas2");
        person.setLastName("Infabtes");
        person.setIdentification("123456901");
        person.setIdentificationTypeId(1);
        person.setAge("34");
        person.setCityBirth("leticia");

        when(personRepository.save(person)).thenReturn(person);
        assertThat(personService.editPerson(person).getAge().equals("34"));

    }

    @Test
    public void whenGetAllThenReturnListPerson(){
        assertNotNull(personService.findAll());
    }

    @Test
    public void whenValidGetIdThenReturnPerson(){
        Person personFound = personService.findById(1);
        Assertions.assertThat(personFound.getAge().equals("56"));
    }
}