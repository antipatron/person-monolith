package com.fakecompany.personmonolith.controller;

import com.fakecompany.personmonolith.dto.PersonDto;
import com.fakecompany.personmonolith.dto.PersonImageDto;
import com.fakecompany.personmonolith.exception.DataNotFoundException;
import com.fakecompany.personmonolith.facade.PersonFacade;
import com.fakecompany.personmonolith.service.PersonService;
import com.fakecompany.personmonolith.util.StandardResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    public List<PersonImageDto> personImageDtoList = new ArrayList<>();
    public PersonImageDto personImageDto;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    PersonService personService;
    @MockBean
    PersonFacade personFacade;

    private static ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    PersonController personController;

    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);
        MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt",
                "text/plain", "some xml".getBytes());

        personImageDto = PersonImageDto.builder()
                .personId(99)
                .name("OSCAR")
                .lastName("KILO")
                .identification("12345678")
                .identificationTypeId(4)
                .age(20)
                .cityBirth("Tolima")
                .imageId("1234abc")
                .imageUrl("https://url.com")
                .build();

        personImageDtoList.add(personImageDto);

        System.out.println("Iniciando el meotodo: "+personImageDto.toString());
    }

    @AfterEach
    public void tearDown(){
        System.out.println("Finalizando cada metodo");
    }

    @Test
    public void whenGivenId_shouldUpdatePerson_ifFound() throws Exception {
        PersonImageDto personImageDtoToEdit = PersonImageDto.builder()
                .personId(99)
                .name("OSCAR DARIO")
                .lastName("KILO")
                .identification("12345678")
                .identificationTypeId(4)
                .age(20)
                .cityBirth("Tolima")
                .imageId("1234abc")
                .imageUrl("https://url.com")
                .build();

        String json = mapper.writeValueAsString(personImageDtoToEdit);


        MockMultipartFile imageToSend= new MockMultipartFile("image", "dummy.png",
                "text/plain", "".getBytes());
        MockMultipartFile person = new MockMultipartFile("person", "foo.txt",
                "application/json", json.getBytes());

        Mockito.when(personFacade
                .editPerson(personImageDtoToEdit, imageToSend))
                .thenReturn(personImageDtoToEdit);

        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/person");
        builder.with(request -> {
            request.setMethod("PUT");
            return request;
        });

        mockMvc.perform(builder
                .file(person)
                .file(imageToSend))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.status").value("CORRECT_TRANSACTION"))
                .andExpect(jsonPath("$.body.name").isNotEmpty());
    }


    @Test
    public void shouldThrowExceptionWhenUserDoesntExist() throws Exception {
        PersonImageDto personImageDtoToEdit = PersonImageDto.builder()
                .personId(99)
                .name("OSCAR DARIO")
                .lastName("KILO")
                .identification("12345678")
                .identificationTypeId(4)
                .age(20)
                .cityBirth("Tolima")
                .imageId("1234abc")
                .imageUrl("https://url.com")
                .build();

        String json = mapper.writeValueAsString(personImageDtoToEdit);

        MockMultipartFile imageToSend= new MockMultipartFile("image", "dummy.png",
                "text/plain", "".getBytes());
        MockMultipartFile person = new MockMultipartFile("person", "foo.txt",
                "application/json", json.getBytes());

        Mockito.doThrow(new DataNotFoundException("user doesnt exist")).when(personFacade).editPerson(personImageDtoToEdit, imageToSend);

        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/person");
        builder.with(request -> {
            request.setMethod("PUT");
            return request;
        });

        mockMvc.perform(builder
                .file(person)
                .file(imageToSend))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void createPerson() throws Exception {
        PersonImageDto personImageDtoToCreate = PersonImageDto.builder()
                .name("OSCAR DARIO")
                .lastName("KILO")
                .identification("12345678")
                .identificationTypeId(4)
                .age(20)
                .cityBirth("Tolima")
                .imageId("1234abc")
                .imageUrl("https://url.com")
                .build();

        String json = mapper.writeValueAsString(personImageDtoToCreate);

        MockMultipartFile imageToSend= new MockMultipartFile("image", "dummy.png",
                "text/plain", "".getBytes());
        MockMultipartFile person = new MockMultipartFile("person", "foo.txt",
                "application/json", json.getBytes());

        Mockito.when(personFacade.createPerson(any(), any())).thenReturn(personImageDtoToCreate);

        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/person");
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("POST");
                return request;
            }
        });

        mockMvc.perform(builder
                .file(person)
                .file(imageToSend))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.status").value("CORRECT_TRANSACTION"))
                .andExpect(jsonPath("$.body.name").isNotEmpty());
    }

    @Test
    public void deletePersonWhenDeleteMethod() throws Exception {
        Integer personId = 2;
        doNothing().when(personFacade).deletePerson(any());

        mockMvc.perform(delete("/person/delete").param("personId",String.valueOf(personId)))
                .andExpect(status().isAccepted())
                .andDo(print())
                .andExpect(jsonPath("$.status").value("CORRECT_TRANSACTION"))
                .andExpect(jsonPath("$.message").value("delete.person.ok"));

    }

    @Test
    public void findAll() throws Exception {

        Mockito.when(personFacade.findAll()).thenReturn(personImageDtoList);
        mockMvc.perform(get("/person/get-all"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.status").value("CORRECT_TRANSACTION"))
                .andExpect(jsonPath("$.body").isNotEmpty());

    }

    @Test
    public void findAllGreaterThanEqual() throws Exception {


        Integer age = 2;
        PersonDto personDto = PersonDto.builder().id(1).name("A").lastName("A").identification("12345")
                .identificationTypeId(4).age(20).cityBirth("A").build();
        List<PersonDto> personDtoList = new ArrayList<>();
        personDtoList.add(personDto);

        Mockito.when(personFacade.findByAgeGreaterThanEqual(any())).thenReturn(personDtoList);

        mockMvc.perform(get("/person/get-age-greaterthan-equal")
                .param("age", String.valueOf(age)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.status").value("CORRECT_TRANSACTION"))
                .andExpect(jsonPath("$.body").isNotEmpty());
        verify(personFacade).findByAgeGreaterThanEqual(age);

    }

    @Test
    public void findAllLessThanEqual() throws Exception {
        Integer age = 20;
        PersonDto personDto = PersonDto.builder().id(1).name("A").lastName("A").identification("12345")
                .identificationTypeId(4).age(20).cityBirth("A").build();
        List<PersonDto> personDtoList = new ArrayList<>();
        personDtoList.add(personDto);

        when(personFacade.findByAgeLessThanEqual(any())).thenReturn(personDtoList);

       mockMvc.perform(get("/person/get-age-lessthan-equal")
                .param("age", String.valueOf(age)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.status").value("CORRECT_TRANSACTION"))
                .andExpect(jsonPath("$.body").isNotEmpty());

        verify(personFacade).findByAgeLessThanEqual(age);

    }

}
