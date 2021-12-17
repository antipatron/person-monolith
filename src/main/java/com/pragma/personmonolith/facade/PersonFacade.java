package com.pragma.personmonolith.facade;

import com.pragma.personmonolith.dto.PersonDto;
import com.pragma.personmonolith.dto.PersonImageDto;
import com.pragma.personmonolith.exception.ImageNotComeBodyException;
import com.pragma.personmonolith.model.Image;
import com.pragma.personmonolith.model.Person;
import com.pragma.personmonolith.mapper.PersonMapper;
import com.pragma.personmonolith.service.ImageService;
import com.pragma.personmonolith.service.PersonService;
import com.pragma.personmonolith.util.ObjectTypeConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.pragma.personmonolith.util.ObjectTypeConverter.image2Base64;
import static com.pragma.personmonolith.util.OptionalFieldValidator.imageComeOnBody;

@Service
@Transactional
public class PersonFacade {
    private PersonService personService;
    private ImageService imageService;
    private PersonMapper personMapper;

    //TODO rafactorizar a I
    public PersonFacade(PersonService personService,ImageService imageService,
                        PersonMapper personMapper) {
        this.personService = personService;
        this.imageService = imageService;
        this.personMapper = personMapper;
    }

    public PersonImageDto createPerson(PersonImageDto personImageDto, MultipartFile imagePart){
        Person person = mappingPerson(personImageDto);
        person = personService.createPerson(person);
        personImageDto.setPersonId(person.getId());

        if (!imagePart.isEmpty()){
            Image image= mappingImage(personImageDto.getImageId(), person.getId(),imagePart);
            image = imageService.createImage(image);
            personImageDto.setImageId(image.getId());
        }

        return personImageDto;
    }



    public PersonImageDto editPerson(PersonImageDto personImageDto, MultipartFile imagePart){

        Person person = mappingPerson(personImageDto);
        PersonDto personDto = personMapper.toDto(personService.editPerson(person));

        //TODO puedo refactorizar esta parte y juntarla con la parte del guardar.
        PersonImageDto personImageDtoEdit = new PersonImageDto();

        if (!imagePart.isEmpty()){
            //TODO comprobar que si tenga imagen en bd (restriccion es una persona una imagen)
            if(hasImage(personImageDto.getPersonId())){
                if(imageComeOnBody(personImageDto.getImageId())){
                    Image imageEdit = mappingImage(personImageDto.getImageId(), personImageDto.getPersonId(), imagePart);
                    imageEdit = imageService.editImage(imageEdit);
                    personImageDtoEdit.setImageId(imageEdit.getId());
                }else{
                    throw new ImageNotComeBodyException("exception.not_come_body.image");
                }

            }else{
                Image image = imageService.createImage(mappingImage(personImageDto.getImageId(),personImageDto.getPersonId(), imagePart));
                personImageDtoEdit.setImageId(image.getId());
            }
        }
        
        personImageDtoEdit.setPersonId(personDto.getId());
        personImageDtoEdit.setName(personDto.getName());
        personImageDtoEdit.setLastName(personDto.getLastName());
        personImageDtoEdit.setIdentification(personDto.getIdentification());
        personImageDtoEdit.setIdentificationTypeId(personDto.getIdentificationTypeId());
        personImageDtoEdit.setAge(personDto.getAge());
        personImageDtoEdit.setCityBirth(personDto.getCityBirth());

        return personImageDtoEdit;
    }

    private Image mappingImage(String imageId, Integer personId, MultipartFile imagePart){
        Image image = new Image();
        image.setId(imageId);
        image.setImage(image2Base64(imagePart));
        image.setPersonId(personId);
        return image;
    }

    private Person mappingPerson(PersonImageDto personImageDto){
        Person person = new Person();
        person.setId(personImageDto.getPersonId());

        person.setName(personImageDto.getName());
        person.setLastName(personImageDto.getLastName());
        person.setIdentification(personImageDto.getIdentification());
        person.setIdentificationTypeId(personImageDto.getIdentificationTypeId());
        person.setAge(personImageDto.getAge());
        person.setCityBirth(personImageDto.getCityBirth());

        return person;

    }

    private boolean hasImage(Integer personId){
        return !imageService.findByPersonId(personId).getImage().isEmpty();
    }

    public void deletePerson(Integer personId){
        //TODO buscar si la persona tiene imagen, si es así, eliminar la imagen
        Image image = imageService.findByPersonId(personId);
        if(!image.getImage().isEmpty()){
            imageService.deleteImage(image.getId());
        }

        personService.deletePerson(personId);

    }

    public List<PersonImageDto> findAll(){

        List<PersonDto> personDtoList = personMapper.toDto(personService.findAll());
        List<PersonImageDto> personImageDtoList = new ArrayList<>();


        personDtoList.forEach(personDto -> {

            PersonImageDto personImageDto = new PersonImageDto();

            personImageDto.setPersonId(personDto.getId());
            personImageDto.setName(personDto.getName());
            personImageDto.setLastName(personDto.getLastName());
            personImageDto.setIdentification(personDto.getIdentification());
            personImageDto.setIdentificationTypeId(personDto.getIdentificationTypeId());
            personImageDto.setAge(personDto.getAge());
            personImageDto.setCityBirth(personDto.getCityBirth());
            personImageDto.setImageId(imageService.findByPersonId(personDto.getId()).getId());

            personImageDtoList.add(personImageDto);

        });



        return personImageDtoList;


    }

    public List<PersonDto> findByAgeGreaterThanEqual(Integer age){
        return personMapper.toDto(personService.findByAgeGreaterThanEqual(age));
    }

    public List<PersonDto> findByAgeLessThanEqual(Integer age){
        return personMapper.toDto(personService.findByAgeLessThanEqual(age));
    }
}
