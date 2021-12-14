package com.pragma.personmonolith.controller;


import com.pragma.personmonolith.dto.PersonDto;
import com.pragma.personmonolith.facade.PersonFacade;
import com.pragma.personmonolith.model.Person;
import com.pragma.personmonolith.util.StandardResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/person")
public class PersonController {
    private PersonFacade personFacade;

    public PersonController(PersonFacade personFacade) {
        this.personFacade = personFacade;
    }

    @PostMapping
    @ApiOperation(value = "Save person", response = PersonDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La petición fue procesada con éxito"),
            @ApiResponse(code = 400, message = "La petición es inválida"),
            @ApiResponse(code = 500, message = "Error del servidor al procesar la respuesta"),
    })
    public ResponseEntity<StandardResponse<PersonDto>> createProduct(
            @Valid @RequestBody PersonDto personDto){
        PersonDto personDto1 = personFacade.createPerson(personDto);
        return ResponseEntity.ok(new StandardResponse<>(
                StandardResponse.StatusStandardResponse.OK,
                "person.create.ok",
                personDto1));
    }

}
