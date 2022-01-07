package com.fakecompany.personmonolith.dto;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdentificationTypeDto {
    private Integer id;
    private String description;
}
