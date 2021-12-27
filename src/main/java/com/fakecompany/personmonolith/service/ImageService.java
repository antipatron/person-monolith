package com.fakecompany.personmonolith.service;

import com.fakecompany.personmonolith.model.Image;
import com.fakecompany.personmonolith.model.ImageRepository;
import com.fakecompany.personmonolith.exception.DataConstraintViolationException;
import com.fakecompany.personmonolith.exception.DataDuplicatedException;
import com.fakecompany.personmonolith.exception.DataNotFoundException;
import com.fakecompany.personmonolith.exception.ObjectNoEncontradoException;
import lombok.AllArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class ImageService {
    private ImageRepository imageRepository;

    public Image createImage(Image image){
        if(Objects.nonNull(image.getId())){
            Optional<Image> imageOptional = imageRepository.findById(image.getId());
            if(imageOptional.isPresent()){
                throw new DataDuplicatedException("exception.data_duplicated.image");
            }
        }

        try {
            return imageRepository.insert(image);
        }catch (DataIntegrityViolationException e) {
            throw new DataConstraintViolationException("exception.data_constraint_violation.image");
        }
    }

    public Image editImage(Image image){
        if(Objects.isNull(image.getId())){
            throw new ObjectNoEncontradoException("exception.objeto_no_encontrado");
        }
        imageRepository.save(image);

        return image;
    }

    public void deleteImage(String imageId){
        if(Objects.nonNull(imageId)){
            Optional<Image> imageOptional = imageRepository.findById(imageId);
            if(!imageOptional.isPresent()){
                throw new DataNotFoundException("exception.data_not_found.image");
            }
        }

        imageRepository.deleteById(imageId);
    }

    public List<Image> findAll(){
        List<Image> imageList = imageRepository.findAll();
        if (imageList.isEmpty()){
            throw new DataNotFoundException("exception.data_not_found.image");
        }
        return imageList;
    }

    public Image findByPersonId(Integer personId){
        if(Objects.isNull(personId)){
            throw new ObjectNotFoundException(personId, "exception.objeto_no_encontrado");
        }
        Image imageTemp = new Image();
        imageTemp.setImageUrl("");
        return imageRepository.findByPersonId(personId).orElse(imageTemp);

    }

    public Image findById(String id){
        if(Objects.isNull(id)){
            throw new ObjectNoEncontradoException("exception.objeto_no_encontrado");
        }
        return imageRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException("exception.data_not_found.image"));

    }

    public Image findByPersonIdAndId(Integer personId, String id){
        if(Objects.isNull(personId)){
            throw new ObjectNoEncontradoException("exception.objeto_no_encontrado");
        }

        if(Objects.isNull(id)){
            throw new ObjectNoEncontradoException("exception.objeto_no_encontrado");
        }

        return imageRepository.findByPersonIdAndId(personId, id).orElseThrow(()-> new DataNotFoundException("exception.data_not_found.image"));
    }

}
