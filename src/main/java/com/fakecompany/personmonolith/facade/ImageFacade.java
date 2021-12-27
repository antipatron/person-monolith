package com.fakecompany.personmonolith.facade;

import com.fakecompany.personmonolith.dto.ImageDto;
import com.fakecompany.personmonolith.exception.DataNotFoundException;
import com.fakecompany.personmonolith.mapper.ImageMapper;
import com.fakecompany.personmonolith.model.Image;
import com.fakecompany.personmonolith.service.ImageService;
import com.fakecompany.personmonolith.service.PersonService;
import com.fakecompany.personmonolith.exception.ImageNotComeBodyException;
import com.fakecompany.personmonolith.exception.PersonJustOneImageException;
import com.fakecompany.personmonolith.service.FileStoreService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.fakecompany.personmonolith.util.OptionalFieldValidator.imageFileComeOnBody;
import static com.fakecompany.personmonolith.util.OptionalFieldValidator.imageIdComeOnBody;

@Service
@Transactional
@AllArgsConstructor
public class ImageFacade {

    private ImageService imageService;
    private PersonService personService;
    private ImageMapper imageMapper;
    private FileStoreService fileStoreService;

    public ImageDto createImage(Integer personId, MultipartFile imagePart){
        ImageDto imageDto = new ImageDto();
        personService.findById(personId);
        if(hasImage(personId)){
            throw new PersonJustOneImageException("exception.person_just_one_image.image");
        }
        if(!imagePart.isEmpty()){
            final String imageName = imagePart.getOriginalFilename();
            Image imageS3 = fileStoreService.createFile(imageName,personId,imagePart);
            imageDto.setImageUrl(imageS3.getImageUrl());
            imageDto.setImageName(imageS3.getImageName());
            imageDto.setPersonId(personId);
        }else{
            throw new ImageNotComeBodyException("exception.image_not_come_body.image");
        }

        return imageMapper.toDto(imageService.createImage(imageMapper.toEntity(imageDto)));
    }

    public ImageDto editImage(ImageDto imageDto, MultipartFile imagePart){
        personService.findById(imageDto.getPersonId());
        ImageDto imageDtoEdit = imageMapper.toDto(imageService.findByPersonIdAndId(imageDto.getPersonId(), imageDto.getId()));
        final String imageName = imagePart.getOriginalFilename();

        if (imageFileComeOnBody(imagePart)){
            if(hasImage(imageDto.getPersonId(), imageDto.getId())){
                if(imageIdComeOnBody(imageDto.getId())){
                    fileStoreService.deleteFile(imageDtoEdit.getImageName(),imageDto.getPersonId());
                    Image imageS3 = fileStoreService.createFile(imageName,imageDto.getPersonId(),imagePart);
                    imageS3.setId(imageDto.getId());
                    imageDtoEdit = imageMapper.toDto(imageService.editImage(imageS3));
                }else {
                    throw new ImageNotComeBodyException("exception.not_come_body.image");
                }
            }else{
                throw new DataNotFoundException("exception.data_not_found.image");
            }
        }

        return imageDtoEdit;
    }

    private boolean hasImage(Integer personId, String id){
        return !imageService.findByPersonIdAndId(personId, id).getImageUrl().isEmpty();
    }
    private boolean hasImage(Integer personId){
        return !imageService.findByPersonId(personId).getImageUrl().isEmpty();
    }



    public void deleteImage(String imageId){
        ImageDto imageDto = imageMapper.toDto(imageService.findById(imageId));
        fileStoreService.deleteFile(imageDto.getImageName(), imageDto.getPersonId());
        imageService.deleteImage(imageId);

    }

    public List<ImageDto> findAll(){
        return imageMapper.toDto(imageService.findAll());
    }

}
