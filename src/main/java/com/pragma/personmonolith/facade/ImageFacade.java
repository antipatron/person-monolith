package com.pragma.personmonolith.facade;

import com.pragma.personmonolith.dto.ImageDto;
import com.pragma.personmonolith.exception.DataNotFoundException;
import com.pragma.personmonolith.exception.ImageNotComeBodyException;
import com.pragma.personmonolith.exception.PersonJustOneImageException;
import com.pragma.personmonolith.mapper.ImageMapper;
import com.pragma.personmonolith.model.Image;
import com.pragma.personmonolith.service.FileStoreService;
import com.pragma.personmonolith.service.ImageService;
import com.pragma.personmonolith.service.PersonService;
import com.pragma.personmonolith.util.ObjectTypeConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.pragma.personmonolith.util.ObjectTypeConverter.image2Base64;
import static com.pragma.personmonolith.util.OptionalFieldValidator.imageFileComeOnBody;
import static com.pragma.personmonolith.util.OptionalFieldValidator.imageIdComeOnBody;

@Service
@Transactional
public class ImageFacade {

    private ImageService imageService;
    private PersonService personService;
    private ImageMapper imageMapper;
    private FileStoreService fileStoreService;

    public ImageFacade(ImageService imageService,PersonService personService, FileStoreService fileStoreService, ImageMapper imageMapper) {
        this.imageService = imageService;
        this.personService = personService;
        this.fileStoreService = fileStoreService;
        this.imageMapper = imageMapper;
    }

    public ImageDto createImage(Integer personId, MultipartFile imagePart){
        ImageDto imageDto = new ImageDto();
        personService.findById(personId);
        if(hasImage(personId)){
            throw new PersonJustOneImageException("exception.person_just_one_image.image");
        }
        if(!imagePart.isEmpty()){
            final String imageName = imagePart.getOriginalFilename();
            Image imageS3 = fileStoreService.createFile(imageName,personId,imagePart);
            imageDto.setImage(imageS3.getImage());
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
        return !imageService.findByPersonIdAndId(personId, id).getImage().isEmpty();
    }
    private boolean hasImage(Integer personId){
        return !imageService.findByPersonId(personId).getImage().isEmpty();
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
