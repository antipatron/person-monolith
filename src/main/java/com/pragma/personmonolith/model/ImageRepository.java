package com.pragma.personmonolith.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Optional;

@EnableMongoRepositories
public interface ImageRepository extends MongoRepository<Image, String> {

    Optional<Image> findByPersonId(Integer personId);
    Optional<Image> findByPersonIdAndId(Integer personId, String id);




}
