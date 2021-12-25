package com.pragma.personmonolith.service;

import com.pragma.personmonolith.model.Image;
import com.pragma.personmonolith.util.FileStorageS3Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.apache.http.entity.ContentType.*;

@Service
public class FileStoreService {

    @Value("${amazon.s3.bucket-name.one}")
    private String bucketName;

    @Value("${amazon.s3.url.one}")
    private String urlBucket;

    private final FileStorageS3Util fileStorageS3Util;

    public FileStoreService(FileStorageS3Util fileStorageS3Util) {
        this.fileStorageS3Util = fileStorageS3Util;
    }

    public Image createFile(String title, Integer personId, MultipartFile file) {
        //check if the file is empty
        if (file.isEmpty()) {
            throw new IllegalStateException("Cannot upload empty file");
        }
        //Check if the file is an image
        if (!Arrays.asList(IMAGE_PNG.getMimeType(),
                IMAGE_BMP.getMimeType(),
                IMAGE_GIF.getMimeType(),
                IMAGE_JPEG.getMimeType()).contains(file.getContentType())) {
            throw new IllegalStateException("FIle uploaded is not an image");
        }
        //get file metadata
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        //Save Image in S3 and then save Todo in the database
        String path = String.format("%s/%s", bucketName, personId);
        String fileName = String.format("%s", title);
        try {
            fileStorageS3Util.upload(path, fileName, Optional.of(metadata), file.getInputStream());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to upload file", e);
        }
        final Image image = new Image();
        image.setImageUrl(urlBucket.concat(String.valueOf(personId)).concat("/").concat(fileName));
        image.setImageName(fileName);
        image.setPersonId(personId);

        return image;
    }

    public void deleteFile(String title, Integer personId) {

        //Save Image in S3 and then save Todo in the database
        String path = String.format("%s/%s", bucketName, personId);
        fileStorageS3Util.delete(path, title);


    }

}
