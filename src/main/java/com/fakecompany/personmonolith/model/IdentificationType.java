package com.fakecompany.personmonolith.model;

import lombok.*;

import javax.persistence.*;


@Entity
@Table(name = "identification_type")
@AllArgsConstructor @NoArgsConstructor
@Builder @Getter
@Setter @Generated
public class IdentificationType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String description;
}
