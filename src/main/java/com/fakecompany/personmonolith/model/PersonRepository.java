package com.fakecompany.personmonolith.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Integer> {

    List<Person> findByAgeGreaterThanEqual(Integer age);
    List<Person> findByAgeLessThanEqual(Integer age);


}
