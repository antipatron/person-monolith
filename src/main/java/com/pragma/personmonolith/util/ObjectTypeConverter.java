package com.pragma.personmonolith.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

public class ObjectTypeConverter {

    public static String image2Base64(MultipartFile image){
        byte[] imageBytes = new byte[0];
        try {
            imageBytes = image.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String imageBase64 = Base64.getEncoder().encodeToString(imageBytes);

        return imageBase64;
    }



}
