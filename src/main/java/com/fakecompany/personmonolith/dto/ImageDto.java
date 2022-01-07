package com.fakecompany.personmonolith.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageDto {

    private String id;
    private String imageUrl;
    private String imageName;

    @NotNull
    private Integer personId;
}
