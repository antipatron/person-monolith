package com.pragma.personmonolith.controller;


import com.pragma.personmonolith.dto.PersonDto;
import com.pragma.personmonolith.dto.PersonImageDto;
import com.pragma.personmonolith.facade.PersonFacade;
import com.pragma.personmonolith.util.StandardResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/person")
public class PersonController {
    private PersonFacade personFacade;

    public PersonController(PersonFacade personFacade) {
        this.personFacade = personFacade;
    }

    @PostMapping()
    @ApiOperation(value = "Save person with image", response = PersonImageDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La petición fue procesada con éxito"),
            @ApiResponse(code = 400, message = "La petición es inválida"),
            @ApiResponse(code = 500, message = "Error del servidor al procesar la respuesta"),
    })
    public ResponseEntity<StandardResponse<PersonImageDto>> createPerson(
            @Valid @RequestPart("person") PersonImageDto personImageDto, @RequestPart MultipartFile image){

        Logger.getGlobal().log(Level.INFO, "Imagen recibida: "+image.getOriginalFilename());

        PersonImageDto personImageDto1 = personFacade.createPerson(personImageDto, image);
        return ResponseEntity.ok(new StandardResponse<>(
                StandardResponse.StatusStandardResponse.OK,
                "person.create.ok",
                personImageDto1));
    }

    @PutMapping
    @ApiOperation(value = "Edit person", response = PersonImageDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La petición fue procesada con éxito"),
            @ApiResponse(code = 400, message = "La petición es inválida"),
            @ApiResponse(code = 500, message = "Error del servidor al procesar la respuesta"),
    })
    public ResponseEntity<StandardResponse<PersonImageDto>> editPerson(
            @Valid @RequestPart("person") PersonImageDto personImageDto, @RequestPart MultipartFile image){
        PersonImageDto personImageDto1 = personFacade.editPerson(personImageDto, image);
        return ResponseEntity.ok(new StandardResponse<>(
                StandardResponse.StatusStandardResponse.OK,
                "person.edit.ok",
                personImageDto1));
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "Delete person by id", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La petición fue procesada con éxito"),
            @ApiResponse(code = 400, message = "La petición es inválida"),
            @ApiResponse(code = 500, message = "Error del servidor al procesar la respuesta"),
    })
    public ResponseEntity<StandardResponse<String>> deletePerson(
            @RequestParam(name = "personId")  Integer personId){

        personFacade.deletePerson(personId);
        return ResponseEntity.accepted().body(new StandardResponse<>(StandardResponse.StatusStandardResponse.OK,"delete.person.ok"));

    }

    @GetMapping("/get-all")
    @ApiOperation(value = "Get all", response = PersonDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La petición fue procesada con éxito"),
            @ApiResponse(code = 400, message = "La petición es inválida"),
            @ApiResponse(code = 500, message = "Error del servidor al procesar la respuesta"),
    })
    public ResponseEntity<StandardResponse<List<PersonDto>>> findAll(){

        List<PersonDto> personDtoList = personFacade.findAll();
        return ResponseEntity.ok(new StandardResponse<>(
                StandardResponse.StatusStandardResponse.OK,
                personDtoList));
    }

    @GetMapping("/get-age-greaterthan-equal")
    @ApiOperation(value = "Get findByAgeGreaterThanEqual", response = PersonDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La petición fue procesada con éxito"),
            @ApiResponse(code = 400, message = "La petición es inválida"),
            @ApiResponse(code = 500, message = "Error del servidor al procesar la respuesta"),
    })
    public ResponseEntity<StandardResponse<List<PersonDto>>> findByAgeGreaterThanEqual(@RequestParam(name = "age") Integer age ){

        List<PersonDto> personDtoList = personFacade.findByAgeGreaterThanEqual(age);
        return ResponseEntity.ok(new StandardResponse<>(
                StandardResponse.StatusStandardResponse.OK,
                personDtoList));
    }

    @GetMapping("/get-age-lessthan-equal")
    @ApiOperation(value = "Get findByAgeLessThanEqual", response = PersonDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La petición fue procesada con éxito"),
            @ApiResponse(code = 400, message = "La petición es inválida"),
            @ApiResponse(code = 500, message = "Error del servidor al procesar la respuesta"),
    })
    public ResponseEntity<StandardResponse<List<PersonDto>>> findByAgeLessThanEqual(@NotBlank @RequestParam(name = "age") Integer age ){

        List<PersonDto> personDtoList = personFacade.findByAgeLessThanEqual(age);
        return ResponseEntity.ok(new StandardResponse<>(
                StandardResponse.StatusStandardResponse.OK,
                personDtoList));
    }


}
