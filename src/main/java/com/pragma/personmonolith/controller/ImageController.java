package com.pragma.personmonolith.controller;

import com.pragma.personmonolith.dto.ImageDto;
import com.pragma.personmonolith.facade.ImageFacade;
import com.pragma.personmonolith.util.StandardResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/image")
public class ImageController {

    private ImageFacade imageFacade;

    public ImageController(ImageFacade imageFacade) {
        this.imageFacade = imageFacade;
    }

    @PostMapping
    @ApiOperation(value = "Save image", response = ImageDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La petición fue procesada con éxito"),
            @ApiResponse(code = 400, message = "La petición es inválida"),
            @ApiResponse(code = 500, message = "Error del servidor al procesar la respuesta"),
    })
    public ResponseEntity<StandardResponse<ImageDto>> createImage(
            @NotNull @RequestParam("personId") Integer personId, @RequestPart MultipartFile image){
        ImageDto imageDto1 = imageFacade.createImage(personId, image);

        return ResponseEntity.ok(new StandardResponse<>(
                StandardResponse.StatusStandardResponse.OK,
                "image.create.ok",
                imageDto1));
    }

    @PutMapping
    @ApiOperation(value = "Edit image", response = ImageDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La petición fue procesada con éxito"),
            @ApiResponse(code = 400, message = "La petición es inválida"),
            @ApiResponse(code = 500, message = "Error del servidor al procesar la respuesta"),
    })
    public ResponseEntity<StandardResponse<ImageDto>> editImage(
            @Valid @RequestPart("imageDto") ImageDto imageDto,@Valid @RequestPart MultipartFile image){
        ImageDto imageDto1 = imageFacade.editImage(imageDto, image);
        return ResponseEntity.ok(new StandardResponse<>(
                StandardResponse.StatusStandardResponse.OK,
                "image.edit.ok",
                imageDto1));
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "Delete image by id", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La petición fue procesada con éxito"),
            @ApiResponse(code = 400, message = "La petición es inválida"),
            @ApiResponse(code = 500, message = "Error del servidor al procesar la respuesta"),
    })
    public ResponseEntity<StandardResponse<String>> deleteImage(
            @RequestParam(name = "imageId")  String imageId){

        imageFacade.deleteImage(imageId);
        return ResponseEntity.accepted().body(new StandardResponse<>(StandardResponse.StatusStandardResponse.OK,"delete.image.ok"));

    }

    @GetMapping("/get-all")
    @ApiOperation(value = "Get all", response = ImageDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La petición fue procesada con éxito"),
            @ApiResponse(code = 400, message = "La petición es inválida"),
            @ApiResponse(code = 500, message = "Error del servidor al procesar la respuesta"),
    })
    public ResponseEntity<StandardResponse<List<ImageDto>>> findAll(){

        List<ImageDto> imageDtoList = imageFacade.findAll();
        return ResponseEntity.ok(new StandardResponse<>(
                StandardResponse.StatusStandardResponse.OK,
                imageDtoList));
    }

}
