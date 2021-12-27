package com.fakecompany.personmonolith.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor @NoArgsConstructor @Data @Builder @Getter @Setter @Generated
public class PersonDto {

    private Integer id;
    @NotBlank
    private String name;
    @NotBlank
    private String lastName;
    @NotBlank
    private String identification;
    @NotNull
    private Integer identificationTypeId;
    @NotBlank
    @Size(max = 3)
    private Integer age;
    @NotBlank
    private String cityBirth;

}
