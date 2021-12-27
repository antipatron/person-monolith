package com.fakecompany.personmonolith.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor @NoArgsConstructor @Data @Builder @Getter @Setter @Generated
public class PersonImageDto {

    private Integer personId;
    @NotBlank
    private String name;
    @NotBlank
    private String lastName;
    @NotBlank
    private String identification;
    @NotNull
    private Integer identificationTypeId;
    @NotNull
    private Integer age;
    @NotBlank
    private String cityBirth;
    private String imageId;
    private String imageUrl;

}
