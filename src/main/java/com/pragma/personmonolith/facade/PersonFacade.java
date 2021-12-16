package com.pragma.personmonolith.facade;

import com.pragma.personmonolith.dto.PersonDto;
import com.pragma.personmonolith.dto.PersonImageDto;
import com.pragma.personmonolith.model.Image;
import com.pragma.personmonolith.model.Person;
import com.pragma.personmonolith.mapper.PersonMapper;
import com.pragma.personmonolith.service.ImageService;
import com.pragma.personmonolith.service.PersonService;
import com.pragma.personmonolith.util.ObjectTypeConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
        personImageDtoEdit.setPersonId(personDto.getId());
        personImageDtoEdit.setName(personDto.getName());
        personImageDtoEdit.setLastName(personDto.getLastName());
        personImageDtoEdit.setIdentification(personDto.getIdentification());
        personImageDtoEdit.setIdentificationTypeId(personDto.getIdentificationTypeId());
        personImageDtoEdit.setAge(personDto.getAge());
        personImageDtoEdit.setCityBirth(personDto.getCityBirth());

        if (!imagePart.isEmpty()){
            if(personImageDto.getImageId()!=null){
                Image imageEdit = mappingImage(personImageDto.getImageId(), personImageDto.getPersonId(), imagePart);
                imageEdit = imageService.editImage(imageEdit);
                personImageDtoEdit.setImageId(imageEdit.getId());
            }else{
                Image image = imageService.createImage(mappingImage(personImageDto.getImageId(),personImageDto.getPersonId(), imagePart));
                personImageDtoEdit.setImageId(image.getId());
            }
        }

        return personImageDtoEdit;
    }

    private Image mappingImage(Integer imageId, Integer personId, MultipartFile imagePart){
        Image image = new Image();
        image.setId(imageId);
        image.setImage(ObjectTypeConverter.image2Base64(imagePart));
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

    public void deletePerson(Integer personId){
        personService.deletePerson(personId);

    }

    public List<PersonDto> findAll(){
        return personMapper.toDto(personService.findAll());
    }


}
