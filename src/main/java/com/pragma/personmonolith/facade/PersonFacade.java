package com.pragma.personmonolith.facade;

import com.pragma.personmonolith.dto.PersonDto;
import com.pragma.personmonolith.dto.PersonImageDto;
import com.pragma.personmonolith.exception.ImageNotComeBodyException;
import com.pragma.personmonolith.mapper.PersonMapper;
import com.pragma.personmonolith.model.Image;
import com.pragma.personmonolith.model.Person;
import com.pragma.personmonolith.service.FileStoreService;
import com.pragma.personmonolith.service.ImageService;
import com.pragma.personmonolith.service.PersonService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.pragma.personmonolith.util.OptionalFieldValidator.imageFileComeOnBody;
import static com.pragma.personmonolith.util.OptionalFieldValidator.imageIdComeOnBody;

@Service
@Transactional
public class PersonFacade {
    private PersonService personService;
    private ImageService imageService;
    private PersonMapper personMapper;
    private FileStoreService fileStoreService;

    //TODO rafactorizar a I
    public PersonFacade(PersonService personService,ImageService imageService,FileStoreService fileStoreService,
                        PersonMapper personMapper) {
        this.personService = personService;
        this.imageService = imageService;
        this.fileStoreService = fileStoreService;
        this.personMapper = personMapper;
    }

    public PersonImageDto createPerson(PersonImageDto personImageDto, MultipartFile imagePart){
        Person person = mappingPerson(personImageDto);
        person = personService.createPerson(person);
        personImageDto.setPersonId(person.getId());

        if (!imagePart.isEmpty()){
            Image imageS3 =  fileStoreService.createFile(imagePart.getOriginalFilename(),personImageDto.getPersonId(),imagePart);
            Image image= imageService.createImage(imageS3);
            personImageDto.setImageId(image.getId());

        }

        return personImageDto;
    }



    public PersonImageDto editPerson(PersonImageDto personImageDto, MultipartFile imagePart){

        Person person = mappingPerson(personImageDto);
        PersonDto personDto = personMapper.toDto(personService.editPerson(person));

        //TODO puedo refactorizar esta parte y juntarla con la parte del guardar.
        PersonImageDto personImageDtoEdit = new PersonImageDto();
        personImageDtoEdit.setImageId(imageService.findByPersonId(personImageDto.getPersonId()).getId());
        final String imageName = imagePart.getOriginalFilename();

        if (imageFileComeOnBody(imagePart)){
            if(hasImage(personImageDto.getPersonId())){
                if(imageIdComeOnBody(personImageDto.getImageId())){
                    Image imageFind = imageService.findByPersonIdAndId(personImageDto.getPersonId(), personImageDto.getImageId());
                    fileStoreService.deleteFile(imageFind.getImageName(), personImageDto.getPersonId());
                    Image imageS3 = fileStoreService.createFile(imageName, personImageDto.getPersonId(),imagePart);

                    Image imageEdit = mappingImage(personImageDto.getImageId(), personImageDto.getPersonId(), imageS3.getImage(), imageName);
                    imageEdit = imageService.editImage(imageEdit);
                    personImageDtoEdit.setImageId(imageEdit.getId());
                }else {
                    throw new ImageNotComeBodyException("exception.not_come_body.image");
                }
            }else{
                Image imageS3 = fileStoreService.createFile(imageName, personImageDto.getPersonId(),imagePart);
                Image image = imageService.createImage(mappingImage(personImageDto.getImageId(),personImageDto.getPersonId(),imageS3.getImage(),imageName));
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

    private Image mappingImage(String imageId, Integer personId, String imagePart, String imageName){
        Image image = new Image();
        image.setId(imageId);
        image.setImage(imagePart);
        image.setImageName(imageName);
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
            fileStoreService.deleteFile(image.getImageName(),personId);
        }

        personService.deletePerson(personId);

    }

    public List<PersonImageDto> findAll(){
        List<PersonImageDto> personImageDtoList = new ArrayList<>();

        List<PersonDto> personDtoList = personMapper.toDto(personService.findAll());
        List<Image> imageList = imageService.findAll();

        personDtoList.forEach(personDto -> {

            PersonImageDto personImageDto = new PersonImageDto();

            personImageDto.setPersonId(personDto.getId());
            personImageDto.setName(personDto.getName());
            personImageDto.setLastName(personDto.getLastName());
            personImageDto.setIdentification(personDto.getIdentification());
            personImageDto.setIdentificationTypeId(personDto.getIdentificationTypeId());
            personImageDto.setAge(personDto.getAge());
            personImageDto.setCityBirth(personDto.getCityBirth());

            Optional<Image> imageOptional =  imageList.stream()
                    .filter(image -> image.getPersonId().equals(personDto.getId())).findFirst();

            if(imageOptional.isPresent()){
                personImageDto.setImageId(imageOptional.get().getId());
                personImageDto.setImage(imageOptional.get().getImage());
            }


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
