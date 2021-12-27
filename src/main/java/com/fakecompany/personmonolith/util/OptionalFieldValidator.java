package com.fakecompany.personmonolith.util;

import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

public class OptionalFieldValidator {

    public static boolean imageIdComeOnBody(String imageId){
        return !Objects.isNull(imageId);
    }

    public static boolean imageFileComeOnBody(MultipartFile imagePart ){
        return !Objects.isNull(imagePart)&&!imagePart.isEmpty();
    }

}
