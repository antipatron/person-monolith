package com.pragma.personmonolith.facade;

import com.pragma.personmonolith.dto.ImageDto;
import com.pragma.personmonolith.dto.PersonDto;
import com.pragma.personmonolith.dto.PersonImageDto;
import com.pragma.personmonolith.exception.ImageNotComeBodyException;
import com.pragma.personmonolith.mapper.ImageMapper;
import com.pragma.personmonolith.mapper.PersonMapper;
import com.pragma.personmonolith.model.Image;
import com.pragma.personmonolith.model.Person;
import com.pragma.personmonolith.service.FileStoreService;
import com.pragma.personmonolith.service.ImageService;
import com.pragma.personmonolith.service.PersonService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.pragma.personmonolith.util.ModelMaperUtil.getMapperPersonaImageDto2PersonDto;
import static com.pragma.personmonolith.util.OptionalFieldValidator.imageFileComeOnBody;
import static com.pragma.personmonolith.util.OptionalFieldValidator.imageIdComeOnBody;

@Service
@Transactional
@AllArgsConstructor
public class PersonFacade {
    private static final String CREATE = "create";
    private static final String EDIT = "edit";

    private PersonService personService;
    private ImageService imageService;
    private PersonMapper personMapper;
    private ImageMapper imageMapper;
    private FileStoreService fileStoreService;
    private ModelMapper modelMapper;

    public PersonImageDto createPerson(PersonImageDto personImageDto, MultipartFile imagePart){
        PersonDto personDto = setUpMappingAndPersistence(personImageDto, CREATE);
        personImageDto.setPersonId(personDto.getId());

        if (!imagePart.isEmpty()){
            ImageDto imageDto = storeNewImage(imagePart.getOriginalFilename(),personImageDto.getPersonId(),imagePart);
            personImageDto.setImageId(imageDto.getId());
            personImageDto.setImageUrl(imageDto.getImageUrl());
        }

        return personImageDto;
    }

    public PersonImageDto editPerson(PersonImageDto personImageDto, MultipartFile imagePart){
        PersonDto personDto = setUpMappingAndPersistence(personImageDto,EDIT);

        ImageDto imageToEdit = imageMapper.toDto(imageService.findByPersonId(personImageDto.getPersonId()));
        PersonImageDto personImageDtoEdit = modelMapper.map(personDto, PersonImageDto.class);

        if (imageFileComeOnBody(imagePart)){
            final String nameNewImage = imagePart.getOriginalFilename();
            if(!imageToEdit.getImageUrl().isEmpty()){
                if(imageIdComeOnBody(personImageDto.getImageId())&&
                isOwnImage(imageToEdit.getId(), personImageDto.getImageId())){
                    imageToEdit = replaceImage(imageToEdit.getImageName(),nameNewImage, personImageDto.getImageId(),
                            personImageDto.getPersonId(),imagePart);
                }else {
                    throw new ImageNotComeBodyException("exception.not_come_body.image");
                }
            }else{
                imageToEdit = storeNewImage(nameNewImage,personImageDto.getPersonId(),imagePart);
            }
        }

        personImageDtoEdit.setPersonId(personDto.getId());
        personImageDtoEdit.setImageId(imageToEdit.getId());
        personImageDtoEdit.setImageUrl(imageToEdit.getImageUrl());

        return personImageDtoEdit;
    }

    public void deletePerson(Integer personId){
        final Image image = imageService.findByPersonId(personId);
        if(!image.getImageUrl().isEmpty()){
            imageService.deleteImage(image.getId());
            fileStoreService.deleteFile(image.getImageName(),personId);
        }
        personService.deletePerson(personId);
    }

    public List<PersonImageDto> findAll(){
        final List<PersonImageDto> personImageDtoList = new ArrayList<>();
        final List<PersonDto> personDtoList = personMapper.toDto(personService.findAll());
        final List<Image> imageList = imageService.findAll();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        personDtoList.forEach(personDto -> System.out.println(personDto.toString()));

        personDtoList.forEach(personDto -> {

            System.out.println("indi 1: "+personDto.toString());
            PersonImageDto personImageDto = modelMapper.map(personDto, PersonImageDto.class);
            personImageDto.setPersonId(personDto.getId());

            System.out.println("big indi: "+personImageDto.toString());
            Optional<Image> imageOptional =  imageList.stream()
                    .filter(image -> image.getPersonId().equals(personDto.getId())).findAny();
            if(imageOptional.isPresent()){
                System.out.println("url: "+imageOptional.get().getImageUrl());
                personImageDto.setImageId(imageOptional.get().getId());
                personImageDto.setImageUrl(imageOptional.get().getImageUrl());
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

    private ImageDto storeNewImage(final String nameNewImage, Integer personId, MultipartFile imagePart){
        Image imageS3 = fileStoreService.createFile(nameNewImage, personId,imagePart);
        return imageMapper.toDto(imageService.createImage(imageS3));
    }

    private ImageDto replaceImage(String nameOldImage, String nameNewImage, String imageId,Integer personId,
                                  MultipartFile imagePart){

        fileStoreService.deleteFile(nameOldImage, personId);
        Image imageS3 = fileStoreService.createFile(nameNewImage, personId,imagePart);
        imageS3.setId(imageId);
        return imageMapper.toDto(imageService.editImage(imageS3));
    }

    private PersonDto setUpMappingAndPersistence(PersonImageDto personImageDto, final String persistenceType){
        modelMapper= getMapperPersonaImageDto2PersonDto();
        PersonDto personDto = modelMapper.map(personImageDto, PersonDto.class);
        if(persistenceType.equals(CREATE)){
            return personMapper.toDto(personService.createPerson(personMapper.toEntity(personDto)));
        }else {
            return personMapper.toDto(personService.editPerson(personMapper.toEntity(personDto)));
        }
    }

    private boolean isOwnImage(String imageIdBd, String imageIdRequest){
        return imageIdBd.equals(imageIdRequest);
    }

}
